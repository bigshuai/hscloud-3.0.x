package com.hisoft.hscloud.common.util;

import java.net.InetAddress;

public class IPConvert {
	
	/**
	 * 将IP进制(如:192.168.1.1)转换成十进制(如:3232235777)
	 * @param sIP
	 * @return
	 */
	public static long getIntegerIP(String sIP) {  
        long ip10 = 0;  
        String[] ss = sIP.trim().split("\\.");  
        for (int i = 0; i < 4; i++) {  
            ip10 += Math.pow(256, 3 - i) * Integer.parseInt(ss[i]);  
        }  
        return ip10;  
    } 
	
	/**
	 * 将十进制(如:3232235777)IP转换成IP进制(如:192.168.1.1)
	 * @param iIP
	 * @return
	 */
	public static String getStringIP(long iIP) {  
        String ip = "";  
        long temp = 0;  
  
        for (int i = 3; i >= 0; i--) {  
            temp = iIP / (long) Math.pow(256, i) % 256;  
            if (i == 3) {  
                ip = ip + temp;  
            } else {  
                ip = ip + "." + temp;  
            }  
        }   
        return ip;  
    }
	
	 /**
     * 把IP地址转化为字节数组
     * @param ipAddr
     * @return byte[]
     */
    public static byte[] ipToBytesByInet(String ipAddr) {
        try {
            return InetAddress.getByName(ipAddr).getAddress();
        } catch (Exception e) {
            throw new IllegalArgumentException(ipAddr + " is invalid IP");
        }
    }
    
    /**
     * 根据位运算把 byte[] -> int
     * @param bytes
     * @return int
     */
    public static int bytesToInt(byte[] bytes) {
        int addr = bytes[3] & 0xFF;
        addr |= ((bytes[2] << 8) & 0xFF00);
        addr |= ((bytes[1] << 16) & 0xFF0000);
        addr |= ((bytes[0] << 24) & 0xFF000000);
        return addr;
    }
	
	/**
     * 把IP地址转化为int
     * @param ipAddr
     * @return int
     */
    public static int ipToInt(String ipAddr) {
        try {
            return bytesToInt(ipToBytesByInet(ipAddr));
        } catch (Exception e) {
            throw new IllegalArgumentException(ipAddr + " is invalid IP");
        }
    }
	
	/**
     * 把int->ip地址
     * @param ipInt
     * @return String
     */
    public static String intToIp(int ipInt) {
        return new StringBuilder().append(((ipInt >> 24) & 0xff)).append('.')
                .append((ipInt >> 16) & 0xff).append('.').append(
                        (ipInt >> 8) & 0xff).append('.').append((ipInt & 0xff))
                .toString();
    }

    /**
     * 把192.168.1.1/24 转化为int数组范围
     * @param ipAndMask
     * @return int[]
     */
    public static int[] cidrToIntIPs(String ipAndMask) {

        String[] ipArr = ipAndMask.split("/");
        if (ipArr.length != 2) {
            throw new IllegalArgumentException("invalid ipAndMask with: "
                    + ipAndMask);
        }
        int netMask = Integer.valueOf(ipArr[1].trim());
        if (netMask < 0 || netMask > 31) {
            throw new IllegalArgumentException("invalid ipAndMask with: "
                    + ipAndMask);
        }
        int ipInt = ipToInt(ipArr[0]);
        int netIP = ipInt & (0xFFFFFFFF << (32 - netMask));
        int hostScope = (0xFFFFFFFF >>> netMask);
        return new int[] { netIP, netIP + hostScope };
    }

    /**
     * 把192.168.1.1/24 转化为IP数组范围
     * @param ipAndMask
     * @return String[]
     */
    public static String[] cidrToStringIPs(String ipAndMask) {
        int[] ipIntArr = cidrToIntIPs(ipAndMask);
        return new String[] {intToIp(ipIntArr[0]),intToIp(ipIntArr[1])};
    }


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//System.out.println(getIntegerIP("192.168.1.1"));
		//System.out.println(getStringIP(getIntegerIP("192.168.1.1")));
		String[] ips = cidrToStringIPs("192.168.4.1/24");
		for(int i=0;i<ips.length;i++){
			System.out.println(ips[i]);
		}
	}

}
