package top.fangli.rpc.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fangli.rpc.enumeration.RpcError;
import top.fangli.rpc.exception.RpcException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fangli
 */
public class DefaultServiceProvider implements ServiceProvider {

    private static final Logger logger = LoggerFactory.getLogger(DefaultServiceProvider.class);

    private final static Map<String, Object> SERVICE_MAP = new ConcurrentHashMap<>();
    private final static Set<String> REGISTERED_SERVICE = ConcurrentHashMap.newKeySet();


    @Override
    public synchronized <T> void addServiceProvider(T service) {
        String serviceName = service.getClass().getCanonicalName();
        if (REGISTERED_SERVICE.contains(serviceName)) {
            return;
        }
        REGISTERED_SERVICE.add(serviceName);
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if (interfaces.length == 0) {
            throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        for (Class<?> i : interfaces) {
            SERVICE_MAP.put(i.getCanonicalName(), service);
        }

        logger.info("向接口:{} 注册服务:{}", interfaces, serviceName);


    }

    @Override
    public synchronized Object getServiceProvider(String serviceName) {
        Object service = SERVICE_MAP.get(serviceName);
        if (service == null) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}
