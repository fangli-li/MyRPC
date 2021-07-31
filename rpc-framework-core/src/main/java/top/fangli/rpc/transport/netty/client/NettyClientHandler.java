package top.fangli.rpc.transport.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import top.fangli.rpc.entity.RpcResponse;

/**
 * @author fangli
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        try{
            log.info(String.format("客户端接收到消息: %s", msg));
            AttributeKey<RpcResponse<Object>> key = AttributeKey.valueOf("rpcResponse"+msg.getRequestId());
            ctx.channel().attr(key).set(msg);
            ctx.channel().close();
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }
}
