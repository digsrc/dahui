package com.dh.cltf.fw.net;

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
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ip = "172.18.103.14";
		port = 23;
		userName = "root";
		password = "vertex25";
		
		telnetConnection = new TelnetConnection(ip, port, userName, password);
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		telnetConnection.disconnect();
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testCreateTelnetConnection() throws AppException {
		
		telnetConnection.connect();

		telnetConnection.enable();
		
		synchronized (this) {
			try {
				wait(1000 * 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

//	@Test
//	public void testTelnetLogin() throws AppException {
//		TelnetConnection telnetConnection = new TelnetConnection(ip, port);
//		telnetConnection.connect();
//		
//		TelnetCommand command = new TelnetCommand("");
//		
//		telnetConnection.send(message);
//		
//		telnetConnection.disconnect();
//	}
}
