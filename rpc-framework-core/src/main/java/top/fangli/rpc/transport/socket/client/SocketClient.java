package top.fangli.rpc.transport.socket.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fangli.rpc.registry.NacosServiceRegistry;
import top.fangli.rpc.registry.ServiceRegistry;
import top.fangli.rpc.serializer.CommonSerializer;
import top.fangli.rpc.transport.RpcClient;
import top.fangli.rpc.entity.RpcRequest;
import top.fangli.rpc.entity.RpcResponse;
import top.fangli.rpc.enumeration.ResponseCode;
import top.fangli.rpc.enumeration.RpcError;
import top.fangli.rpc.exception.RpcException;
import top.fangli.rpc.transport.socket.util.ObjectReader;
import top.fangli.rpc.transport.socket.util.ObjectWriter;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Socket方式远程方法调用的消费者（客户端）
 * @author ziyang
 */
public class SocketClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    private final ServiceRegistry serviceRegistry;

    private CommonSerializer serializer;

    public SocketClient() {
        this.serviceRegistry = new NacosServiceRegistry();
    }


    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if(serializer==null){
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        InetSocketAddress inetSocketAddress = serviceRegistry.lookupService(rpcRequest.getInterfaceName());
        try (Socket socket = new Socket()) {
            socket.connect(inetSocketAddress);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            ObjectWriter.writeObject(outputStream, rpcRequest, serializer);
            Object obj = ObjectReader.readObject(inputStream);
            RpcResponse rpcResponse = (RpcResponse) obj;
            if(rpcResponse == null) {
                logger.error("服务调用失败，service：{}", rpcRequest.getInterfaceName());
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, " service:" + rpcRequest.getInterfaceName());
            }
            if(rpcResponse.getStatusCode() == null || rpcResponse.getStatusCode() != ResponseCode.SUCCESS.getCode()) {
                logger.error("调用服务失败, service: {}, response:{}", rpcRequest.getInterfaceName(), rpcResponse);
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, " service:" + rpcRequest.getInterfaceName());
            }
            return rpcResponse.getData();
        } catch (IOException e) {
            logger.error("调用时有错误发生：", e);
            throw new RpcException("服务调用失败: ", e);
        }
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }

}
