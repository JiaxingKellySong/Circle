package client;
import communication.Message;
import lombok.Setter;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Setter
public class CircleClientMessageSender {
	private Socket socket;
    private ObjectOutputStream objectOutputStream;
	
	public CircleClientMessageSender() throws IOException {
        socket = CircleClientConfig.getInstance().getSocket();
		objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
	}

    public void sendMessage(Message message) throws IOException {
        objectOutputStream.writeObject(message);
        objectOutputStream.flush();
    }


}
