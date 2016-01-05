package server;

import communication.Message;
import lombok.Builder;
import lombok.Setter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by jiaxing song on 2015/10/5.
 *
 */
@Builder
@Setter
public class CircleServerThread implements Runnable {

    private Socket socket;

    private MessageRouter messageRouter;

    private boolean isOnline;

    private ObjectInputStream objectInputStream;


    @Override
    public void run() {
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            while (isOnline) {
                Message clientMessage = (Message) objectInputStream.readObject();
                if (clientMessage.getMessageType() == Message.HANDSHAKE) {
                    processHandshakeMessage(clientMessage);
                }
                else if (clientMessage.getMessageType() == Message.TEXT) {
                    processTextMessage(clientMessage);
                } else {
                    messageRouter.forwardMessage(clientMessage);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void processHandshakeMessage(Message message) {
        messageRouter.insertEntry(message.getMessageSrcID(), socket);
    }

    private void processTextMessage(Message message) throws IOException {
        messageRouter.forwardMessage(message);
    }
}
