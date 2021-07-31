package top.fangli.rpc.transport;

import top.fangli.rpc.serializer.CommonSerializer;

/**
 * 服务器类通用接口
 *
 * @author fangli
 */
public interface RpcServer {

    /**
     * 启动服务器
     * @param port 监听端口
     */
    void start();

    void setSerializer(CommonSerializer serializer);

    <T> void publishService(Object service, Class<T> serviceClass);

}
