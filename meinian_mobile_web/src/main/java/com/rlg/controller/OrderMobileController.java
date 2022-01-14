package com.rlg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.rlg.constant.MessageConstant;
import com.rlg.constant.RedisMessageConstant;
import com.rlg.entity.Result;
import com.rlg.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderMobileController {

    @Reference
    OrderService orderService;

    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/findById")
    public Result findById(Integer id){

        Map map = null;
        try {
            map = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }

    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map) {
//        System.out.println("map--------"+map);

//        - 获得用户信息
//        - 校验验证码(redis里面存的和用户输入的比较)
//        - 调用业务, 进行预约, 响应


        try {
            // ① 在页面获取手机号
            String telephone = (String) map.get("telephone");
            //从Redis中获取缓存的验证码，key为手机号+RedisConstant.SENDTYPE_ORDER
            String code = jedisPool.getResource().get(telephone+ RedisMessageConstant.SENDTYPE_ORDER);
            //② 在页面获取验证码
            String  validateCode = (String) map.get("validateCode");
            //校验手机验证码
            if(validateCode == null || !code.equals(validateCode) ){
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
            Result result =null;

            //调用旅游预约服务
            result = orderService.order(map);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDER_FAIL);
        }
    }
}
