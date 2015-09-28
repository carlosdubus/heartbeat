import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;

public class Client {
  public static BufferedReader connect() throws Exception {
    Socket s = new Socket("127.0.0.1", 3000);
    BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
    return input;
  }

  public static void main(String[] args) throws Exception {
    BufferedReader input = connect();
    String stream = null;
    int count = 0;

    while (count<10) {
      stream = input.readLine();
      if(stream == null){
        
        System.out.println("Reconnecting...");
        Thread.sleep(1000);

        try{
          input = connect();
          count = 0;
        }catch(ConnectException ce){
          count += 1;
        }

      }else{
        System.out.println(stream);
      }
    }
  }
}