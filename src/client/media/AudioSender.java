package client.media;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by jiaxing song on 2015/11/4.
 * Send the voice to socket output stream.
 */
public class AudioSender implements Runnable{

    private Socket socket;
    private AudioFormat audioFormat;
    private DataLine.Info targetInfo;
    private ObjectOutputStream objectOutputStream;
    private TargetDataLine targetLine;

    private boolean stop;

    public AudioSender(Socket socket) {
        this.socket = socket;
        try {
            this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.stop = false;
        audioFormat = AudioConfig.getAudioFormat();
        targetInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
        try {
            targetLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
            targetLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        targetLine.start();
        int numBytesRead;
        byte[] targetData = new byte[targetLine.getBufferSize() / 5];

        while (!stop) {
            numBytesRead = targetLine.read(targetData, 0, targetData.length);
            if (numBytesRead == -1)	break;

            try {
                objectOutputStream.write(targetData);
                objectOutputStream.flush();
            } catch (IOException e) {
                System.out.println("The audio channel is closed");
                break;
            }
        }
    }

    public void stop() {
        this.stop = true;
    }
}
