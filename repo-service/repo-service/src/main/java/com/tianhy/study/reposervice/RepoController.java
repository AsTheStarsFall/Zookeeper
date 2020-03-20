package com.tianhy.study.reposervice;

import org.springframework.web.bind.annotation.*;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2020/1/7 6:59
 **/
@RestController
public class RepoController {

    @PutMapping("/repo/{pid}")
    public void repo(@PathVariable("pid") String pid) {

        System.out.println("扣减库存商品：" + pid);

    }
}
