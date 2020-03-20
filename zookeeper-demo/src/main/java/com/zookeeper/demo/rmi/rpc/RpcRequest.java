package com.zookeeper.demo.rmi.rpc;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/3/3
 **/
@Setter
@Getter
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = -9100893052391757993L;
    private String className;
    private String methodName;
    private Object[] parameters;
    private String version;
}
