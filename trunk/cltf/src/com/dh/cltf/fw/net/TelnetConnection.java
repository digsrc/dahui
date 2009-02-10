package com.dh.cltf.fw.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
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

	private String ip;
	private int port;
	private String userName;
	private String password;

	ResponseReceiverThread receiverThread;
	private TelnetClient telnet;
	private InputStream in;
	private PrintStream out;

	private StringBuffer response = new StringBuffer();

	public TelnetConnection(String ip, int port) {
		super();
		this.ip = ip;
		this.port = port;
	}

	public TelnetConnection(String ip, int port, String userName,
			String password) {
		super();
		this.ip = ip;
		this.port = port;
		this.userName = userName;
		this.password = password;
	}

	public void connect() throws AppException {
		telnet = new TelnetClient();
		try {
			telnet.addOptionHandler(new SuppressGAOptionHandler(false, true,
					false, false));
			telnet.addOptionHandler(new EchoOptionHandler(false, false, false,
					true));
		} catch (InvalidTelnetOptionException e) {
			e.printStackTrace();
		}
		try {

			Log.info("Trying to connect to " + ip + ":" + port);
			telnet.connect(ip, port);
			in = telnet.getInputStream();
			out = new PrintStream(telnet.getOutputStream());
			login();
			
			receiverThread = new ResponseReceiverThread(in);
			receiverThread.addResponseListener(new DslamTelnetProtocol());
			receiverThread.start();
			
			synchronized (this) {
				try {
					wait(1000 * 3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
//			
			Log.info("Connected to " + ip + ":" + port + " OK.");
		} catch (SocketException e) {
			Log.error(e);
			throw new AppException("Create telnet connection exception.", e);
		} catch (IOException e) {
			Log.error(e);
			throw new AppException("Create telnet connection exception.", e);
		}
		// catch (InvalidTelnetOptionException e) {
		// Log.error(e);
		// throw new AppException("Init Telnet connection exception.", e);
		// }
	}

	private void login() {
		String tip = receiveText();
		send(userName);

		tip = receiveText();
		send(password);

		tip = receiveText("switch> ");

		Log.info("login ok.");
	}

	public void enable() {
		send("enable");
//		receiveText();
//
//		send("show cli");
//		receiveText();
	}

	public void disconnect() throws AppException {
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

	public String receiveText() {
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
			// synchronized (this) {
			// try {
			// wait(500);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }
			while (in.available() > 0) {
				int a = in.available();
				byte[] buff = new byte[a];
				ret_read = in.read(buff, 0, a);
				sb.append(new String(buff));
				System.out.println(new String(buff));
			}
			// do {
			// availableBytes = in.available();
			// if (availableBytes >= 0) {
			// ret_read = in.read(buff);
			// }
			// if (ret_read > 0) {
			// System.out.print(new String(buff, 0, ret_read));
			// }
			// } while (ret_read >= 0);
		} catch (IOException e) {
			Log.warn(e);
		}

		return sb.toString();
	}

	public String receiveText(String endFlagStr) {
		StringBuilder sb = null;
		try {
			sb = new StringBuilder();

			byte ch = (byte) in.read();
			while (ch > 0) {
				sb.append((char) ch);
				if (sb.indexOf(endFlagStr) >= 0) {
					break;
				}
				ch = (byte) in.read();
			}
		} catch (IOException e) {
			Log.warn(e);
		}

		Log.debug("<----" + sb.toString());
		return sb.toString();
	}

	public void send(String message) {
		out.println(message);
		out.flush();
		Log.debug("---->" + message);
	}

	public String send(String message, String endStrFlag) {
		send(message, endStrFlag);
		String resp = receiveText();
		Log.debug(resp);
		return resp;
	}
}
