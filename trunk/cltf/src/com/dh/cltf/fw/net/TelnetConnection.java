package com.dh.cltf.fw.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.telnet.EchoOptionHandler;
import org.apache.commons.net.telnet.InvalidTelnetOptionException;
import org.apache.commons.net.telnet.SuppressGAOptionHandler;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TerminalTypeOptionHandler;

import com.dh.cltf.fw.AppException;
import com.dh.cltf.fw.device.TelnetableDevice;

public class TelnetConnection implements IConnection {
	private static final Log Log = LogFactory.getLog(TelnetConnection.class);

	public static final long TIMEOUT = 1000 * 60;
	private static final int TELNET_CLIENT_BUFFER_SIZE =  10 * 1024;
	private byte[] RESP_BUF = new byte[TELNET_CLIENT_BUFFER_SIZE];
	StringBuffer responseBuf = new StringBuffer();                    
	
	private ConnectionStateEnum state = ConnectionStateEnum.NOT_CONNECT;

	private TelnetableDevice device;

	private IReceiverListener receiverListener;
	private ResponseReceiverThread receiverThread;
	private TelnetClient telnet;
	private InputStream in;
	private PrintStream out;

	
	public TelnetConnection(TelnetableDevice device) {
		this.device = device;
		this.receiverThread = new ResponseReceiverThread(this);
	}
	
	public void addReceiverLisetener(IReceiverListener receiverListener) {
		this.receiverListener = receiverListener;
		receiverThread.addResponseListener(receiverListener);
	}
	
	/**
	 * Initialize the telnet client.
	 */
	private void initTelnetSetting() {
		telnet = new TelnetClient();
		try {
			TerminalTypeOptionHandler ttopt = 
				new TerminalTypeOptionHandler("VT100", false, false, true, false);
			SuppressGAOptionHandler sgaoopt = 
				new SuppressGAOptionHandler(false, true, false, false);
			EchoOptionHandler echoOpt = 
				new EchoOptionHandler(false, false, false, true);
			
			telnet.addOptionHandler(ttopt);
			telnet.addOptionHandler(sgaoopt);
			telnet.addOptionHandler(echoOpt);
		} catch (InvalidTelnetOptionException e) {
			Log.warn(e);
		}
	}
	
	/**
	 * Connect to the target machine with specified IP:Port. 
	 * 
	 * Before to call the open() method, a receiver listener must be set by using
	 * addReceiverLisetner(IReceiverListener receiverListern). 
	 */
	public void open() throws AppException {
		assert receiverListener == null : "Must set reciver listener to process the reponse.";
		
		initTelnetSetting();		
		try {
			Log.info("Trying to connect to " + device.getIp() + ":" + device.getPort());
			
			// telnet connect to device.
			telnet.connect(device.getIp(), device.getPort());
			
			// get the telnet connection's input and output .
			in = telnet.getInputStream();
			out = new PrintStream(telnet.getOutputStream());
			
			login();
			
			receiverThread.setInputStream(in);
			
//			// TODO: hack code. !!!
//			((WindowsTelnetStreamParser) receiverListener).setOut(out);
			
			// start the thread to monitor the data from input stream. 
			receiverThread.start();
			
			state = ConnectionStateEnum.CONNECTED;
			Log.info("Connected to " + device.getIp() + ":" + device.getPort() + " OK.");
		} catch (SocketException e) {
			Log.error(e);
			throw new AppException("Create telnet connection exception.", e);
		} catch (IOException e) {
			Log.error(e);
			throw new AppException("Create telnet connection exception.", e);
		}
	}
	

	private void login() throws AppException {
		readForFlag(device.getTelnetServer().getLoginTip());
		send(device.getLoginId());

		readForFlag(device.getTelnetServer().getPasswordTip());
		send(device.getPassword());

		readForFlag(device.getTelnetServer().getCommandLineTips());
		Log.info("login ok.");
	}
	
	private void readForFlag(String endFlags) throws AppException{
		readForFlag(new String[] {endFlags});
	}
	
