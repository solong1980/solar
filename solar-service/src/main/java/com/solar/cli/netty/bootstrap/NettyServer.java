package com.solar.cli.netty.bootstrap;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.solar.cli.netty.controller.MsgDispatcher;
import com.solar.cli.netty.net.HeartbeatServerHandler;
import com.solar.cli.netty.net.SolarAPPClientHandler;
import com.solar.cli.netty.net.codec.SolarAPPDecoder;
import com.solar.cli.netty.net.codec.SolarAPPEncoder;
import com.solar.db.InitDBServers;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class NettyServer {
	private static final int READ_IDEL_TIME_OUT = Integer.MAX_VALUE; // 读超时
	private static final int WRITE_IDEL_TIME_OUT = Integer.MAX_VALUE;// 写超时
	private static final int ALL_IDEL_TIME_OUT = 30; // 所有超时s

	public static MsgDispatcher msgDispatcher = new MsgDispatcher();;

	private int port;

	public NettyServer(int port) {
		try {
			InitDBServers.getInstance().initServersFun();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		this.port = port;
	}

	public void run() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup);
			b.channel(NioServerSocketChannel.class);

			b.handler(new LoggingHandler(LogLevel.INFO));

			b.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();
					pipeline.addLast(new IdleStateHandler(READ_IDEL_TIME_OUT, WRITE_IDEL_TIME_OUT, ALL_IDEL_TIME_OUT,
							TimeUnit.SECONDS));
					pipeline.addLast(new HeartbeatServerHandler());
					pipeline.addLast("decoder", new SolarAPPDecoder());
					pipeline.addLast("encoder", new SolarAPPEncoder());
					pipeline.addLast(new SolarAPPClientHandler());
				}
			});
			b.option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

			ChannelFuture f = b.bind(port).sync();

			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8080;
		}
		new NettyServer(port).run();
	}
}