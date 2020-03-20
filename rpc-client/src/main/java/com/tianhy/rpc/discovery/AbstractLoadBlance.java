package com.tianhy.rpc.discovery;

import java.util.List;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2020/1/11 7:45
 **/
public abstract class AbstractLoadBlance implements LoadBlanceStrategy {
    @Override
    public String selectHost(List<String> list) {
        if(list.isEmpty()){
            return null;
        }
        if(list.size()==1){
            return list.get(0);
        }
        return doSelect(list);
    }

    protected abstract String doSelect(List<String> list);
}
