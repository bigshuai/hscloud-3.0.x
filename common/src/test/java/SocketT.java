import net.sf.json.JSONObject;

import com.hisoft.hscloud.common.util.SocketUtil;

public class SocketT implements Runnable{

		private String uuid;
		public SocketT(String vmid){
			uuid = vmid;
		}
		public String getUuid() {
			return uuid;
		}
		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			JSONObject json = new JSONObject();
			 json.accumulate("IP", "192.168.177.11");
			 json.accumulate("UUID", uuid);
			 JSONObject jsonRoot = new JSONObject();
			 jsonRoot.accumulate("Node", json);
			 SocketUtil su = new SocketUtil(); 
			 String response = su.sendRequest("192.168.177.11",jsonRoot.toString());
		}  
		
}
