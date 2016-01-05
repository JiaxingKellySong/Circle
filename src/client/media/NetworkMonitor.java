package client.media;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by jiaxing song on 2015/11/16.
 * The monitor measures the RTT in current network and adjust the resolution of video chat.
 */
public class NetworkMonitor implements Runnable{

    // adjust the resolution
    static final int INTERVAL = 5000;

    private VideoFrameSender videoFrameSender;
    private String ip = "localhost";

    private boolean stop = false;

    public NetworkMonitor(VideoFrameSender videoFrameSender) {
        this.videoFrameSender = videoFrameSender;
        this.ip = videoFrameSender.getIP();
        System.out.println("[Monitor Constructor]: "+this.ip);
    }

    public String runSystemCommand(String command) {
        String result = "";

        try {
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader inputStream = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String s;
            // reading output stream of the command
            while ((s = inputStream.readLine()) != null) {
                result = s;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String ping(String ip) {
        return runSystemCommand("ping " + ip + " -n 3");
    }

    public int getAvgRTT(String ip) {
        String line = ping(ip);
        return Integer.valueOf(line.substring(line.lastIndexOf('=') + 1, line.lastIndexOf("ms")).trim());
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        while (!stop) {
            adjustSenderResolution();
            try {
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void adjustSenderResolution() {
        int rtt = getAvgRTT(ip);
        int width = 160, height = 120;
        if (rtt < 10) {
            width *= 4;
            height *= 4;
        } else if (rtt < 50) {
            width *= 2;
            height *= 2;
        }
        videoFrameSender.adjustResolution(width, height);
        System.out.println("The monitor adjusted the resolution: "+width +"*"+height);
    }
}
