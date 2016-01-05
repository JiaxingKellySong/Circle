package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CircleServer{
	
	private ServerSocket serverSocket;
	private MessageRouter messageRouter;

	public CircleServer() throws Exception {
		// TODO Auto-generated constructor stub
		serverSocket = new ServerSocket(9999);
		messageRouter = new MessageRouter();
	}
	
	public void startService() throws IOException {
		System.out.println("The server is online...");
		while (true) {
			// create a new socket for coming client
			Socket clientSocket = serverSocket.accept();
            // start a new thread to handle this client
            CircleServerThread serverThread = CircleServerThread.builder()
                    .isOnline(true)
                    .messageRouter(messageRouter)
                    .socket(clientSocket)
                    .build();

            new Thread(serverThread).start();
		}
	}

}

