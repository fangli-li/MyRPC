package top.fangli.rpc;

/**
 * 服务器类通用接口
 *
 * @author fangli
 */
public interface RpcServer {

    /**
     * 启动服务器
     *
     * @param port 监听端口
     */
    void start(int port);

}
