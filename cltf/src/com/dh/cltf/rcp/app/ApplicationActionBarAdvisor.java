package com.dh.cltf.rcp.app;


import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import com.dh.cltf.rcp.Activator;
import com.dh.cltf.rcp.RcpConstant;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	private IWorkbenchAction quitAction;
	private IWorkbenchAction aboutAction;
	private IWorkbenchAction newWindowAction;
	private IAction consolePersAction;
	
    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    protected void makeActions(IWorkbenchWindow window) {
    	quitAction = ActionFactory.QUIT.create(window);
    	aboutAction = ActionFactory.ABOUT.create(window);
    	newWindowAction = ActionFactory.OPEN_NEW_WINDOW.create(window);
    	register(quitAction);
    	register(aboutAction);
    	register(newWindowAction);
    	
    	consolePersAction = new ConsolePersAction();
    	register(consolePersAction);
    }

    protected void fillMenuBar(IMenuManager menuBar) {
    	IMenuManager fileMenu = new MenuManager("File(&F)", IWorkbenchActionConstants.M_FILE);
    	IMenuManager persMenu = new MenuManager("Perspective(&P)");
    	IMenuManager helpMenu = new MenuManager("Help(&H)", IWorkbenchActionConstants.M_HELP);
    	
    	menuBar.add(fileMenu);
    	menuBar.add(persMenu);
    	menuBar.add(helpMenu);
    	
    	fileMenu.add(newWindowAction);
    	fileMenu.add(new Separator());
    	fileMenu.add(quitAction);
    	persMenu.add(consolePersAction);
    	helpMenu.add(aboutAction);
    }
    
    protected void fillCoolBar(ICoolBarManager coolBar) {
    	IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
    	coolBar.add(new ToolBarContributionItem(toolbar, "main"));
    	
    	toolbar.add(consolePersAction);
    	toolbar.add(new Separator());
    	toolbar.add(quitAction);
    	
    }
    
    
    private static class ConsolePersAction extends Action {
    	public ConsolePersAction() {
    		setId("ACTION_CONSOLE_PERSPECTIVE");
    		setText("Console");
    	}
    	
    	public void run() {
    		Activator.showPerspective(RcpConstant.CONSOLE_PERSPECTIVE_ID);
    	}
    }
}
