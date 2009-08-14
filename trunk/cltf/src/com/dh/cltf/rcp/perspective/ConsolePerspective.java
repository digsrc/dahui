package com.dh.cltf.rcp.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.dh.cltf.rcp.RcpConstant;

public class ConsolePerspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		
		IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, 0.26f, editorArea);
		left.addView(RcpConstant.NAVIGATOR_VIEW_ID);
		//left.addView(RcpConstant.DEVICE_LIST_VIEW_ID);
		
		IFolderLayout right = layout.createFolder("right", IPageLayout.RIGHT, 0.74f, editorArea);
		right.addView(RcpConstant.DEVICE_LIST_VIEW_ID);
		
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.7f, "right");
		bottom.addView(RcpConstant.DEVICE_CONSOLE_VIEW_ID);
		
		
//		left.addView(RcpConstant.DEVICE_LIST_VIEW_ID);
		
	}
}
