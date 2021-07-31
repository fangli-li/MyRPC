package top.fangli.rpc.transport;

import lombok.extern.slf4j.Slf4j;
import top.fangli.rpc.entity.RpcRequest;
import top.fangli.rpc.entity.RpcResponse;
import top.fangli.rpc.enumeration.ResponseCode;
import top.fangli.rpc.provider.DefaultServiceProvider;
import top.fangli.rpc.provider.ServiceProvider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author fangli
 */
@Slf4j
public class RequestHandler {

    private static final ServiceProvider serviceProvider;

    static {
        serviceProvider = new DefaultServiceProvider();
    }
    public Object handle(RpcRequest rpcRequest) {
        Object result = null;
        Object service = serviceProvider.getServiceProvider(rpcRequest.getInterfaceName());
        try {
            result = invokeTargetMethod(rpcRequest, service);
            log.info("服务:{} 成功调用方法:{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("调用或发送时有错误发生：", e);
        } return result;
    }

    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) throws IllegalAccessException, InvocationTargetException {
        Method method;
        try {
            method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
        } catch (NoSuchMethodException e) {
            return RpcResponse.fail(ResponseCode.NOT_FOUND_METHOD,rpcRequest.getRequestId());
        }
        return method.invoke(service, rpcRequest.getParameters());
    }

}
