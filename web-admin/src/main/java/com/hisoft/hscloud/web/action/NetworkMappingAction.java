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
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.IPConvert;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.wgdawn.persist.model.AppPriceSystem;
import com.wgdawn.persist.model.ApplicationInvoiceAccount;
import com.wgdawn.persist.model.NetworkMapping;
import com.wgdawn.persist.more.mapper.MoreNetworkMappingMapper;
import com.wgdawn.service.NetworkMappingService;

/**
 * 资源使用情况 <功能详细描述>
 * 
 * @author feifei
 * @version [版本号, 2015-8-10]
 */
public class NetworkMappingAction extends HSCloudAction {
	private static final long serialVersionUID = 3101080193100376483L;
	private Logger logger = Logger.getLogger(this.getClass());
	private String month;
	private String year;
	private File file;
	private String fileFileName;
	private String uploadFileType;
	private int id;
	private String networkType;
	private String insideNetwork;
	private String outsideNetwork;
	
	private Page<NetworkMapping> networkMappingPage = new Page<NetworkMapping>();
    private int page;
    private int limit;
    private NetworkMapping  networkMappingVO;
	@Autowired
	private NetworkMappingService networkMappingService;
	@Autowired
	private MoreNetworkMappingMapper moreNetworkMappingMapper;
	

    /**
     * 删除价格体系信息
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void delNetworkMapping() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter delNetworkMapping method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
            	if(networkMappingVO!=null){
            		if(networkMappingVO.getId()!=null){
            			networkMappingService.deleteByPrimaryKey(networkMappingVO.getId());
            			fillActionResult(true);
                	}
            	}
            }catch(Exception e){
            	fillActionResult(false);
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit delNetworkMapping method.takeTime:" + takeTime + "ms");
		}
    }
    
   
    
    public void selectNetworkMappingList(){
    	long beginRunTime = 0;
    	List<NetworkMapping> list=new ArrayList<NetworkMapping>();
    	Admin admin = (Admin) this.getCurrentLoginUser();
    	
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter selectNetworkMappingList method.");			
		}
		networkMappingPage.setPageNo(page);
		networkMappingPage.setPageSize(limit);        
		networkMappingPage.setResult(new ArrayList<NetworkMapping>());
        Map<String,Object> countMap = new HashMap<String,Object>();
		Map<String,Object> queryMap = new HashMap<String,Object>();
		
		networkMappingPage.setTotalCount(networkMappingService.countByMoreNetworkMapping(countMap));
		queryMap.putAll(countMap);
		queryMap.put("start",(page - 1) * limit);
		queryMap.put("size", limit);
        list=networkMappingService.selectByMoreNetworkMapping(queryMap);
        networkMappingPage.setResult(list);
        fillActionResult(networkMappingPage);
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit selectNetworkMappingList method.takeTime:" + takeTime + "ms");
		}
    }
    
	/**
	 * 导入
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void insertNetworkMapping() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAvailableSupplier method.");
		}
		String fileName = fileFileName;
		if (null != fileName && "" != fileName) {
			try {
				NetworkMapping networkMapping = new NetworkMapping();
				Map<String, Object> aruMap = new HashMap<String, Object>();
//				aruMap.put("month", month);
//				aruMap.put("year", year);
			//	List<NetworkMapping> NetworkMappingList = networkMappingService.selectByMoreNetworkMapping(aruMap);

				// 根据年月查询当前数据如果重复则覆盖没有重复插入到数据库
//				if (null != NetworkMappingList && !NetworkMappingList.isEmpty()) {
//					networkMappingService.deleteByPrimaryKey(networkMapping.getId());
//				}
				// 读取文件取得文件对象的list
				List<NetworkMapping> list = readAppResourceUsageXls(file);
				for (NetworkMapping networkMappings : list) {
					networkMappingService.insertNetworkMapping(networkMappings);
				}
				//super.fillActionResult(Constants.OPTIONS_SUCCESS);
				HttpServletResponse response = ServletActionContext.getResponse();
				response.setContentType("text/html");
				response.getWriter().write("{success:true}");
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
	 * 读取xls文件内容
	 * 
	 * @return List<AppResourceUsage>对象
	 * @throws IOException
	 *             输入/输出(i/o)异常
	 */
	private List<NetworkMapping> readAppResourceUsageXls(File file) throws IOException {
		Admin admin = null;
		admin= (Admin) super.getCurrentLoginUser();
		Long updateId=admin.getId();
		InputStream is = new FileInputStream(file);
		XSSFWorkbook hssfWorkbook = new XSSFWorkbook(is);
		NetworkMapping xlsDto = null;
		List<NetworkMapping> list = new ArrayList<NetworkMapping>();
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
				xlsDto = new NetworkMapping();
				XSSFCell Networktype = hssfRow.getCell(0);
				if (Networktype == null) {
					continue;
				}
				xlsDto.setNetworkType(getValue(Networktype));
				XSSFCell Insidenetwork = hssfRow.getCell(1);
				if (Insidenetwork == null) {
					continue;
				}

				xlsDto.setInsideNetwork(getValue(Insidenetwork));

				//加密内网IP
				xlsDto.setInsideNetworkInteger(IPConvert.getIntegerIP(getValue(Insidenetwork)));
				
				
				XSSFCell Outsidenetwork = hssfRow.getCell(2);
				if (Outsidenetwork == null) {
					xlsDto.setOutsideNetwork("");
				}else{
				xlsDto.setOutsideNetwork(getValue(Outsidenetwork));
				}
				xlsDto.setCreateTime(new Date());
				xlsDto.setUpdateId(Integer.parseInt(updateId.toString()));
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

	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the networkType
	 */
	public String getNetworkType() {
		return networkType;
	}

	/**
	 * @param networkType the networkType to set
	 */
	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	/**
	 * @return the insideNetwork
	 */
	public String getInsideNetwork() {
		return insideNetwork;
	}

	/**
	 * @param insideNetwork the insideNetwork to set
	 */
	public void setInsideNetwork(String insideNetwork) {
		this.insideNetwork = insideNetwork;
	}

	/**
	 * @return the outsideNetwork
	 */
	public String getOutsideNetwork() {
		return outsideNetwork;
	}

	/**
	 * @param outsideNetwork the outsideNetwork to set
	 */
	public void setOutsideNetwork(String outsideNetwork) {
		this.outsideNetwork = outsideNetwork;
	}

	/**
	 * @return the networkMappingPage
	 */
	public Page<NetworkMapping> getNetworkMappingPage() {
		return networkMappingPage;
	}

	/**
	 * @param networkMappingPage the networkMappingPage to set
	 */
	public void setNetworkMappingPage(Page<NetworkMapping> networkMappingPage) {
		this.networkMappingPage = networkMappingPage;
	}

	/**
	 * @return the networkMappingVO
	 */
	public NetworkMapping getNetworkMappingVO() {
		return networkMappingVO;
	}

	/**
	 * @param networkMappingVO the networkMappingVO to set
	 */
	public void setNetworkMappingVO(NetworkMapping networkMappingVO) {
		this.networkMappingVO = networkMappingVO;
	}

	/**
	 * @return the networkMappingService
	 */
	public NetworkMappingService getNetworkMappingService() {
		return networkMappingService;
	}

	/**
	 * @param networkMappingService the networkMappingService to set
	 */
	public void setNetworkMappingService(NetworkMappingService networkMappingService) {
		this.networkMappingService = networkMappingService;
	}
	
	
	

}
