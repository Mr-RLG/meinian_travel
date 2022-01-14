package com.rlg.dao;

import com.rlg.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    long findCountByDate(Date orderDate);

    void editNumberByDate(OrderSetting orderSetting);

    void add(OrderSetting orderSetting);

    List<OrderSetting> getOrderSettingByMonth(Map<String, Object> map);

    OrderSetting findOrderSetingByOrderDate(Date date);

    void editReservationByOrderDate(OrderSetting orderSetting);
}
