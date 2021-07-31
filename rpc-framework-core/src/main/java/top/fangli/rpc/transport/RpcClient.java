package top.fangli.rpc.transport;

import top.fangli.rpc.entity.RpcRequest;
import top.fangli.rpc.serializer.CommonSerializer;

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

    /**
     * @param serializer 序列化器
     */
    void setSerializer(CommonSerializer serializer);
}
