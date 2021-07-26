package top.fangli.rpc.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import top.fangli.rpc.RpcClient;
import top.fangli.rpc.codec.CommonDecoder;
import top.fangli.rpc.codec.CommonEncoder;
import top.fangli.rpc.entity.RpcRequest;
import top.fangli.rpc.entity.RpcResponse;
import top.fangli.rpc.serializer.JsonSerializer;
import top.fangli.rpc.serializer.KryoSerializer;

import java.util.EventListener;

/**
 * 使用netty方式提供服务
 * @author fangli
 */

@Slf4j
public class NettyClient implements RpcClient {

    private final String host;
    private final int port;
    private static final Bootstrap bootstrap;

    public NettyClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    static {
        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new CommonDecoder());
                        pipeline.addLast(new CommonEncoder(new KryoSerializer()));
                        pipeline.addLast(new NettyClientHandler());
                    }
                });
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        try{
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            log.info("客户端连接到服务器 {}:{}", host, port);
            Channel channel = channelFuture.channel();

            if(channel!=null){
                channel.writeAndFlush(rpcRequest).addListener(future -> {
                    if(future.isSuccess()) {
                        log.info(String.format("客户端发送消息: %s", rpcRequest.toString()));
                    } else {
                        log.error("发送消息时有错误发生: ", future.cause());
                    }
                });
                channel.closeFuture().sync();
                AttributeKey<RpcResponse<Object>> key = AttributeKey.valueOf("rpcResponse");
                RpcResponse rpcResponse = channel.attr(key).get();
                return rpcResponse.getData();
            }
        } catch (InterruptedException e) {
            log.error("发送消息时有错误发生: ", e);
        }
        return null;
    }
}
