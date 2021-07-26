package top.fangli.test;

import top.fangli.rpc.api.HelloService;
import top.fangli.rpc.registry.DefaultServiceRegistry;
import top.fangli.rpc.registry.ServiceRegistry;
import top.fangli.rpc.socket.server.SocketServer;

/**
 * @author fangli
 */
public class SocketTestServer {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        SocketServer socketServer = new SocketServer(serviceRegistry);
        socketServer.start(9000);
    }

}
