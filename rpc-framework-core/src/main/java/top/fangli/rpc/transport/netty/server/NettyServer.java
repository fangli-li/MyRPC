package top.fangli.rpc.transport.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fangli.rpc.codec.CommonDecoder;
import top.fangli.rpc.codec.CommonEncoder;
import top.fangli.rpc.enumeration.RpcError;
import top.fangli.rpc.exception.RpcException;
import top.fangli.rpc.provider.DefaultServiceProvider;
import top.fangli.rpc.provider.ServiceProvider;
import top.fangli.rpc.registry.NacosServiceRegistry;
import top.fangli.rpc.registry.ServiceRegistry;
import top.fangli.rpc.serializer.CommonSerializer;
import top.fangli.rpc.transport.RpcServer;

import java.net.InetSocketAddress;


/**
 * NIO
 *
 * @author fangli
 */
public class NettyServer implements RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private final String host;
    private final int port;

    private final ServiceRegistry serviceRegistry;
    private final ServiceProvider serviceProvider;

    private CommonSerializer serializer;

    public NettyServer(String host, int port) {
        this.host = host;
        this.port = port;
        serviceProvider = new DefaultServiceProvider();
        serviceRegistry = new NacosServiceRegistry();
    }


    @Override
    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new CommonEncoder(serializer));
                            pipeline.addLast(new CommonDecoder());
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(host, port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("启动服务时发生错误: ", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public <T> void publishService(Object service, Class<T> serviceClass) {
        if (serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        serviceRegistry.register(serviceClass.getCanonicalName(), new InetSocketAddress(host, port));
        serviceProvider.addServiceProvider(service);
        start();
    }
}
