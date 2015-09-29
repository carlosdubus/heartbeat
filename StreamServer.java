import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Random;

public class StreamServer {

  int port;
  HeartbeatSender hbSender;
  long startTime;

  public StreamServer(int port, HeartbeatSender hbSender) {
    this.port = port;
    this.hbSender = hbSender;
  }

  public void run() throws Exception {
    this.hbSender.start();
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
      StreamGenerator sg = new StreamGenerator(0);

      while (true) {
        out.println(sg.nextData());
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
    StreamServer ss = new StreamServer(3000, new HeartbeatSender("127.0.0.1", 1234, 1000));
    ss.run();
  }
}