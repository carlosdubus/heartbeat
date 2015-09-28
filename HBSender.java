import java.net.*;
public class HBSender {
	public static void main(String[] args) throws Exception {
		DatagramSocket ds = new DatagramSocket();
		try {
			String str = "heartbeat";
			InetAddress ip = InetAddress.getByName("127.0.0.1");

			DatagramPacket dp = new DatagramPacket(str.getBytes(), str.length(), ip, 3000);
			while (true) {
				ds.send(dp);
				System.out.print(".");
				Thread.sleep(1000);
			}
		} finally {
			ds.close();
		}
	}
}
