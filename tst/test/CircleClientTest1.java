package test;

import client.CircleClient;
import communication.Message;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class CircleClientTest1 {

    private CircleClient client;

    @Before
	public void initClient() throws IOException {
//        client = new CircleClient("huang zhi", new TestHandler(),"127.0.0.1", 9999);
        client = new CircleClient("huang zhi", new TestHandler(),"192.168.23.2", 9999);
//        client = new CircleClient("huang zhi", new TestHandler(),"192.168.22.10", 9999);
//        client = new CircleClient("huang zhi", new TestHandler(),"172.20.10.2", 9999);
	}

    @Test
    public void testTextMessage() throws IOException {

        ArrayList<String> desList = new ArrayList<>();
        desList.add("huang zhi2");
        Message message = new Message();
        message.setMessageType(Message.TEXT);
        message.setMessageSrcID("huang zhi");
        message.setMessageDesIDList(desList);
        message.setMessageContent("Hello world.");
        client.sendTextMessage(message);
        client.sendTextMessage(message);
        client.sendTextMessage(message);
    }

    @Test
    public void testVideoMessage() throws IOException {

        ArrayList<String> desList = new ArrayList<>();
        desList.add("yanghrong@outlook.com");
        Message message = new Message();
        message.setMessageType(Message.VIDEO_INVITATION);
        message.setMessageSrcID("huang zhi");
        message.setMessageDesIDList(desList);
        message.setMessageContent("192.168.23.1");
//        message.setMessageContent("172.20.10.3");
        client.startVideoChat(message);
        while (true) {

        }
    }

    @Test
    public void testVoiceMessage() throws IOException {

        ArrayList<String> desList = new ArrayList<>();
        desList.add("yanghrong@outlook.com");
        Message message = new Message();
        message.setMessageType(Message.VOICE_INVITATION);
        message.setMessageSrcID("huang zhi");
        message.setMessageDesIDList(desList);
        message.setMessageContent("192.168.23.1");

        client.startVoiceChat(message);
        while (true) {

        }
    }

    @Test
    public void testLocalVideoMessage() throws IOException {

        ArrayList<String> desList = new ArrayList<>();
        desList.add("huang zhi2");
        Message message = new Message();
        message.setMessageType(Message.VIDEO_INVITATION);
        message.setMessageSrcID("huang zhi");
        message.setMessageDesIDList(desList);
        message.setMessageContent("127.0.0.1");
        client.startVideoChat(message);
        while (true) {

        }
    }

    @Test
    public void testLocalVoiceMessage() throws IOException {

        ArrayList<String> desList = new ArrayList<>();
        desList.add("huang zhi2");
        Message message = new Message();
        message.setMessageType(Message.VOICE_INVITATION);
        message.setMessageSrcID("huang zhi");
        message.setMessageDesIDList(desList);
        message.setMessageContent("127.0.0.1");
        client.startVideoChat(message);
        while (true) {

        }
    }

    @Test
    public void testDialog() {
        String reminderContent =
                "Your friend would like to start a video chat!";
        String reminderTitle = "A video chat invitation";
        int dialogResult = JOptionPane.showConfirmDialog(null, reminderContent, reminderTitle,
                JOptionPane.INFORMATION_MESSAGE,JOptionPane.YES_NO_OPTION);
    }

    @Test
    public void testSocket() throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        while(true) {
            System.out.println("Waiting.");
            Socket socket = serverSocket.accept();
            System.out.println("Connected");
        }

//        Socket socket = new Socket();
//        InetSocketAddress address = new InetSocketAddress("192.168.23.10", 1234);
//        socket.connect(address);

    }
}
