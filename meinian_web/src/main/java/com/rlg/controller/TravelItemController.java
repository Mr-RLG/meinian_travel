package com.rlg.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.rlg.constant.MessageConstant;
import com.rlg.entity.PageResult;
import com.rlg.entity.QueryPageBean;
import com.rlg.entity.Result;
import com.rlg.pojo.TravelItem;
import com.rlg.service.TravelItemService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/travelItem")
public class TravelItemController {

    //远程注入
    @Reference
    private TravelItemService travelItemService;


    /**
     * 依据id查出对应的自由行
     * @param
     * @return
     */
    @RequestMapping("/findAll")
    public Result  findAll(){
        List<TravelItem> list = null;
        try {
            list = travelItemService.findAll();
        } catch (Exception e) {
            return new Result(false,MessageConstant.QUERY_TRAVELITEM_FAIL,list);
        }
        return new Result(true,MessageConstant.QUERY_TRAVELITEM_SUCCESS,list);
    }

    /**
     * 依据id查出对应的自由行
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        TravelItem travelItem = null;
        try {
            travelItem = travelItemService.findById(id);
        } catch (Exception e) {
            return new Result(false,MessageConstant.QUERY_TRAVELITEM_FAIL,travelItem);
        }
        return new Result(true,MessageConstant.QUERY_TRAVELITEM_SUCCESS,travelItem);
    }


    /***
     * 编辑自由行
     * @param travelItem
     * @return
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody TravelItem travelItem){

        try {
            travelItemService.edit(travelItem);  // ctrl + Alt + t 异常
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_TRAVELITEM_FAIL);
        }

        return new Result(true, MessageConstant.EDIT_TRAVELITEM_SUCCESS);
    }
    /***
     * 删除自由行
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Integer id){

        try {
            travelItemService.delete(id);  // ctrl + Alt + t 异常
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_TRAVELGROUP_FAIL);
        }

        return new Result(true, MessageConstant.DELETE_TRAVELGROUP_SUCCESS);
    }

    /***
     * 添加自由行
     * @param travelItem
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody TravelItem travelItem){

        try {
            travelItemService.add(travelItem);  // ctrl + Alt + t 异常
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_TRAVELITEM_FAIL);
        }

        return new Result(true, MessageConstant.ADD_TRAVELITEM_SUCCESS);
    }


    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

       PageResult pageResult = travelItemService.findPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
       return pageResult;

    }
}
