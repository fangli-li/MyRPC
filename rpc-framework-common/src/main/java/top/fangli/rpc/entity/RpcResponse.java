package top.fangli.rpc.entity;

import lombok.Data;
import top.fangli.rpc.enumeration.ResponseCode;

import java.io.Serializable;

/**
 * 提供者返回的结果
 *
 * @author fangli
 */

@Data
public class RpcResponse<T> implements Serializable {

    public RpcResponse() {
    }

    /**
     * 响应对应的请求号
     */
    private String requestId;
    /**
     * 响应状态码
     */
    private Integer statusCode;

    /**
     * 响应code的描述信息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    public static <T> RpcResponse<T> success(T data, String requestId) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setRequestId(requestId);
        response.setStatusCode(ResponseCode.SUCCESS.getCode());
        response.setData(data);
        return response;
    }

    public static <T> RpcResponse<T> fail(ResponseCode code, String requestId) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setRequestId(requestId);
        response.setStatusCode(code.getCode());
        response.setMessage(code.getMessage());
        return response;
    }
}
