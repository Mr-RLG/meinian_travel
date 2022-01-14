package com.rlg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rlg.constant.MessageConstant;
import com.rlg.entity.PageResult;
import com.rlg.entity.QueryPageBean;
import com.rlg.entity.Result;
import com.rlg.pojo.TravelGroup;
import com.rlg.service.TravelGroupService;
import org.apache.ibatis.session.ResultContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/travelgroup")
public class TravelGroupController {

    @Reference
    TravelGroupService travelGroupService;

    @RequestMapping("/add")
    public Result add(Integer[] travelItemIds,@RequestBody TravelGroup travelGroup){
        travelGroupService.add(travelItemIds,travelGroup);
        return new Result(true, MessageConstant.ADD_TRAVELGROUP_SUCCESS);
    }


    @RequestMapping("/findAll")
    public Result findAll(){

        List<TravelGroup> list = travelGroupService.findAll();
        if(list != null && list.size() > 0){
            return new Result(true,MessageConstant.QUERY_TRAVELGROUP_SUCCESS,list);
        }
        return new Result(true,MessageConstant.QUERY_TRAVELGROUP_FAIL);
    }


    @RequestMapping("/findTravelItemIdByTravelgroupId")
    public List findTravelItemIdByTravelgroupId(Integer id){
       List<Integer> list = travelGroupService.findTravelItemIdByTravelgroupId(id);
        System.out.println(list+"        list--"+id+"---          -- ");
        return list;
    }

    @RequestMapping("/edit")
    public Result edit(Integer[] travelItemIds,@RequestBody TravelGroup travelGroup){
        travelGroupService.edit(travelItemIds,travelGroup);
        return new Result(true, MessageConstant.EDIT_TRAVELGROUP_SUCCESS);
    }


    @RequestMapping("/findById")
    public Result findById(Integer id){

        TravelGroup travelGroup = travelGroupService.findById(id);
        return new Result(true,MessageConstant.QUERY_TRAVELGROUP_SUCCESS,travelGroup);
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

        PageResult pageResult = travelGroupService.findPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
        return pageResult;
    }
}
