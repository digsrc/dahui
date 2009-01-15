package com.dh.cltf.app.logic;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dh.cltf.app.view.DeviceConsoleView;

public class LogGenerator extends Thread {
	
	private static final Log logger = LogFactory.getFactory().getInstance(DeviceConsoleView.class);
    
	public LogGenerator(String tname) {
		super(tname);
	}
	
	public static void genLog() { 
		System.out.println("====genLog");
		Date now = new Date();
		System.out.println(now);
		logger.debug("debug message " + now);
		logger.warn("waring " + now);
	}
	
	public void run() {
		LogGenerator.genLog();
	}
}
