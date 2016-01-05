/*
package server;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import communication.Message;
import lombok.Setter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

@Deprecated
@Setter
public class CircleServerWorker extends UntypedActor {

	private MessageRouter messageRouter;

    private boolean isOnline;

	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		if (message instanceof ClientServerInternalMessage.Connect) {
            isOnline = true;
            this.messageRouter = ((ClientServerInternalMessage.Connect) message).getMessageRouter();
            ActorRef senderWorker =
                    getContext().actorOf(Props.create(CircleServerSenderWorker.class));
		    getSelf().tell(message, senderWorker);
		}
	}

    private class CircleServerSenderWorker extends UntypedActor {

        Socket socket = null;
        @Override
        public void onReceive(Object message) throws Exception {
            if (message instanceof ClientServerInternalMessage.Connect) {
                socket =  ((ClientServerInternalMessage.Connect) message).getSocket();
                ObjectInputStream objInput = new ObjectInputStream(socket.getInputStream());
                while (isOnline) {
                    Message clientMessage = (Message) objInput.readObject();
                    if (clientMessage.getMessageType() == Message.HANDSHAKE) {
                        processHandshakeMessage(clientMessage);
                    }
                    else if (clientMessage.getMessageType() == Message.TEXT) {
                        processTextMessage(clientMessage);
                    }
                }
            }

        }

        private void processHandshakeMessage(Message message) {
            messageRouter.insertEntry(message.getMessageSrcID(), socket);
        }

        private void processTextMessage(Message message) throws IOException {
            messageRouter.forwardMessage(message);
        }
    }

}
*/
