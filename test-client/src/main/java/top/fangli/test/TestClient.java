package top.fangli.test;

import top.fangli.rpc.api.HelloObject;
import top.fangli.rpc.api.HelloService;
import top.fangli.rpc.client.RpcClient;
import top.fangli.rpc.client.RpcClientProxy;

public class TestClient {
    public static void main(String[] args) {
        RpcClientProxy proxy = new RpcClientProxy("localhost", 9000);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject helloObject = new HelloObject(99, "hello world");
        String res = helloService.hello(helloObject);
        System.out.println("res = " + res);
    }
}
