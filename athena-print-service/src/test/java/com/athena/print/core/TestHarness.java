package com.athena.print.core;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.SequenceInputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
 

import com.toft.core3.util.FileCopyUtils;

public class TestHarness {
    public long timeTasks() throws InterruptedException, IOException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(2);
        final java.net.Socket socket = new java.net.Socket(); 
        socket.connect(new InetSocketAddress("10.26.171.88", 9100), 2000);
            Thread t1 = new Thread() {
                public void run() {
                	PrintWriter socketWriter = null;
                    try {
                        
                		
                		socketWriter = new PrintWriter(socket.getOutputStream());		
                		final Vector<FileInputStream> v = new Vector<FileInputStream>();
                		v.add(new FileInputStream("C:/oracle/print/2012-12-18/W0000000239/W0000000239-BLX800001.txt"));
                		v.add(new FileInputStream("C:/oracle/print/2012-12-18/W0000000239/W0000000239-W00024800P.txt"));
                		v.add(new FileInputStream("C:/oracle/print/2012-12-18/W0000000239/W0000000239-W00024900P.txt"));
                		v.add(new FileInputStream("C:/oracle/print/2012-12-18/W0000000239/W0000000239-W00025000P.txt"));
                		v.add(new FileInputStream("C:/oracle/print/2012-12-18/W0000000239/W0000000239-W00078600P.txt"));
                		v.add(new FileInputStream("C:/oracle/print/2012-12-18/W0000000239/W0000000239-W00078700P.txt"));
                		v.add(new FileInputStream("C:/oracle/print/2012-12-18/W0000000239/W0000000239-W00078800P.txt"));
                		v.add(new FileInputStream("C:/oracle/print/2012-12-18/W0000000239/W0000000239-W00078900P.txt"));
                		v.add(new FileInputStream("C:/oracle/print/2012-12-18/W0000000239/W0000000239-W00079000P.txt"));
                		v.add(new FileInputStream("C:/oracle/print/2012-12-18/W0000000239/W0000000239-W00079100P.txt"));
                		v.add(new FileInputStream("C:/oracle/print/2012-12-18/W0000000239/W0000000239-W00079200P.txt"));
                		v.add(new FileInputStream("C:/oracle/print/2012-12-18/W0000000239/W0000000239-W00079300P.txt"));
                		v.add(new FileInputStream("C:/oracle/print/2012-12-18/W0000000239/W0000000239-W00079400P.txt"));
                		v.add(new FileInputStream("C:/oracle/print/2012-12-18/W0000000239/W0000000239-W00079500P.txt"));
                		v.add(new FileInputStream("C:/oracle/print/2012-12-18/W0000000239/W0000000239-W00079600P.txt"));
                		v.add(new FileInputStream("C:/oracle/print/2012-12-18/W0000000239/W0000000239-W00079700P.txt"));
                        startGate.await();
                        try { 
                        	System.out.println("t1线程开始发文件");
                    		Enumeration<FileInputStream> en = v.elements();	        
                    		BufferedInputStream bis = new BufferedInputStream(new SequenceInputStream(en));
                    		InputStreamReader inputStreamReader = new InputStreamReader(bis);
                    		FileCopyUtils.copy(inputStreamReader, socketWriter);
                    		System.out.println("t1线程结束发文件");
                        } catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							
                            endGate.countDown();
                        }
                    } catch (InterruptedException ignored) {
                    } catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally { 
						socketWriter.close();
						System.out.println("t1线程关闭socket");
					}
                }
            };
            t1.start();

            Thread t2 = new Thread() {
                public void run() {  
                	PrintWriter socketWriter = null;
                    try {  
                		socketWriter = new PrintWriter(socket.getOutputStream());		
                		final Vector<FileInputStream> v = new Vector<FileInputStream>();
                		v.add(new FileInputStream("C:/oracle/print/2012-12-18/W0000000296/W0000000296-W00000042.txt"));
                		v.add(new FileInputStream("C:/oracle/print/2012-12-18/W0000000296/W0000000296-W000009292.txt"));
                		v.add(new FileInputStream("C:/oracle/print/2012-12-18/W0000000296/W0000000296-W000009293.txt"));
                		v.add(new FileInputStream("C:/oracle/print/2012-12-18/W0000000296/W0000000296-W000009294.txt"));
                		v.add(new FileInputStream("C:/oracle/print/2012-12-18/W0000000296/W0000000296-W000009295.txt"));
                		v.add(new FileInputStream("C:/oracle/print/2012-12-18/W0000000296/W0000000296-W000009296.txt"));
                		v.add(new FileInputStream("C:/oracle/print/2012-12-18/W0000000296/W0000000296-W000009297.txt"));
                		v.add(new FileInputStream("C:/oracle/print/2012-12-18/W0000000296/W0000000296-W000009298.txt"));
                		v.add(new FileInputStream("C:/oracle/print/2012-12-18/W0000000296/W0000000296-W000009299.txt"));
                		v.add(new FileInputStream("C:/oracle/print/2012-12-18/W0000000296/W0000000296-W000009300.txt"));
                		v.add(new FileInputStream("C:/oracle/print/2012-12-18/W0000000296/W0000000296-W000009303.txt"));
                        startGate.await();
                        try { 
                        	System.out.println("t2线程开始发文件");
                    		Enumeration<FileInputStream> en = v.elements();	        
                    		BufferedInputStream bis = new BufferedInputStream(new SequenceInputStream(en));
                    		InputStreamReader inputStreamReader = new InputStreamReader(bis);
                    		FileCopyUtils.copy(inputStreamReader, socketWriter);
                    		System.out.println("t2线程结束发文件");
                        } catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
                        } finally { 
                            endGate.countDown();
                        }
                    } catch (InterruptedException ignored) {
                    } catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally { 
						socketWriter.close();
						System.out.println("t2线程关闭socket");
					}
                }
            };
            t2.start();
            
            
        long start = System.nanoTime();
        startGate.countDown();
        endGate.await();
        long end = System.nanoTime();
        socket.close();
        return end - start;
    }
    
	
	public static void createFile() throws IOException{
		String picpath= File.separator + "Oracle" + File.separator + "print";
		System.out.println(picpath);
		Calendar ca = Calendar.getInstance();
		File path = new File(picpath);
		if(!path.exists()){
			path.mkdir();
		}
		picpath = picpath + File.separator + ca.get(Calendar.YEAR)+"-"+(ca.get(Calendar.MONTH)+1)+"-"+ca.get(Calendar.DATE);
		path = new File(picpath);
		if(!path.exists()){
			path.mkdir();
		}
		picpath = picpath + File.separator + "123ABC";
		path = new File(picpath);
		if(!path.exists()){
			path.mkdir();
		}
		path= new File(picpath+"a.txt");
		
		path.createNewFile();
	}
	
    public static void main(String[] args) throws InterruptedException, IOException {
//    	TestHarness t = new TestHarness();  
//    	System.out.print((float)t.timeTasks());
    	createFile();
    }
 
}


