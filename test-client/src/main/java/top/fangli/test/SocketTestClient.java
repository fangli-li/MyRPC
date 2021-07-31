package top.fangli.test;

import top.fangli.rpc.serializer.JsonSerializer;
import top.fangli.rpc.transport.RpcClientProxy;
import top.fangli.rpc.api.HelloObject;
import top.fangli.rpc.api.HelloService;
import top.fangli.rpc.transport.socket.client.SocketClient;

/**
 * @author fangli
 */
public class SocketTestClient {
    public static void main(String[] args) {
        SocketClient client = new SocketClient();
        client.setSerializer(new JsonSerializer());
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
