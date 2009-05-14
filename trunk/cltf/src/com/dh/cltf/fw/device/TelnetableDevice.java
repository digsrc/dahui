package com.dh.cltf.fw.device;

/**
 * This class represents the device which can be telnet to.
 * 
 * @author Dahui
 *
 */
public class TelnetableDevice implements IDevice {

	private String deviceName;
	
	private String ip;
	
	private int port;
	
	private String loginId;
	
	private String password;
	
	private ITelnetServer telnetServer;
	
	
	public TelnetableDevice(ITelnetServer telnetServer) {
		this.telnetServer = telnetServer;
	}
	
	public TelnetableDevice(String deviceName, String ip, int port,
			String loginId, String password) {
		this.deviceName = deviceName;
		this.ip = ip;
		this.port = port;
		this.loginId = loginId;
		this.password = password;
	}
	
	public TelnetableDevice(String name, String ip, int port,
			String loginId, String password, ITelnetServer telnetServer) {
		this(name, ip, port, loginId, password);		
		this.telnetServer = telnetServer;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ITelnetServer getTelnetServer() {
		return telnetServer;
	}

	public void setTelnetServer(ITelnetServer telnetServer) {
		this.telnetServer = telnetServer;
	}



	public DeviceTypeEnum getDeviceType() {
		return DeviceTypeEnum.TELNETABLE_DEVICE;
	}
	
	
	
}
