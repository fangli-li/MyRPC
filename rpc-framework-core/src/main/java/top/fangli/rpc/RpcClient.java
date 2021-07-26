package top.fangli.rpc;

import top.fangli.rpc.entity.RpcRequest;

/**
 * 客户端类通用接口
 *
 * @author fangli
 */
public interface RpcClient {
    /**
     * 发送请求
     *
     * @param rpcRequest 请求数据包
     * @return 结果
     */
    Object sendRequest(RpcRequest rpcRequest);

}
