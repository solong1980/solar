package com.dyz.test.gamemock;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.List;

public class HelloServer {
	private static NioEventLoopGroup bossGroup = new NioEventLoopGroup();
	private static NioEventLoopGroup workGroup = new NioEventLoopGroup();

	public static void main(String args[]) {
		// Server服务启动器
		ServerBootstrap bootstrap = new ServerBootstrap();

		bootstrap.group(bossGroup, workGroup);
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.option(ChannelOption.SO_BACKLOG, 128);
		bootstrap.option(ChannelOption.TCP_NODELAY, true);
		bootstrap.option(ChannelOption.SO_REUSEADDR, true);
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

		bootstrap.handler(new NettyServerHandler());

		// 设置一个处理客户端消息和各种消息事件的类(Handler)
		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast(new MyDecoder(), new StringEncoder(),
						new NettySocketInboundHandler());
			}
		});
		// 开放8000端口供客户端访问。
		ChannelFuture future = bootstrap.bind(new InetSocketAddress(8300));
		if (future.isSuccess()) {

		}
		
		
	}

	public static void shut() {
		bossGroup.shutdownGracefully();
		workGroup.shutdownGracefully();
	}

	public enum MyDecoderState {
		READ_LENGTH, READ_CONTENT;
	}

	public static class Msg {
		public int readFlag;
		public int length;
		public int msgCode;
		public String content;
	}

	private static class MyDecoder extends ReplayingDecoder<MyDecoderState> {
		private int readFlag;
		private int length;
		private int msgCode;

		public MyDecoder() {
			super(MyDecoderState.READ_LENGTH);
		}

		@Override
		protected void decode(ChannelHandlerContext ctx, ByteBuf buf,
				List<Object> out) throws Exception {
			switch (state()) {
			case READ_LENGTH:
				readFlag = buf.readInt();
				if (readFlag == 1) {
					msgCode = buf.readInt();
					length = buf.readInt();
					checkpoint(MyDecoderState.READ_CONTENT);
				} else {
					break;
				}
			case READ_CONTENT:
				if (buf.readableBytes() < length)
					break;
				byte[] req = new byte[length];
				buf.readBytes(req);
				String body = new String(req, "UTF-8");

				Msg msg = new Msg();
				msg.readFlag = readFlag;
				msg.length = length;
				msg.msgCode = msgCode;
				msg.content = body;
				checkpoint(MyDecoderState.READ_LENGTH);
				out.add(msg);
				break;
			default:
				throw new Error("Shouldn't reach here.");
			}
		}

	}

	private static class MyEncoder extends MessageToByteEncoder<Object> {

		public MyEncoder() {
		}

		@Override
		protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out)
				throws Exception {
			String content = msg.toString();
			int length = content.getBytes().length;
			ByteBuf buffer = ctx.alloc().buffer(4 + 4 + length);
			buffer.writeInt(1);
			buffer.writeInt(length);
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			DataOutputStream dataOutputStream = new DataOutputStream(b);
			dataOutputStream.writeUTF(content);
			out.writeBytes(b.toByteArray());
		}

	}

	private static class NettySocketOutboundHandler extends
			ChannelOutboundHandlerAdapter {

		@Override
		public void write(ChannelHandlerContext ctx, Object msg,
				ChannelPromise promise) throws Exception {
			super.write(ctx, msg, promise);
		}
	}

	private static class NettySocketInboundHandler extends
			ChannelInboundHandlerAdapter {
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg)
				throws Exception {
			System.out.println("NettyServerInboundHandler.channelRead");
			super.channelRead(ctx, msg);
			System.out.println(msg.getClass());

			ctx.write("HelloWorld");
			ctx.flush();
		}

		@Override
		public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
			System.out.println("NettyServerInboundHandler.handlerAdded");
			super.handlerAdded(ctx);
		}

		@Override
		public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
			System.out.println("NettyServerInboundHandler.handlerRemoved");
			super.handlerRemoved(ctx);
		}

	}

	private static class NettyServerHandler extends ChannelHandlerAdapter {
		@Override
		public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
			System.out.println("NettyServerHandler.handlerAdded");
			super.handlerAdded(ctx);
		}

		@Override
		public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
			System.out.println("NettyServerHandler.handlerRemoved");
			super.handlerRemoved(ctx);
		}

	}
}
