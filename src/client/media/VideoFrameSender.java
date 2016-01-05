package client.media;

import communication.DataFrame;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by jiaxing song on 2015/11/1.
 * This class capture the web-camera and send the data frame.
 */
public class VideoFrameSender implements Runnable  {

    private static int FRAME_WIDTH = 160;
    private static int FRAME_HEIGHT = 120;

    private ObjectOutputStream objectOutputStream;
    private Socket socket;
    private VideoCapture camera;

    private boolean stop = false;
    private boolean RESET_RESOLUTION = false;

    public VideoFrameSender(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        camera = new VideoCapture(0);
        // set the default resolution
        camera.set(Videoio.CAP_PROP_FRAME_WIDTH, FRAME_WIDTH);
        camera.set(Videoio.CAP_PROP_FRAME_HEIGHT, FRAME_HEIGHT);

        Mat frame = new Mat();
        camera.read(frame);

        if(!camera.isOpened()){
            System.out.println("Error");
        }
        else {
            System.out.println("Start sending video...");
            while(!stop){
                if (camera.read(frame)){
                    try {
                        BufferedImage image =MatToBufferedImage(frame);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(image, "png", baos );
                        baos.flush();
                        byte[] bytes = baos.toByteArray();
                        objectOutputStream.writeObject(new DataFrame(bytes));
                        objectOutputStream.flush();
                        Thread.sleep(50);
                        if (RESET_RESOLUTION) {
                            camera.set(Videoio.CAP_PROP_FRAME_WIDTH, FRAME_WIDTH);
                            camera.set(Videoio.CAP_PROP_FRAME_HEIGHT, FRAME_HEIGHT);
                            RESET_RESOLUTION = false;
                        }
                    } catch (IOException e) {
                        System.out.println("The video chat ended.");
                        break;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        camera.release();
    }

    public BufferedImage MatToBufferedImage(Mat frame) {
        //Mat() to BufferedImage
        int type = 0;
        if (frame.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (frame.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        frame.get(0, 0, data);

        return image;
    }

    public void stop() {
        this.stop = true;
    }

    public void adjustResolution(int width, int height) {
        VideoFrameSender.FRAME_WIDTH = width;
        VideoFrameSender.FRAME_HEIGHT = height;
        RESET_RESOLUTION = true;
    }

    public String getIP() {
        return this.socket.getInetAddress().getHostName();
    }

}


