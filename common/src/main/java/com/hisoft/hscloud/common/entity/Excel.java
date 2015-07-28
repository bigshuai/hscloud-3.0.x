package com.hisoft.hscloud.common.entity;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * 
 * @author Minggang Excel 导出
 *         模板元素   表名 表头 模板行 统计行
 *         表名       rowNumber = 0 必选
 *         表头       rowNumber = 1 必选
 *         模板行  rowNumber = 2 可选，（没有时，必需为空，占位）
 *         统计行  rowNumber = 3 可选，（没有时，必需为空，占位）
 *         注意: 
 *            1. 必须有 表名  表头   否则导出会不正确。
 *            2. 导出对象 中字段顺序与表头数据一致。
 *            3. 按字段顺序导出，导出个数为标头和类字段个数两者中的 最小值。
 *         
 * 
 */
public class Excel {
	
	private  final int titleRowNum = 1;//写数据起始行   
	
	private Boolean hasCountCell; //是否有统计行
	
	private HSSFDataFormat format;
	
	private HSSFSheet currentSheet = null;//当前操作的sheet.
	
	private HSSFRow tRow = null;
	
	private Object o = null;
	
	private Object value = null;
	
	private List<Object> sheetDateList = new ArrayList<Object>();
	
	private List<Method> dateMethods = new ArrayList<Method>();
	
	private InputStream excelStream;
	
	private Map<String, List<Object>> datas = new HashMap<String, List<Object>>();
	
	private HSSFWorkbook wb = null;

	private Map<String, HSSFSheet> sheets = new HashMap<String, HSSFSheet>();

	private Map<String, HSSFCellStyle> styleMap = new HashMap<String, HSSFCellStyle>();
	
	private List<HSSFCell> tempCells = new ArrayList<HSSFCell>();//模板行的cell。
	
	public Excel(InputStream excelStream) {

		super();
		this.excelStream = excelStream;
		this.init();

	}

	public Excel(InputStream excelStream, Map<String, List<Object>> datas) {

		super();
		this.excelStream = excelStream;
		this.datas = datas;
		this.init();
		this.fillData();

	}
	
