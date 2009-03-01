package com.dh.cltf.fw.net;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ResponseReceiverThread extends Thread {
	private static final Log Log = LogFactory.getLog(ResponseReceiverThread.class);
	
	private InputStream in;
	
	private volatile boolean isStopReceiver;
	
	private Vector<IReceiverListener> respListners = new Vector<IReceiverListener>();
	
	public ResponseReceiverThread() {
		super("responseReceiver-Thread");
	}
	
	public void run() {
		assert in != null : "Input stream must be set!";
		
		Log.info("Telnet receiver thread started ... ");
		try {
			char ch = (char) in.read();
			while (ch > 0 && ! isStopReceiver) {
				for (IReceiverListener l : respListners) {
					l.processResponseData(ch);
				}
				ch = (char) in.read();
				System.out.print(ch);
			}
		}catch (IOException e) {
			Log.warn(e.getMessage());
		}catch (Throwable t) {
			Log.warn(t);
		}
		Log.info("Telnet receiver thread END ... ");
	}

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
