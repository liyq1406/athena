package com.athena.print.module.pcenter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.SequenceInputStream;
import java.net.InetSocketAddress;
import java.util.Enumeration;
import java.util.Vector;

import com.toft.core3.util.FileCopyUtils;

/**
 * 测试打印PGL
 * @author dsimedd001
 *
 */
public class SocketPrintTest2 {

	/**
	 * socket
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		java.net.Socket socket = new java.net.Socket();
		socket.connect(new InetSocketAddress("10.26.171.86", 9100), 2000);
		PrintWriter socketWriter = new PrintWriter(socket.getOutputStream());		
		
		Vector<FileInputStream> v = new Vector<FileInputStream>();
		
		v.add(new FileInputStream("c:/test001.txt"));
		v.add(new FileInputStream("c:/test002.txt"));
	    v.add(new FileInputStream("c:/test003.txt"));
	    v.add(new FileInputStream("c:/test004.txt"));
 
	    Enumeration<FileInputStream> en = v.elements();	        
	    BufferedInputStream bis = new BufferedInputStream(new SequenceInputStream(en));
		InputStreamReader inputStreamReader = new InputStreamReader(bis);
		FileCopyUtils.copy(inputStreamReader, socketWriter);
		socketWriter.close();
	}

}
