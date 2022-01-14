package com.rlg.service;

import com.rlg.entity.PageResult;
import com.rlg.entity.QueryPageBean;
import com.rlg.pojo.Address;

import java.util.List;
import java.util.Map;

public interface AddressService {
    List<Address> findAllMaps();

    PageResult findPage(QueryPageBean queryPageBean);

    void deleteById(Integer id);

    void addAddress(Map map);
}
