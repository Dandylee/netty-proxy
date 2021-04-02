package com.yy.xh.example.proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * ProxyServiceHandler
 *
 * @Date 2021/3/31
 * @Author Dandy
 */
@Slf4j
@Sharable
public class ProxyServiceHandler extends ChannelInboundHandlerAdapter {

    private volatile Map<Channel, Channel> channelMap = new HashMap<>();

    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        Attribute<String> type = channel.attr(AttributeKey.valueOf("type"));
        if (type.get().equals("client")) {
            log.info("[process] client active!");
            Channel proxyChannel = getProxyChannel(ctx, "127.0.0.1", 8189);
            channelMap.put(channel, proxyChannel);
            channelMap.put(proxyChannel, channel);
        } else {
            log.info("[process] proxy active!");
            ctx.read();
            ctx.write(Unpooled.EMPTY_BUFFER);
        }
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        final Channel in = ctx.channel();
        Attribute<String> type = in.attr(AttributeKey.valueOf("type"));
        log.info("[process] {} channel active!", type);
        ByteBuf byteBuf = (ByteBuf) msg;
        String string = ((ByteBuf) msg).toString(Charset.defaultCharset());
        Channel outChannel = channelMap.get(in);
        if (null == outChannel) {
            throw new IllegalArgumentException();
        }
        try {
            System.out.println(string);

            outChannel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        log.info("[process] client listener!");
                        in.read();
                    } else {
                        future.channel().close();
                    }
                }
            });
        } catch (Exception e) {
            log.error("error message", e);
            ReferenceCountUtil.release(msg);
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Attribute<String> type = ctx.channel().attr(AttributeKey.valueOf("type"));
        System.out.println("Have a connection! type: " + type.get());
        close(ctx.channel());
    }

    private void close(Channel channel){
        Channel bindChannel = channelMap.get(channel);
            channelMap.remove(channel);
        if (bindChannel != null){
            channelMap.remove(bindChannel);
        }
        if (null != channel && channel.isActive()) {
            channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }

    private Channel getProxyChannel(ChannelHandlerContext ctx, String remoteHost, int remotePort) {
        final Channel inboundChannel = ctx.channel();
        Channel proxyChannel = channelMap.get(inboundChannel);
        if (proxyChannel != null) {
            log.info("[process] 从缓存中获取channel!");
            return proxyChannel;
        }
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(inboundChannel.eventLoop()).channel(NioSocketChannel.class).handler(this)
                .attr(AttributeKey.valueOf("type"), "remoteProxy").option(ChannelOption.AUTO_READ, false);

        ChannelFuture f = bootstrap.connect(new InetSocketAddress(remoteHost, remotePort));
        Channel channel = f.channel();

        f.addListener(future -> {
            if (future.isSuccess()) {
                log.info("[process] proxy listener!");
                inboundChannel.read();
            } else {
                inboundChannel.close();
            }
        });

        log.info("[process] 生成代理channel!");
        return channel;
    }
}
