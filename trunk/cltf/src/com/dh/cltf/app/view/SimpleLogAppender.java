package com.dh.cltf.app.view;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

public class SimpleLogAppender extends AppenderSkeleton {

	private static SimpleLogAppender simpleLogAppender = new SimpleLogAppender();
	
	private ILogListener logListener;

	protected int entries;
	protected int maxEntries;

	private SimpleLogAppender() {
		super();
	}
	
	public static SimpleLogAppender getInstance() {
		return simpleLogAppender;
	}

	@Override
	protected void append(LoggingEvent event) {
		String category = event.getLoggerName();
		String logMessage = event.getRenderedMessage();
		String threadDescription = event.getThreadName();
		String level = event.getLevel().toString();
		logListener.processLogMsg(level + " " + threadDescription + " " + category + "  -->" + logMessage + "\r\n");
	}

	public void close() {
	}

	public boolean requiresLayout() {
		return false;
	}

	public void setLogListener(ILogListener logListener) {
		this.logListener = logListener;
	}

}
