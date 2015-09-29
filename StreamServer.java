import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Random;

public class StreamServer {

  int port;
  HBSender hbSender;
  long startTime;

  public StreamServer(int port, HBSender hbSender) {
    this.port = port;
    this.hbSender = hbSender;
  }

  public void run() throws Exception {
    ServerSocket listener = new ServerSocket(port);
    try {
      Socket socket = listener.accept();
      handleClient(socket);
    } finally {
      listener.close();
    }
  }

  public long uptime() {
    return System.currentTimeMillis() - this.startTime;
  }

  void raiseRandomException() throws Exception {
    long r = randInt(5000, 10000);
    if (uptime() > r) {
      throw new Exception("Random server exception");
    }
  }

  void handleClient(Socket socket) throws Exception {
    startTime = System.currentTimeMillis();
    try {
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      int count = 0;

      while (true) {
        out.println(count);
        count ++;
        Thread.sleep(1000);
        raiseRandomException();
      }
    } catch (Exception ex) {
      System.err.println(ex.getMessage());
      hbSender.interrupt();
    } finally {
      socket.close();
    }
  }

  long randInt(int min, int max) {
    Random rand = new Random();
    int randomNum = rand.nextInt((max - min) + 1) + min;

    return randomNum;
  }

  public static void main(String[] args) throws Exception {
    HBSender hs = new HBSender("127.0.0.1", 3000, 1000);
    hs.start();
    System.out.println("Waiting for client...");
    StreamServer ss = new StreamServer(3000, hs);
    ss.run();
  }
}