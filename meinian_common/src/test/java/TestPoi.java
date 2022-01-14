import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

public class TestPoi {

    @Test
    public void readExcel() throws IOException {

        //创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook("d:/test.xlsx");
        //获取工作表，既可以根据工作表的顺序获取，也可以根据工作表的名称获取
        XSSFSheet sheet = workbook.getSheetAt(0);
        //遍历工作表获得行对象
        for (Row row: sheet) {
            //遍历行对象获取单元格对象
            for(Cell cell:row){
                //注意：数字类型,需要修改excel单元格的类型，否则报错。
                System.out.println(cell.getStringCellValue());
            }
        }

        workbook.close();
    }

    @Test
    public void exportExcel_lastRow() throws IOException {
        //创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook("D:\\test.xlsx");
        //获取工作表，既可以根据工作表的顺序获取，也可以根据工作表的名称获取
        XSSFSheet sheet = workbook.getSheetAt(0);
        //获取当前工作表最后一行的行号，行号从0开始
        int lastRowNum = sheet.getLastRowNum();
        for(int i=0;i<=lastRowNum;i++){
            //根据行号获取行对象
            XSSFRow row = sheet.getRow(i);
            // 再获取单元格对象
            short lastCellNum = row.getLastCellNum();
            for(short j=0;j<lastCellNum;j++){
                // 获取单元格对象的值
                String value = row.getCell(j).getStringCellValue();
                System.out.println(value);
            }
        }
        workbook.close();
    }

    // 导入excel
    @Test
    public void importExcel() throws IOException {
        //在内存中创建一个Excel文件
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表，指定工作表名称
        XSSFSheet sheet = workbook.createSheet("党员表");
        //创建行，0表示第一行
        XSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("编号");
        row.createCell(1).setCellValue("姓名");
        row.createCell(2).setCellValue("年龄");

        XSSFRow row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("001");
        row1.createCell(1).setCellValue("任立钢");
        row1.createCell(2).setCellValue("26");

        XSSFRow row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue("002");
        row2.createCell(1).setCellValue("任立钢2");
        row2.createCell(2).setCellValue("26");

        FileOutputStream os = new FileOutputStream("D:/file1.xlsx");
        workbook.write(os);
        os.flush();
        os.close();
        workbook.close();
    }

}
