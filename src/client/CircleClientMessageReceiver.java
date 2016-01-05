package client;

import client.media.*;
import communication.Message;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class CircleClientMessageReceiver implements Runnable{

    private String remoteID, localID;
	public boolean isOnline;
	private Socket socket;
    private ReceiverHandler receiverHandler;
    private CircleClientMessageSender sender;
    private InetSocketAddress videoSocketAddress;
    private InetSocketAddress voiceSocketAddress;

    private VideoFrameSender videoFrameSender;
    private AudioSender audioSender;
    private NetworkMonitor monitor;

	public CircleClientMessageReceiver(ReceiverHandler receiverHandler,
                                       CircleClientMessageSender sender) throws IOException {
		this.socket = CircleClientConfig.getInstance().getSocket();
        this.receiverHandler = receiverHandler;
        this.sender = sender;
		isOnline = true;
	}

	public void run() {
		// TODO Auto-generated method stub
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            JFrame dummy = new JFrame();
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            dummy.setLocation(dim.width/2-dummy.getSize().width/2, dim.height/2-dummy.getSize().height/2);
            dummy.setAlwaysOnTop(true);

            while (isOnline) {
                Message message = (Message) objectInputStream.readObject();
                System.out.println("type:"+message.getMessageType());

                if (message.getMessageType() == Message.VIDEO_INVITATION) {
                    String reminderContent =
                            "Your friend " + message.getMessageSrcID() + " would like to start a video chat!";
                    String reminderTitle = "A video chat invitation";
                    dummy.setVisible(false);
                    int dialogResult =
                            JOptionPane.showConfirmDialog(dummy, reminderContent, reminderTitle, JOptionPane.OK_CANCEL_OPTION);
                    if(dialogResult == JOptionPane.OK_OPTION) {
                        getIDs(message);
                        videoSocketAddress = new InetSocketAddress(message.getMessageContent(), CircleClientConfig.VIDEO_PORT);
                        voiceSocketAddress = new InetSocketAddress(message.getMessageContent(), CircleClientConfig.VOICE_PORT);
                        sender.sendMessage(buildAcceptMessage(message, Message.VIDEO_INVITATION_RESPONSE));
                    }
                    dummy.dispose();
                }
                else if (message.getMessageType() == Message.VOICE_INVITATION) {
                    String reminderContent =
                            "Your friend " + message.getMessageSrcID() + " would like to start a voice chat!";
                    String reminderTitle = "A voice chat invitation";
                    dummy.setVisible(false);
                    int dialogResult =
                            JOptionPane.showConfirmDialog(dummy, reminderContent, reminderTitle, JOptionPane.OK_CANCEL_OPTION);
                    if(dialogResult == JOptionPane.OK_OPTION) {
                        getIDs(message);
                        voiceSocketAddress = new InetSocketAddress(message.getMessageContent(), CircleClientConfig.VOICE_PORT);
                        sender.sendMessage(buildAcceptMessage(message, Message.VOICE_INVITATION_RESPONSE));
                    }
                    dummy.dispose();
                }
                else if (message.getMessageType() == Message.VIDEO_INVITATION_RESPONSE
                        && message.getMessageContent().equals(Message.ACCEPT)) {
                    getIDs(message);
                    // the other side accept the video chat invitation
                    sender.sendMessage(buildServerReadyMessage(message, Message.VIDEO_SERVER_READY));
                    startVideoServer();
                    sender.sendMessage(buildServerReadyMessage(message, Message.VOICE_SERVER_READY));
                    startVoiceServer();
                }
                else if (message.getMessageType() == Message.VOICE_INVITATION_RESPONSE
                        && message.getMessageContent().equals(Message.ACCEPT)) {
                    getIDs(message);
                    // the other side accept the voice chat invitation
                    sender.sendMessage(buildServerReadyMessage(message, Message.VOICE_SERVER_READY));
                    startVoiceServer();
                }
                else if (message.getMessageType() == Message.VIDEO_SERVER_READY) {
                    Thread.sleep(100);
                    startVideoClient();
                }
                else if (message.getMessageType() == Message.VOICE_SERVER_READY) {
                    Thread.sleep(100);
                    startVoiceClient();
                }
                else if (message.getMessageType() == Message.RESOLUTION_CONTROL) {
                    int resolution = Integer.valueOf(message.getMessageContent());
                    if (resolution == -1) {
                        monitor = new NetworkMonitor(videoFrameSender);
                        // start the automatic adjustment
                        new Thread(monitor).start();
                    } else {
                        // stop the automatic adjustment
                        if (monitor != null) { monitor.stop(); }
                        int width = (int) Math.sqrt(resolution / 12) * 4;
                        int height = (int) Math.sqrt(resolution / 12) * 3;
                        System.out.println(resolution+" "+width+" "+height);
                        videoFrameSender.adjustResolution(width, height);
                    }
                }
                else {
                    receiverHandler.reaction(message);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startVideoServer() {
        try {
            ServerSocket videoServer = new ServerSocket(CircleClientConfig.VIDEO_PORT);
            videoServer.setSoTimeout(CircleClientConfig.TIME_OUT);
            // start as a sever, and waiting for the other end to response
            System.out.println("Waiting clients...");
            Socket videoSocket = videoServer.accept();
            videoFrameSender = new VideoFrameSender(videoSocket);
            VideoFrameReceiver videoFrameReceiver = new VideoFrameReceiver(videoSocket);
            videoFrameReceiver.setMessageSender(sender);
            videoFrameReceiver.setID(remoteID, localID);
            new Thread(videoFrameSender).start();
            new Thread(videoFrameReceiver).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void startVideoClient() throws IOException, ClassNotFoundException {
        Socket videoSocket = new Socket();
        videoSocket.connect(videoSocketAddress);
        VideoFrameReceiver videoFrameReceiver = new VideoFrameReceiver(videoSocket);
        videoFrameReceiver.setMessageSender(sender);
        videoFrameReceiver.setID(remoteID, localID);
        videoFrameSender = new VideoFrameSender(videoSocket);
        new Thread(videoFrameReceiver).start();
        new Thread(videoFrameSender).start();
    }

    private void startVoiceServer() {
        try {
            ServerSocket voiceServer = new ServerSocket(CircleClientConfig.VOICE_PORT);
            voiceServer.setSoTimeout(CircleClientConfig.TIME_OUT);
            // start as a sever, and waiting for the other end to response
            Socket voiceSocket = voiceServer.accept();
            audioSender = new AudioSender(voiceSocket);
            new Thread(audioSender).start();
            new Thread(new AudioReceiver(voiceSocket)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startVoiceClient() throws IOException {
        Socket voiceSocket = new Socket();
        voiceSocket.connect(voiceSocketAddress);
        new Thread(new AudioReceiver(voiceSocket)).start();
        new Thread(new AudioSender(voiceSocket)).start();
    }

    private Message buildAcceptMessage(Message receivedMessage, int type) {
        ArrayList<String> desList = new ArrayList<>();
        desList.add(receivedMessage.getMessageSrcID());
        Message message = new Message();
        message.setMessageType(type);
        message.setMessageContent(Message.ACCEPT);
        message.setMessageDesIDList(desList);
        message.setMessageSrcID(receivedMessage.getMessageDesIDList().get(0));
        return message;
    }

    private Message buildServerReadyMessage(Message receivedMessage, int type) {
        ArrayList<String> desList = new ArrayList<>();
        desList.add(receivedMessage.getMessageSrcID());
        Message message = new Message();
        message.setMessageType(type);
        message.setMessageDesIDList(desList);
        message.setMessageSrcID(receivedMessage.getMessageDesIDList().get(0));
        return message;
    }

    /**
     * Get the local ID and remote ID by parsing the received message
     * */
    private void getIDs(Message message) {
        localID = message.getMessageDesIDList().get(0);
        remoteID = message.getMessageSrcID();
    }
}
