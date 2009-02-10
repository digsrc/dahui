package com.dh.cltf.fw.command;

import java.util.concurrent.Callable;

public interface ICommand extends Callable<String> {

	public String call();
	
	public String getExecutor();
}
