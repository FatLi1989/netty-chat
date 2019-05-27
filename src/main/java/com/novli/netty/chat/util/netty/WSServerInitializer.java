package com.novli.netty.chat.util.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WSServerInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //websocket 基于http协议 所以要http编解码器
        pipeline.addLast(new HttpServerCodec());
        //支持大数据的写入
        pipeline.addLast(new ChunkedWriteHandler());
        //对httpMessage进行聚合 聚合成FullHttpRequest和FullHttpResponse
        //几乎在netty中的编程,都会使用到此handler
        pipeline.addLast(new HttpObjectAggregator(1024*64));

        // ========================== 以上是用于支持http协议 ============================

        //websocket 服务器处理的协议, 用于给服务端用于访问的路由
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        //自定义handler
        pipeline.addLast(new chatHandler());

    }
}
