package com.hisoft.hscloud.web.util;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



/**
 * @author: hisoft
 * @brief: db util
 * @log: 2012-12-26 2:12:15
 **/
public class DBUtil {

	private static Logger logger = Logger.getLogger(DBUtil.class);
	private static Map<String, String> _sqlCache;
	
	private final static String SQL_TAG = "sql";
	private final static String SQL_ID = "id";
	private final static String SQL_FILE_NAME = "sql.xml";
	
	private static void setup() {

		_sqlCache = new HashMap<String, String>();
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance(); 
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(DBUtil.class.getClassLoader().getResourceAsStream(SQL_FILE_NAME)); 
			NodeList sqls = doc.getElementsByTagName(SQL_TAG);
			for (int i = 0; i < sqls.getLength(); i++) {
				Node node = sqls.item(i);
				NamedNodeMap attr = node.getAttributes();
				String id = attr.getNamedItem(SQL_ID).getNodeValue().trim();
				String sql = node.getFirstChild().getNodeValue().trim();
				
				_sqlCache.put(id, sql);
				
				if (sql == null || "".equals(sql.trim())) {
					logger.error("sql.xml is wrong!![%s]" + id);
				}
			}
		} catch (Exception e) {
			logger.error(e.getStackTrace().toString());
		} 
	
	}
	
	/** fn-hd
	  * rem: get sql
	  * @param id
	  * @return
	  * author: hisoft 2012-12-26 2:12:29
	  * log: 
	  */
	public static String getSql(String id) {
		if (_sqlCache == null || _sqlCache.size() == 0) {
			setup();
		}
		return _sqlCache.get(id);
	}
//    public static String buildSQL(String sql,Map<String,String> args){
//    	StringBuffer sb=new StringBuffer();
//    	sb.append(sql);
//         for(Map.Entry<String, String> entry:args.entrySet()){
//        	if(entry.getKey().equals("user_id")){
//        		sb.append(" vm_owner ='"+entry.getValue()+"' and");
//        	}else if(entry.getKey().equals("vm_id")){
//        	    sb.append(" id ='"+entry.getValue()+"' and");
//        	}
//        }
//		return sb.substring(0,sb.length()-4);
//    }
//    public static String getParamValue(Map<String,String> args){
//    	 for(Map.Entry<String, String> entry:args.entrySet()){
//         	if(entry.getKey().equals("machine_no")){
//         		return entry.getValue();
//         	}else if(entry.getKey().equals("vm_id")){
//         		return entry.getValue();
//         	}
//         }
//		return null;
//    	
//    }
    
}
