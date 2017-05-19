package com.athena.util.uid;

public class CreateUid {
	
	private static long lastTime = System.currentTimeMillis();  
    private static int lastCount =  32768;  
    private static Object mutex = new Object();  
    private static long ONE_SECOND = 1000L; 

	/**
     * 仿hibernate生成随机uid主键类型的值  add by zhangl 20110212
     * 
     * @param int size    定制返回主键的长度(当参数小于8的时候,默认为8)
     * @return String uid 返回的主键
     * @throws BusinessServiceException
     */
	public static String getUID(int size) throws RuntimeException {
		
		String uid = "";
		
		try{
			uid = createUID( size );
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
		return uid;
	}
	
    private static String createUID(int size) throws Exception{ 
    	
        long l = 0L;  
        int word0 = 0;  
        int i = 0;
        
        if( size <= 8 ){
        	size = 8;
        }
        
        synchronized(mutex){  
        	
            if(lastCount == -32767){  
            	
                for(boolean flag = false; !flag;){  
                	
                    l = System.currentTimeMillis(); 
                    
                    if(l < lastTime + ONE_SECOND){                           
                        Thread.currentThread();  
                        Thread.sleep(ONE_SECOND);                          
                    }else{  
                        lastTime = l;  
                        lastCount = 32768;  
                        flag = true;  
                    }  
                }  
            }else{  
                l = lastTime;  
            } 
            word0 = lastCount--;  
            i = getHostUniqueNum();  
        }
               
        String s = Integer.toString(i, 16)  + Long.toString(l, 16)  + Integer.toString(word0, 16);  
        
        if(s.length() > size ){  
            s = s.substring(s.length() - size );  
        }
        
        return s;
        
    }
    
    private static int getHostUniqueNum(){  
        return (new Object()).hashCode();  
    }  

}
