package com.rlg.dao;

import com.github.pagehelper.Page;
import com.rlg.pojo.Address;

import java.util.List;
import java.util.Map;

public interface AddressDao {
    List<Address> findAllMaps();

    Page<Address> findPage(String queryString);

    void deleteById(Integer id);

    void addAddress(Map map);
}
