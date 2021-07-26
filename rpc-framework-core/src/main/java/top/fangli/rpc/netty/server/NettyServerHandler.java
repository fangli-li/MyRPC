package top.fangli.rpc.netty.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import top.fangli.rpc.RequestHandler;
import top.fangli.rpc.entity.RpcRequest;
import top.fangli.rpc.entity.RpcResponse;
import top.fangli.rpc.registry.DefaultServiceRegistry;
import top.fangli.rpc.registry.ServiceRegistry;

/**
 * 处理RpcRequest的handler
 *
 * @author fangli
 */

@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final RequestHandler REQUEST_HANDLER;
    private static final ServiceRegistry SERVICE_REGISTRY;

    static {
        REQUEST_HANDLER = new RequestHandler();
        SERVICE_REGISTRY = new DefaultServiceRegistry();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        try {
            log.info("服务器收到请求:{}", msg);
            String interfaceName = msg.getInterfaceName();
            Object service = SERVICE_REGISTRY.getService(interfaceName);
            Object result = REQUEST_HANDLER.handle(msg, service);
            ChannelFuture future = ctx.writeAndFlush(RpcResponse.success(result));
            future.addListener(ChannelFutureListener.CLOSE);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("处理过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }

}
