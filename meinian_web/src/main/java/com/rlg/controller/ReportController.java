package com.rlg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.rlg.constant.MessageConstant;
import com.rlg.entity.Result;
import com.rlg.service.MemberService;
import com.rlg.service.ReportService;
import com.rlg.service.SetmealService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    ReportService reportService;

    @Reference
    MemberService memberService;

    @Reference
    SetmealService setmealService;
    /***
     * 统计会员数量
     * @return
     */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        // 获取日历对象
        Calendar calendar = Calendar.getInstance();
        //根据当前时间，获取前12个月的日历(当前日历2020-02，12个月前，日历时间2019-03)
        //第一个参数，日历字段
        //第二个参数，要添加到字段中的日期或时间
        calendar.add(calendar.MONTH,-12);
        List<String> list = new ArrayList<>();
        for(int i = 0;i < 12;i++){
            calendar.add(calendar.MONTH,1);
            list.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
        }
        Map map = new HashMap<String,List>();
        map.put("months",list);
        List<Integer> memberCount = memberService.getMemberReport(list);
        map.put("memberCount",memberCount);
       return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }

    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        // 组织套餐名称+套餐名称对应的数据
        List<Map<String, Object>>  setmealList = setmealService.findSetmealCount();

        Map map = new HashMap<>();

        List<Object> setmealNames = new ArrayList<>();
//        List<Object> setmealCount = new ArrayList<>();
//        for (Map<String, Object> m : setmealList) {
//            Long setmealcount = (Long) m.get("value");
//            setmealCount.add(setmealcount);
//        }
//        map.put("setmealCount",setmealCount);
        map.put("setmealCount",setmealList);
        for (Map<String, Object> m : setmealList) {
            String setmealName = (String) m.get("name");
            setmealNames.add(setmealName);
        }
        map.put("setmealNames",setmealNames);
        System.out.println("-----------map getSetmealReport；-----=   "+map);
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,map);

    }

    @RequestMapping("/getBusinessReportData")
    public  Result getBusinessReportData(){
        Map<String,Object> map = reportService.getBusinessReportData();
        return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
    }

    @RequestMapping("/exportBusinessReport")
    public void  exportBusinessReport(HttpServletRequest request, HttpServletResponse response){

        try {
            //远程调用报表服务获取报表数据
            Map<String,Object> result = reportService.getBusinessReportData();
            //取出返回结果数据，准备将报表数据写入到Excel文件中

            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            //获得Excel模板文件绝对路径
            //file.separator这个代表系统目录中的间隔符，说白了就是斜线。
            //request.getSession().getServletContext().getRealPath("template")获取部署到服务器上的template的路径
            String temlateRealPath = request.getSession().getServletContext().getRealPath("template")+ File.separator+"report_template.xlsx";

            //读取模板文件创建Excel表格对象
            XSSFWorkbook workbook = new XSSFWorkbook(temlateRealPath);

            Sheet sheet = workbook.getSheetAt(0);

            Row row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate); //日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日出游数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周出游数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月出游数

            int rowNum = 12;
            //热门套餐
            for (Map map : hotSetmeal) {
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum ++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比

            }
            //5.输出文件，以流形式文件下载，另存为操作
            ServletOutputStream out = response.getOutputStream();

            // 下载的数据类型（excel类型）
            response.setContentType("application/vnd.ms-excel");
            // 设置下载形式(通过附件的形式下载)
            response.setHeader("content-Disposition","attachment;filename=report.xlsx");
            //写给浏览器，文件下载
            workbook.write(out);

            //关闭
            out.flush();;
            out.close();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
            //跳转错误页面  没写
        }


    }
}
