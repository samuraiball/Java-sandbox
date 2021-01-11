package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import server.EchoOutBoundHandler;

import java.net.InetSocketAddress;

public final class ExampleClient {


    public static void main(String[] args) throws Exception {
        ExampleClient exampleClient = new ExampleClient();
        exampleClient.start();
    }


    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        EchoOutBoundHandler outBoundHandler = new EchoOutBoundHandler();
        EchoClient client = new EchoClient();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress("localhost", 8080))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(outBoundHandler)
                                    .addLast(client);
                        }
                    });
            ChannelFuture f = bootstrap.connect().sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }

    }

}
