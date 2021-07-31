package top.fangli.rpc.transport.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import top.fangli.rpc.entity.RpcRequest;
import top.fangli.rpc.entity.RpcResponse;
import top.fangli.rpc.enumeration.RpcError;
import top.fangli.rpc.exception.RpcException;
import top.fangli.rpc.registry.NacosServiceRegistry;
import top.fangli.rpc.registry.ServiceRegistry;
import top.fangli.rpc.serializer.CommonSerializer;
import top.fangli.rpc.transport.RpcClient;
import top.fangli.rpc.util.RpcMessageChecker;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 使用netty方式提供服务
 *
 * @author fangli
 */

@Slf4j
public class NettyClient implements RpcClient {

    private static final Bootstrap bootstrap;

    private final ServiceRegistry serviceRegistry;

    private CommonSerializer serializer;

    static {
        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true);
    }

    public NettyClient() {
        this.serviceRegistry = new NacosServiceRegistry();
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if (serializer == null) {
            log.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        AtomicReference<Object> result = new AtomicReference<>(null);

        try {

            InetSocketAddress inetSocketAddress = serviceRegistry.lookupService(rpcRequest.getInterfaceName());
            Channel channel = ChannelProvider.get(inetSocketAddress, serializer);

            if (channel.isActive()) {
                channel.writeAndFlush(rpcRequest).addListener(future -> {
                    if (future.isSuccess()) {
                        log.info(String.format("客户端发送消息: %s", rpcRequest.toString()));
                    } else {
                        log.error("发送消息时有错误发生: ", future.cause());
                    }
                });
                channel.closeFuture().sync();
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse" + rpcRequest.getRequestId());
                RpcResponse rpcResponse = channel.attr(key).get();
                RpcMessageChecker.check(rpcRequest, rpcResponse);
                result.set(rpcResponse.getData());
            }else {
                System.exit(0);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return result.get();
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }
}
