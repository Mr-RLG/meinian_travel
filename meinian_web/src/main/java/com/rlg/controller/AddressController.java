package com.rlg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.rlg.entity.PageResult;
import com.rlg.entity.QueryPageBean;
import com.rlg.entity.Result;
import com.rlg.pojo.Address;
import com.rlg.service.AddressService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/address")
public class AddressController {

    @Reference
    AddressService addressService;

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult addresses = null;
        try {
            addresses = addressService.findPage(queryPageBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addresses;

    }


    @RequestMapping("/findAllMaps")
    public Map findAllMaps(){
        Map map = new HashMap<>();

        List<Address> addressList = addressService.findAllMaps();

        //1、定义分店坐标集合
        List<Map> gridnMaps=new ArrayList<>();
        //2、定义分店名称集合
        List<Map> nameMaps=new ArrayList();


        for (Address address : addressList) {
            Map gridnMap=new HashMap();
            Map nameMap=new HashMap();

            // 获取经度
            String lng = address.getLng();
            // 获取纬度
            String lat = address.getLat();
            gridnMap.put("lng",lng);
            gridnMap.put("lat",lat);
            gridnMaps.add(gridnMap);
            // 获取地址的名字
            String addressName = address.getAddressName();
            nameMap.put("addressName",addressName);
            nameMaps.add(nameMap);
        }

        map.put("gridnMaps",gridnMaps);
        map.put("nameMaps",nameMaps);
        return map;
    }

    @RequestMapping("/deleteById")
    public Result deleteById(Integer id){
        addressService.deleteById(id);
        return new Result(true, "已删除地址");
    }

    @RequestMapping("/addAddress")
    public  Result addAddress(@RequestBody Map map){

        addressService.addAddress(map);
        return new Result(true,"增加地址成功");

    }

}
