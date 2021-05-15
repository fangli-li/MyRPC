package top.fangli.rpc.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fangli.rpc.registry.ServiceRegistry;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * 服务端（提供者）
 * @author fangli
 */
public class RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    private final ExecutorService threadPool;
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 50;
    private static final int KEEP_ALIVE_TIME = 60;

    private RequestHandler requestHandler = new RequestHandler();
    private final ServiceRegistry serviceRegistry;

    public RpcServer(ServiceRegistry serviceRegistry){
        this.serviceRegistry = serviceRegistry;
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME, TimeUnit.SECONDS,workingQueue, threadFactory);

    }
    public void start(int port){
        try(ServerSocket serverSocket = new ServerSocket(port)){
            logger.info("服务器启动……");
            Socket socket;

            while ((socket=serverSocket.accept())!=null){
                logger.info("客户端连接! IP为: "+socket.getInetAddress());
                threadPool.execute(new RequestHandlerThread(socket,serviceRegistry, requestHandler));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            logger.error("服务器启动时有异常发生:", e);
        }
    }

}
