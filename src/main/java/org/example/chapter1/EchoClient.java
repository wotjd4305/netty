package org.example.chapter1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public final class EchoClient {
    public static void main(String args[]) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();    //NIO 처리를 다루는 이벤트 루프 인스턴스 생성
        try {
            Bootstrap b = new Bootstrap();                //클라이언트 를 셋팅 할수 있는 인스턴스 bootStrap 생성
            b.group(group)
                    .channel(NioSocketChannel.class)            // Nio 전송 채널을 사용 하도록 셋팅
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new EchoClientHandler());
                        }
                    });
            ChannelFuture f = b.connect("localhost", 8888).sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
