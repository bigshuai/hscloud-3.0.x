package com.pactera.hscloud.common4j.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 
 * <一句话功能简述> <功能详细描述>
 * 
 * @author houyh
 * @version [版本号, 2013-4-24]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class InvokeMethodUtil {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object invokeMethod(Object methodObject, String methodName,
			Object[] args) throws Exception {
		try {
			Class ownerClass = methodObject.getClass();
			Class[] argsClass = new Class[args.length];
			for (int i = 0, j = args.length; i < j; i++) {
				argsClass[i] = args[i].getClass();
			}
			Method method = ownerClass.getMethod(methodName, argsClass);
			return method.invoke(methodObject, args);
		} catch (Exception e) {
			StringBuilder msg=new StringBuilder(); 
		        if (e instanceof InvocationTargetException) {  
		            Throwable targetEx = ((InvocationTargetException) e)  
		                    .getTargetException();  
		            if (targetEx != null) {  
		                msg.append(targetEx.toString()).append("\n")
		                .append(Arrays.toString(targetEx.getStackTrace()));
		            }  
		        } else {  
		        	msg.append(e.toString()).append("\n")
	                .append(Arrays.toString(e.getStackTrace()));  
		        }  

			throw new Exception("invoke "
					+ methodObject.getClass().getSimpleName() + "'s method:"
					+ methodName + " Exception." + "\n" + msg);
		}
	}

	public static void main(String[] args) throws Exception {
		invokeMethod(new InvokeMethodUtil(), "getNone", new Object[] {});
	}

	public void getNone() {
		System.out.println("no args");
	}
}
