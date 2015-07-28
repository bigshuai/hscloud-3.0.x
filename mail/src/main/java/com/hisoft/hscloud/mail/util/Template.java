package com.hisoft.hscloud.mail.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.log4j.Logger;

public class Template {
	
	static Logger log = Logger.getLogger(Template.class);
    String path = null;
    String template = null;
    
	public String getTemplate() {
		return template;
	}

	public Template(String path) {
		
		if(null == path || "".equals(path)){
			log.info("path:"+path);
		}else{
			this.path = path;
			StringBuilder sb=new StringBuilder();
			InputStream is = Template.class.getClassLoader().getResourceAsStream(this.path);
			String line="";
			try {
				InputStreamReader reader = new InputStreamReader(is,"UTF-8");
				BufferedReader br=new BufferedReader(reader);
				while ((line=br.readLine())!=null) {
					sb.append(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		    this.template = sb.toString();
//		    log.info("template:"+this.template);
		    System.out.println(this.template);
		}
		
	}
	
	public  String getReplacedTemplate(Map<String, String> map){
		
		String templateStr = this.template.toString();
		for (String key : map.keySet()) {
			if (map.get(key) != null) {
				templateStr = templateStr.replaceAll(
						"\\{params:" + key + "\\}", map.get(key));
			}
		}
//		log.info("templateStr:"+templateStr);
		System.out.println(templateStr);
		return templateStr;
		
	}
	
//	public static  void main(String[] args){
//		String path = "\\getPassword.templete";
//		Map map = new HashMap<String,String>();
//		map.put("password", "jjjj");
//		new Template(path).getReplacedTemplate(map);
//	}
	
	
	
	
	

}
