package com.dh.cltf.fw.net;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dh.cltf.fw.AppException;

public class ResponseReceiverThread extends Thread {
	private static final Log Log = LogFactory.getLog(ResponseReceiverThread.class);
	
	private static final int TELNET_CLIENT_BUFFER_SIZE =  10 * 1024;
	private byte[] respBytes = new byte[TELNET_CLIENT_BUFFER_SIZE];
	
	private StringBuffer responseBuf;
	private String encoding;
	
	private InputStream in;
	
	private volatile boolean isStopReceiver;
	
	private Vector<IReceiverListener> respListners = new Vector<IReceiverListener>();
	
	/**
	 * This thread is used to receive the response from the server.
	 * 
	 * @param responseBuf This is the buffer shared with TelnetConnection class.
	 */
	public ResponseReceiverThread(TelnetConnection telnetConnection) {
		super("responseReceiver-Thread");
		this.responseBuf = telnetConnection.responseBuf;
		
		this.encoding = telnetConnection.getDevice().getTelnetServer().getEncoding();
	}
	
	public void run() {
		try {
				int len = in.read(respBytes);
				if (len == -1) {
					Log.warn("Network stream is closed.");
					Log.warn("ResponseReceiverThread exit."); 
					return;
				}
				
				String tmpResp = "";
				while (len >= 0 && ! isStopReceiver) {
					tmpResp = new String(respBytes, 0, len, encoding);
					synchronized (responseBuf) {
						responseBuf.append(tmpResp);
						responseBuf.notifyAll();
					}
					len = in.read(respBytes);
					if (len == -1) {
						Log.warn("Network stream is closed."); 
						Log.warn("ResponseReceiverThread exit."); 
						return;
					}
				}				
			
		} catch (IOException e) {
			Log.warn(e);
		}
		Log.debug("ResponseReceiverThread Exit."); 
	}
	
//	public void run() {
//		assert in != null : "Input stream must be set!";
//		
//		Log.info("Telnet receiver thread started ... ");
//		try {
//			char ch = (char) in.read();
//			while (ch > 0 && ! isStopReceiver) {
//				for (IReceiverListener l : respListners) {
//					l.processResponseData(ch);
//				}
//				ch = (char) in.read();
//				System.out.print(ch);
//			}
//		}catch (IOException e) {
//			Log.warn(e.getMessage());
//		}catch (Throwable t) {
//			Log.warn(t);
//		}
//		Log.info("Telnet receiver thread END ... ");
//	}

	public void setInputStream(InputStream in) {
		this.in = in;
	}

	public void setStopReceiver(boolean isStopReceiver) {
		Log.info("Set flag to stop the response receiver thread.");
		this.isStopReceiver = isStopReceiver;
	}
	
	public void addResponseListener(IReceiverListener listener) {
		Log.debug("addResponseListener " + listener);
		respListners.add(listener);
	}
}
