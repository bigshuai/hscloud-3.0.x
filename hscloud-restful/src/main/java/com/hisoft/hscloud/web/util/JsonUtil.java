package com.hisoft.hscloud.web.util;

/**
 * -----------------------------------------------------------------
 * Copyright (c) 2012 hisoft, All Rights Reserved.
 * This software is the proprietary information of hisoft.
 * Use is subject to strict licensing terms.
 * -----------------------------------------------------------------
 *
 * @author: hisoft Feb 25, 2013   2:13:03 PM
 * @brief: 类功能描述
 * @log: hisoft
 */




import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * @author: hisoft
 * @brief: 类功能描述
 * @log: Feb 25, 2013 2:13:03 PM
 **/
public class JsonUtil {
	public static <T>T jsonToBean(String json,Class<T> clazz)
    {
        JSONObject jsonObject = JSONObject.fromObject(json);
        return (T)JSONObject.toBean(jsonObject,clazz);
    }
    @SuppressWarnings("deprecation")
    public static  <T>List<T>  jsonToList(String json,Class<T> clazz)
    {
        System.out.println("json:"+json);
        JSONArray arry=JSONArray.fromObject(json);
        return JSONArray.toList(arry,clazz);
    }
    public static  <T>T[] jsonToArray(String json,Class<T> clazz)
    {
        JSONArray arry=JSONArray.fromObject(json);
        return (T [])JSONArray.toArray(arry,clazz);
    }
    public static String listToJson(List<?> list)
    {
        return JSONSerializer.toJSON(list).toString();
    }

}
