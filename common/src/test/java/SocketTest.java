public class SocketTest{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Thread(new SocketT("03e9b6b7-2274-4cf1-8f7b-5c6fefa67ac3")).start();
		new Thread(new SocketT("b0014d43-856f-4c23-abbc-ec180176da54")).start();
		new Thread(new SocketT("5ae33b3e-55a9-41e6-8410-939780c409ff")).start();
		new Thread(new SocketT("97ad02bf-a6da-4310-94a3-1f27809763f1")).start();
		new Thread(new SocketT("74ae91d9-884b-4cf2-8361-da45fab5bc99")).start();
		new Thread(new SocketT("00783d3f-1a06-4590-a88f-2d2fd5e2beea")).start();
	}
}
