package com.athena.ckx.module.transTime;

import java.util.ArrayList;
import java.util.List;

import com.athena.ckx.entity.transTime.CkxBiaozgzsj;



public class Test {
	public static List<CkxBiaozgzsj> getlist(){
		List<CkxBiaozgzsj> list=new ArrayList<CkxBiaozgzsj>();
//		CkxBiaozgzsj b=new CkxBiaozgzsj();
//		b.setXiehzt("0001");
//		b.setXuh(1);
//		b.setKaissj("0");
//		b.setJiessj("3");
//		
//		CkxBiaozgzsj b1=new CkxBiaozgzsj();
//		b1.setXiehzt("0001");
//		b1.setXuh(2);
//		b1.setKaissj("5");
//		b1.setJiessj("8");
//		
//		CkxBiaozgzsj b2=new CkxBiaozgzsj();
//		b2.setXiehzt("0001");
//		b2.setXuh(3);
//		b2.setKaissj("9");
//		b2.setJiessj("12");
		
//		list.add(b);list.add(b1);list.add(b2);
		return list;
	}
	public static void function(){
//		List<CkxBiaozgzsj> list=getlist();
//		float count=0;//卸货站台总工作时间
//		int pc=10;//送货频次
//		for (CkxBiaozgzsj b : list) {
//			float k=Float.valueOf(b.getKaissj());//工作开始时间
//			float j=Float.valueOf(b.getJiessj());//工作结束时间
//			count+=(j-k);//循环累加工作时间，得到总工作时间
//		}
//		float add=count/pc;// 1.4   送货时间间隔=总工作时间/送货频次
//		float rod =0;//随机时间
//		float begin=Float.valueOf(list.get(0).getKaissj());//开始时间（起始的）
//		float afterEnd=0;
//		float x=0;
//		
//		System.out.println(add);
//		int s=0;
//		for (CkxBiaozgzsj b : list) {
//			float k=Float.valueOf(b.getKaissj());//工作开始时间
//			float j=Float.valueOf(b.getJiessj());//工作结束时间
//			x+=k-afterEnd;
//			afterEnd=j;
//			System.out.println(k+"================"+x+"================="+j);
//
//
//			for (int i = 0; i < pc; i++) {
//				s++;
//				float biaoz = rod + i * add;// 标准时间偏量
//				float shij=biaoz+x;//实际时间偏量
//				float dao=begin+shij;
//				if(dao>=k&&dao<j){
//					System.out.println(biaoz+"----------"+shij+"----------"+dao);
//				}
//			}
//		}
//		System.out.println(s);
	}
	
	
	
	
	public static void function1(){
//		List<CkxBiaozgzsj> list=getlist();
//		float count=0;//卸货站台总工作时间
//		int pc=2;//送货频次
//		for (CkxBiaozgzsj b : list) {
//			float k=Float.valueOf(b.getKaissj());//工作开始时间
//			float j=Float.valueOf(b.getJiessj());//工作结束时间
//			count+=(j-k);//循环累加工作时间，得到总工作时间
//		}
//		float add=count/pc;// 1.4   送货时间间隔=总工作时间/送货频次
//		float rod =(float) 0.2;//随机时间
//		float begin=Float.valueOf(list.get(0).getKaissj());//开始时间（起始的）
//		float afterEnd=0;
//		float x=0;
//		
//		System.out.println(add);
//		int h=0;
//		int s=0;
//		for (CkxBiaozgzsj b : list) {
//			float k=Float.valueOf(b.getKaissj());//工作开始时间
//			float j=Float.valueOf(b.getJiessj());//工作结束时间
//			x+=k-afterEnd;
//			afterEnd=j;
//			System.out.println(k+"================"+x+"================="+j);
//
//
//			for (int i = h; i < pc; i++) {
//				s++;
//				float biaoz = rod + i * add;// 标准时间偏量
//				float shij=biaoz+x;//实际时间偏量
//				float dao=begin+shij;
//				if(dao>=k&&dao<j){
//					System.out.println(biaoz+"----------"+shij+"----------"+dao);
//				}
//				if(dao>j){
//					h=i-1;break;
//				}
//				
//			}
//		}
//		System.out.println(s);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		function1();
	}
}
