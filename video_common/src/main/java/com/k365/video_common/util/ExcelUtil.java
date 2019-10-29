package com.k365.video_common.util;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Gavin
 * @date 2019/9/16 14:02
 * @description：
 */
public class ExcelUtil {

    /**
     * 根据版本选择创建Workbook的方式 2003-xls  2007-xlsx
     * @param filePath Excel文件路径
     * @return
     */
    public static Workbook getWorkbook(String filePath, InputStream is) throws IOException{
        if (filePath.matches("^.+\\.(?i)(xls)$")) {
            return new HSSFWorkbook(is);
        }
        if(filePath.matches("^.+\\.(?i)(xlsx)$")){
            return new XSSFWorkbook(is);
        }
        return null;
    }

    /**
     *
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell) {
        Object result = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case STRING:
                    result = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    result = cell.getNumericCellValue();
                    break;
                case BOOLEAN:
                    result = cell.getBooleanCellValue();
                    break;
                case FORMULA:
                    result = cell.getCellFormula();
                    break;
                case ERROR:
                    result = cell.getErrorCellValue();
                    break;
                case BLANK:
                    break;
                default:
                    break;
            }
        }
        return result.toString();
    }


}
