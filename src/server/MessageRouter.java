package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import communication.Message;
import lombok.NonNull;

public class MessageRouter {
	private HashMap<String, ObjectOutputStream> routingMap = new HashMap<String, ObjectOutputStream>();
	
	public void insertEntry(@NonNull String id, @NonNull Socket socket) {

        try {
            routingMap.put(id, new ObjectOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("Fail to insert socket into routing table");
            e.printStackTrace();
        }

    }
	
	public void removeEntry(@NonNull String id) {
		routingMap.remove(id);
	}
	
	public void forwardMessage(@NonNull Message msg) throws IOException {
        ObjectOutputStream objectOutputStream;
		for (String DesID : msg.getMessageDesIDList()) {
            if (routingMap.containsKey(DesID)) {
                objectOutputStream = routingMap.get(DesID);
                objectOutputStream.writeObject(msg);
                objectOutputStream.flush();
            }
		}
	}
}
