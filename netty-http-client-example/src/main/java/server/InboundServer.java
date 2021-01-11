package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class InboundServer {

    public static void main(String[] args) throws Exception {
        InboundServer inboundServer = new InboundServer();
        inboundServer.run();
    }

    public void run() throws Exception {
        final EchoInboundHandler echoInboundHandler = new EchoInboundHandler();
        final EchoOutBoundHandler echoOutBoundHandler = new EchoOutBoundHandler();
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            ServerBootstrap boot = new ServerBootstrap();
            boot.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(8080))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(echoOutBoundHandler)
                                    .addLast(echoInboundHandler);
                        }
                    });
            ChannelFuture cf = boot.bind().sync();
            cf.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }


    }
}
