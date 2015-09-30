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
        startTime = System.currentTimeMillis();
        this.hbSender.start();
        ServerSocket serverSocket = new ServerSocket(port);
        try {
            new Thread(new UnkonwRandomFault(this)).start();
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress());
                new Thread(new ConnectionHandler(socket)).start();
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            System.err.println("Server uptime: " + uptime());
            hbSender.interrupt();
        } finally {
            serverSocket.close();
        }
    }

    public long uptime() {
        return System.currentTimeMillis() - this.startTime;
    }

    public static void main(String[] args) throws Exception {
        StreamServer ss = new StreamServer(3000, new HeartbeatSender("127.0.0.1", 1234, 1000));
        ss.run();
    }

    class UnkonwRandomFault implements Runnable {
        long initialTime;
        long finalTime;
        StreamServer ss;

        public UnkonwRandomFault(StreamServer ss){
            this.initialTime = System.currentTimeMillis();
            this.finalTime = randInt(8000, 15000);
            System.out.println("Unkonw failure will happen in " + this.finalTime + " milliseconds");
        }
        public void run(){
            while (true) {
                if(uptime() > this.finalTime){
                    System.err.println("A failure in the server occurred");
                    System.exit(0);
                }
            }
        }
        public long uptime() {
            return System.currentTimeMillis() - this.initialTime;
        }
        private long randInt(int min, int max) {
            Random rand = new Random();
            int randomNum = rand.nextInt((max - min) + 1) + min;
            return randomNum;
        }
    }

    class ConnectionHandler implements Runnable {
        Socket socket;
        long clientStartTime;
        public ConnectionHandler(Socket socket) {
            this.socket = socket;
        }
        public void run() {
            clientStartTime = System.currentTimeMillis();
            try {
                PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
                StreamGenerator sg = new StreamGenerator(0);

                while (true) {
                    out.println(sg.nextData());
                    Thread.sleep(1000);
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
                System.err.println("Connection [" + socket.getInetAddress() + "] uptime: " + uptime());
            }
        }
        public long uptime() {
            return System.currentTimeMillis() - this.clientStartTime;
        }
    }
}
