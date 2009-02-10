package com.dh.cltf.app.view;

public interface ILogListener {
	
	public static final String DSLAM_CONSOLE_LOG_NAME= "DslamConsole";
	public static final String N2X_CONSOLE_LOG_NAME= "N2xConsole";
	
	public void initLogAppender(String[] loggerName);
	
	public void processLogMsg(String logMsg);
	
}
