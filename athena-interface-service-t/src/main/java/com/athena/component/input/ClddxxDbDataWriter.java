package com.athena.component.input;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;



import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;


/**
 * 车辆订单信息接口输入类
 * @author GJ
 *
 */
public class ClddxxDbDataWriter extends DbDataWriter{
	protected static Logger logger = Logger.getLogger(ClddxxDbDataWriter.class);	//定义日志方法
	protected List shangXJSList = new ArrayList();
	protected Record jpRecord = new Record();
	protected String kanBanSJ = null; 
	private String logInfo = "";
	private String logDate = "";
	private List userCenters = null;
	protected int buZ = 0; //步长
	public ClddxxDbDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	/**
	 * 日期格式化
	 * @param datestr
	 * @return String
	 */
	public String SubString(String datestr)
	{
		String yyyy=null;
		String mm=null;
		String dd=null;
		if(null!=datestr&&!"".equals(datestr)){
		yyyy=datestr.substring(4);
		mm=datestr.substring(2,4);
		dd=datestr.substring(0,2);
		if("9999".equals(yyyy)){
			yyyy="2099";
		 }
	    }
		String date=yyyy+"-"+mm+"-"+dd;
		return date;
	}
	
	
	/**
	 * 解析数据之前清空车辆订单信息表数据
	 */
	public boolean before() {
		try{
			String sql="delete from "+SpaceFinal.spacename_ddbh+".in_clddxx";
			super.execute(sql);
			String sqlStr="delete from "+SpaceFinal.spacename_ddbh+".ddbh_shangyhsjjh";
			super.execute(sqlStr);
			
			userCenters = GetUserCenter();
		
		}catch(RuntimeException e)
		{
			logger.error(e.getMessage());
		}
		return super.before();
	}
	
	@Override
	public boolean beforeRecord(int rowIndex, Object line) {
		if(line!=null){
			String user = line.toString().substring(0, 2).trim();
			if(userCenters!=null&&userCenters.size()>0){
				if(userCenters.contains(user)){
					return true;
				}else{
					return false;
				}
			}else{
				return true;
			}
		}
		return true;
	}
	
	/**
	 * 行解析之后处理方法
	 * @param rowIndex 行标
	 * @param record 行数据集合
	 * @author GJ
	 */
	@Override
	public void afterRecord(int rowIndex, Record record, Object line) {
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			try{
			if(null!=record){
				String yjjzlsj=record.getString("yjjzlsj");//获得预计进总量时间
				if(!"".equals(yjjzlsj)){
					String yjjzlsj1=yjjzlsj.replace(".", "");
					String yjjzlsj2=this.SubString(yjjzlsj1);//预计进总量时间格式化
					Date yjjzlsj3=format.parse(yjjzlsj2);
					record.put("yjjzlsj", yjjzlsj3);//存入集合
				}
				String yjjhzrq=record.getString("yjjhzrq");//获得预计进焊装日期
				if(!"".equals(yjjhzrq)){
					String yjjhzrq1=yjjhzrq.replace(".", "");
					String yjjhzrq2=this.SubString(yjjhzrq1);//预计进总量时间格式化
					Date yjjhzrq3=format.parse(yjjhzrq2);
					record.put("yjjhzrq", yjjhzrq3);//存入集合
				}
				String yjsyhsj=record.getString("yjsyhsj");//获得预计商业化时间
				if(!"".equals(yjsyhsj)){
					String yjsyhsj1=yjsyhsj.replace(".", "");
					String yjsyhsj2=this.SubString(yjsyhsj1);//预计商业化时间格式化
					Date yjsyhsj3=format.parse(yjsyhsj2);
					record.put("yjsyhsj", yjsyhsj3);//存入集合
				} 
				//上线顺序号
				int t_Sxsxh = 0;
				String sxsxh = strNull(record.getString("sxsxh"));
				if("".equals(sxsxh)){
					sxsxh = "0";
				}
				t_Sxsxh = Integer.parseInt(sxsxh);
				record.put("sxsxh",t_Sxsxh);
				
				String usercenter = record.getString("usercenter");
				String lcdv24 = record.getString("lcdv24").substring(0,6);
				String scxh = record.getString("scxh");
				String hanzscx = GetHanBN(usercenter,lcdv24,scxh);
				
				//存入创建时间和处理状态初始值
				record.put("cj_date", new Date());
				record.put("clzt", 0);
				record.put("hanzscx", hanzscx);
			}
		   super.afterRecord(rowIndex, record, line);
		}catch(Exception e)
		{
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
		}
	 }
	
