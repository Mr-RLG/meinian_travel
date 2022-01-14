package com.rlg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.rlg.constant.MessageConstant;
import com.rlg.entity.Result;
import com.rlg.pojo.Setmeal;
import com.rlg.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {

    @Reference
    SetmealService setmealService;

    @RequestMapping("/getSetmeal")
    public Result getSetmeal(){

        List<Setmeal> list = setmealService.getSetmeal();
        return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
    }

    //显示详情
    @RequestMapping("/findById")
    public Result findById(Integer id){
         Setmeal setmeal = setmealService.findById(id);
         return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }
    //只查套餐对象，不查关联表数据
    @RequestMapping("/getSetmealById")
    public Result getSetmealById(Integer id){

        Setmeal setmeal = setmealService.getSetmealById(id);
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }

}
