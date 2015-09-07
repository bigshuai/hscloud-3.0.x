package com.hisoft.hscloud.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.wgdawn.persist.model.AppResourceUsage;
import com.wgdawn.persist.model.MoreCloudDatabaseResource;
import com.wgdawn.service.AppReportService;
import com.wgdawn.service.AppResourceUsageService;

/**
 * 资源使用情况 <功能详细描述>
 * 
 * @author yangby
 * @version [版本号, 2015-7-23]
 */
public class HcResourceUsageAction extends HSCloudAction {
	private static final long serialVersionUID = 3101080193100376483L;
	private Logger logger = Logger.getLogger(this.getClass());
	private String month;
	private String year;
	private File file;
	private String fileFileName;
	private String uploadFileType;
	@Autowired
	private AppResourceUsageService appResourceUsageService;
	@Autowired
	private AppReportService appReportService;

	/**
	 * 领导-云应用-11.利用率统计
	 */
	public void insertUtilizationStatisticsList() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter insertUtilizationStatisticsList method.");
		}
		String fileName = fileFileName;
		if (null != fileName && "" != fileName) {
			try {
				Map<String, Object> aruMap = new HashMap<String, Object>();
				aruMap.put("year", year);
				// 删除当年数据
				appReportService.deleteUtilizationStatisticsList(aruMap);
				// 读取文件取得文件对象的list(需要参数:file/map的key)
				String[] columns = { "cpu", "networkBandwidth", "hard", "memory" };
				List<Map<String, Object>> list = readXlsx(file, columns, 3, 2, 14, 5);
				List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
				// 插入数据库
				for (int i = 0; i < 12; i++) {
					Map<String, Object> mapLi;
					int j = i + 1;
					String dateStr = year + "-" + j + "-1";
					for (int a = 0; a < columns.length; a++) {
						mapLi = new HashMap<String, Object>();
						int b = a + 1;
						mapLi.put("type", b);
						mapLi.put("amount", list.get(i).get(columns[a]));
						mapLi.put("createTime", dateStr);
						result.add(mapLi);
					}
				}
				appReportService.insertUtilizationStatisticsList(result);
				HttpServletResponse response = ServletActionContext.getResponse();
		 	       response.setContentType("text/html");
		         try {
					//response.getWriter().write("{success:true}");
					response.getWriter().write("{success:true}");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			super.fillActionResult(Constants.OPTIONS_FAILURE);
		}

		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit insertUtilizationStatisticsList method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * 领导-云虚机-3.资源使用情况统计
	 */
	public void insertVMUseSituation() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter insertVMUseSituation method.");
		}
		String fileName = fileFileName;
		if (null != fileName && "" != fileName) {
			try {
				Map<String, Object> aruMap = new HashMap<String, Object>();
				aruMap.put("year", year);
				// 删除当年数据
				appReportService.deleteVMUseSituationByTime(aruMap);
				// 读取文件取得文件对象的list(需要参数:file/map的key)
				String[] columns = { "storageSpace", "operatingSystem", "networkBandwidth", "cpu", "memory", "dataBase", "ip" };
				List<Map<String, Object>> list = readXlsx(file, columns, 2, 2, 13, 8);
				// 插入数据库
				for (int i = 0; i < 12; i++) {
					int j = i + 1;
					String dateStr = year + "-" + j + "-1";
					// 将传入的日期插入create_time字段
					list.get(i).put("createTime", dateStr);
				}
				appReportService.insertVMUseSituation(list);
				HttpServletResponse response = ServletActionContext.getResponse();
		 	       response.setContentType("text/html");
		         try {
					//response.getWriter().write("{success:true}");
					response.getWriter().write("{success:true}");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			super.fillActionResult(Constants.OPTIONS_FAILURE);
		}

		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit insertVMUseSituation method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * 10.云应用部署情况统计(前十)
	 */
	public void insertResourceDeploymentSituation() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter insertResourceDeploymentSituation method.");
		}
		String fileName = fileFileName;
		if (null != fileName && "" != fileName) {
			try {
				Map<String, Object> aruMap = new HashMap<String, Object>();
				aruMap.put("month", month);
				aruMap.put("year", year);
				// 删除当月数据
				appResourceUsageService.deleteAppResourceUsageByTime(aruMap);
				// 读取文件取得文件对象的list(需要参数:file/map的key)
				String[] columns = { "enterpriseName", "bandwidth", "ip", "storageHigh", "storageOptimization", "firewall", "sqlserver", "mysql", "otherDatabase", "cpu", "memory" };
				List<Map<String, Object>> list = readXlsx(file, columns, 4, 1, 10, 11);
				// 插入数据库
				String dateStr = year + "-" + month + "-1";
				// 将传入的日期插入create_time字段
				for (Map<String, Object> map : list) {
					map.put("createTime", dateStr);
				}
				appResourceUsageService.insertResourceDeploymentSituationBatch(list);
				HttpServletResponse response = ServletActionContext.getResponse();
		 	       response.setContentType("text/html");
		         try {
					//response.getWriter().write("{success:true}");
					response.getWriter().write("{success:true}");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			super.fillActionResult(Constants.OPTIONS_FAILURE);
		}

		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit insertResourceDeploymentSituation method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * 领导-下架的应用统计 <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void vmResourceUsageMySQL() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAvailableSupplier method.");
		}
		String fileName = fileFileName;
		if (null != fileName && "" != fileName) {
			try {
				Map<String, Object> aruMap = new HashMap<String, Object>();
				// aruMap.put("month", month);
				aruMap.put("year", year);
				//List<MoreCloudDatabaseResource> AppResourceUsageList = appResourceUsageService.selectAppResourceUsageByDate(aruMap);
				// 根据年月查询当前数据如果重复则覆盖没有重复插入到数据库
				//if (null != AppResourceUsageList && !AppResourceUsageList.isEmpty()) {
					appResourceUsageService.deleteMoreCloudDatabaseResourceByDate(aruMap);
				//}
				// 读取文件取得文件对象的list
				List<MoreCloudDatabaseResource> list = readMoreCloudDatabaseResourceXls(file);
				for (int i = 0; i < 12; i++) {
					int j = i + 1;
					// 将传入的日期插入create_time字段
					SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");// 然后创建一个日期格式化类
					String toConvertString = year + "-" + j + "-1 00:00:00";
					Date convertResult = null;
					try {
						convertResult = format.parse(toConvertString);
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println(convertResult);
					list.get(i).setCreateDate(convertResult);;
				}
				
				for (MoreCloudDatabaseResource moreCloudDatabaseResource : list) {
					appResourceUsageService.insertMoreCloudDatabaseResource(moreCloudDatabaseResource);
				}
				// fillActionResult(true);
				HttpServletResponse response = ServletActionContext.getResponse();
		 	       response.setContentType("text/html");
		         try {
					//response.getWriter().write("{success:true}");
					response.getWriter().write("{success:true}");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			super.fillActionResult(Constants.OPTIONS_FAILURE);
		}

		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAvailableSupplier method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * 领导-下架的应用统计 <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void otherResourceUsage() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAvailableSupplier method.");
		}
		String fileName = fileFileName;
		if (null != fileName && "" != fileName) {
			try {
				Map<String, Object> aruMap = new HashMap<String, Object>();
				aruMap.put("month", month);
				aruMap.put("year", year);
				List<AppResourceUsage> AppResourceUsageList = appResourceUsageService.selectAppResourceUsageByDate(aruMap);
				// 根据年月查询当前数据如果重复则覆盖没有重复插入到数据库
				if (null != AppResourceUsageList && !AppResourceUsageList.isEmpty()) {
					appResourceUsageService.deleteAppResourceUsageByDate(aruMap);
				}
				// 读取文件取得文件对象的list
				List<AppResourceUsage> list = readAppResourceUsageXls(file);
				for (AppResourceUsage appResourceUsage : list) {
					appResourceUsageService.insertAppResourceUsage(appResourceUsage);
				}
				// fillActionResult(true);
				//super.fillActionResult(Constants.OPTIONS_SUCCESS);
				HttpServletResponse response = ServletActionContext.getResponse();
		 	       response.setContentType("text/html");
		         try {
					//response.getWriter().write("{success:true}");
					response.getWriter().write("{success:true}");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			super.fillActionResult(Constants.OPTIONS_FAILURE);
		}

		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAvailableSupplier method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * 读取(领导:10.资源部署情况统计)Excel
	 * 
	 * @throws Exception
	 * 
	 */
	private List<Map<String, Object>> readXlsx(File file, String[] columns// 封装之后的key
			, int startRow, int startCol, int endRow, int endCol) throws Exception {
		InputStream is = new FileInputStream(file);
		XSSFWorkbook wb = new XSSFWorkbook(is);
		XSSFSheet sheet = wb.getSheetAt(0);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 从第四行开始读取数据，然后封装成List返回(可将行数提取)
		for (int i = startRow - 1; i < endRow; i++) {
			XSSFRow row = sheet.getRow(i);
			Map<String, Object> map = new HashMap<String, Object>();
			int a = 0;
			for (int j = startCol - 1; j < endCol; j++) {
				XSSFCell cell = row.getCell(j);
				String column = null;
				column = columns[a];
				a++;
				map.put(column, getCellFormatValue(cell).trim());
			}
			list.add(map);
		}
		return list;
	}

	/**
	 * 根据HSSFCell类型设置数据
	 * 
	 * @param cell
	 * @return
	 */
	private String getCellFormatValue(XSSFCell cell) {
		String cellvalue = "";
		if (cell != null) {
			// 判断当前Cell的Type
			switch (cell.getCellType()) {
			// 如果当前Cell的Type为NUMERIC
			case XSSFCell.CELL_TYPE_NUMERIC:
			case XSSFCell.CELL_TYPE_FORMULA: {
				// 判断当前的cell是否为Date
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					// 如果是Date类型则，转化为Data格式

					// 方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
					// cellvalue = cell.getDateCellValue().toLocaleString();

					// 方法2：这样子的data格式是不带带时分秒的：2011-10-12
					Date date = cell.getDateCellValue();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					cellvalue = sdf.format(date);

				}
				// 如果是纯数字
				else {
					// 取得当前Cell的数值
					cellvalue = String.valueOf(cell.getNumericCellValue());
				}
				break;
			}
			// 如果当前Cell的Type为STRIN
			case XSSFCell.CELL_TYPE_STRING:
				// 取得当前的Cell字符串
				cellvalue = cell.getRichStringCellValue().getString();
				break;
			// 默认的Cell值
			default:
				cellvalue = " ";
			}
		} else {
			cellvalue = "";
		}
		return cellvalue;

	}
	
	/**
	 * 读取xls文件内容
	 * 
	 * @return List<AppResourceUsage>对象
	 * @throws IOException
	 *             输入/输出(i/o)异常
	 */
	private List<MoreCloudDatabaseResource> readMoreCloudDatabaseResourceXls(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		XSSFWorkbook hssfWorkbook = new XSSFWorkbook(is);
		MoreCloudDatabaseResource xlsDto = null;
		List<MoreCloudDatabaseResource> list = new ArrayList<MoreCloudDatabaseResource>();
		// 循环工作表Sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			// 循环行Row
			for (int rowNum = 2; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				xlsDto = new MoreCloudDatabaseResource();
				XSSFCell db = hssfRow.getCell(1);
				if (db == null) {
					continue;
				}
				xlsDto.setDataBase(Integer.valueOf(getValue(db)));
				list.add(xlsDto);
			}
		}
		return list;
	}
	
	/**
	 * 读取xls文件内容
	 * 
	 * @return List<AppResourceUsage>对象
	 * @throws IOException
	 *             输入/输出(i/o)异常
	 */
	private List<AppResourceUsage> readAppResourceUsageXls(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		XSSFWorkbook hssfWorkbook = new XSSFWorkbook(is);
		AppResourceUsage xlsDto = null;
		List<AppResourceUsage> list = new ArrayList<AppResourceUsage>();
		// 循环工作表Sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			// 循环行Row
			for (int rowNum = 4; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				xlsDto = new AppResourceUsage();
				XSSFCell xh = hssfRow.getCell(0);
				if (xh == null) {
					continue;
				}
				xlsDto.setName(getValue(xh));
				XSSFCell bandwidth = hssfRow.getCell(1);
				if (bandwidth == null) {
					continue;
				}
				xlsDto.setBandwidth(Integer.valueOf(getValue(bandwidth)));

				XSSFCell ip = hssfRow.getCell(2);
				if (ip == null) {
					continue;
				}
				xlsDto.setIp(Integer.valueOf(getValue(ip)));

				XSSFCell highPerformance = hssfRow.getCell(3);
				if (highPerformance == null) {
					continue;
				}
				xlsDto.setHighPerformance(Integer.valueOf(getValue(highPerformance)));

				XSSFCell optimized = hssfRow.getCell(4);
				if (optimized == null) {
					continue;
				}
				xlsDto.setOptimized(Integer.valueOf(getValue(optimized)));

				XSSFCell virtualFirewall = hssfRow.getCell(5);
				if (virtualFirewall == null) {
					continue;
				}
				xlsDto.setVirtualFirewall(Integer.valueOf(getValue(virtualFirewall)));

				XSSFCell sqlServer = hssfRow.getCell(6);
				if (sqlServer == null) {
					continue;
				}
				xlsDto.setSqlServer(Integer.valueOf(getValue(sqlServer)));

				XSSFCell mysql = hssfRow.getCell(7);
				if (mysql == null) {
					continue;
				}
				xlsDto.setMysql(Integer.valueOf(getValue(mysql)));

				XSSFCell nonRelationalDatabases = hssfRow.getCell(8);
				if (nonRelationalDatabases == null) {
					continue;
				}
				xlsDto.setNonRelationalDatabases(Integer.valueOf(getValue(nonRelationalDatabases)));

				XSSFCell cpu = hssfRow.getCell(9);
				if (cpu == null) {
					continue;
				}
				xlsDto.setCpu(Integer.valueOf(getValue(cpu)));

				XSSFCell ram = hssfRow.getCell(10);
				if (ram == null) {
					continue;
				}
				xlsDto.setRam(Integer.valueOf(getValue(ram)));
				SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");// 然后创建一个日期格式化类
				String toConvertString = year + "-" + month + "-01 00:00:00";
				Date convertResult = null;
				try {
					convertResult = format.parse(toConvertString);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println(convertResult);
				xlsDto.setCreateTime(convertResult);
				list.add(xlsDto);
			}
		}
		return list;
	}

	/**
	 * 得到Excel表中的值
	 * 
	 * @param hssfCell
	 *            Excel中的每一个格子
	 * @return Excel中每一个格子中的值
	 */
	@SuppressWarnings("static-access")
	private String getValue(XSSFCell hssfCell) {
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			// 返回布尔类型的值
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			// 返回数值类型的值
			return String.valueOf((int) hssfCell.getNumericCellValue());
		} else {
			// 返回字符串类型的值
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}

	public String getUploadFileType() {
		return uploadFileType;
	}

	public void setUploadFileType(String uploadFileType) {
		this.uploadFileType = uploadFileType;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

}
