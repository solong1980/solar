package com.dyz.test.gamemock;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.string.StringDecoder;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class HelloClient {
	private static ScheduledExecutorService executor = Executors
			.newScheduledThreadPool(1);
	public static final int PORT = 8300;
	private static final String IP = "localhost";

	public static EventLoopGroup group = new NioEventLoopGroup();

	public static void main(String[] args) {
		HelloClient client = new HelloClient();
		client.connect();
		for (int i = 0; i < 3; i++) {

		}
	}

	public void connect() {
		// Client服务启动器
		Bootstrap b = new Bootstrap();
		b.group(group).channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch)
							throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast(new StringDecoder());
						pipeline.addLast(new MyEncoder());
						pipeline.addLast(new HelloClientHandler());
					}
				});
		// 启动客户端
		ChannelFuture f;
		try {
			f = b.connect(IP, PORT).sync();
			// 等待连接关闭
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
			executor.submit(new Runnable() {

				@Override
				public void run() {
					HelloClient client = new HelloClient();
					client.connect();
				}
			});
		}
	}

	private static class MyEncoder extends MessageToByteEncoder<Object> {

		public MyEncoder() {
		}

		@Override
		protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out)
				throws Exception {
			String m = (String) msg;
			out.writeInt(2);
			out.writeInt(2);
			out.writeInt(2);
			out.writeInt(2);
			out.writeInt(2);
			out.writeInt(1);
			out.writeInt(123);
			out.writeInt(m.length());
			out.writeBytes(m.getBytes());
		}

	}

	private static class HelloClientHandler extends
			ChannelInboundHandlerAdapter {

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) {
			ByteBuf m = (ByteBuf) msg; // (1)
			try {
				long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
				System.out.println(new Date(currentTimeMillis));
				ctx.close();
			} finally {
				m.release();
			}
		}

		// 连接成功后，向server发送消息
		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			String msg = "Are you ok?";
			ctx.write(msg);
			ctx.flush();
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
			cause.printStackTrace();
			ctx.close();
		}
	}

}
