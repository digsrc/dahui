package com.dh.cltf.rcp.view;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

import com.dh.cltf.fw.device.IDevice;
import com.dh.cltf.fw.device.TelnetableDevice;

public class DeviceView extends ViewPart {

	private TableViewer tableViewer;
	
	@Override
	public void createPartControl(Composite parent) {
		createDeviceListTable(parent);
	}
	
	private void createDeviceListTable(Composite parent) {
		Composite topComp = new Composite(parent, SWT.NONE);
		topComp.setLayout(new FillLayout());
		
		int style = SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL;
		tableViewer = new TableViewer(topComp, style);
		
		TableColumn nameColumn = new TableColumn(tableViewer.getTable(), SWT.NONE);
		nameColumn.setWidth(300);
		TableColumn ipColumn = new TableColumn(tableViewer.getTable(), SWT.NONE);
		ipColumn.setWidth(200);
		TableColumn isBusyColumn = new TableColumn(tableViewer.getTable(), SWT.NONE);
		isBusyColumn.setWidth(100);
		
		DeviceLabelProvider labelProvider = new DeviceLabelProvider();
		DeviceContentProvider contentProvider = new DeviceContentProvider();
		tableViewer.setLabelProvider(labelProvider);
		tableViewer.setContentProvider(contentProvider);
		
		List<IDevice> devices = new ArrayList<IDevice>();
		for (int i = 0; i < 9; i ++) {
			TelnetableDevice dev = new TelnetableDevice("name" + i, "ip" + i, i, null, null);
			devices.add(dev);
		}
		tableViewer.setInput(devices);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

}