	//提取上线时间数据 add by pan.rui
	@SuppressWarnings("rawtypes")
	private List GetShangXSJ(String usercenter,String sxc,String yjjhzrq){
		List shangXSJ=null;
		try{
			StringBuffer strbuf=new StringBuffer();
			strbuf.append("select to_char(JUEDSK,'YYYY-MM-DD HH24:mi:ss') as JUEDSK from "+SpaceFinal.spacename_ddbh+".CKX_GONGZSJMB ");
			strbuf.append("where usercenter = '"+strNull(usercenter)+"' ");
			strbuf.append("and CHANX = '"+strNull(sxc)+"' ");
			strbuf.append("and GONGZR >= to_date('"+strNull(yjjhzrq)+"','yyyy-MM-dd') and rownum <=5000 ");
			strbuf.append("order by JUEDSK ");
			shangXSJ=select(strbuf.toString());
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return shangXSJ;
	}
	
	//根据用户中心和生产线取整车生产节拍
	private int GetJieP(String usercenter,String sxc){
		Map map=null;
		int buz = 0;
		try{
			int jiep = 0;
			double d_buz = 0;
			StringBuffer strbuf=new StringBuffer();
			strbuf.append("select SHENGCJP from "+SpaceFinal.spacename_ddbh+".CKX_SHENGCX ");
			strbuf.append("where usercenter = '"+strNull(usercenter)+"' ");
			strbuf.append("and SHENGCXBH = '"+strNull(sxc)+"' ");
			map=selectOne(strbuf.toString());
			String SHENGCJP = strNull(map.get("SHENGCJP"));
			if(!"".equals(SHENGCJP)){
				jiep = Integer.parseInt(SHENGCJP);
				d_buz = (double)60 / jiep;
				BigDecimal mData = new BigDecimal(String.valueOf(d_buz)).setScale(0, BigDecimal.ROUND_HALF_UP);
				buz = Integer.parseInt(String.valueOf(mData));
			}else{
				buz = 0;
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			logInfo = "查询生产节拍异常！";
			throw new RuntimeException(e.getMessage());
		}
		return buz;
	}
	
	//得到焊装线编号
	private String GetHanBN(String usercenter,String lcdv,String scxh){
		String hanBN=null;
		try{
			StringBuffer strbuf=new StringBuffer();
			strbuf.append("select t1.chejbhhz||t1.shengcxbhhz hzbh from "+SpaceFinal.spacename_ddbh+".ckx_chexpt t1 ");
			strbuf.append("where t1.usercenter = '"+strNull(usercenter)+"' ");
			strbuf.append("and t1.CHEJBHZZ = '"+strNull(usercenter)+"5' ");
			strbuf.append("and t1.lcdv = '"+strNull(lcdv)+"' ");
			strbuf.append("and t1.shengcxbhzz = '"+strNull(scxh)+"' ");
			Map hanZMap = selectOne(strbuf.toString());
			hanBN = strNull(hanZMap.get("hzbh"));
		}catch(Exception e){
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		return hanBN;
	}
	
	//生产线是否都具有生产节拍
	public boolean GetJPMap(){
		boolean flag = true;
		String sql = "";
		try{
			String jpDate = "";
			sql=" SELECT T.USERCENTER,T.HANZSCX FROM "+SpaceFinal.spacename_ddbh+".IN_CLDDXX T " 
			  +" WHERE HANZSCX IS NOT NULL AND YJJHZRQ IS NOT NULL "
			  +" AND TO_CHAR(T.YJJHZRQ,'YYYY-MM-DD') != '2099-12-31' "
			  +" group by T.USERCENTER,T.HANZSCX ";
			List inList = select(sql);
			if(!inList.isEmpty()&&inList.size()>0){
				for(int i=0;i<inList.size();i++){
					String usercenter = strNull(((Map)inList.get(i)).get("USERCENTER")); //用户中心
					String scxh = strNull(((Map)inList.get(i)).get("HANZSCX")); //焊装生产线号
					jpDate = usercenter+scxh;
					logDate = "用户中心："+usercenter+";产线："+scxh;
					int ibuz = GetJieP(usercenter, scxh);
					jpRecord.put(jpDate, ibuz);
				}
			}else{
				logInfo = "无法匹配CKX_CHEXPT的悍装生产线，没有数据入库，请确认！";
				flag = false;
			}
		}catch(Exception e){
			flag = false;
			logInfo = "生产节拍计算异常！【"+sql+"】查询出错！";
			logger.error(e.getMessage());
		}
		return flag;
	}
	
	//需要判断是否不存生产线的数据
	private boolean IsGetShengCX(){
		boolean flag = true;
		try{
			String sql=" SELECT COUNT(1) CXNUM FROM "+SpaceFinal.spacename_ddbh+".IN_CLDDXX T " 
			  +" WHERE HANZSCX IS NULL AND YJJHZRQ IS NOT NULL  "
			  +" AND TO_CHAR(T.YJJHZRQ,'YYYY-MM-DD') != '2099-12-31' "
			  +" AND TO_CHAR(T.YJJZLSJ,'YYYYMMDD') >= TO_CHAR(SYSDATE,'YYYYMMDD') ";
			int cxNum = Integer.parseInt(selectValue(sql).toString());
			if(cxNum > 0){
				logInfo = "存在【"+cxNum+"】条数据无法匹配的生产线(HANZSCX)，请查看是否表(IN_CLDDXX,CKX_CHEXPT)数据存在业务问题！";
				flag = false;
			}
		}catch(Exception e){
			flag = false;
			logInfo = "无匹配的生产线(HANZSCX)，请查看是否表(IN_CLDDXX,CKX_CHEXPT)数据存在业务问题！";
			logger.error(e.getMessage());
		}
		return flag;
	}
	
	//是否存在coddc数据
	private boolean isExistsCoddc(){
		StringBuffer strbuf = new StringBuffer();
		boolean flag = false;
		try {
			strbuf.append(" select count(1) coddcnum from "+SpaceFinal.spacename_ddbh+".ckx_chexpt z, ");
			strbuf.append(SpaceFinal.spacename_ddbh+".in_cldv_coddc y,"+SpaceFinal.spacename_ddbh+".in_clddxx t ");
			strbuf.append(" where y.coddc is null and z.usercenter = t.usercenter ");
			strbuf.append("  and z.shengcxbhzz = t.scxh ");
			strbuf.append("  and z.CHEJBHZZ = t.usercenter || '5' ");
			strbuf.append("  and z.lcdv = substr(t.lcdv24,1,6) ");
			strbuf.append("  and y.lcdv1424 = t.lcdv24||t.lcdv ");
			strbuf.append("  and t.shangxsj is not null ");
			int coddcNum = Integer.valueOf(selectValue(strbuf.toString()).toString());
			if(coddcNum > 0){
				logInfo = "存在【"+coddcNum+"】条数据无法匹配coddc！";
				flag = true;
			}
		} catch (RuntimeException e) {
			logInfo = "数据库查询异常或者连接出错！";
			logger.error(e.getMessage());
		}
		return flag;
	}
	
	//插入表进行数据更新
	public void after() {
		String SID=DbDataWriter.getUUID();//唯一标示
		String EID=DbDataWriter.getUUID();//唯一标示
		try{
			//首先判断是否存在没有生产线的数据
			boolean cxFlag = IsGetShengCX();
			if(!cxFlag){
				File_ErrorInfo(EID,"3050",SID,logInfo,"");

			}
			//然后查询是否存在生产节拍
			boolean flag = GetJPMap();
			//如果有数据，则进行上线时间和开班时间操作
			if(flag){
				String sql=" SELECT T.USERCENTER,T.WHOF,T.HANZSCX,TO_CHAR(T.YJJHZRQ,'YYYY-MM-DD') YJJHZRQ,T.SXSXH FROM "+SpaceFinal.spacename_ddbh+".IN_CLDDXX T " 
						  +" WHERE HANZSCX IS NOT NULL AND YJJHZRQ IS NOT NULL "
						  +" AND TO_CHAR(T.YJJHZRQ,'YYYY-MM-DD') != '2099-12-31' "
						  +" AND TO_CHAR(T.YJJZLSJ,'YYYYMMDD') >= TO_CHAR(SYSDATE,'YYYYMMDD') "
						  +" ORDER BY T.USERCENTER,T.HANZSCX,T.YJJHZRQ,T.SXSXH ";
				String updateSql = "";
				String d_shangXSJ = "";
				String compDate = "";
				int num = 1;
				int sxxuh = 0;
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:ss");
				List inList = select(sql);
				int maxNum = 0;
				if(!inList.isEmpty()){
					for(int i=0;i<inList.size();i++){
						String usercenter = strNull(((Map)inList.get(i)).get("USERCENTER")); //用户中心
						String scxh = strNull(((Map)inList.get(i)).get("HANZSCX")); //焊装生产线号
						String YJJHZRQ = strNull(((Map)inList.get(i)).get("YJJHZRQ")).substring(0,10);//进入焊装时间
						String hzxuh = strNull(((Map)inList.get(i)).get("SXSXH")); //循环号
						String tmp_compDate = usercenter+scxh+YJJHZRQ;
						String tmp_JP = usercenter+scxh;
						if(!"".equals(hzxuh)){
							sxxuh = Integer.parseInt(hzxuh);
						}
						String whof = strNull(((Map)inList.get(i)).get("WHOF")); //of单号
						logDate = "用户中心:"+usercenter+";OF号:"+whof+";预计焊装时间："+YJJHZRQ;
						if(sxxuh == 0){
							continue;
						}
						if(compDate.equals(tmp_compDate)){
							num++;
						}else{
							num = 1;
						}
						if(num==1){
							buZ = Integer.parseInt(jpRecord.getString(tmp_JP));
							shangXJSList = GetShangXSJ(usercenter,scxh,YJJHZRQ);
							maxNum = shangXJSList.size();
							String EID_1=DbDataWriter.getUUID();//唯一标示
							if(buZ!=0){
								if(maxNum>0){
									d_shangXSJ = strNull(((Map)shangXJSList.get(0)).get("JUEDSK"));
									//d_shangXSJ = d_shangXSJ.substring(0,d_shangXSJ.length()-2);
									kanBanSJ = d_shangXSJ;
									updateSql = "update "+SpaceFinal.spacename_ddbh+".IN_CLDDXX set SHANGXSJ = to_date('"+d_shangXSJ+"','yyyy-MM-dd hh24:mi:ss'),KAIBSJ=to_date('"+kanBanSJ+"','yyyy-MM-dd hh24:mi:ss'),sxsxh='"+num+"' where usercenter = '"+usercenter+"'" 
											  + " and whof = '"+whof+"'";
								}else{
									logInfo = "无计算上线时间和开班时间！";
									File_ErrorInfo(EID_1,"3050",SID,logInfo,logDate);
									//return;
								}
							}else{
								File_ErrorInfo(EID_1,"3050",SID,logInfo,logDate);
							}
						} else{
							if(buZ!=0){
								if(maxNum>0){
									int xuh = (num -1) * buZ;
									if(xuh<= maxNum){
										d_shangXSJ = strNull(((Map)shangXJSList.get(xuh)).get("JUEDSK"));
										//d_shangXSJ = d_shangXSJ.substring(0,d_shangXSJ.length()-2);
										updateSql = "update "+SpaceFinal.spacename_ddbh+".IN_CLDDXX set SHANGXSJ = to_date('"+d_shangXSJ+"','yyyy-MM-dd hh24:mi:ss'),KAIBSJ=to_date('"+kanBanSJ+"','yyyy-MM-dd hh24:mi:ss'),sxsxh='"+num+"' where usercenter = '"+usercenter+"'" 
										  + " and whof = '"+whof+"'";
									}
//									else{
//										logInfo = "无计算上线时间和开班时间(车辆数量或步长过大)！";
//										File_ErrorInfo(EID,"3050",SID,logInfo,logDate);
//										return;
//									}
								}
							}
						}						
						if(!"".equals(updateSql)){
							execute(updateSql);
						}else{
							logInfo = "(车辆数量或步长过大)！";
							String EID_2=DbDataWriter.getUUID();//唯一标示
							File_ErrorInfo(EID_2,"3050",SID,logInfo,logDate);
							return;
						}
						compDate = tmp_compDate;
					}
				}else{
					String EID_3=DbDataWriter.getUUID();//唯一标示
					logInfo = "无匹配的用户中心数据，请查看是否表(IN_CLDDXX)数据存在业务问题！";
					File_ErrorInfo(EID_3,"3050",SID,logInfo,logDate);
					return;
				}
				if(isExistsCoddc()){
					String EID_4=DbDataWriter.getUUID();//唯一标示
					File_ErrorInfo(EID_4,"3050",SID,logInfo,"");
				}
			}else{
				String EID_5=DbDataWriter.getUUID();//唯一标示
				File_ErrorInfo(EID_5,"3050",SID,logInfo,logDate);
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			if("".equals(logInfo)){
				logInfo = "计算上线时间和开班时间出错！";
			}
			File_ErrorInfo(EID,"3050",SID,logInfo,logDate);
			throw new RuntimeException(e.getMessage());
		}
		super.after();
	}

	
//	/**
//	 * 是否存在无商业化时间数据
//	 */
//	private boolean isExistsShangYHSJ(){
//		StringBuffer strbuf = new StringBuffer();
//		boolean flag = true;
//		try {
//			
//			strbuf.append("select count(1) ShangYHSJnum from (select y.coddc coddc ");
//			strbuf.append("	from "+SpaceFinal.spacename_ddbh+".ckx_chexpt z, ");
//			strbuf.append(SpaceFinal.spacename_ddbh+".in_cldv_coddc y, ");
//			strbuf.append(SpaceFinal.spacename_ddbh+".in_clddxx t ");
//			strbuf.append("	where z.usercenter = t.usercenter ");
//			strbuf.append("	  and z.shengcxbhzz = t.scxh ");
//			strbuf.append("	  and z.CHEJBHZZ = t.usercenter || '5' ");
//			strbuf.append("	  and z.lcdv = substr(t.lcdv24, 1, 6) ");
//			strbuf.append("	  and y.lcdv1424 = t.lcdv24 || t.lcdv ");
//			//strbuf.append("	  and t.shangxsj is not null ");  
//			strbuf.append(" and t.HANZSCX IS NOT NULL ");
//			strbuf.append(" and t.YJJHZRQ IS NOT NULL ");
//			strbuf.append("	and TO_CHAR(T.YJJHZRQ,'YYYY-MM-DD') != '2099-12-31' ");
//			strbuf.append("	and TO_CHAR(T.YJSYHSJ,'YYYY-MM-DD') != '0001-01-01' ");
//			strbuf.append("	group by t.usercenter,y.coddc,t.yjsyhsj ");
//			strbuf.append("	minus  ");
//			strbuf.append("	select t1.coddc coddc from (select t.usercenter,y.coddc,t.yjsyhsj ");
//			strbuf.append("	from "+SpaceFinal.spacename_ddbh+".ckx_chexpt z, ");
//			strbuf.append(SpaceFinal.spacename_ddbh+".in_cldv_coddc y, ");
//			strbuf.append(SpaceFinal.spacename_ddbh+".in_clddxx  t ");
//			strbuf.append("	where z.usercenter = t.usercenter ");
//			strbuf.append(" and z.shengcxbhzz = t.scxh ");
//			strbuf.append(" and z.CHEJBHZZ = t.usercenter || '5' ");
//			strbuf.append(" and z.lcdv = substr(t.lcdv24, 1, 6) ");
//			strbuf.append(" and y.lcdv1424 = t.lcdv24 || t.lcdv ");
//			//strbuf.append("	  and t.shangxsj is not null ");  
//			strbuf.append(" and t.HANZSCX IS NOT NULL ");
//			strbuf.append(" and t.YJJHZRQ IS NOT NULL ");
//			strbuf.append("	and TO_CHAR(T.YJJHZRQ,'YYYY-MM-DD') != '2099-12-31' ");
//			strbuf.append("	and TO_CHAR(T.YJSYHSJ,'YYYY-MM-DD') != '0001-01-01' ");
//			strbuf.append(" group by t.usercenter,y.coddc,t.yjsyhsj) t1, ");
//			strbuf.append(" (select t.usercenter,t.coddc,t.ecomqssj,t.ecomjssj from "+SpaceFinal.spacename_ddbh+".ddbh_CODDCxhdlj t ");
//			strbuf.append(" 	group by t.usercenter,t.coddc,t.ecomqssj,t.ecomjssj) t2 ");
//			strbuf.append(" where t1.usercenter = t2.usercenter ");
//			strbuf.append(" and t1.coddc = t2.coddc ");
//			strbuf.append(" and t1.yjsyhsj between t2.ecomqssj and t2.ecomjssj) ");
////			strbuf.append("select count(1) ShangYHSJnum from ");
////			strbuf.append(" (select t.usercenter,t.whof,y.coddc,t.yjsyhsj from "+SpaceFinal.spacename_ddbh+".ckx_chexpt z, ");
////			strbuf.append(SpaceFinal.spacename_ddbh+".in_cldv_coddc y,"+SpaceFinal.spacename_ddbh+".in_clddxx t ");
////			strbuf.append(" where z.usercenter = t.usercenter ");
////			strbuf.append("  and z.shengcxbhzz = t.scxh ");
////			strbuf.append("  and z.CHEJBHZZ = t.usercenter || '5' ");
////			strbuf.append("  and z.lcdv = substr(t.lcdv24,1,6) ");
////			strbuf.append("  and y.lcdv1424 = t.lcdv24||t.lcdv ");
////			strbuf.append("  and t.shangxsj is not null) t1,"+SpaceFinal.spacename_ddbh+".ddbh_CODDCxhdlj t2 ");
////			strbuf.append(" where t1.usercenter = t2.usercenter ");
////			strbuf.append("  and t1.coddc = t2.coddc");
////			strbuf.append("  and t1.yjsyhsj between t2.ecomqssj and t2.ecomjssj");
//			int ShangYHSJNum = Integer.valueOf(selectValue(strbuf.toString()).toString());
//			if(ShangYHSJNum > 0){
//				logInfo = "存在无法匹配SHANGYHSJ(商业化时间)，请查看是否表(IN_CLDDXX,DDBH_CODDCXHDLJ)数据存在业务问题！";
//				flag = false;
//			}
//		} catch (RuntimeException e) {
//			logInfo = "存在无法匹配SHANGYHSJ(商业化时间)，请查看是否表(IN_CLDDXX,DDBH_CODDCXHDLJ)数据存在业务问题！";
//			logger.error(e.getMessage());
//		}
//		return flag;
//	}
	/**
	 * 空串处理
	 * 
	 * @param obj
	 *            对象
	 * @return 处理后字符串
	 * @author GJ
	 * @date 2011-10-26
	 */
	private String strNull(Object obj) {// 对象为空返回空串,不为空toString
		return obj == null ? "" : obj.toString().trim();
	}
	
	/**
	 * 记录数据日志表
	 */
	public void File_ErrorInfo(String EID,String CID,String SID,String file_errorinfo,String error_date){
		StringBuffer strbuf = new StringBuffer();
		try {
			strbuf.append("insert into " +SpaceFinal.spacename_ddbh+ ".in_errorfile(EID,INBH,SID,FILE_ERRORINFO,ERROR_DATE,YUNXKSSJ)  ");
			strbuf.append(" values(  ");
			strbuf.append("'" + strNull(EID) + "',");
			strbuf.append("'" + strNull(CID) + "',");
			strbuf.append("'" + strNull(SID) + "',");
			strbuf.append("substr('" + strNull(file_errorinfo) + "',0,700),");
			strbuf.append("'" + strNull(error_date) + "',");
			strbuf.append(" sysdate )");
			execute(strbuf.toString());
		} catch (RuntimeException e) {
			logger.error(e.getMessage());
		}
	}
	
	//得到配置文件的用户中心
	private List GetUserCenter(){
		InputStream  in = ClddxxDbDataWriter.class.getResourceAsStream("/config/exchange/urlPath.properties");
		Properties pp = new Properties();
		List userCenters = new ArrayList();
		try {
			pp.load(in);
			String usercenter = pp.getProperty("usercenter");
			if(usercenter.contains(",")){
				String[] users = usercenter.split(",");
				if(users!=null&&users.length>0){
					for(int i=0;i<users.length;i++){
						if("".equals(strNull(users[i]))){
							userCenters.add(users[i]);
						}
					}
				}
			}else{
				userCenters.add(usercenter);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return userCenters;
	}
	
}
