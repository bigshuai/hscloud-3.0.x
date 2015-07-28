package com.hisoft.hscloud.common.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONObject;

/**
 * 用于客户端类型VNC请求
 * @author dinghb
 *
 */
public class SocketUtil {
	private Socket socket = null;  
	public static Map<String, String> socketMap = new HashMap<String, String>();
	private int port;
	private int timeout;
	private String proxyIP;
	private int proxyPortStart;
	private int proxyPortEnd;
	private int portCount;
	private Object[] portsPool = null;

	public String getProxyIP() {
		return proxyIP;
	}

	public void setProxyIP(String proxyIP) {
		this.proxyIP = proxyIP;
	}

	public SocketUtil(){
		 socketMap = PropertiesUtils.getPropertiesMap("socket.properties", socketMap);
		 port = Integer.valueOf(socketMap.get("socket.port").trim());
		 timeout = Integer.valueOf(socketMap.get("socket.timeout").trim());
		 proxyIP = socketMap.get("socket.proxyIP").trim();
		 proxyPortStart = Integer.valueOf(socketMap.get("socket.proxyPortStart").trim());
		 proxyPortEnd = Integer.valueOf(socketMap.get("socket.proxyPortEnd").trim());
		 portCount = Integer.valueOf(socketMap.get("socket.portCount").trim());
	 }
	
	 private Socket getSocket(String host){
		 try {
			socket = new Socket(host,port);
			socket.setSoTimeout(timeout);
		} catch (UnknownHostException e) {
			System.out.println("主机ip找不到");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("此端口号上无server监听");
			e.printStackTrace();
		}
		 return socket;
	 }
	 
	 /**
	  * 向服务端发送数据，并获得返回信息
	  * @param host
	  * @param jsonRequest
	  * @return
	  */
	 public String sendRequest(String host,String jsonRequest) {
		Socket s = getSocket(host);
		String response = null;
		try {
			//获取输出流，用于客户端向服务器端发送数据
			 DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			 //dos.writeUTF(jsonRequest);
			 dos.write(jsonRequest.getBytes());
			 System.out.println("request:"+jsonRequest);
			 //获取输入流，用于接收服务器端发送来的数据
			 response = this.readStream(s.getInputStream());
			 System.out.println("response:"+response); 
		} catch (Exception e) {
			System.out.println("socket 关闭异常");
			e.printStackTrace();
		}finally{
			try {
				s.close();
			} catch (IOException e) {
				System.out.println("socket 关闭异常");
				e.printStackTrace();
			}
		}
		return response;
	 }
	 
	 private void bindPort(String host, int port) throws Exception {
		 Socket s = new Socket();
		 s.bind(new InetSocketAddress(host, port));
		 s.close();
	 }
	 /**
	  * 查看端口是否可用
	  * @param port
	  * @return
	  */
	 public boolean isPortAvailable(int port) {
		 try {
			 bindPort("0.0.0.0", port);
		     bindPort(proxyIP, port);
		     return true;
		   } catch (Exception e) {
			   System.out.println(port+":被占用，"+e.toString());
		       return false;
		   }
	 }
	 
	 /**
	  * 输入流转换为字符串
	  * @param inStream
	  * @return
	  * @throws Exception
	  */
	 private String readStream(InputStream inStream) throws Exception {  
		    ByteArrayOutputStream outSteam = new ByteArrayOutputStream();  
		    byte[] buffer = new byte[1024];  
		    int len = -1;  
		    if ((len = inStream.read(buffer)) != -1) {  
		        outSteam.write(buffer, 0, len);  
		    }
		    outSteam.close();  
		    inStream.close();  
		    return new String(outSteam.toByteArray());  
		}  
	 /**
	  * 获取随机端口
	  * @return
	  */
	 public Object[] getRandomPorts(){
		 if(portsPool==null){
			 portsPool = getRandomPortsPool(portCount,proxyPortStart,proxyPortEnd);
		 }
		 return portsPool;
		}
	 
	 /**
	  * 获取随机端口池
	  * @param count 端口个数
	  * @param max 端口最大值
	  * @param min 端口最小值
	  * @return
	  */
	 private Object[] getRandomPortsPool(int count,int min,int max){
			Random random = new Random();
	        Object[] values = new Object[count];
	        HashSet<Integer> hashSet = new HashSet<Integer>();
	        
	        // 生成随机数字并存入HashSet
	        while(hashSet.size() < values.length){
	            hashSet.add(random.nextInt(max-min+1) + min);
	        }
	        values = hashSet.toArray();
	        return values;
		}
	 
	 public static void main(String[] args) {
		 JSONObject json = new JSONObject();
		 json.accumulate("IP", "192.168.10.110");
		 json.accumulate("UUID", "e4a48546-8754-449d-9687-bffde227b0c3");
		 JSONObject jsonRoot = new JSONObject();
		 jsonRoot.accumulate("Node", json);
		 SocketUtil su = new SocketUtil(); 
		 String response = su.sendRequest("192.168.4.11",jsonRoot.toString());
		 System.out.println(response);
	}
}
