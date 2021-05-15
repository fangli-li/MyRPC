package top.fangli.test;

import top.fangli.rpc.api.HelloService;
import top.fangli.rpc.registry.DefaultServiceRegistry;
import top.fangli.rpc.registry.ServiceRegistry;
import top.fangli.rpc.server.RpcServer;

public class TestServer {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        RpcServer rpcServer = new RpcServer(serviceRegistry);
        rpcServer.start(9000);
    }
}
