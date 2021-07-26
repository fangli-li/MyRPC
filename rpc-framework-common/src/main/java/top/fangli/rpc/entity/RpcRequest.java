package top.fangli.rpc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;


/**
 * @author fangli
 */
@Data
@AllArgsConstructor
public class RpcRequest implements Serializable {

    public RpcRequest() {
    }

    /**
     * 调用接口名字
     */
    private String interfaceName;

    /**
     * 调用方法名
     */
    private String methodName;

    /**
     * 参数列表
     */
    private Object[] parameters;

    /**
     * 参数列表的类型
     */
    private Class<?>[] paramTypes;

}
