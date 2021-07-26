package top.fangli.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fangli.rpc.api.HelloObject;
import top.fangli.rpc.api.HelloService;

public class HelloServiceImpl implements HelloService {
    private static final Logger log = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(HelloObject helloObject) {
        log.info("接收到: {}",helloObject.getMessage());
        return "这是调用的返回值，id=" + helloObject.getId();
    }
}
