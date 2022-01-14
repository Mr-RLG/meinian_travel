package com.rlg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.rlg.dao.MemberDao;
import com.rlg.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberDao memberDao;

    @Override
    public List<Integer> getMemberReport(List<String> list) {
        List<Integer> memeberCountList  = new ArrayList<>();
        for(int i = 0;i < list.size();i++){
            String regTime = list.get(i);
            int count = memberDao.getMemberReportByRegTime(regTime);
            memeberCountList.add(count);
        }
        return memeberCountList;

    }
}
