package com.dh.cltf.fw.command;

import java.util.Date;

import com.dh.cltf.fw.AppException;
import com.dh.cltf.fw.net.IConnection;

public class TelnetCommand implements ICommand {
	private static long sequence = 0;
	
	private long id;
	private String command;
	private String endFlag;
	private String result;
	
	private Date executeTime;
	private Date endTime;
	
	private IConnection connection;
	
	public TelnetCommand(String command) {
		this.id = getNextId();
		this.command = command;
	}
	
	public TelnetCommand(String command, String endFlag) {
		this.id = getNextId();
		this.command = command;
		this.endFlag = endFlag;
	}
	
	public static synchronized long getNextId() {
		return sequence ++;
	}

	public String call() {
		try {
			connection.send(command);
		} catch (AppException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getExecutor() {
		return null;
	}
	

	
}
