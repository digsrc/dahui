package com.dh.cltf.rcp.app.view;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
//import org.eclipse.ui.console.ConsolePlugin;
//import org.eclipse.ui.console.IConsole;
//import org.eclipse.ui.console.IConsoleManager;
//import org.eclipse.ui.console.MessageConsole;
//import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.part.ViewPart;

public class NavigatorView extends ViewPart {
	private static final Log Log = LogFactory.getLog(ILogListener.DSLAM_CONSOLE_LOG_NAME);
	private static final Log DebugLog = LogFactory.getLog(NavigatorView.class);
		
	// Generate a log to test GUI appender.
	private Action genLogAction;
	
	private Action showConsoleAction;
	
	@Override
	public void createPartControl(Composite parent) {
		Composite topComp = new Composite(parent, SWT.NONE);
		topComp.setLayout(new FillLayout());
		
		genLogAction = new Action() {
			public void run() {
				Date now = Calendar.getInstance().getTime();
				Log.debug("Log generated from Navigator view. " + now.getTime());
				Log.debug("It is ok.");
			}
		};
		genLogAction.setText("Generate Log");
		genLogAction.setToolTipText("Generate Log");
		
		
		showConsoleAction = new Action() {
			public void run() {
				Log.info("show console");
				DebugLog.debug("Hello Iam debug log.");
//				IConsoleManager mgr = ConsolePlugin.getDefault().getConsoleManager();
//				MessageConsole console = new MessageConsole("myconsole1", null);
//				mgr.addConsoles(new IConsole[] {console});
//				mgr.showConsoleView(console);
//				
//				MessageConsoleStream stream = console.newMessageStream();
//				stream.setColor(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
//				stream.print("Hello console1");
			}
		};
		showConsoleAction.setText("Show Console");
		showConsoleAction.setToolTipText("Show console");
		
		
		IToolBarManager toolBarManager = getViewSite().getActionBars().getToolBarManager();
		toolBarManager.add(genLogAction);
		toolBarManager.add(showConsoleAction);
		
		IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
		menuManager.add(genLogAction);
		menuManager.add(showConsoleAction);
	}

	@Override
	public void setFocus() {
	}


}
