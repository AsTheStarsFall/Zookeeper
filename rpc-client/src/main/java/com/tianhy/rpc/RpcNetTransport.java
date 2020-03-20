package com.tianhy.rpc;

import com.tianhy.request.RpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.*;


/**
 * {@link}
 *
 * @Desc: 消息传输
 * @Author: thy
 * @CreateTime: 2019/6/19
 **/
public class RpcNetTransport extends SimpleChannelInboundHandler<Object> {

    private String serviceAddress;

    private Object result;

    public RpcNetTransport(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public Object send(RpcRequest rpcRequest) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0,
                                    4, 0, 4));
                            ch.pipeline().addLast(new LengthFieldPrepender(4));
                            //参数编解码器
                            ch.pipeline().addLast("encoder", new ObjectEncoder());
                            ch.pipeline().addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                            ch.pipeline().addLast(RpcNetTransport.this);
                        }
                    });

            String[] split = serviceAddress.split(":");
            ChannelFuture future = bootstrap.connect(split[0], Integer.valueOf(split[1])).sync();
            future.channel().writeAndFlush(rpcRequest).sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();

        }
        return result;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.result = msg;
    }

}
