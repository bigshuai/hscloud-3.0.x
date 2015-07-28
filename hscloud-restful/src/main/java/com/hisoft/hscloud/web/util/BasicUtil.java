/* 
* 文 件 名:  BasicUtil.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-6-26 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.util; 

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.hisoft.hscloud.web.vo.OrderResultVo;
import com.hisoft.hscloud.web.vo.ResultVo;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-6-26] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class BasicUtil {
    /**
     * <封装返回值> 
    * <功能详细描述> 
    * @param response
    * @param orderResultVo 
    * @see [类、类#方法、类#成员]
     */
    public static void printResult(HttpServletResponse response, OrderResultVo orderResultVo) {
        Gson gson = new Gson();
        try {
            response.setContentType("text/html;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            ServletOutputStream out = response.getOutputStream();
            OutputStreamWriter ow = new OutputStreamWriter(out, "utf-8");
            String resultJson=gson.toJson(orderResultVo).toString().replaceAll("\\\\", "");
            ow.write(resultJson);
            ow.flush();
            ow.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void printResult(HttpServletResponse response, String url) {
        try {
            response.setContentType("text/html;charset=utf-8");
            ServletOutputStream out = response.getOutputStream();
            OutputStreamWriter ow = new OutputStreamWriter(out);
            ow.write("{\"url\":\""+url+"\"}");
            ow.flush();
            ow.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static char[] digital = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static char[] lowerCaseLetters = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','v','y','z'};
    private static char[] upperCaseLetters = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','V','Y','Z'};
    /**
     * <密码校验> 
    * <功能详细描述> 
    * @param password
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public static boolean checkPassword(String password) {
        boolean digitalFlag = false;
        boolean lowerLetterFlag = false;
        boolean upperLetterFlag = false; 
        
        boolean result = false;
        if(password.length() < 6) {
            return result;
        }
        
        for(int i = 0; i < password.length(); i++) {
            char character = password.charAt(i);
            if(checkCharType(digital, character) == true) {
                digitalFlag = true;
            } else if(checkCharType(lowerCaseLetters, character) == true) {
                lowerLetterFlag = true;
            } else if(checkCharType(upperCaseLetters, character) == true) {
                upperLetterFlag = true;
            } else {
                return result;
            }
        }
        if(digitalFlag == true && lowerLetterFlag == true && upperLetterFlag == true) {
            result = true;
        }
        return result;
    }
    
    /**
     * <检查字母类型> 
    * <功能详细描述> 
    * @param array
    * @param character
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public static boolean checkCharType(char[] array, char character) {
        for(char temp : array) {
            if(temp == character) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * <封装返回值> 
    * <功能详细描述> 
    * @param result
    * @param reason 
    * @see [类、类#方法、类#成员]
     */
    public static OrderResultVo getOrderResultVo(boolean result,String reason){
        OrderResultVo orderResultVo=new OrderResultVo();
        orderResultVo.setReason(reason);
        orderResultVo.setSuccess(result);
        return orderResultVo;
    }
    
    /**
     * <填充ResultVo对象> 
    * <功能详细描述> 
    * @param reason
    * @param response 
    * @see [类、类#方法、类#成员]
     */
    public static void fillResultVoFalse(String reason, HttpServletResponse response) {
        ResultVo resultVo = new ResultVo();
        resultVo.setSuccess(false);
        resultVo.setReason(reason);
        printResult(response, resultVo);
    }
    /**
     * <ip校验> 
    * <功能详细描述> 
    * @param accessIp
    * @param queryIp
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public static boolean checkIP(String accessIp, String queryIp) {
        boolean ipIsValid=false;
        String[] arrayIp = queryIp.split(";");
        for(String ip : arrayIp) {
            if(accessIp.equals(ip) || "0.0.0.0".equals(ip)){
                ipIsValid=true;
                break;
            }
        }
        return ipIsValid;
    }
    /**
     * <生成返回值> 
    * <功能详细描述> 
    * @param response
    * @param resultVo 
    * @see [类、类#方法、类#成员]
     */
    public static void printResult(HttpServletResponse response, ResultVo resultVo) {
        Gson gson = new Gson();
        try {
            response.setContentType("text/html;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            ServletOutputStream out = response.getOutputStream();
            OutputStreamWriter ow = new OutputStreamWriter(out, "utf-8");
            String resultJson=gson.toJson(resultVo).toString().replaceAll("\\\\", "");
            ow.write(resultJson);
            ow.flush();
            ow.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * <email验证> 
    * <功能详细描述> 
    * @param email
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public static boolean checkEmail(String email) {
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[0-9a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }
}
