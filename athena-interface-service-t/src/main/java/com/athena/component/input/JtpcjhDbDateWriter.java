package com.athena.component.input;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;


import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;
import com.athena.util.uid.CreateUid;
import com.toft.core2.dao.database.DbUtils;

public class JtpcjhDbDateWriter extends DbDataWriter {
	protected static Logger logger = Logger.getLogger(JtpcjhDbDateWriter.class);	//定义日志方法
	protected List shangXJSList = new ArrayList();
	protected String kanBanSJ = null; 
	protected int buZ = 0; //步长
	private String logInfo = "";
	private String logDate = "";
	private List userCenters = null;
	public JtpcjhDbDateWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}

	
	/**
	 * 解析前操作	
	 */
	@Override
	public boolean before() {
		try{
        String sql="delete from "+SpaceFinal.spacename_ddbh+".in_jtpcjh";
        super.execute(sql);
		}catch(RuntimeException e){
			logger.error(e.getMessage());
		}
		return super.before();
	}

	/**
	 * 行解析前处理
	 */
	@Override
	public boolean beforeRecord(int rowIndex, Object line) {
		if (rowIndex==1) {// 文件第一行不导入表
			return false;
		}
		else{
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
		}
		return true;
		
	}

	
    /**
     * 解析后操作
     */
	@Override
	public void afterRecord(int rowIndex, Record record, Object line) {
		try{
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		String jtrq=record.getString("jtrq");
		if(!"".equals(jtrq)){
		String jtrq1=DateTimeUtil.DateStr(jtrq);
		Date D_jtrq=format.parse(jtrq1);
		record.put("jtrq",D_jtrq);
		}
		//上线顺序号
		int t_Sxsxh = 0;
		String sxsxh = strNull(record.getString("jtxx"));
		if("".equals(sxsxh)){
			sxsxh = "0";
		}
		t_Sxsxh = Integer.parseInt(sxsxh);
		record.put("jtxx",t_Sxsxh);
		
		//存入创建时间和处理状态初始值
		record.put("cj_date", new Date());
		record.put("clzt", 0);
		
		super.afterRecord(rowIndex, record, line);
		}catch(Exception e){
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
			strbuf.append("and GONGZR = to_date('"+strNull(yjjhzrq)+"','yyyy-MM-dd') and rownum <=2000 ");
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
			StringBuffer strbuf=new StringBuffer();
			strbuf.append("select SHENGCJP from "+SpaceFinal.spacename_ddbh+".CKX_SHENGCX ");
			strbuf.append("where usercenter = '"+strNull(usercenter)+"' ");
			strbuf.append("and SHENGCXBH = '"+strNull(sxc)+"' ");
			map=selectOne(strbuf.toString());
			String SHENGCJP = strNull(map.get("SHENGCJP"));
			if(!"".equals(SHENGCJP)){
				jiep = Integer.parseInt(SHENGCJP);
			}
			double d_buz = (double)60 / jiep;
			BigDecimal mData = new BigDecimal(String.valueOf(d_buz)).setScale(0, BigDecimal.ROUND_HALF_UP);
			buz = Integer.parseInt(String.valueOf(mData));
		}catch(Exception e){
			logger.error(e.getMessage());
			logInfo = "生产节拍为空值 或查询不到！";
			throw new RuntimeException(e.getMessage());
		}
		return buz;
	}
	
	//是否存在coddc数据
	private boolean isExistsCoddc(){
		StringBuffer strbuf = new StringBuffer();
		boolean flag = false;
		try {
			strbuf.append(" select count(1) coddcnum from "+SpaceFinal.spacename_ddbh+".in_jtpcjh z, ");
			strbuf.append(SpaceFinal.spacename_ddbh+".in_cldv_coddc y ");
			strbuf.append(" where y.lcdv1424(+) = z.lcdv24||z.lcdv3500 ");
			strbuf.append("  and z.shangxsj is not null ");
			strbuf.append("  and y.coddc is null");
			int coddcNum = Integer.valueOf(selectValue(strbuf.toString()).toString());
			if(coddcNum > 0){
				flag = true; //存在无匹配CODDC
			}
		} catch (RuntimeException e) {
			logInfo = "存在无匹配CODDC，请查看是否表(IN_JTPCJH,IN_CLDV_CODDC)数据存在业务问题！";
			logger.error(e.getMessage());
		}
		return flag;
	}
	
	//插入表进行数据更新
	@Override
	public void after() {
		String SID=DbDataWriter.getUUID();//唯一标示
		String EID=DbDataWriter.getUUID();//唯一标示
		try{

			String sql="SELECT T.USERCENTER,T.OFH,T.ZZX,T.JTRQ,T.JTXX FROM "+SpaceFinal.spacename_ddbh+".IN_JTPCJH T "
					  +" WHERE T.ZZX IS NOT NULL AND T.JTRQ IS NOT NULL "
					  +" ORDER BY T.USERCENTER,T.ZZX,T.JTRQ,T.JTXX ";
			String updateSql = "";
			String d_shangXSJ = "";
			String compDate = "";
			int num = 1;
			int sxxuh = 0;
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:ss");
			List inList = select(sql);
			if(!inList.isEmpty()){
				for(int i=0;i<inList.size();i++){
					String usercenter = strNull(((Map)inList.get(i)).get("USERCENTER")); //用户中心
					String zzx = strNull(((Map)inList.get(i)).get("ZZX")); //总装生产线号
					String scxh = usercenter +"5"+zzx;
					String YJJHZRQ = strNull(((Map)inList.get(i)).get("JTRQ")).substring(0,10);//JT时间
					String hzxuh = strNull(((Map)inList.get(i)).get("JTXX")); //JT顺序号
					String tmp_compDate = usercenter+scxh+YJJHZRQ;
					if(!"".equals(hzxuh)){
						sxxuh = Integer.parseInt(hzxuh);
					}
					String whof = strNull(((Map)inList.get(i)).get("OFH")); //of单号
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
						shangXJSList = GetShangXSJ(usercenter,scxh,YJJHZRQ);
						buZ = GetJieP(usercenter,scxh);
						if(shangXJSList.size()>0){
							d_shangXSJ = strNull(((Map)shangXJSList.get(0)).get("JUEDSK"));
							//d_shangXSJ = d_shangXSJ.substring(0,d_shangXSJ.length()-2);
							kanBanSJ = d_shangXSJ;
							updateSql = "update "+SpaceFinal.spacename_ddbh+".IN_JTPCJH set SHANGXSJ = to_date('"+d_shangXSJ+"','yyyy-MM-dd hh24:mi:ss'),KAIBSJ=to_date('"+kanBanSJ+"','yyyy-MM-dd hh24:mi:ss'),JTXX='"+num+"' where usercenter = '"+usercenter+"'" 
									  + " and OFH = '"+whof+"'";
						}else{
							logInfo = "无计算上线时间和开班时间！";
							File_ErrorInfo(EID,"3060",SID,logInfo,logDate);
							insertDDBH_YICBJ("3060",usercenter,"九天排产计划(JT的顺序)","2",logInfo); //记录用户异常报警
							return;
						}
					} else{
						if(shangXJSList.size()>0){
							int xuh = (num -1) * buZ;
							d_shangXSJ = strNull(((Map)shangXJSList.get(xuh)).get("JUEDSK"));
							//d_shangXSJ = d_shangXSJ.substring(0,d_shangXSJ.length()-2);
							updateSql = "update "+SpaceFinal.spacename_ddbh+".IN_JTPCJH set SHANGXSJ = to_date('"+d_shangXSJ+"','yyyy-MM-dd hh24:mi:ss'),KAIBSJ=to_date('"+kanBanSJ+"','yyyy-MM-dd hh24:mi:ss'),JTXX='"+num+"' where usercenter = '"+usercenter+"'" 
							  + " and OFH = '"+whof+"'";
						}
					}
					if(!"".equals(updateSql)){
						execute(updateSql);
					}
					compDate = tmp_compDate;
				}
				if(isExistsCoddc()){
					logInfo = "无匹配CODDC，请查看是否表(IN_JTPCJH,IN_CLDV_CODDC)数据存在业务问题！";
					File_ErrorInfo(EID,"3060",SID,logInfo,"");
				}
			}else{
				logInfo = "用户中心不这匹配，无数据！";
				File_ErrorInfo(EID,"3060",SID,logInfo,logDate);
				return;
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			logger.error(e.getMessage());
			if("".equals(logInfo)){
				logInfo = "计算上线时间和开班时间出错！";
			}
			File_ErrorInfo(EID,"3060",SID,logInfo,logDate);
			throw new RuntimeException(e.getMessage());
		}
		super.after();
	}
	
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
			strbuf.append("insert into "+SpaceFinal.spacename_ddbh+".in_errorfile(EID,INBH,SID,FILE_ERRORINFO,ERROR_DATE,YUNXKSSJ)  ");
			strbuf.append(" values(  ");
			strbuf.append("'" + strNull(EID) + "',");
			strbuf.append("'" + strNull(CID) + "',");
			strbuf.append("'" + strNull(SID) + "',");
			strbuf.append("substr('" + strNull(file_errorinfo) + "',0,700),");
			strbuf.append("'" + strNull(error_date) + "',");
			strbuf.append(" sysdate ) ");
			execute(strbuf.toString());
		} catch (RuntimeException e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 记录用户异常报警日志表
	 * DbUtils.execute(strbuf.toString(),datasourceid);
	 * Mantis 0006532 修改
	 * jsmk 计算模块,cId 接口编号
	 * @return
	 */
	public void insertDDBH_YICBJ(String cid,String usercenter,String chnname,String yclx,String ycxx){
		StringBuffer strbuf=new StringBuffer();
		try{
		strbuf.append("insert into "+SpaceFinal.spacename_ddbh+".DDBH_YICBJ");
		strbuf.append("(CID,USERCENTER,EXENANME,CHNNAME,CUOWLX,MEMO,CREATE_TIME)");
		strbuf.append("values( ");
		strbuf.append(" '" + CreateUid.getUID(20)+"',");
		strbuf.append(" '" + usercenter+"',");
		strbuf.append(" '" + cid+"',");
		strbuf.append(" '" + chnname+"',");
		strbuf.append("'" + yclx + "',");
		strbuf.append("substr('" + ycxx + "',0,25),");
		strbuf.append("sysdate)");
		execute(strbuf.toString());
		}catch(RuntimeException e){
			logger.error(e.getMessage());
		}finally{
			DbUtils.freeConnection(conn);
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
