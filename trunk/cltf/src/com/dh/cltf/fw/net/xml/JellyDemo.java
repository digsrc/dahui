package com.dh.cltf.fw.net.xml;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.XMLOutput;

public class JellyDemo {

	public static void main(String[] args) throws Exception {
	    OutputStream output = new FileOutputStream("c:\\jellyDemo.output.txt");
	    JellyContext context = new JellyContext();
	    XMLOutput xmlOutput = XMLOutput.createXMLOutput(output);
	    context.runScript("src/com/dh/cltf/fw/net/xml/jellyDemo.xml", xmlOutput);
        xmlOutput.flush();
	}
}
