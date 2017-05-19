package com.athena.ckx.module.transTime;

import java.util.ArrayList;
import java.util.List;

import com.athena.ckx.entity.transTime.CkxBiaozgzsj;

public class CkxBiaozgzsjTest {
	
	
	public static List<CkxBiaozgzsj> getlist(){
		List<CkxBiaozgzsj> list=new ArrayList<CkxBiaozgzsj>();
//		CkxBiaozgzsj b=new CkxBiaozgzsj();
//		
//		
//		list.add(b);list.add(b1);list.add(b2);
		return list;
	}
	
	
	
	
	
	public static void main(String[] args) {

//		List<CkxBiaozgzsj> list=getlist();
//		int pc=10;//送货频次
//		double count=0;//卸货站台总工作时间
//		for (CkxBiaozgzsj b : list) {
//			double k=Double.valueOf(b.getKaissj());//工作开始时间
//			double j=Double.valueOf(b.getJiezsj());//工作结束时间
//			count=count+(j-k);//循环累加工作时间，得到总工作时间
//		}
//		double add=count/pc;// 1.4   送货时间间隔=总工作时间/送货频次
//		double rod =1.3;//随机时间数（在begin到add之间随机）
//		System.out.println(add);
//		double begin=Double.valueOf(list.get(0).getKaissj());//开始时间（起始的）
//		double end=0;//结束时间（保留的是此次循环结束的结束时间）
//		double a=0;//休息时间（保留的是截止到此次循环开始时的所有休息时间）
//		int h=0;//频次的循环次数（是第几次循环）
//		for (CkxBiaozgzsj b : list) {
//			double k=Double.valueOf(b.getKaissj());//本次循环的开始时间
//			double j=Double.valueOf(b.getJiessj());//本次循环的结束时间
//			
//			for (int i = h; i < pc; i++) {//循环频次
//				double biaoz = rod + i * add;//标准时间偏量
////				System.out.println(begin+"--------"+(float) biaoz+"  ==========="+(float)(begin+biaoz));
//				double d=a+biaoz;//实际时间偏量（休息时间（注释1）+标准时间偏量）
//				//注释1：先假如在时间范围内
//				double n=d+begin;//实际到货时间（实际时间偏量+开始时间（起始））
//				if(n>k&&n<j){//如果在范围内，直接打印
//					System.out.println(begin+"--"+biaoz+"     ："+(float)d+"     ："+(float)n);
//				}
//				else if(n>j){ //如果实际到货时间大于结束时间，则保存此次的结束时间，循环的次数，跳出循环
//					end=j;
//					h=i;
//					break;
//				}else if(n<k){//如果小于开始时间，
//					//则实际到货时间=实际到货时间+此次循环的开始时间-上次循环结束时间
//					//休息时间=休息时间+此次循环的开始时间-上次循环结束时间
//					//System.out.println(a);
//					d=d+k-end;//如果小于开始时间，则实际时间偏量=实际时间偏量+加上此次循环的开始时间-上次循环的结束时间
//					n=d+begin;
//					System.out.println(begin+"--"+biaoz+"     ："+(float)(d)+"     ："+(float)n);
//					a+=k-end;
//				}
//				
//			}
//			
//			System.out.println("----------------------------------");
//		}
//			
//			
			
			
	}
		
		
		
		/*public static void function(){
			List<CkxBiaozgzsj> list=getlist();
			double count=0;//卸货站台总工作时间
			int pc=10;//送货频次
			for (CkxBiaozgzsj b : list) {
//				double k=Double.valueOf(b.getKaissj());//工作开始时间
//				double j=Double.valueOf(b.getJiessj());//工作结束时间
//				count=count+(j-k);//循环累加工作时间，得到总工作时间
			}
			double add=count/pc;// 1.4   送货时间间隔=总工作时间/送货频次
			double rod =0;
			
			for (int i = 0; i < pc; i++){
				double biaoz = rod + i * add;//标准时间偏量
				System.out.println((float)biaoz);
			}
		}*/
		
		
		
		
		
		
		
		
		
		
	
		
		
//		double c=0;//保存截止时间在工作时间以外多余的量    比如工作时间为9:00--11:00  但计算出的截止时间为11:30， 则保存多出来的这30分钟
//		System.out.println(add);
//		
//		double afterEnd=0;
//		for (CkxBiaozgzsj b : list) {
//			double k=Double.valueOf(b.getKaissj());
//			double j=Double.valueOf(b.getJiessj());
//			double begin=k+c;//频段开始时间=工作开始时间+多余量（初始为0）
//			double end=begin+add;//频段结束时间=频段开始时间+送货时间间隔
//			for (int i = 0; i < pc; i++) {
//				if(end>j){//如果频段结束时间大于工作结束时间,记录多余量，并停止本次循环
//					c=end-j;
////					System.out.print(begin+"---"+(end+(k-afterEnd))+"\t");
//					break;
//				}
//				System.out.print(begin+"---"+end+"\t");
//				begin=end;
//				end=end+add;
//				
//			}
//			
////			System.out.println(k-afterEnd);
//			System.out.println();
//			afterEnd=j;
//		}
	
}
