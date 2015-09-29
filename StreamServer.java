import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class StreamServer {

  int port;

  public StreamServer(int port) {
    this.port = port;
  }

  public void run() throws Exception {
    ServerSocket listener = new ServerSocket(port);
    try {
      while (true) {
        Socket socket = listener.accept();
        try {
          PrintWriter out =
            new PrintWriter(socket.getOutputStream(), true);
          int count = 0;
          while (true) {
            out.println(count);
            count ++;
            Thread.sleep(1000);
          }
        } finally {
          socket.close();
        }
      }
    } finally {
      listener.close();
    }
  }

  public static void main(String[] args) throws Exception {
    HBSender hs = new HBSender("127.0.0.1", 3000, 1000);
    hs.start();
    System.out.println("Waiting for client...");
    StreamServer ss = new StreamServer(3000);
    ss.run();
  }
}