package com.dh.cltf.fw.net;

import static org.junit.Assert.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dh.cltf.fw.AppException;
import com.dh.cltf.fw.device.ITelnetServer;
import com.dh.cltf.fw.device.TelnetableDevice;
import com.dh.cltf.fw.device.WindowsTelnetServer;
import com.dh.cltf.fw.telnet.WindowsTelnetStreamParser;

public class TestTelnetConnectionToXP {
	private static final Log Log = LogFactory.getLog(TestTelnetConnectionToXP.class);
	
	private static ITelnetServer server = null;
	private static TelnetableDevice device = null;
	
	private static TelnetConnection telnetConnection = null;
	private static WindowsTelnetStreamParser commandResultParser = new WindowsTelnetStreamParser();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		server = new WindowsTelnetServer();
		
		device = new TelnetableDevice("dahuiXP", "localhost", 23, "telnetClient", "password");
		device.setTelnetServer(server);
		
		telnetConnection = new TelnetConnection(device);		
		telnetConnection.addReceiverLisetener(commandResultParser);
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	
	/**
	 * Test the TelnetConnection's open() method.
	 * 
	 * @throws AppException
	 */
	@Test
	public void testTelnetConnection() throws AppException {
		telnetConnection.open();
		assertEquals(telnetConnection.getState(), ConnectionStateEnum.CONNECTED);
	}
	
	@Test
	public void testDir() {
		String result = null;
		final int count = 2;
		
		for (int i = 0; i < count; i ++) {
			result = telnetConnection.sendAndWaitResponse("dir");
			assertTrue(result.indexOf("<DIR>") >0 );
			assertTrue(result.endsWith(">"));
			
			result = telnetConnection.sendAndWaitResponse("d:");
			result = telnetConnection.sendAndWaitResponse("dir");
			
			result = telnetConnection.sendAndWaitResponse("c:");
			result = telnetConnection.sendAndWaitResponse("cd C:\\prj21");
			
		}
	}

	@Test
	public void testClose() throws AppException {
		telnetConnection.close();
	}
}
