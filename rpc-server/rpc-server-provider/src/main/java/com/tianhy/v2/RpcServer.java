package com.tianhy.v2;

import com.tianhy.v2.registry.IRegistryCenter;
import com.tianhy.v2.registry.RegistryCenterWithZk;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;


/**
 * {@link}
 *
 * @Desc: RPC服务端
 * @Author: thy
 * @CreateTime: 2019/6/19
 **/
@Component
public class RpcServer implements ApplicationContextAware, InitializingBean {

    Map<String, Object> handlerMap = new HashMap<>();

    IRegistryCenter registryCenter = new RegistryCenterWithZk();

    //读取配置文件
    ResourceBundle resourceBundle = ResourceBundle.getBundle("application");


    private int port;

    public RpcServer(int port) {
        this.port = port;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //通过注解获取到beans
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (!beansWithAnnotation.isEmpty()) {
            for (Object bean : beansWithAnnotation.values()) {
                RpcService annotation = bean.getClass().getAnnotation(RpcService.class);
                String name = annotation.value().getName();
                String version = annotation.version();
                if (StringUtils.isNotBlank(version)) {
                    name += "-" + version;
                }
                handlerMap.put(name, bean);
                //服务注册
                registryCenter.registry(name, "127.0.0.1:" + port);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0,
                                    4, 0, 4));
                            ch.pipeline().addLast(new LengthFieldPrepender(4));
                            //参数编解码器
                            ch.pipeline().addLast("encoder", new ObjectEncoder());
                            ch.pipeline().addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                            ch.pipeline().addLast(new ProcessHandler(handlerMap));
                        }
                    });
            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } finally {

        }


    }
}
