package top.fangli.rpc.transport.socket.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fangli.rpc.serializer.CommonSerializer;
import top.fangli.rpc.transport.RequestHandler;
import top.fangli.rpc.entity.RpcRequest;
import top.fangli.rpc.entity.RpcResponse;
import top.fangli.rpc.provider.ServiceProvider;
import top.fangli.rpc.transport.socket.util.ObjectReader;
import top.fangli.rpc.transport.socket.util.ObjectWriter;

import java.io.*;
import java.net.Socket;

/**
 * 处理RpcRequest的工作线程
 *
 * @author fangli
 */
public class RequestHandlerThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerThread.class);

    private Socket socket;
    private RequestHandler requestHandler;
    private ServiceProvider serviceRegistry;
    private CommonSerializer serializer;

    public RequestHandlerThread(Socket socket, RequestHandler requestHandler, ServiceProvider serviceRegistry, CommonSerializer serializer) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serviceRegistry = serviceRegistry;
        this.serializer = serializer;
    }

    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {
            RpcRequest rpcRequest = (RpcRequest) ObjectReader.readObject(inputStream);
            Object result = requestHandler.handle(rpcRequest);
            RpcResponse<Object> response = RpcResponse.success(result, rpcRequest.getRequestId());
            ObjectWriter.writeObject(outputStream,response,serializer);
        } catch (IOException e) {
            logger.error("调用或发送时有错误发生：", e);
        }
    }

}
