package ru.demo.echo.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(EchoServerHandler.class);
    private final ByteBuf bufForInMsg = new PooledByteBufAllocator(true).directBuffer(5);

    public EchoServerHandler() {
        logger.info("new EchoServerHandler created");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
        logger.info("channel:{}", ctx.channel());

        var in = (ByteBuf) msg;

        if (in.readableBytes() > bufForInMsg.capacity()) {
            bufForInMsg.capacity(in.readableBytes());
        }

        bufForInMsg.resetReaderIndex();
        bufForInMsg.resetWriterIndex();
        try {
            while (in.isReadable()) {
                in.readBytes(bufForInMsg, in.readableBytes());
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }

        try (var byteArray = new ByteArrayOutputStream()) {
            bufForInMsg.readBytes(byteArray, bufForInMsg.readableBytes());

            handleRequest(ctx, byteArray);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error(cause.getMessage(), cause);
        ctx.close();
    }

    private void handleRequest(ChannelHandlerContext ctx, ByteArrayOutputStream byteArray) {
        logger.info("this:{}, channel:{}, data:{}", this, ctx.channel(), byteArray);

        var request = byteArray.toString();
        ctx.writeAndFlush(Unpooled.copiedBuffer(request, CharsetUtil.UTF_8));
    }
}
