package com.rlg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.rlg.dao.OrderSettingDao;
import com.rlg.pojo.OrderSetting;
import com.rlg.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    OrderSettingDao orderSettingDao;

    @Override
    public void add(ArrayList<OrderSetting> orderSettings) {
        for (OrderSetting orderSetting : orderSettings) {
            // 判断当前的日期之前是否已经被设置过预约日期，使用当前时间作为条件查询数量
            long count = orderSettingDao.findCountByDate(orderSetting.getOrderDate());
            if(count > 0){
                // 如果设置过预约日期，更新number数量
                orderSettingDao.editNumberByDate(orderSetting);
            }else {
                // 如果没有设置过预约日期，执行保存
                orderSettingDao.add(orderSetting);
            }
        }
    }


    /**  传递的参数
     *   date（格式：2019-8）
     *  构建的数据List<Map>
     *    map.put("date",1);
     map.put("number",120);
     map.put("reservations",10);

     *  查询方案：SELECT * FROM t_ordersetting WHERE orderDate LIKE '2019-08-%'
     *  查询方案：SELECT * FROM t_ordersetting WHERE orderDate BETWEEN '2019-9-1' AND '2019-9-31'
     */
//根据日期查询预约设置数据
    @Override
    public List<Map> getOrderSettingByMonth(String date) {

        String startDate = date+"-1"; //2019-12-1
        String endDate = date+"-31";
        Map<String, Object> map = new HashMap<>();
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
        // 3.将List<OrderSetting>，组织成List<Map>
        List<Map> data = new ArrayList<>();
        for (OrderSetting orderSetting : list) {
            Map orderSettingMap = new HashMap<>();
            orderSettingMap.put("date",orderSetting.getOrderDate().getDate());
            orderSettingMap.put("number",orderSetting.getNumber());
            orderSettingMap.put("reservations",orderSetting.getReservations());
            data.add(orderSettingMap);
        }
        return data;
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        System.out.println("---------------orderDatre"+orderSetting.getOrderDate()+"------------");
        long count = orderSettingDao.findCountByDate(orderSetting.getOrderDate());
        if(count>0){
            orderSettingDao.editNumberByDate(orderSetting);
        }else {
            orderSettingDao.add(orderSetting);
        }
    }

}
