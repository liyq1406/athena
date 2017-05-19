package com.athena.ckx.util;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public final class RandomUtil {

	public static Double getInt(Double range){
		Random random=new Random();		
		 double rang =(double) random.nextInt(Integer.parseInt(new DecimalFormat("0").format(range)));
		 return rang<range?rang:rang-1;
	}
	
	public static void main(String[] args) {
//		System.out.println(RandomUtil.getInt(120.0));
		Map<String,String> map = new HashMap<String, String>();
		String s = (String)map.get("sss");
		System.out.println(s);
	}
}
