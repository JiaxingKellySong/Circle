package client.media;

import communication.DataFrame;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by jiaxing song on 2015/11/7.
 * Audio receiver thread.
 */
public class AudioReceiver implements Runnable {
    private JFrame audioFrame;
    private JPanel timePanel;
    private JLabel timeLabel;

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private Timer timer;
    private int counter, hh = 0, mm = 0, ss = 0;

    DataLine.Info sourceInfo;

    public AudioReceiver (Socket socket) {
        this.socket = socket;
        try {
            objectInputStream = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        audioFrame = new JFrame();
        timePanel = new JPanel();
        timeLabel = new JLabel();
        timeLabel.setText("Voice Chatting:   "+String.format("%02d", hh) +":"+String.format("%02d", mm) + ":" +
                String.format("%02d",ss));
        timePanel.add(timeLabel);
        audioFrame.add(timePanel);
        counter = 0;
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                counter++;
                hh = counter / 3600;
                mm = (counter % 3600) / 60;
                ss = counter % 60;
                timeLabel.setText("Voice Chatting:   "+String.format("%02d", hh) +":"+String.format("%02d", mm) + ":"
                        + String.format("%02d",ss));
            }
        });
        timer.start();
        audioFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        audioFrame.pack();
        audioFrame.setVisible(true);
    }
    @Override
    public void run() {
        sourceInfo = new DataLine.Info(SourceDataLine.class, AudioConfig.getAudioFormat());

        SourceDataLine sourceLine = null;
        try {
            sourceLine = (SourceDataLine) AudioSystem.getLine(sourceInfo);
            sourceLine.open(AudioConfig.getAudioFormat());
            sourceLine.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        while (sourceLine != null) {
            try {
                byte[] data = new byte[sourceLine.getBufferSize() / 5];
                objectInputStream.readFully(data);
                sourceLine.write(data, 0, data.length);

            } catch (IOException e) {
                System.out.println("The audio channel is closed");
                break;
            }
        }
    }
}
