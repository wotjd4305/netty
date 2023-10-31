package org.example.chapter4;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;

public class HttpHelloWorldServerInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;

    public HttpHelloWorldServerInitializer(SslContext sslCtx){
        this.sslCtx = sslCtx;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        if(sslCtx != null){
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }
        p.addLast(new HttpServerCodec()); // 네티가 제공하는 HTTP 서버 코덱, 인코더 디코더 모두 생성
        p.addLast(new HttpHelloWorldServerHandler()); // HTTP 데이터에 대한 처리
    }
}