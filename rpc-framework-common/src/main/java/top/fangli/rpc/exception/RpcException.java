package top.fangli.rpc.exception;

import top.fangli.rpc.enumeration.RpcError;

/**
 * 自定义RPC调用异常
 */
public class RpcException extends RuntimeException {
    public RpcException(RpcError rpcError, String detail) {
        super(rpcError.getMessage() + ": " + detail);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RpcError rpcError) {
        super(rpcError.getMessage());
    }

}
