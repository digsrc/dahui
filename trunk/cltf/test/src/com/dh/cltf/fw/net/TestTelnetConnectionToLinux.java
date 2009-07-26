package com.dh.cltf.fw.net;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dh.cltf.fw.AppException;
import com.dh.cltf.fw.command.TelnetCommand;
import com.dh.cltf.fw.device.ITelnetServer;
import com.dh.cltf.fw.device.LinuxTelnetServer;
import com.dh.cltf.fw.device.TelnetableDevice;
import com.dh.cltf.fw.telnet.WindowsTelnetStreamParser;

public class TestTelnetConnectionToLinux {
	
	private static ITelnetServer server = null;
	private static TelnetableDevice device = null;
	private static TelnetConnection telnetConnection = null;
	private static WindowsTelnetStreamParser commandResultParser = new WindowsTelnetStreamParser();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		server = new LinuxTelnetServer();
		
		device = new TelnetableDevice("dahuiCentOS", "192.168.242.128", 23, "root", "password");
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
	public void testLs() {
		telnetConnection.sendAndWaitResponse("ls");
	}
	
	@Test
	public void testClose() throws AppException {
		telnetConnection.close();
	}
}
