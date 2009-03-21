package com.dh.cltf.rcp.view;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.IViewDescriptor;

import com.dh.cltf.rcp.RcpConstant;

public class LogGuiAppender extends AppenderSkeleton {

	protected ILogRecorder logRecoder;

	protected int entries;
	protected int maxEntries;

	public LogGuiAppender() {
		super();
		
		IViewDescriptor vd = PlatformUI.getWorkbench().getViewRegistry().find(RcpConstant.DEVICE_CONSOLE_VIEW_ID);
		if (vd instanceof ILogRecorder) {
			logRecoder = (ILogRecorder) vd;
		}

        Logger logger = Logger.getLogger("console");
        Layout layout = new PatternLayout("%p [%t] %c (%F:%L) - %m%n");
       
        setLayout(layout);
        logger.addAppender(this);
	}
	
	public LogGuiAppender(ILogRecorder recoder) {
		logRecoder = recoder;
	}

	@Override
	protected void append(LoggingEvent event) {
		String category = event.getLoggerName();
		String logMessage = event.getRenderedMessage();
		String threadDescription = event.getThreadName();
		String level = event.getLevel().toString();
		logRecoder.appendLogMsg(level + " " + threadDescription + " " + category + "  -->" + logMessage + "\r\n");
	}

	public void close() {
	}

	public boolean requiresLayout() {
		return true;
	}

}
