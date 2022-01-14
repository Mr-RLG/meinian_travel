package com.rlg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rlg.dao.TravelGroupDao;
import com.rlg.entity.PageResult;
import com.rlg.pojo.TravelGroup;
import com.rlg.service.TravelGroupService;
import com.rlg.service.TravelItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = TravelGroupService.class)
@Transactional
public class TravelGroupServiceImpl implements TravelGroupService {

    @Autowired
    TravelGroupDao travelGroupDao;

    @Override
    public void edit(Integer[] travelItemIds, TravelGroup travelGroup) {
        travelGroupDao.edit(travelGroup);

        /**
         * 2：修改跟团游和自由行的中间表（先删除，再创建）
         * 之前的数据删除
         * 再新增页面选中的数据
         */
        // 删除之前中间表的数据
        travelGroupDao.deleteTravelGroupAndTravelItemByTravelGroupId(travelGroup.getId());
        // 再新增页面选中的数据
        setTravelGroupAndTravlItem(travelGroup.getId(),travelItemIds);


    }

    @Override
    public List<TravelGroup> findAll() {
        return travelGroupDao.findAll();
    }


    @Override
    public void add(Integer[] travelItemIds, TravelGroup travelGroup) {
        // 1 新增跟团游，向t_travelgroup中添加数据，新增后返回新增的id
        travelGroupDao.add(travelGroup);
        // 2 新增跟团游和自由行中间表t_travelgroup_travelitem新增数据(新增几条，由travelItemIds决定)
        setTravelGroupAndTravlItem(travelGroup.getId(),travelItemIds);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {

        PageHelper.startPage(currentPage,pageSize);
        Page<TravelGroup> page = travelGroupDao.findPage(queryString);
//        System.out.println(page.getResult()+"      -    ----------------");
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public TravelGroup findById(Integer id) {

        return travelGroupDao.findById(id);

    }

    @Override
    public List<Integer> findTravelItemIdByTravelgroupId(Integer id) {

        return travelGroupDao.findTravelItemIdByTravelgroupId(id);

    }

    private void setTravelGroupAndTravlItem(Integer id, Integer[] travelItemIds) {
        if(travelItemIds != null && travelItemIds.length > 0){
           for(int i = 0;i < travelItemIds.length;i++){
               Map map = new HashMap<>();
               map.put("travelGroup",id);
               map.put("travelItem",travelItemIds[i]);
               travelGroupDao.setTravelGroupAndTravlItem(map);
           }
        }
    }
}