	private void readForFlag(String[] endFlags) throws AppException{
		try {
//			int len = in.read(RESP_BUF);
//			if (len == -1) {
//				throw new AppException("Network stream is closed."); 
//			}
//			String buf = new String(RESP_BUF, 0, len, device.getTelnetServer().getEncoding());
//			boolean isFindFlag = false;
//			if (endFlags.length > 1) {
//				for (String flag : endFlags) {
//					if (buf.endsWith(flag)) {
//						isFindFlag = true;
//						break;
//					}
//				}
//			}else {
//				isFindFlag = buf.endsWith(endFlags[0]);
//			}
			String buf = "";
			boolean isFindFlag = false;
			while (! isFindFlag) {
				int len = in.read(RESP_BUF);
				if (len == -1) {
					throw new AppException("Network stream is closed."); 
				}
				buf += new String(RESP_BUF, 0, len, device.getTelnetServer().getEncoding());
				isFindFlag = false;
				if (endFlags.length > 1) {
					for (String flag : endFlags) {
						if (buf.endsWith(flag)) {
							isFindFlag = true;
							break;
						}
					}
				}else {
					isFindFlag = buf.endsWith(endFlags[0]);
				}
			}
			Log.debug(buf);
		} catch (IOException e) {
			Log.warn(e);
			throw new AppException(e); 
		}

	}

	public void enable() {
		send("enable");
//		receiveText();
//
		send("show list");
//		receiveText();
	}

	public void close() throws AppException {
		Log.info("close()");
		try {
			if (receiverThread != null) {
				receiverThread.setStopReceiver(true);
			}
			telnet.disconnect();
			Log.info("Telnet disconnected from " + device.getIp() + ":" + device.getPort() + " OK.");
		} catch (IOException e) {
			Log.warn(e);
			throw new AppException("Disconnect telnet connection exception.", e);
		}
	}

	public String receive() {
		String resp = "";
		synchronized (responseBuf) {
			{
				try {
					responseBuf.wait(TIMEOUT);
				} catch (InterruptedException e) {
					Log.warn(e);
				}
				resp += responseBuf.toString();
				responseBuf.delete(0, responseBuf.length());
				responseBuf.notify();
			}while (! resp.endsWith(">"));
		}
		Log.debug("\r\n<---" + resp);

		return resp;
	}


	
	public void send(String message) {
		out.println(message);
		out.flush();
		Log.debug("---->" + message);
	}

	public String sendAndWaitResponse(String message) {
		String result = "";
		
		out.println(message);
		out.flush();
		Log.debug("---->" + message);
		
		
		synchronized (responseBuf) {	
			responseBuf.notifyAll();
			while (! result.endsWith(">")){
				try {
					responseBuf.wait(TIMEOUT);
					result += responseBuf.toString();
					responseBuf.delete(0, responseBuf.length());
				} catch (InterruptedException e) {
					Log.warn(e);
				}
			}
		}
		//result = processData(result);
		Log.debug(result);
		return result;
	}
	
	
	private String processData(String data) {
		byte[] colorBeg = { 0X1b, 0X5b, 0X30, 0X30, 0X6d, 0X1b, 0X5b };
		byte[] colorEnd = { 0X1b, 0X5b, 0X30, 0X30, 0X6d };
		String colorSettingBeg = null;
		String colorSettingEnd = null;
		try {
			colorSettingBeg = new String(colorBeg, "US-ASCII");
			colorSettingEnd = new String(colorEnd, "US-ASCII");
		} catch (UnsupportedEncodingException e) {
			Log.warn(e);
		}

		byte[] bytes = data.getBytes();
		byte[] newBytes = new byte[data.length()];
		int j = 0;
		for (int i = 0; i < bytes.length; i ++) {
			if (i + 5 < bytes.length && bytes[i] == 0x1B && bytes[i + 1] == 0x5B) {
				i += 4;
			}else {
				newBytes[j ++] = bytes[i];
			}
		}
		
		data = new String(newBytes);
		return data;
	}
	
	public ConnectionStateEnum getState() {
		return state;
	}

	public TelnetableDevice getDevice() {
		return device;
	}

	public void setDevice(TelnetableDevice device) {
		this.device = device;
	}

}
