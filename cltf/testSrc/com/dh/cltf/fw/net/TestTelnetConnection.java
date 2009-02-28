package com.dh.cltf.fw.net;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dh.cltf.fw.AppException;
import com.dh.cltf.fw.command.TelnetCommand;

public class TestTelnetConnection {
	private static String ip;
	private static int port;
	private static String userName;
	private static String password;
	
	private static TelnetConnection telnetConnection = null;
	private static WindowsTelnetStreamParser commandResultParser = new WindowsTelnetStreamParser();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ip = "127.0.0.1";
		port = 23;
		
		telnetConnection = new TelnetConnection(ip, port);
		telnetConnection.addReceiverLisetner(commandResultParser);
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		while (telnetConnection.getState() != ConnectionStateEnum.CLOSE) {
			Thread.sleep(1000);
		}
		telnetConnection.close();
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
	public void testCreateTelnetConnection() throws AppException {
		
		telnetConnection.open();

		assertEquals(telnetConnection.getState(), ConnectionStateEnum.CONNECTED);
	}

	@Test
	public void testTelnetLogin() throws AppException {
		
		TelnetCommand command = new TelnetCommand("");
//		
//		telnetConnection.send(message);
//		
//		telnetConnection.disconnect();
	}
}
