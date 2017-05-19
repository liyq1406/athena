package com.athena.print.module.pcenter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;

import com.toft.core3.util.FileCopyUtils;

/**
 * 测试打印PGL
 * @author dsimedd001
 *
 */
public class SocketPrintTest {

	/**
	 * socket
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		for( int i=0; i<3; i++){
			java.net.Socket socket = new java.net.Socket();
			socket.connect(new InetSocketAddress("10.26.171.86", 9100), 0);
			PrintWriter socketWriter = new PrintWriter(socket.getOutputStream());
			socketWriter.println("---this is the ---"+(i+1)+"--print---------");
			FileInputStream input = new FileInputStream(new File("c:/test003.txt"));
			InputStreamReader inputStreamReader = new InputStreamReader(input);
			FileCopyUtils.copy(inputStreamReader, socketWriter);
			socketWriter.close();
		}
	}

}
