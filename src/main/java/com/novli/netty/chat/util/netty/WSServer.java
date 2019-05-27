package com.novli.netty.chat.util.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WSServer {

    private static class SingletionWSServer {
        static final WSServer instance = new WSServer();
    }

    public static WSServer getInstance() {
        return SingletionWSServer.instance;
    }

    private EventLoopGroup masterGroup;

    private EventLoopGroup slaveGroup;

    private ServerBootstrap server;

    private ChannelFuture future;

    public void start() {
        this.future = server.bind(8081);
        log.info("netty webSocket Server 已经启动 --------------");
    }

    public WSServer() {
        masterGroup = new NioEventLoopGroup();
        slaveGroup = new NioEventLoopGroup();
        server = new ServerBootstrap();
        server.group(masterGroup, slaveGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new WSServerInitializer());
    }
}
