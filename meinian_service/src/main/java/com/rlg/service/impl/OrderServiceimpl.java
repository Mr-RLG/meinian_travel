package com.rlg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.rlg.constant.MessageConstant;
import com.rlg.dao.MemberDao;
import com.rlg.dao.OrderDao;
import com.rlg.dao.OrderSettingDao;
import com.rlg.entity.Result;
import com.rlg.pojo.Member;
import com.rlg.pojo.Order;
import com.rlg.pojo.OrderSetting;
import com.rlg.service.OrderService;
import com.rlg.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceimpl implements OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    OrderSettingDao orderSettingDao;

    @Autowired
    MemberDao memberDao;

    @Override
    public Result order(Map map) throws Exception {
        /*
        1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
        4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
        5、预约成功，更新当日的已预约人数
        * */
        int setmealId = Integer.parseInt((String) map.get("setmealId"));
        String orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        // 使用预约时间查询预约设置表，看看是否可以 进行预约

        OrderSetting orderSetting = orderSettingDao.findOrderSetingByOrderDate(date);
        // 如果预约设置表等于null，说明不能进行预约，压根就没有开团
        if(orderSetting == null){
            // 如果没有说明预约设置表没有进行设置，此时不能预约
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);

        }else {
        //2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
            int number = orderSetting.getNumber();
            int reservations = orderSetting.getReservations();
        //如果预约人数大于等于最大预约数，此时不能预约，提示“预约已满”
            if(number <= reservations) {
                return new Result(false, MessageConstant.ORDER_FULL);
            }
        }

//        3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
        // 根据手机号，查询会员表，判断当前预约人是否是会员
        String telephone = (String) map.get("telephone");

        Member member = memberDao.findMemberByTelephone(telephone);
        //如果是会员，防止重复预约（一个会员、一个时间、一个套餐不能重复，否则是重复预约） 非会员 就快速注册
        if(member == null){
            member = new Member();
            member.setName((String) map.get("name"));
            member.setSex((String)map.get("age"));
            member.setIdCard((String)map.get("idCard"));
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());

            memberDao.add(member);  //需要主键回填
        }else{
            // 检查是否重复预约
            //将findOrderByCondition方法封装为一个通用方法，可以实现动态SQL查询
            Order order = new Order();
            order.setOrderDate(date);
            order.setMemberId(member.getId());
            order.setSetmealId(setmealId);
            List<Order> list = orderDao.findOrderByCondition(order);
            //已经有了 重复预约了
            if(list != null && list.size() > 0){
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }

        //        4.进行预约
        //         - 向t_order表插入一条记录
        //         - t_ordersetting表里面预约的人数reservations+1
        Order order = new Order();
        order.setSetmealId(setmealId);
        order.setMemberId(member.getId());
        order.setOrderDate(date);
        order.setOrderType(Order.ORDERTYPE_WEIXIN);
        order.setOrderStatus(Order.ORDERSTATUS_NO);

        orderDao.add(order);  //主键回填

        //         - t_ordersetting表里面预约的人数reservations+1
        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.editReservationByOrderDate(orderSetting);

        return new Result(true,MessageConstant.ORDER_SUCCESS,order);
    }

    @Override
    public Map findById(Integer id) {
        return orderDao.findById(id);
    }
}
