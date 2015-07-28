package com.pactera.hscloud.hscloudhandler.util;

import java.net.*;
import java.util.*;
public class IPMACUtil {	
	public static String getMacAddr() {
		String MacAddr = "";
		try {
			NetworkInterface NIC = NetworkInterface.getByName("eth0");
			if(null==NIC){
				NIC=NetworkInterface.getByName("em1");
			}
			byte[] buf = NIC.getHardwareAddress();
			StringBuffer sb = new StringBuffer();
	        
	        for(int i=0;i<buf.length;i++){
	            if(i!=0){
	                sb.append(":");
	            }
	            //mac[i] & 0xFF 是为了把byte转化为正整数
	            String s = Integer.toHexString(buf[i] & 0xFF);
	            sb.append(s.length()==1?0+s:s);
	        }
			MacAddr = sb.toString().toUpperCase();
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return MacAddr;
	}

	public static String getLocalIP() {
		String ip = "";
		try {
			Enumeration<?> e1 = (Enumeration<?>) NetworkInterface
					.getNetworkInterfaces();
			while (e1.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) e1.nextElement();
				if (ni.getName().equals("eth0")||ni.getName().equals("em1")) {
					Enumeration<?> e2 = ni.getInetAddresses();
					while (e2.hasMoreElements()) {
						InetAddress ia = (InetAddress) e2.nextElement();
						if (ia instanceof Inet6Address)
							continue;
						ip = ia.getHostAddress();
					}
					break;
				} 
			}
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return ip;
	}
}