package com.dh.cltf.fw.script;

public class MyTask {
	
	private int x;
	private String y;
	
    public void run() {
        System.out.println("MyTask:  x=" + x + "  y=" + y);
    }

    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(String y) {
        this.y = y;
    }
}

