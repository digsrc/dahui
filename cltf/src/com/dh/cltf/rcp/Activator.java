package com.dh.cltf.rcp;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}


	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}


	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(RcpConstant.PLUGIN_ID, path);
	}
	
	public static void showPerspective(String perspectiveId) {
		try {
	        String pbar = "com.dh.cltf.rcp.ConsolePerspective,default";
			PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.PERSPECTIVE_BAR_EXTRAS, pbar);
			PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.PERSPECTIVE_BAR_EXTRAS, pbar);
	        PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR,IWorkbenchPreferenceConstants.TOP_RIGHT);
	        PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.SHOW_TEXT_ON_PERSPECTIVE_BAR,"true"); 
	        
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			PlatformUI.getWorkbench().showPerspective(perspectiveId, window);
		}catch(WorkbenchException e) {
			e.printStackTrace();
		}
	}
}
