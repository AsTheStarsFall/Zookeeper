package com.tianhy.study.orderservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * {@link}
 *
 * @Desc: 订单服务
 * @Author: thy
 * @CreateTime: 2020/1/7 6:45
 **/
@RestController
public class OrderController {

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    @GetMapping("/order")
    public String order() {
        System.out.println("开始下单");
        //扣减库存，调用其他服务

        RestTemplate template = restTemplateBuilder.build();
        String url = "http://localhost:8081/repo/{1}";
        template.put(url, null, 10001);

        return "SUCCESS";
    }
}
