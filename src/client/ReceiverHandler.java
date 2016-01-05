package client;

import communication.Message;

/**
 * Created by jiaxing song on 2015/10/2.
 * This interface is used for the client to react after receiving a message from the server.
 */
public interface ReceiverHandler {
    void reaction(Message message);

}
