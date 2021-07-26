package top.fangli.rpc.serializer;

/**
 * 通用的序列化，反序列化接口
 * @author fangli
 */
public interface CommonSerializer {

    /**
     * 将对象序列化
     * @param obj 序列化的对象
     * @return 序列化的字节流
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化
     * @param bytes 字节流
     * @param clazz 目标类型
     * @return 反序列化后的对象
     */
    Object deSerialize(byte[] bytes, Class<?> clazz);

    /**
     * 获取序列化类型的code
     * @return 返回序列化类型code
     */
    int getCode();

    /**
     * 通过code获序列化类型
     * @param code code
     * @return 序列化器
     */
    static CommonSerializer getByCode(int code){
        switch (code){
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }


}
