package org.example.chapter5;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.example.chapter1.EchoServerHandler;

public class EchoServer {
    static Integer PORT = 8888;

    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(); // 인수가 없는 경우, 하드웨어 CPU 코어 수의 2배

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


//            ChannelFuture f = b.bind(PORT).sync();
//            f.channel().closeFuture().sync();

            // 위의 주석을 풀어 씀.
            ChannelFuture bindFuture = b.bind(8888); // 에코 서버가 8888을 바인딩 하도록 호출, 바인딩 완료 전에 ChannelFuture 객체 돌려줌
            bindFuture.sync(); // 해당 객체의 작업이 완료 될 떄까지 바인딩
            Channel serverChannel = bindFuture.channel();
            ChannelFuture closeFuture = serverChannel.closeFuture(); // 네티 내부에서 채널이 생성될떄 CloseFuture 객체도 생성 됨.
            closeFuture.sync(); // 종료 될떄까지 연결 종료 이벤트를 받으며, CloseFuture에는 동작설정이 없음으로 아무동작안함.

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
