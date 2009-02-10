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
	
	private Vector<IResponseListener> respListners = new Vector<IResponseListener>();
	
	public ResponseReceiverThread(InputStream in) {
		super("TelnetReceiverThread");
		this.in = in;
	}
	
	public void run() {
		Log.info("Telnet receiver thread started ... ");
		try {
			char ch = (char) in.read();
			while (ch > 0 && ! isStopReceiver) {
				for (IResponseListener l : respListners) {
					l.processResponseData(ch);
				}
				ch = (char) in.read();
			}
		} catch (IOException e) {
			Log.warn(e.getMessage());
		}
		Log.info("Telnet receiver thread END ... ");
	}

	public void setStopReceiver(boolean isStopReceiver) {
		this.isStopReceiver = isStopReceiver;
	}
	
	public void addResponseListener(IResponseListener listener) {
		respListners.add(listener);
	}
}
