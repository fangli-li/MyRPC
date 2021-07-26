package top.fangli.test;

import top.fangli.rpc.RpcClient;
import top.fangli.rpc.RpcClientProxy;
import top.fangli.rpc.api.HelloObject;
import top.fangli.rpc.api.HelloService;
import top.fangli.rpc.netty.client.NettyClient;

/**
 * @author fangli
 */
public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient("localhost", 9999);
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject helloObject = new HelloObject(111, "hello world!");
        String res = helloService.hello(helloObject);
        System.out.println(res);

    }
}
