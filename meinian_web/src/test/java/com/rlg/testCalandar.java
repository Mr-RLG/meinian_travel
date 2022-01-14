package com.rlg;

import org.junit.Test;

import java.util.Calendar;

public class testCalandar {

    @Test
    public void test01(){
        Calendar c = Calendar.getInstance();
//        c.add(Calendar.MONTH);
        System.out.println(c.getTime());
    }
//    Sat Mar 12 13:09:10 CST 2022
}
