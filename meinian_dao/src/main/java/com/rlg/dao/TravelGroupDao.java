package com.rlg.dao;

import com.github.pagehelper.Page;
import com.rlg.pojo.TravelGroup;

import java.util.List;
import java.util.Map;

public interface TravelGroupDao {
    void setTravelGroupAndTravlItem(Map map);

    void add(TravelGroup travelGroup);

    Page<TravelGroup> findPage(String queryString);

    TravelGroup findById(Integer id);

    List<Integer> findTravelItemIdByTravelgroupId(Integer id);

    void edit(TravelGroup travelGroup);

    void deleteTravelGroupAndTravelItemByTravelGroupId(Integer id);

    List<TravelGroup> findAll();

    TravelGroup findTravelGroupListById(Integer id);
}
