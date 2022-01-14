package com.rlg.controller;

import com.rlg.constant.MessageConstant;
import com.rlg.constant.RedisMessageConstant;
import com.rlg.entity.Result;
import com.rlg.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        //发送短信 暂时不做了 没钱
        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,5*60, String.valueOf(code));

        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);

    }

}
