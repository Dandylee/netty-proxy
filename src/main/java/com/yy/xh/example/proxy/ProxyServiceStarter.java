package com.yy.xh.example.proxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * ProxyServiceStarter
 *
 * @Date 2021/3/31
 * @Author Dandy
 */
public class ProxyServiceStarter {

    private String file;
    private ProxyConfigs configs;

    public ProxyServiceStarter(String file) {
        this.file = file;
        configs =
    }

    private String host;

    public void run() throws InterruptedException {
        EventLoopGroup boss = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() / 2);
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.group(boss, worker).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ProxyServiceHandler());
                }
            }).childAttr(AttributeKey.valueOf("type"), "client")
                    .option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

            SocketAddress socketAddress = new InetSocketAddress(host, port);
            ChannelFuture f = sb.bind(socketAddress).sync();
            f.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        int port = 8081;
//        String host = "127.0.0.1";
//        if (args.length > 0) {
//            port = Integer.parseInt(args[0]);
//            if (args.length > 1) {
//                host = args[1];
//            }
//        }
        String file = parseConfig(args);
        if (file == null){
            throw new RuntimeException("config file not found");
        }

        new ProxyServiceStarter(port, host).run();
    }

    public static String parseConfig(String[] args){
        String configFile = null;

        for (String arg : args){
            if (arg.startsWith("-config")){
                String[] params = arg.split("=");
                configFile = params[1];
            }
        }
        return configFile;
    }
}
