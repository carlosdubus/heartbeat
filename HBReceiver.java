import java.net.*;
public class HBReceiver {

	int checkingInterval = 100;

	int expireTime = 1000;

	long lastUpdatedTime;

	DatagramSocket socket;

	public void run() throws Exception {
		try {
			while (true) {
				receiveNextPacket();
				long time = System.currentTimeMillis();
				if (time - lastUpdatedTime > expireTime) {
					notifyFailure();
				}
				Thread.sleep(checkingInterval);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			notifyFailure();
		}
	}

	void notifyFailure() {
		System.out.println("Server is dead");
	}

	DatagramSocket createSocket() throws Exception {
		if (socket == null) {
			socket = new DatagramSocket(3000);
			socket.setSoTimeout(expireTime);
		}
		return socket;
	}

	DatagramPacket receiveNextPacket() throws Exception {
		DatagramSocket ds = createSocket();
		byte[] buf = new byte[1024];
		DatagramPacket dp = new DatagramPacket(buf, 1024);
		ds.receive(dp);
		lastUpdatedTime = System.currentTimeMillis();
		return dp;
	}

	public static void main(String[] args) throws Exception {
		new HBReceiver().run();
	}
}
