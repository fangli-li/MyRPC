package top.fangli.test;

import top.fangli.rpc.api.HelloService;
import top.fangli.rpc.netty.server.NettyServer;
import top.fangli.rpc.registry.DefaultServiceRegistry;
import top.fangli.rpc.registry.ServiceRegistry;

/**
 * @author fangli
 */
public class NettyTestServer {
    public static void main(String[] args) {
        HelloService service = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(service);
        NettyServer nettyServer = new NettyServer();
        nettyServer.start(9999);

    }
}
