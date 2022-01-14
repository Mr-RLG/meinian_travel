package com.rlg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rlg.dao.TravelItemDao;
import com.rlg.entity.PageResult;
import com.rlg.pojo.TravelItem;
import com.rlg.service.TravelItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = TravelItemService.class)
@Transactional
public class travelItemServiceImpl implements TravelItemService {

    @Autowired
    private TravelItemDao travelItemDao;


    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {

        // 不使用分页插件PageHelper
        // 至少写2条sql语句完成查询
        // 第1条，select count(*) from t_travelitem，查询的结果封装到PageResult中的total
        // 第2条，select * from t_travelitem where NAME = '001' OR CODE = '001' limit ?,?
        //（0,10）（10,10）(（currentPage-1)*pageSize,pageSize）
        // 使用分页插件PageHelper（简化上面的操作）
        // 1：初始化分页操作
        PageHelper.startPage(currentPage,pageSize);
        System.out.println("---------------"+queryString+"+-----------11");
        // 2：使用sql语句进行查询（不必在使用mysql的limit了）
        Page<TravelItem> page = travelItemDao.findPage(queryString);
        System.out.println("---------------"+queryString+"+-----------22");
        // 3：封装
        return new PageResult(page.getTotal(),page.getResult());

    }

    //ctrl+i  实现接口方法
    @Override
    public void add(TravelItem travelItem) {

        travelItemDao.add(travelItem);

    }

    @Override
    public void edit(TravelItem travelItem) {
        travelItemDao.edit(travelItem);
    }

    @Override
    public List<TravelItem> findAll() {
        return travelItemDao.findAll();
    }

    @Override
    public void delete(Integer id) {
        travelItemDao.delete(id);
    }

    @Override
    public TravelItem findById(Integer id) {
        return travelItemDao.findById(id);
    }
}
