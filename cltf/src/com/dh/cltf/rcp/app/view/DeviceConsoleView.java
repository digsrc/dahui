package com.dh.cltf.rcp.app.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;



public class DeviceConsoleView extends ViewPart implements ILogListener{
	private static final Log dslam_Log = LogFactory.getLog(DSLAM_CONSOLE_LOG_NAME);
	private static final Log n2x_Log = LogFactory.getLog(N2X_CONSOLE_LOG_NAME);
	
	private Text msgBox;
	private Action clearAction;
	
	private final int defaultFontSize = 9;
	
	
	@Override
	public void createPartControl(Composite parent) {
		constructComponents(parent);
		initializeToolBar();
		initializeMenuBar();
		
		String[] perceivedLogger = {DSLAM_CONSOLE_LOG_NAME, N2X_CONSOLE_LOG_NAME};
		initLogAppender(perceivedLogger);
	}

	@Override
	public void setFocus() {
	}
	
	
	private void constructComponents(Composite parent) {
		Composite topComp = new Composite(parent, SWT.NONE);
		topComp.setLayout(new FillLayout());
        
        msgBox = new Text(topComp, SWT.READ_ONLY | SWT.BORDER | SWT.V_SCROLL
				| SWT.H_SCROLL | SWT.MULTI);
        msgBox.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2,
				1));
        msgBox.setFont(new Font(Display.getDefault(), "Courier New", defaultFontSize, SWT.NORMAL));
		/* set white background */
        msgBox.setBackground(new Color(Display.getDefault(), 255, 255, 255));
        msgBox.append("hello world");
        

        
		clearAction = new Action() {
			public void run() {
				msgBox.setText("");
				dslam_Log.debug("Clear Action is clicked.");
				n2x_Log.warn("from n2x device.");
//				LogGenerator genLogThread = new LogGenerator("genLog-Thread");
//				genLogThread.start();
			}
		};
//		clearAction.setImageDescriptor();
		clearAction.setText("Clear");
		clearAction.setToolTipText("Clear");		
	}
	
	private void initializeToolBar() {
		IToolBarManager toolBarManager = getViewSite().getActionBars()
				.getToolBarManager();
		toolBarManager.add(clearAction);
	}
	
	private void initializeMenuBar() {
		IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
		menuManager.add(clearAction);
	}

	
	/**
	 * 
	 * @param appenderName
	 */
	public void initLogAppender(String[] loggerNames) {
		// Create appender instance.
		SimpleLogAppender appender = SimpleLogAppender.getInstance();
		appender.setLogListener(this);
		
		// Create/Get logger
		Logger[] loggers = new Logger[loggerNames.length];
		for (int i = 0; i < loggerNames.length; i ++) {
			loggers[i] = Logger.getLogger(loggerNames[i]);
			loggers[i].setLevel(Level.ALL);
			// set the appender to the logger.
			loggers[i].addAppender(appender);
		}
    }
	
	public void processLogMsg(String msg) {
		msgBox.append(msg);
	}
}
