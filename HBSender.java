import java.net.*;

public class HBSender extends Thread {
  int sendingInterval;
  String ip;
  int port;

  public HBSender(String ip, int port, int sendingInterval) {
    this.ip = ip;
    this.port = port;
    this.sendingInterval = sendingInterval;
  }

  public void run() {
    DatagramSocket ds = null;
    try {
      ds = new DatagramSocket();
      String str = ".";
      InetAddress ipAddress = InetAddress.getByName(ip);

      DatagramPacket dp = new DatagramPacket(str.getBytes(), str.length(), ipAddress, port);

      while (true) {
        ds.send(dp);
        System.out.print(".");
        Thread.sleep(sendingInterval);
      }
    } catch (Exception e) {
      System.out.println("HBSender dead");
      e.printStackTrace();
    } finally {
      if (ds != null)
        ds.close();
    }
  }
}
