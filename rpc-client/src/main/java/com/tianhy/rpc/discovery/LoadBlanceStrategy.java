package com.tianhy.rpc.discovery;

import java.util.List;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2020/1/11 7:44
 **/
public interface LoadBlanceStrategy {

    String selectHost(List<String> list);
}