	/**
	 * 初始化excel.
	 */
	private void init() {

		try {
			
			POIFSFileSystem fs;
			fs = new POIFSFileSystem(this.excelStream);
			this.wb = new HSSFWorkbook(fs);
			format = wb.createDataFormat();
			this.registerCellStyle();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 填充数据。
	 */
	private void fillData(){

		for (int i = 0; i < this.wb.getNumberOfSheets(); i++) {
			HSSFSheet sheet = this.wb.getSheetAt(i);
			String sheetName = sheet.getSheetName();
			sheets.put(sheetName, sheet);
		}
		
		for (String key : this.sheets.keySet()) {
			this.currentSheet = this.sheets.get(key);
			this.sheetDateList = this.datas.get(key);
		    this.createSheet();
		}
		
	}

	/**
	 * 注册excel样式。
	 */
	private void registerCellStyle() {
		this.styleMap.put("java.lang.Number", this.createCellStyle(this.format
				.getFormat("#,##0.00;[Red]-#,##0.00")));
		this.styleMap.put("java.util.Date", this.createCellStyle(HSSFDataFormat
				.getBuiltinFormat("m/d/yy h:mm")));
		this.styleMap.put("java.lang.String", this.createCellStyle(null));
	}

	private HSSFCellStyle createCellStyle(Short fmt) {
		HSSFCellStyle cellStyle = wb.createCellStyle();
		if(null != fmt){
			cellStyle.setDataFormat(fmt);
		}
		return cellStyle;

	}

	private void createSheet() {
		
		HSSFSheet sheet = this.currentSheet;
		List<Object> list = this.sheetDateList;
		
		if (null != sheet && null != list && !list.isEmpty()) {
			int tempRowNum = titleRowNum+1;
			int countRowNum = titleRowNum+2;
			HSSFRow titleRow = this.currentSheet.getRow(titleRowNum);
			HSSFRow countRow = this.currentSheet.getRow(countRowNum);
			int lastRowNum = this.currentSheet.getLastRowNum();
			if(null != countRow){
				this.hasCountCell = true;
			}else{
				this.hasCountCell = false;
			}
			this.tRow = this.currentSheet.getRow(tempRowNum);
			this.getMethods(list.get(0));
			int dataSize = list.size();
			Short cellTotal = (short)(titleRow.getLastCellNum()>this.dateMethods.size()?this.dateMethods.size():titleRow.getLastCellNum());
			int cRowNum = tempRowNum+dataSize;
			//没有模板行
			if(null == tRow){
				for (int i = 0; (i < list.size()) && (i <= 65500); i++) {
					o = list.get(i);
					if(this.hasCountCell && i<dataSize-1){
						//移动模板中的行
						this.currentSheet
								.shiftRows(i+countRowNum, i+lastRowNum + 1, 1, true, true, true);
				    }
					this.createRow(i + tempRowNum, cellTotal);
				}
				//有统计行
				if(this.hasCountCell){
					this.createCountRow(cRowNum);
				}
			}else{
				
				this.initTempCells();
				for (int i = 0; (i < dataSize) && (i <= 65500); i++) {
					o = list.get(i);
					if(this.hasCountCell && i<dataSize-1){
						//移动模板中的行
						this.currentSheet
								.shiftRows(i+countRowNum, i+lastRowNum + 1, 1, true, true, true);
				    }
					this.createRow(i + tempRowNum, cellTotal);
				}
				//有统计行
				if(this.hasCountCell){
					this.createCountRow(cRowNum);
				}
			}
			this.currentSheet = null;
			this.tRow =null;
			this.hasCountCell=null;
			this.tempCells.clear();
			this.sheetDateList.clear();
			this.dateMethods.clear();

		}
		
	}
	
	/**
	 * 统计行
	 * @param cRowNum
	 */
	private void createCountRow(int cRowNum){
		HSSFRow cRow = this.currentSheet.getRow(cRowNum);
		int formulaNum = cRowNum+1;//公式中数字从一开始。
		int c = cRow.getLastCellNum();
		for(int j = 0;j<c;j++){
			HSSFCell cCell = cRow.getCell(j);
			if (null != cCell && HSSFCell.CELL_TYPE_FORMULA == cCell.getCellType()) {
				//获得模板中公式
				String formula = cCell.getCellFormula();
				cCell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
				String f = this.analyseFormula(formula,formulaNum);
				cCell.setCellFormula(f);
				cCell.setCellStyle(null != cCell ? cCell.getCellStyle():this.styleMap.get("java.lang.Number"));
			}
		}
	}
	
	/**
	 * 公式分析。
	 * @param formula 模板公式
	 * @param replace
	 * @return
	 */
	private String analyseFormula(String formula,Object replace){
		
		String sum = "SUM\\(([A-Z]){1}([0-9]){1,}:([A-Z]){1}([0-9]){1,}\\)";//判断是否为求和公式
		String cul = "^([+-]{0,1}[A-Z]{1}[0-9]{1,}[+-]{1}){1,}([A-Z]{1}[0-9]{1,})$";//判断是否为 A1-B1+C1式公式 或  +A1-B1+C1
		String equ = "^[A-Z]{1}[0-9]{1,}$";
		Pattern pats = Pattern.compile(sum);  
		Matcher mats = pats.matcher(formula);  
		if(mats.find()){
			if(!mats.group(1).equals(mats.group(3)) && mats.group(2).equals(mats.group(4))){//判断是否为横向相加
				 String r = ((Integer)replace).toString();
				 formula = formula.replaceAll("[0-9]{1,}",r);
				 return formula;
			 }else if(mats.group(1).equals(mats.group(3)) && mats.group(2).equals(mats.group(4))){//判断是否为纵向相加
				 //行从零开始，公式中数字从一开始。
				 String r = String.valueOf((((Integer)replace).intValue())-1);
				 //替换公式中最后的数值
				 return new StringBuffer(new StringBuffer(formula).reverse().toString().replaceFirst("[0-9]{1,}", new StringBuffer(r).reverse().toString())).reverse().toString();
			 } else{
				 return null;
			 }
			
		}
		Pattern pate = Pattern.compile(equ);  
		Matcher mate = pate.matcher(formula);  
		if(mate.find()){
			return formula;
		}
		Pattern patc = Pattern.compile(cul);  
		Matcher matc = patc.matcher(formula); 
		//判断是否为A1-B1+C1式公式
		if(matc.find()){
			String r = ((Integer)replace).toString();
			    //替换公式中所有数值
			 return formula.replaceAll("[0-9]{1,}",r);
		}else{
			 String r = ((Integer)replace).toString();
			 formula = formula.replaceAll("[0-9]{1,}",r);
			 return formula;
		}
		
	}

	/**
	 * 获得模板CELL。
	 */
	private void initTempCells(){
		if(null != this.tRow){
			for (int i = 0  ;i<this.tRow.getLastCellNum();i++) {
				tempCells.add(this.tRow.getCell(i));
			}
		}
	}
	
	/**
	 * 获得需导出对象的方法
	 * @param o  导出数据类
	 */
	private void getMethods(Object o) {

		Class<?> clazz = o.getClass();
		Field[] fs = clazz.getDeclaredFields();
		for (int i = 0; i < fs.length; i++) {

			try {

				String fieldName = fs[i].getName();
				System.out.println(fieldName);
				PropertyDescriptor pd = new PropertyDescriptor(fieldName, clazz);
				Method getMethod = pd.getReadMethod();
				this.dateMethods.add(getMethod);

			} catch (IntrospectionException e) {
				this.dateMethods.add(null);
				e.printStackTrace();
			}

		}
	}

	
	/**
	 * 创建行
	 * @param rownum  行号
	 * @param cellTotal cell总数
	 */
	private void createRow(int rownum, int cellTotal) {

		
		HSSFRow row = this.currentSheet.createRow(rownum);
		row.setHeight(null != this.tRow?this.tRow.getHeight():(short)270);
		int size = this.dateMethods.size();
		for (int i = 0; i < cellTotal; i++) {
			try {
				//获得值
				Method method = this.dateMethods.get(i);
				value = (null == method?null:(i<size?method.invoke(o, new Object[] {}):null));
				this.createCell(row, i, HSSFCellStyle.ALIGN_CENTER, value);
				this.value = null;
				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

		}
	}

	// private HSSFFont createFont() {
	// // 设置单元格字体
	// HSSFFont font = wb.createFont();
	// font.setBoldweight(HSSFFont.COLOR_RED);
	// font.setColor(HSSFFont.COLOR_RED);
	// font.setFontName("宋体");
	// font.setFontHeight((short) 250);
	// return font;
	// }

	// private HSSFCellStyle createCellStyle() {
	//
	// HSSFCellStyle cellStyle = wb.createCellStyle();
	// cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 指定单元格居中对齐
	// cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//
	// 指定单元格垂直居中对齐
	// cellStyle.setWrapText(true);// 指定单元格自动换行
	// if (null != value) {
	// if (value instanceof Date) {
	// cellStyle.setDataFormat(HSSFDataFormat
	// .getBuiltinFormat("m/d/yy h:mm"));
	// } else if (value instanceof Number) {
	// cellStyle.setDataFormat(format.getFormat("0.00"));
	// }
	//
	// }
	//
	// return cellStyle;
	//
	// }

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

		HSSFCell tCell = null;
		if(!this.tempCells.isEmpty()){
			 tCell = this.tempCells.get(col);
		}
		
		HSSFCell cell = row.createCell(col);
		if (null != tCell && HSSFCell.CELL_TYPE_FORMULA == tCell.getCellType()) {
			String formula = tCell.getCellFormula();
			cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
			String f = this.analyseFormula(formula,row.getRowNum() + 1);
			cell.setCellFormula(f);
			cell.setCellStyle(null != tCell ? tCell.getCellStyle():this.styleMap.get("java.lang.Number"));
			return cell;
		}
		if (null != value) {
			if (value instanceof Date) {
				cell.setCellValue((Date) value);
				cell.setCellStyle(null != tCell ? tCell.getCellStyle():this.styleMap.get(Date.class.getName()));
			} else if (value instanceof Number) {
				cell.setCellValue(((Number) value).doubleValue());
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				cell.setCellStyle(null != tCell ? tCell.getCellStyle():this.styleMap.get(Number.class.getName()));
			} else {
				cell.setCellValue(new HSSFRichTextString(val.toString()));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(null != tCell ? tCell.getCellStyle():this.styleMap.get(String.class.getName()));
			}

		} else {
			cell.setCellValue("");
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(null != tCell ? tCell.getCellStyle():this.styleMap.get(String.class.getName()));
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
		File f=null;
		FileOutputStream fos=null;
		try {
			 f = new File(fileName);
			 System.out.println(f.getAbsolutePath());
			 fos = new FileOutputStream(f);
			 wb.write(fos);
			
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			if(null != f){
				f.delete();
			}
		}finally{
			try {
				fos.close();
			} catch (IOException e) {
			}
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
		}finally{
			try {
				is.close();
				os.close();
			} catch (IOException e) {
			}
			
		}
		return os;

	}

	public InputStream getExcleInputStream(){
		return new ByteArrayInputStream(this.wb.getBytes());
	}
	
	public byte[] getExcelBytes() {
		return this.wb.getBytes();
	}
	

}
