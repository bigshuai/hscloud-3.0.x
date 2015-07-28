/* 
* 文 件 名:  ExcelExport.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-5-13 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.om.util; 

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-5-13] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class ExcelExport {
    HSSFDataFormat format;
    private HSSFSheet currentSheet = null;
    private Object o = null;
    private Object value = null;
    private List<Map<String, Object>> sheetList = new ArrayList<Map<String, Object>>();
    private List<Method> methods = new ArrayList<Method>();
    private InputStream excelStream;
    private Map<String, List<Map<String, Object>>> datas = new HashMap<String, List<Map<String, Object>>>();
    private HSSFWorkbook wb = null;
    HSSFFont font = null;
    HSSFCellStyle cellStyle = null;
    
    private List<String> columnList = new ArrayList<String>();

    private Map<String, HSSFSheet> sheets = new HashMap<String, HSSFSheet>();

    public ExcelExport(InputStream excelStream, Map<String, List<Map<String, Object>>> datas, List<String> columnLis) {
        super();
        this.excelStream = excelStream;
        this.datas = datas;
        this.columnList = columnLis;
        this.init();

    }

    private void init() {
        try {
            POIFSFileSystem fs;
            fs = new POIFSFileSystem(this.excelStream);
            this.wb = new HSSFWorkbook(fs);
            format = wb.createDataFormat();
            this.wb.getNumberOfSheets();
            font = this.createFont();
            cellStyle = this.createCellStyle();
            for (int i = 0; i < this.wb.getNumberOfSheets(); i++) {
                HSSFSheet sheet = this.wb.getSheetAt(i);
                String sheetName = sheet.getSheetName();
                sheets.put(sheetName, sheet);
            }
            for (String key : this.datas.keySet()) {
                this.currentSheet = this.sheets.get(key);
                this.sheetList = this.datas.get(key);
                this.createExcel();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void createExcel() {
        HSSFSheet sheet = this.currentSheet;
        List<Map<String, Object>> list = this.sheetList;
        if (null != sheet && !list.isEmpty()) {
          //  this.getMethods(list.get(0));
            int templastRowNum = this.currentSheet.getLastRowNum();
            int cellTotal = this.currentSheet.getRow(templastRowNum)
                    .getLastCellNum();
            int createRowNum = templastRowNum + 1;
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = (Map<String, Object>)list.get(i);
                this.createRow(i + createRowNum, cellTotal, map);
            }

        }
        this.currentSheet = null;
        this.sheetList.clear();
        this.methods.clear();
    }

    /*private void getMethods(Object o) {
        Class clazz = o.getClass();
        Field[] fs = clazz.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {

            try {

                String fieldName = fs[i].getName();
                PropertyDescriptor pd = new PropertyDescriptor(fieldName, clazz);
                Method getMethod = pd.getReadMethod();
                this.methods.add(getMethod);

            } catch (IntrospectionException e) {
                e.printStackTrace();
            }

        }
    }*/

    private void createRow(int rownum, int cellTotal, Map<String, Object> map) {

        
        HSSFRow row = this.currentSheet.createRow(rownum);
        row.setHeight((short) 300);
        
        for (int i = 0; i < cellTotal; i++) {
            try {
                value = map.get(columnList.get(i));
                HSSFCell cell = this.createCell(row, i,
                        HSSFCellStyle.ALIGN_CENTER, value);
                cellStyle.setFont(font);
                cell.setCellType(HSSFCell.ENCODING_UTF_16);
                cell.setCellStyle(cellStyle);
                this.value = null;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } 

        }
    }

    private HSSFFont createFont() {
        // 设置单元格字体
        HSSFFont font = wb.createFont();
        //font.setBoldweight(HSSFFont.COLOR_NORMAL);
        font.setFontName("宋体");
        font.setFontHeight((short) 250);
        return font;
    }

    private HSSFCellStyle createCellStyle() {

        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 指定单元格居中对齐
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
        cellStyle.setWrapText(true);// 指定单元格自动换行
        if (null != value) {
            if (value instanceof Date) {
                cellStyle.setDataFormat(HSSFDataFormat
                        .getBuiltinFormat("m/d/yy h:mm"));
            } else if (value instanceof Number) {
                cellStyle.setDataFormat(format.getFormat("0.00"));
            }

        }

        return cellStyle;

    }

    /**
     * 创建内容单元格
     * 
     * @param row
     *            HSSFRow
     * @param col
     *            short型的列索引
     * @param align
     *            对齐方式
     * @param val
     *            列值
     */
    private HSSFCell createCell(HSSFRow row, int col, short align, Object val) {
        HSSFCell cell = row.createCell(col);
        cell.setCellType(HSSFCell.ENCODING_UTF_16);
        
        if (null != value) {
            if (value instanceof Date) {
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String d=sd.format((Date)value);
                cell.setCellValue(new HSSFRichTextString(d));
            } else if (value instanceof Number) {
                cell.setCellValue(new HSSFRichTextString(new DecimalFormat("0.00").format(value)));
            }else{
                cell.setCellValue(new HSSFRichTextString(val.toString()));
            }
            
        }else{
            cell.setCellValue("");
        }
        return cell;
        
    }

    /**
     * @return the wb
     */
    public HSSFWorkbook getWb() {
        return wb;
    }

    /**
     * 输入EXCEL文件
     * 
     * @param fileName
     *            文件名
     */
    public void outputExcel(String fileName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(fileName));
            wb.write(fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 输出excel流
     * 
     * @return OutputStream
     */
    public OutputStream getExcelOutputStream() {

        InputStream is = new ByteArrayInputStream(this.wb.getBytes());
        OutputStream os = new ByteArrayOutputStream();

        try {

            int ch;
            while ((ch = is.read()) != -1) {
                os.write(ch);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return os;

    }

    public byte[] getExcelBytes() {
        return this.wb.getBytes();
    }
}
