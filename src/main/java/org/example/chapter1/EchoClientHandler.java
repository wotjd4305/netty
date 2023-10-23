package org.example.chapter1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { // 최초 실행 시
        String sendMessage = "Hello, Netty!";
        ByteBuf messageBuffer = Unpooled.buffer(); messageBuffer.writeBytes(sendMessage.getBytes());
        StringBuilder builder = new StringBuilder();

        builder.append("전송한 문자열 [");
        builder.append(sendMessage);
        builder.append(")");

        System.out.println(builder.toString());
        ctx.writeAndFlush(messageBuffer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception { // 서버로 부터 수신된 데이터가 있을 떄 발생
        String readMessage = ((ByteBuf)msg).toString(Charset.defaultCharset());

        StringBuilder builder = new StringBuilder();
        builder.append("수신한 문자열 [");
        builder.append(readMessage);
        builder.append("]");

        System.out.println(builder.toString());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception { // 수신된 데이터를 모두 읽었을때, channelRead 완료시 자동 호출
        ctx.close();


    }
}
