package com.rlg.dao;

import com.rlg.pojo.Member;

public interface MemberDao {
    Member findMemberByTelephone(String telephone);

    void add(Member member);

    int getMemberReportByRegTime(String regTime);

    int getTodayNewMember(String today);

    int getTotalMember();

    int getThisWeekAndMonthNewMember(String date);
}
