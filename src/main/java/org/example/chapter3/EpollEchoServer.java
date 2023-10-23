package org.example.chapter3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.example.chapter1.EchoServerHandler;

public class EpollEchoServer {
    static Integer PORT = 8888;

    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new EpollEventLoopGroup(1);
        EventLoopGroup workerGroup = new EpollEventLoopGroup(); // 인수가 없는 경우, 하드웨어 CPU 코어 수의 2배

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup) // 첫번쨰는 클라이언트 연결 수락 담당하는 스레드, 두뻔째는 연결된 소켓에 대한 I/O 처리
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer() { // 자식 초기화 방법
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new EchoServerHandler()); // 데이터 처리
                        }
                    });

            ChannelFuture f = b.bind(PORT).sync();

            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
