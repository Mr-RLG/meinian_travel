package com.rlg.dao;


import com.github.pagehelper.Page;
import com.rlg.pojo.TravelItem;

import java.util.List;

public interface TravelItemDao {

    void delete(Integer id);

    void add(TravelItem travelItem);

    Page<TravelItem> findPage(String query);

    TravelItem findById(Integer id);

    void edit(TravelItem travelItem);

    List<TravelItem> findAll();

    List<TravelItem> findTravelItemListById(Integer id);
}
