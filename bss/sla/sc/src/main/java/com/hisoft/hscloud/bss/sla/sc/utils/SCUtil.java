package com.hisoft.hscloud.bss.sla.sc.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import com.hisoft.hscloud.common.util.Sort;
import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class SCUtil {


@SuppressWarnings({ "unchecked", "rawtypes" })
public static <T> T strutsJson2Object(Class c) throws Exception{
	return (T) new ObjectMapper(). readValue(ServletActionContext.getRequest().getReader(), c);
}


public static List<Sort> json2Object(String json,TypeReference<List<Sort>> typeReference) throws JsonParseException, JsonMappingException, IOException{
return	new ObjectMapper().readValue(json, typeReference);
}

public static String getRequestString() throws IOException{
	return new BufferedReader(ServletActionContext.getRequest().getReader()).readLine();
}
}
