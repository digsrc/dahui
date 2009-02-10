package com.dh.cltf.fw.net;

import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dh.cltf.fw.device.DslamModeEnum;

public class DslamTelnetProtocol implements IResponseListener {
	private static final Log Log = LogFactory.getLog(DslamTelnetProtocol.class);
	
	public static final String DEVICE_TIP = "switch";
	
	public static final String USER_EXEC_MODE_FLAG_STR = DEVICE_TIP + "> ";
	public static final String PRIVILEGED_EXEC_MODE_FLAG_STR = DEVICE_TIP + "# ";
	public static final String GLOBAL_CONFIGURATION_MODE_FLAG_STR = DEVICE_TIP + "(config)# ";

	public static Hashtable<DslamModeEnum, String> MODE_TABLE = new Hashtable<DslamModeEnum, String>();
	static {
		MODE_TABLE.put(DslamModeEnum.USER_EXEC_MODE, USER_EXEC_MODE_FLAG_STR);
		MODE_TABLE.put(DslamModeEnum.PRIVILEGED_EXEC_MODE, PRIVILEGED_EXEC_MODE_FLAG_STR);
		MODE_TABLE.put(DslamModeEnum.GLOBAL_CONFIGURATION_MODE, GLOBAL_CONFIGURATION_MODE_FLAG_STR);
	}
	
	
	public static final String LOGIN_USERNAME_TIP = "login: ";
	public static final String LOGIN_PASSWORD_TIP = "Password: ";
	
	public static final String LOGIN_WELCOME_TIP = "Welcome to use BTAF:-)";
	
	
	public static final String NEED_PAGEDOWN_FLAG_STR = "--More-- ";

	private DslamModeEnum lastMode;
	private DslamModeEnum currentMode = DslamModeEnum.USER_EXEC_MODE;
	
	public static long INPUT_BUFFER_SIZE = 1024 * 1024;
	private StringBuffer inputCharacters = new StringBuffer();
	
	public void processResponseData(String message) {
		// TODO Auto-generated method stub
		
	}

	public void processResponseData(char ch) {
		Log.trace(ch);
		inputCharacters.append(ch);
		if (inputCharacters.length() > INPUT_BUFFER_SIZE) {
			//TODO
		}
		
		detectCurrentMode();
		getResult();
	}
	
	public void detectFeedMore() {
//		isEndOf();
	}
	
	private boolean isEndOf(StringBuffer source, String str) {
		if (str == null) {
			return false;
		}
		
		int len = source.length();
		if (inputCharacters.lastIndexOf(str) + str.length() == len) {
			return true;
		}
		return false;
	}
	
	public String getResult() {
		String result = null;
		int begIndex = -1;
		int endIndex = -1;
		int length = inputCharacters.length();
		int index = inputCharacters.lastIndexOf(MODE_TABLE.get(currentMode));
		if (index == length - MODE_TABLE.get(currentMode).length()) {
			endIndex = index;
		}
		
		String begFlagStr = MODE_TABLE.get(currentMode);
		if (! lastMode.equals(currentMode)) {
			begFlagStr = MODE_TABLE.get(lastMode);
		}
		if (endIndex > 0) {
			StringBuffer sb = new StringBuffer(inputCharacters.subSequence(0, endIndex));
			begIndex = sb.lastIndexOf(begFlagStr);
		}
		if (begIndex > 0 && begIndex < endIndex) {
			result = inputCharacters.subSequence(begIndex + begFlagStr.length(), endIndex).toString();
			Log.debug("Result=  " + result);
		}
		
		return result;
	}
	
	/**
	 * Detect current mode.
	 * 
	 */
	private void detectCurrentMode() {
		lastMode = currentMode;
		if (inputCharacters.lastIndexOf(USER_EXEC_MODE_FLAG_STR) >= 0) {
			currentMode = DslamModeEnum.USER_EXEC_MODE;
		}else if (inputCharacters.lastIndexOf(PRIVILEGED_EXEC_MODE_FLAG_STR) >= 0) {
			currentMode = DslamModeEnum.PRIVILEGED_EXEC_MODE;
		}else if (inputCharacters.lastIndexOf(GLOBAL_CONFIGURATION_MODE_FLAG_STR) >= 0) {
			currentMode = DslamModeEnum.USER_EXEC_MODE;
		}
		if (lastMode != currentMode) {
			Log.debug("currentMode=" + currentMode + "   oldMode=" + lastMode);
		}
	}
}
