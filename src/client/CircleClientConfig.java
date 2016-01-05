package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


/**
 * A Singleton class
 * */
public class CircleClientConfig {
	
	// instance
	static private CircleClientConfig config = null;

    //parameters
	static public String IP_ADDRESS = "127.0.0.1";
    static public int PORT = 9999;
    static public InetSocketAddress SOCKET_ADDRESS = new InetSocketAddress(IP_ADDRESS , PORT);
    static private Socket socket;

    static public int VIDEO_PORT = 9988;
    static public int VOICE_PORT = 9977;
    static public int TIME_OUT = 30000;

	static CircleClientConfig getInstance() {
		if (config == null) {
			config = new CircleClientConfig();
		}
		return config;
	}
	
	public CircleClientConfig() {
		socket = new Socket();
        SOCKET_ADDRESS = new InetSocketAddress(IP_ADDRESS , PORT);
		try {
            System.out.println(SOCKET_ADDRESS.toString());
			socket.connect(SOCKET_ADDRESS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Socket getSocket() {
		return this.socket;
	}
}
