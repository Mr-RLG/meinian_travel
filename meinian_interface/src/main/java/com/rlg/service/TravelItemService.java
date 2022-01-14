package com.rlg.service;

import com.rlg.entity.PageResult;
import com.rlg.pojo.TravelItem;

import java.util.List;

public interface TravelItemService {  //ctrl + alt +b 进入实现类


    void add(TravelItem travelItem);

    void delete(Integer id);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    TravelItem findById(Integer id);

    void edit(TravelItem travelItem);

    List<TravelItem> findAll();
}
