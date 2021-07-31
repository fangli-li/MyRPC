package top.fangli.rpc.provider;

/**
 * 服务注册接口
 * @author fangli
 */
public interface ServiceProvider {

    /**
     * 将一个服务注册到注册表
     * @param service 待注册服务的主题
     * @param <T> 服务实体类型
     */
    <T> void addServiceProvider(T service);


    /**
     * 根据服务名称获取服务实体
     * @param serviceName 服务名称
     * @return 服务实体
     */
    Object getServiceProvider(String serviceName);
}
