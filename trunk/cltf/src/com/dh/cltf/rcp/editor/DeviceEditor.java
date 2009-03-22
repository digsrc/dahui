package com.dh.cltf.rcp.editor;

import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

/*
 * Editor used as device management. 
 */
public class DeviceEditor extends EditorPartAdapter {
	private TableViewer tv;
	
//	public void createPartContorl(Composite parent) {
//		ViewForm topComp = new ViewForm(parent, SWT.NONE);
//		topComp.setLayout(new FillLayout());
//		createTableViewer(topComp);
//		tv.setContentProvider(new DeviceContentProvider());
//		tv.setLabelProvider(new DeviceLabelIProvider());
//	}
//	
//	private void createTableViewer(Composite parent) {
//		tv = new TableViewer(parent, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
//		Table table = tv.getTable();
//		table.setHeaderVisible(true);
//		table.setLinesVisible(true);
//		table.setLayout(new TableLayout());
//		
//		createColumn(10, "");
//	}
//	
//	private TableColumn 
}
