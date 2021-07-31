package top.fangli.rpc.util;

import lombok.extern.slf4j.Slf4j;
import top.fangli.rpc.entity.RpcRequest;
import top.fangli.rpc.entity.RpcResponse;
import top.fangli.rpc.enumeration.ResponseCode;
import top.fangli.rpc.enumeration.RpcError;
import top.fangli.rpc.exception.RpcException;

/**
 * @author fangli
 */
@Slf4j
public class RpcMessageChecker {
    public static final String INTERFACE_NAME = "interfaceName";

    private RpcMessageChecker(){

    }

    public static void check(RpcRequest request, RpcResponse response){
        if(response == null){
            log.error("调用服务失败,serviceName:{}", request.getInterfaceName());
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + request.getInterfaceName());
        }

        if(!request.getRequestId().equals(response.getRequestId())){
            throw new RpcException(RpcError.RESPONSE_NOT_MATCH, INTERFACE_NAME + ":" + request.getInterfaceName());
        }

        if (response.getStatusCode() == null || !response.getStatusCode().equals(ResponseCode.SUCCESS.getCode())) {
            log.error("调用服务失败,serviceName:{},RpcResponse:{}", request.getInterfaceName(), response);
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + request.getInterfaceName());
        }
    }
}
