package com.study.netty.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * {@link}
 *
 * @Desc: 自定义协议,必须实现序列化
 * @Author: thy
 * @CreateTime: 2019/8/7 1:25
 **/
public class SelfDefineProtocol implements Serializable {

    private String className;
    private String methodName;
    private Class<?> [] parameters;
    private Object[]values;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameters() {
        return parameters;
    }

    public void setParameters(Class<?>[] parameters) {
        this.parameters = parameters;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }
}
