import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {

  /**
   * Runs the client as an application.  First it displays a dialog
   * box asking for the IP address or hostname of a host running
   * the date server, then connects to it and displays the date that
   * it serves.
   */
  public static void main(String[] args) throws IOException {
    Socket s = new Socket("127.0.0.1", 3000);
    BufferedReader input =
      new BufferedReader(new InputStreamReader(s.getInputStream()));
    String answer = null;
    while ((answer = input.readLine()) != null) {
      System.out.println(answer);
    }
  }
}