package com.rlg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.rlg.constant.MessageConstant;
import com.rlg.entity.Result;
import com.rlg.pojo.OrderSetting;
import com.rlg.service.OrderSettingService;
import com.rlg.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orderSetting")
public class OrderSettingController {

    @Reference
    OrderSettingService orderSettingService;

    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            System.out.println("=---controller-----------+orderSetting"+orderSetting+"----------");
            orderSettingService.editNumberByDate(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,MessageConstant.ORDERSETTING_FAIL);
    }

    /**
     * 根据日期查询预约设置数据(获取指定日期所在月份的预约设置数据)
     * @param date
     * @return
     */
    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){

        try {
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(true,MessageConstant.GET_ORDERSETTING_FAIL);
    }

    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile){
        //1.使用POI解析文件 得到List<String[]> list
        //2.把List<String[]> list转成 List<OrderSetting> list
        //3.调用业务 进行保存

        try {
            List<String[]> lists = POIUtils.readExcel(excelFile);
            // 把List<String[]> 数据转换成 List<OrderSetting>数据
            ArrayList<OrderSetting> orderSettings = new ArrayList<>();
            //  迭代里面的每一行数据，进行封装到集合里面
            for (String[] strings : lists) {

                orderSettings.add(new OrderSetting(
                        new Date(strings[0]),Integer.parseInt(strings[1])
                ));
            }
            System.out.println("------------------------"+orderSettings);
            orderSettingService.add(orderSettings);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
    }
}
