package com.dh.cltf.fw.net;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dh.cltf.fw.device.DslamModeEnum;

public class DslamCommandResultParser implements IReceiverListener {
	private static final Log LOG = LogFactory.getLog(DslamCommandResultParser.class);
	
	public static final String DEVICE_TIP = "switch";
	
	public static final String NOT_LOGIN_MODE_FLAG_STR = "__NOTLOGIN__@#$";
	public static final String USER_EXEC_MODE_FLAG_STR = DEVICE_TIP + "> ";
	public static final String PRIVILEGED_EXEC_MODE_FLAG_STR = DEVICE_TIP + "# ";
	public static final String GLOBAL_CONFIGURATION_MODE_FLAG_STR = DEVICE_TIP + "(config)# ";

	public static Hashtable<DslamModeEnum, String> MODE_TABLE = new Hashtable<DslamModeEnum, String>();
	static {
		MODE_TABLE.put(DslamModeEnum.USER_EXEC_MODE, USER_EXEC_MODE_FLAG_STR);
		MODE_TABLE.put(DslamModeEnum.PRIVILEGED_EXEC_MODE, PRIVILEGED_EXEC_MODE_FLAG_STR);
		MODE_TABLE.put(DslamModeEnum.GLOBAL_CONFIGURATION_MODE, GLOBAL_CONFIGURATION_MODE_FLAG_STR);
		MODE_TABLE.put(DslamModeEnum.NOT_LOGIN, NOT_LOGIN_MODE_FLAG_STR);
	}
	
	
	public static final String LOGIN_USERNAME_TIP = "login: ";
	public static final String LOGIN_PASSWORD_TIP = "Password: ";
	
	public static final String LOGIN_WELCOME_TIP = "Welcome to use BTAF:-)";
	
	
	public static final String NEED_PAGEDOWN_FLAG_STR = "--More-- ";

	private DslamModeEnum lastMode = DslamModeEnum.NOT_LOGIN;
	private DslamModeEnum currentMode = DslamModeEnum.USER_EXEC_MODE;
	
	public static long INPUT_BUFFER_SIZE = 1024 * 1024;
	private StringBuffer inputCharacters = new StringBuffer();
	
	/** in, out is the stream related with the device. */
//	private InputStream in;
	private PrintStream out;
	
	public DslamCommandResultParser() {
		
	}
	
	public void processResponseData(String message) {
		// TODO Auto-generated method stub
		
	}

	public void processResponseData(char ch) {
		//LOG.trace(ch);
//		if ('#' == ch) {
//			LOG.debug("----");
//		}
		inputCharacters.append(ch);
		if (inputCharacters.length() > INPUT_BUFFER_SIZE) {
			//TODO
		}
		
//		processPrint();
		proceeMorePage();
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
	
	
	int lastLine = 0;
	private void processPrint() {
//		int index1 = inputCharacters.lastIndexOf(PRIVILEGED_EXEC_MODE_FLAG_STR);
//		int firstIndex1 = inputCharacters.length() - PRIVILEGED_EXEC_MODE_FLAG_STR.length();
//		if (index1 >= 0 && index1 == firstIndex1) {
//			inputCharacters.substring(index1);
//			lastLine = index;
//		}
		
		int index = inputCharacters.lastIndexOf("\r\n");
		int firstIndex = inputCharacters.length() - 2;
		if (index >= 0 && index == firstIndex) {
			String line = inputCharacters.substring(lastLine);
			lastLine = index;
			System.out.println(line);
		}
	}
	
	private void proceeMorePage() {
		int index = inputCharacters.lastIndexOf(NEED_PAGEDOWN_FLAG_STR);
		int firstIndex = inputCharacters.length() - NEED_PAGEDOWN_FLAG_STR.length();
		if (index >= 0 && index == firstIndex) {
			sendPageDown();
		}
	}
	
	private void sendPageDown() {
		LOG.debug("sendPageDown");
		char pageDown = 0x22;
		out.print(pageDown);
		out.flush();
	}
	
	/**
	 * TODO  when the output does not finish(such as --MORE--, need more input),
	 *  the current process logic is not right. 
	 * @return
	 */
	public String getResult() {
		String result = null;
		int begIndex = -1;
		int endIndex = -1;
		int length = inputCharacters.length();
		int index = inputCharacters.lastIndexOf(MODE_TABLE.get(currentMode));
		
		if (inputCharacters.lastIndexOf("write terminal") > 0) {
//			LOG.debug(currentMode + "----" + index + "  " + (length - MODE_TABLE.get(currentMode).length()));
		}
		if (index == length - MODE_TABLE.get(currentMode).length()) {
			LOG.debug("----");
			endIndex = index;
			String begFlagStr = MODE_TABLE.get(currentMode);
			LOG.debug(lastMode + "----" + currentMode);
			if (! lastMode.equals(currentMode)) {
				begFlagStr = MODE_TABLE.get(lastMode);
			}
			if (endIndex > 0) {
				StringBuffer sb = new StringBuffer(inputCharacters.subSequence(0, endIndex));
				begIndex = sb.lastIndexOf(begFlagStr);
			}
			if (begIndex > 0 && begIndex < endIndex) {
				result = inputCharacters.subSequence(begIndex + begFlagStr.length(), endIndex).toString();
				if ("34".equals(result) ) {
					LOG.debug("====");
				}
				LOG.debug("Result=  " + result);
			}
		}
		
		return result;
	}
	
	/**
	 * Detect current mode.
	 * 
	 */
	private void detectCurrentMode() {
		int len = inputCharacters.length();
		int modeFlagIndex = -1;
		for (DslamModeEnum mode : MODE_TABLE.keySet()) {
			modeFlagIndex = inputCharacters.lastIndexOf(MODE_TABLE.get(mode));
			if (modeFlagIndex >= 0
					&& modeFlagIndex == len - MODE_TABLE.get(mode).length()) {
				currentMode = mode;
				if (lastMode != currentMode) {
					LOG.debug("currentMode=" + currentMode + "   oldMode=" + lastMode);
					lastMode = currentMode;
				}
				break;
			}
		}
	}


//	public void setIn(InputStream in) {
//		this.in = in;
//	}

	public void setOut(PrintStream out) {
		this.out = out;
	}
}
