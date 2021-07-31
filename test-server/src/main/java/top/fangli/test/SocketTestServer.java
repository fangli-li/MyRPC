package top.fangli.test;

import top.fangli.rpc.api.HelloService;
import top.fangli.rpc.serializer.JsonSerializer;
import top.fangli.rpc.transport.socket.server.SocketServer;

/**
 * @author fangli
 */
public class SocketTestServer {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        SocketServer socketServer = new SocketServer("localhost", 9000);
        socketServer.setSerializer(new JsonSerializer());
        socketServer.publishService(helloService, HelloService.class);
    }

}
