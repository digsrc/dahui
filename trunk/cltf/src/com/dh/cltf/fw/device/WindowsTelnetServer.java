package com.dh.cltf.fw.device;

public class WindowsTelnetServer implements ITelnetServer {

	public static final String loginTip = "login: ";
	public static final String passwordTip = "password: ";
	public static final String DEFAULT_ENCODING = "GBK";
	
	public static final String defaultCommandLineTip = ">";
	public static final String commandLineTips[] = { ">" };
	
	private String encoding;
	
	public WindowsTelnetServer() {
	}
		
	public String getEncoding() {
		if (encoding == null) {
			return DEFAULT_ENCODING;
		}
		return encoding;
	}

	public String getLoginTip() {
		return loginTip;
	}

	public String getPasswordTip() {
		return passwordTip;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String[] getCommandLineTips() {
		return commandLineTips;
	}
}
