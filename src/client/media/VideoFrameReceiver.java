package client.media;

import client.CircleClientMessageSender;
import communication.DataFrame;
import communication.Message;
import sun.misc.resources.Messages_es;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by jiaxing song on 2015/11/1.
 * Receive, resize and display the frame.
 */
public class VideoFrameReceiver implements Runnable {

    private String remoteID, localID;
    private CircleClientMessageSender messageSender;

    private Socket socket;
    private ObjectInputStream objectInputStream;

    private BufferedImage image;
    private JFrame frame = new JFrame();
    private JPanel controlPanel = new JPanel();
    private VideoPanel videoPanel = new VideoPanel();


    public VideoFrameReceiver(Socket socket) throws IOException, ClassNotFoundException {
        this.socket = socket;
        controlPanel = buildControlPanel();
        frame.getContentPane().add(videoPanel, BorderLayout.CENTER);
        frame.getContentPane().add(controlPanel, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Received Video");
        frame.setLocation(0, 0);
        frame.setSize(480, 360);
        frame.setVisible(true);
    }

    // setters
    public void setMessageSender(CircleClientMessageSender messageSender)  {
        this.messageSender = messageSender;
    }
    public void setID(String remoteID, String localID) {
        this.remoteID = remoteID;
        this.localID = localID;
    }

    private JPanel buildControlPanel() {
        JPanel controlPanel = new JPanel();
        JButton auto = new JButton("Auto");
        JLabel low = new JLabel("Smooth");
        JLabel high = new JLabel("Sharp");
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 640 * 480, 160 * 120);
        addListeners(auto, slider);
        controlPanel.add(auto);
        controlPanel.add(low);
        controlPanel.add(slider);
        controlPanel.add(high);
        return controlPanel;
    }

    private void paintFrame() {
        videoPanel.setImage(image);
        videoPanel.repaint();
        frame.setVisible(true);
    }

    @Override
    public void run() {
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                DataFrame df = (DataFrame) objectInputStream.readObject();
                image = ImageIO.read(new ByteArrayInputStream(df.getBytes()));
                resizeBufferedImage(image);
                paintFrame();
            } catch (IOException e) {
                System.out.println("The session is ended.");
                frame.dispose();
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void resizeBufferedImage(BufferedImage bufferedImage) {
        int resizeWidth = frame.getWidth(), resizeHeight = frame.getHeight();
        Image image = bufferedImage.getScaledInstance(resizeWidth, resizeHeight, Image.SCALE_SMOOTH);
        BufferedImage bufferedResult = new BufferedImage(resizeWidth, resizeHeight, BufferedImage.TYPE_INT_ARGB);
        bufferedResult.getGraphics().drawImage(image, 0, 0, null);
        this.image = bufferedResult;
    }

    private void addListeners(JButton auto, final JSlider slider) {
        auto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // automatically adjust the resolution
                    messageSender.sendMessage(buildResolutionControlMessage(-1));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        slider.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {
                try {
                    // manually adjust the resolution
                    messageSender.sendMessage(buildResolutionControlMessage(slider.getValue()));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }

    private Message buildResolutionControlMessage(int resolution) {
        System.out.println("control:"+localID+" "+remoteID);
        Message message = new Message();
        message.setMessageType(Message.RESOLUTION_CONTROL);
        message.setMessageSrcID(localID);
        ArrayList<String> desList = new ArrayList<>();
        desList.add(remoteID);
        message.setMessageDesIDList(desList);
        message.setMessageContent(String.valueOf(resolution));
        return message;
    }
}
