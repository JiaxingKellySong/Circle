package test;

import org.junit.Test;
import server.CircleServer;

public class CircleServerTest {

	@Test
	public void testServer() throws Exception {
		CircleServer cs = new CircleServer();
		cs.startService();

	}
}
