package com.dh.cltf.rcp.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.dh.cltf.rcp.RcpConstant;

public class ConsolePerspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		
		IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, 0.3f, editorArea);
		left.addView(RcpConstant.NAVIGATOR_VIEW_ID);
		
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.5f, "left");
		bottom.addView(RcpConstant.DEVICE_CONSOLE_VIEW_ID);
		
		IFolderLayout right = layout.createFolder("right", IPageLayout.RIGHT, 0.5f, "left");
		right.addView(RcpConstant.DEVICE_LIST_VIEW_ID);
	}
}
