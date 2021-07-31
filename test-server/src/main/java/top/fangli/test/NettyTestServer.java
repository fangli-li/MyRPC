package top.fangli.test;

import top.fangli.rpc.api.HelloService;
import top.fangli.rpc.serializer.KryoSerializer;
import top.fangli.rpc.transport.netty.server.NettyServer;

/**
 * @author fangli
 */
public class NettyTestServer {
    public static void main(String[] args) {
        HelloService service = new HelloServiceImpl();
        NettyServer server = new NettyServer("localhost", 9999);
        server.setSerializer(new KryoSerializer());
        server.publishService(service,HelloService.class);

    }
}
