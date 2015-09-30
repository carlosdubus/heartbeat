# heartbeat

Follow the next steps to run the example:

```
$ git clone https://github.com/carlosdubus/heartbeat
$ cd heartbeat
$ javac *.java
```
Open three terminals.
In the first terminal, run the StreamServer:
```sh
java StreamServer
```

In the second terminal, run the HeartbeatReceiver:
```sh
java HeartbeatReceiver
```

In the third terminal, run the Client:
```
java Client
```

The client will display the stream received from the StreamServer. The StreamServer will crash between 5 and 10 seconds after a client is connected. When the server crashes, HeartbeatReceiver will detect the failure and write "Server is dead" to the console.
