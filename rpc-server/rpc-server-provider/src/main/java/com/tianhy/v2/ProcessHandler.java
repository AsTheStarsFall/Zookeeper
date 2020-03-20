package com.tianhy.v2;

import com.tianhy.request.RpcRequest;
import io.netty.channel.*;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * {@link}
 *
 * @Desc: 请求处理类
 * @Author: thy
 * @CreateTime: 2019/6/19
 **/
public class ProcessHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private Map<String, Object> handlerMap;

    public ProcessHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    private Object invoke(RpcRequest rpcRequest) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String clazzName = rpcRequest.getClassName();
        String version = rpcRequest.getVersion();

        if (!StringUtils.isEmpty(version)) {
            clazzName += "-" + version;
        }

        Object service = handlerMap.get(clazzName);
        if (service == null) {
            throw new RuntimeException("service not found :" + clazzName);
        }

        Object[] parameters = rpcRequest.getParameters();
        Method method = null;
        if (parameters != null) {
            Class<?>[] types = new Class[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                types[i] = parameters[i].getClass();
            }
            Class<?> clazz = Class.forName(rpcRequest.getClassName());
            method = clazz.getMethod(rpcRequest.getMethodName(), types);
        } else {
            Class<?> clazz = Class.forName(rpcRequest.getClassName());
            method = clazz.getMethod(rpcRequest.getMethodName());
        }
        //反射调用需要
        Object result = method.invoke(service, parameters);
        return result;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        Object result = invoke(msg);
        ctx.writeAndFlush(result).addListener(ChannelFutureListener.CLOSE);
    }
}
