package com.rlg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.rlg.constant.MessageConstant;
import com.rlg.constant.RedisConstant;
import com.rlg.entity.PageResult;
import com.rlg.entity.QueryPageBean;
import com.rlg.entity.Result;
import com.rlg.pojo.Setmeal;
import com.rlg.service.SetmealService;
import com.rlg.utils.AliyunUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    SetmealService setmealService;

    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile){
        try {
            //获取原文件名
            String originalFilename = imgFile.getOriginalFilename();
            // 找到.最后出现的位置
            int lastindex = originalFilename.lastIndexOf('.');
            //获取文件后缀
            String suffix  = originalFilename.substring(lastindex);
            //使用UUID随机产生文件名称，防止同名文件覆盖
            String filename = UUID.randomUUID().toString()+suffix ;
            AliyunUtil.uploadAliyun(imgFile.getInputStream(),filename);

            /***********后补代码***************/
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES ,filename);
            /***********后补代码***************/
            return  new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS);
    }

    @RequestMapping("/add")
    public Result add(Integer[] travelgroupIds,@RequestBody Setmeal setmeal){

        try {
            setmealService.add(travelgroupIds,setmeal);
            /***********后补代码***************/
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
            /***********后补代码***************/
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = setmealService.findPage(queryPageBean.getCurrentPage(),
                                    queryPageBean.getPageSize(),queryPageBean.getQueryString());
        return pageResult;
    }
}
