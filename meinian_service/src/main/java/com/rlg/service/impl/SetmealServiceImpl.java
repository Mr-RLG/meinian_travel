package com.rlg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rlg.dao.SetmealDao;
import com.rlg.entity.PageResult;
import com.rlg.pojo.Setmeal;
import com.rlg.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealDao setmealDao;

    @Override
    public void add(Integer[] travelgroupIds, Setmeal setmeal) {

        // 新增套餐
        setmealDao.add(setmeal);
        //根据主键回填 获取id
        Integer id = setmeal.getId();
        // 2：向套餐和跟团游的中间表中插入数据
        //绑定套餐和跟团游的多对多关系
        if(travelgroupIds != null && travelgroupIds.length > 0){
            setSetmealAndTravelGroup(id,travelgroupIds);
        }

    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {

        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page = setmealDao.findPage(queryString);
        return new PageResult(page.getTotal(),page);

    }

    @Override
    public List<Setmeal> getSetmeal() {
        return  setmealDao.getSetmeal();
    }

    @Override
    public Setmeal findById(Integer id) {

        return setmealDao.findById(id);

    }

    @Override
    public Setmeal getSetmealById(Integer id) {
        return setmealDao.getSetmealById(id);
    }

    @Override
    public List<Map<String, Object>>  findSetmealCount() {
        return setmealDao.findSetmealCount();
    }

    private void setSetmealAndTravelGroup(Integer id, Integer[] travelgroupIds) {
        for (Integer travelgroupid: travelgroupIds) {
            HashMap<String , Integer> map = new HashMap<>();
            map.put("travelgroup_id",travelgroupid);
            map.put("setmeal_id",id);
            setmealDao.setSetmealAndTravelGroup(map);
        }
    }
}
