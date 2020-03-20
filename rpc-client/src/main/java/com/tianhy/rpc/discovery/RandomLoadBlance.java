package com.tianhy.rpc.discovery;


import java.util.List;
import java.util.Random;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2020/1/11 7:49
 **/
public class RandomLoadBlance extends AbstractLoadBlance {
    @Override
    protected String doSelect(List<String> list) {
        int length = list.size();
        //从list集合随机取一个
        Random random = new Random();
        return list.get(random.nextInt(length));
    }
}
