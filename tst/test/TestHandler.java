package test;

import client.ReceiverHandler;
import communication.Message;

import java.util.HashSet;

public class TestHandler implements ReceiverHandler {
    @Override
    public void reaction(Message message) {
        System.out.println("Time: "+message.getMessageTimeStamp());
        System.out.println("Type: "+message.getMessageType());
        System.out.println("Sender: "+message.getMessageSrcID());
        System.out.println("Receiver: "+message.getMessageDesIDList());
        System.out.println("Content:" + message.getMessageContent());
    }
}
