package test;

import client.media.NetworkMonitor;
import org.junit.Test;

/**
 * Created by jiaxing song on 2015/11/16.
 */
public class NetworkMonitorTest {

    @Test
    public void pingTest() {
        NetworkMonitor monitor = new NetworkMonitor(null);
//        System.out.println(monitor.ping("10.136.0.1"));
//        System.out.println(monitor.getAvgRTT("10.136.0.1"));
        System.out.println(monitor.ping("localhost"));
        System.out.println(monitor.getAvgRTT("localhost"));
    }
}
