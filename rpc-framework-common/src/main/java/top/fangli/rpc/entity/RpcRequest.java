package top.fangli.rpc.entity;

import lombok.Data;
import lombok.Builder;
import java.io.Serializable;


/*
* 消费者向提供者发送消息
* @author top.fangli
* */


@Builder
@Data
public class RpcRequest implements Serializable{

    /*
    * 调用接口名字
    * */
    private String interfaceName;

    /*
    * 调用方法名
    * */
    private String methodName;

    /*
    * 参数列表
    * */
    private Object[] parameters;

    /*
    * 参数列表的类型
    * */
    private Class<?>[] paramTypes;

}
