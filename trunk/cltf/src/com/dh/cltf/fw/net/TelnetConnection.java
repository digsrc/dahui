package com.dh.cltf.fw.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.SocketException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.telnet.EchoOptionHandler;
import org.apache.commons.net.telnet.InvalidTelnetOptionException;
import org.apache.commons.net.telnet.SuppressGAOptionHandler;
import org.apache.commons.net.telnet.TelnetClient;

import com.dh.cltf.fw.AppException;

public class TelnetConnection implements IConnection {
	private static final Log Log = LogFactory.getLog(TelnetConnection.class);

	private static final long TIMEOUT = 1000 * 60;

	private ConnectionStateEnum state = ConnectionStateEnum.NOT_CONNECT;

	private String ip;
	private int port;
	private String userName;
	private String password;

	private IReceiverListener receiverListern;
	private ResponseReceiverThread receiverThread;
	private TelnetClient telnet;
	private InputStream in;
	private PrintStream out;

	private StringBuffer response = new StringBuffer();

	public TelnetConnection(String ip, int port) {
		super();
		this.ip = ip;
		this.port = port;
		this.receiverThread = new ResponseReceiverThread();
	}
	
	public TelnetConnection(String ip, int port, String userName,
			String password) {
		this(ip, port);
		this.userName = userName;
		this.password = password;
	}

	public void addReceiverLisetner(IReceiverListener receiverListern) {
		this.receiverListern = receiverListern;
		receiverThread.addResponseListener(receiverListern);
	}
	
	/**
	 * Set the telnet client.
	 */
	private void initTelnetSetting() {
		telnet = new TelnetClient();
		try {
			telnet.addOptionHandler(new SuppressGAOptionHandler(false, true,
					false, false));
			telnet.addOptionHandler(new EchoOptionHandler(false, false, false,
					true));
		} catch (InvalidTelnetOptionException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Connect to the target machine with specified IP:Port. 
	 * 
	 * Before to call the open() method, a receiver listener must be set by using
	 * addReceiverLisetner(IReceiverListener receiverListern). 
	 */
	public void open() throws AppException {
		assert receiverListern == null : "Must set reciver listener to process the reponse.";
		
		initTelnetSetting();		
		try {
			Log.info("Trying to connect to " + ip + ":" + port);
			
			// telnet connect to device.
			telnet.connect(ip, port);
			
			// get the telnet connection's input and output .
			in = telnet.getInputStream();
			receiverThread.setInputStream(in);
			out = new PrintStream(telnet.getOutputStream());
			
			// start the thread to monitor the data from input stream. 
			receiverThread.start();
			
			state = ConnectionStateEnum.CONNECTED;
			Log.info("Connected to " + ip + ":" + port + " OK.");
		} catch (SocketException e) {
			Log.error(e);
			throw new AppException("Create telnet connection exception.", e);
		} catch (IOException e) {
			Log.error(e);
			throw new AppException("Create telnet connection exception.", e);
		}
	}

	private void login() {
		String tip = receive();
		send(userName);

		tip = receive();
		send(password);

//		tip = receiveText("switch> ");

		Log.info("login ok.");
	}

	public void enable() {
		send("enable");
//		receiveText();
//
		send("show list");
//		receiveText();
	}

	public void close() throws AppException {
		try {
			if (receiverThread != null) {
				receiverThread.setStopReceiver(true);
			}
			telnet.disconnect();
			Log.info("Telnet disconnected from " + ip + ":" + port + " OK.");
		} catch (IOException e) {
			e.printStackTrace();
			throw new AppException("Disconnect telnet connection exception.", e);
		}
	}

	public String receive() {
		synchronized (this) {
			try {
				wait(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		StringBuilder sb = null;
		try {
			sb = new StringBuilder();
			byte ch = (byte) in.read();
			while (ch > 0) {
				sb.append((char) ch);
				int availabeBytes = in.available();
				if (availabeBytes > 0) {
					ch = (byte) in.read();
				} else {
					break;
				}
			}
		} catch (IOException e) {
			Log.warn(e);
		}
		Log.debug("\r\n<---" + sb.toString());

		return sb.toString();
	}

	public String receiveTextA() {
		StringBuilder sb = null;
		try {
			sb = new StringBuilder();
			byte ch = (byte) in.read();
			while (ch > 0) {
				sb.append((char) ch);
				ch = (byte) in.read();
			}
		} catch (IOException e) {
			Log.warn(e);
		}
		Log.debug("\r\n<---" + sb.toString());

		return sb.toString();
	}

	public String receiveTextB() {
		synchronized (this) {
			try {
				wait(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		StringBuilder sb = new StringBuilder();
		byte[] buff = new byte[1024];
		int ret_read = 0;
		do {
			try {
				ret_read = in.read(buff);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (ret_read > 0) {
				sb.append(new String(buff, 0, ret_read));
				Log.debug("\r\n<---" + sb.toString());
			}
		} while (ret_read >= 0);

		return sb.toString();
	}

	public String receiveTextByBuf() {
		StringBuilder sb = new StringBuilder();
		int ret_read = 0;
		int availableBytes = 0;
		try {
			while (in.available() > 0) {
				int a = in.available();
				byte[] buff = new byte[a];
				ret_read = in.read(buff, 0, a);
				sb.append(new String(buff));
				System.out.println(new String(buff));
			}

		} catch (IOException e) {
			Log.warn(e);
		}

		return sb.toString();
	}


	
	
	
	public void send(String message) {
		out.println(message);
		out.flush();
		Log.debug("---->" + message);
	}

	public String send(String message, String endStrFlag) {
		send(message, endStrFlag);
		String resp = receive();
		Log.debug(resp);
		return resp;
	}
	
	
	public ConnectionStateEnum getState() {
		return state;
	}

}
