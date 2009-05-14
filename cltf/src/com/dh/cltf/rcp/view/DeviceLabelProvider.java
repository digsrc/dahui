package com.dh.cltf.rcp.view;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.dh.cltf.fw.device.IDevice;

public class DeviceLabelProvider implements ITableLabelProvider {

	public Image getColumnImage(Object element, int columnIndex) {
//		if (element instanceof IDevice) {
//			IDevice device = (IDevice) element;
//			switch (columnIndex) {
//			case 0:
//				return device.getDeviceName();
//			case 1:
//				return device.getIp();
//			}
//		}
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof IDevice) {
			IDevice device = (IDevice) element;
			switch (columnIndex) {
			case 0:
				return device.getDeviceName();
			case 1:
				return device.getIp();
			default:
				break;
			}
		}
		return null;
	}

	public void addListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub

	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public boolean isLabelProperty(Object arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public void removeListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub

	}

}
