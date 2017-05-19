package com.athena.component.input;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;


import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.FileLog;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;
import com.athena.util.date.DateUtil;


/**
 * 零件供应商参考系输入类
 * 
 * @author WL
 * @date 2011-10-20
 */
public class ComSupDbDataWriter extends DbDataWriter {

	private static int HUNDRED_PERCENT = 1;   //份额百分之百100%
	private static int ZERO_PERCENT = 0;      //份额为0
	protected static Logger logger = Logger.getLogger(ComSupDbDataWriter.class);	//定义日志方法
	public ComSupDbDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}

	/**
	 * 行解析后处理方法
	 * 
	 * @param rowIndex
	 *            行标
	 * @param record
	 *            行数据
	 * @author GJ
	 */
	@Override
	public void afterRecord(int rowIndex, Record record, Object line) {
		try {
			synchronized (this) {
			
			// 获取第一个字节为ZTYPE
			String type = line.toString().substring(0, 1).trim();
			// 获取用户中心
			String USERCENTER = record.getString("USERCENTER").trim();
			// 获取零件号
			String LINGJBH = record.getString("LINGJBH").trim();
			// 获取供应商编号
			String GONGYSBH = record.getString("GONGYSBH").trim();
			//获取供应份额
			String GONGYFE= record.getString("GONGYFE").trim();
			//获取失效时间
			String SHENGXSJ=record.getString("SHENGXSJ").trim();
			//定义生效日期格式转换
			String D_SHENGXSJ="";

			//获取供应份配，并且做数据格式转换
			if(!"".equals(GONGYFE)){
				double i_edu=Double.valueOf(GONGYFE);
				double t_edu=i_edu/100;
				record.put("GONGYFE", t_edu);
			}else{
				record.put("GONGYFE", "0");
				GONGYFE = "0";
			}
			//add by pan.rui修改创建编辑人
			record.put("creator", super.dataExchange.getCID());
			record.put("editor", super.dataExchange.getCID());
			//生效时间格式转换为date类型
//			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			if(!"".equals(SHENGXSJ)){
//				D_SHENGXSJ=DateTimeUtil.DateFormat(SHENGXSJ);
//				record.put("SHENGXSJ", df.parse(D_SHENGXSJ));
				D_SHENGXSJ = DateUtil.StringFormatWithLine(SHENGXSJ);
				record.put("SHENGXSJ", D_SHENGXSJ);
			}
			// 根据"用户中心"、"零件号"、"供应商编码"三个维度去查询数据
			Map<String,String> t_map = this.queryLingjGYS(USERCENTER, LINGJBH, GONGYSBH);

			//获取供应商参考系表"发运地"到零件供应商参考系
			Map<String,String> map_fayd = this.queryFaydOfGongys(USERCENTER, GONGYSBH);
			if(!map_fayd.isEmpty()){
				record.put("FAYD", map_fayd.get("fayd"));
			}else{
				record.put("FAYD", "");
			}
			
			// 如果ZTYPE = '1'时
			if ("1".equals(type)) {
				if (t_map.isEmpty()) {// 如果"用户中心"、"零件号"和"供应商编码"三个维度，无数据
					// 如果"用户中心"和"零件号"两个维度，并且当"合同号"不为空，而且”生效标识”为1(有效) 时 查到有数据时配额为0，标识为0(无效)
					Map<String,String> y_map =  this.queryLingjGYSFE(USERCENTER, LINGJBH);
					if (!y_map.isEmpty()) {
						record.put("GONGYFE", ZERO_PERCENT);
						record.put("BIAOS", "0");
					}					
					else {// 如果"用户中心"和"零件号"两个维度，并且当"合同号"不为空，而且”生效标识”为1(有效)  没有查到数据时   配额为100%,标识为1(有效)
						record.put("GONGYFE", HUNDRED_PERCENT);
						record.put("BIAOS", "1");
					}
					//做插入操作 hzg 2013-6-4
					insertLingjgys(record);
					
				}else{
					//只更新"合同号"
					record.put("GONGYFE", t_map.get("GONGYFE"));   //GONGYFE份额不更新
					record.put("SHENGXSJ", t_map.get("SHENGXSJ")); //SHENGXSJ不更新
					record.put("BIAOS", t_map.get("BIAOS"));       //BIAOS不更新
					//做更新操作 hzg 2013-6-4
					updateLingjgys(record);
				}
			}
			// 如果ZTYPE='2'时
			if ("2".equals(type)) {
				String i_biaos = this.convertBiaos(GONGYFE);
				record.put("BIAOS",i_biaos);
				if (!t_map.isEmpty()) {// 如果"用户中心"、"零件号"和"供应商编码"三个维度，查到数据
					record.put("GONGYHTH", t_map.get("GONGYHTH"));  //GONGYHTH合同号不更新,//GONGYFE更新份额,//BIAOS更新
					//做更新操作 hzg 2013-6-4
					updateLingjgys(record);
				}else{
					//做插入操作 hzg 2013-6-4
					insertLingjgys(record);
				}
				//当配额为0时标识为0(失效)，当配额非0时标识为1(生效)
				/*int i_GONGYFE=	Integer.parseInt(GONGYFE);
				if(i_GONGYFE==0){
					record.put("BIAOS","0");
				}else{
					record.put("BIAOS","1");
				}*/
			}
			/*
			record.put("CREATOR", "interface");
			record.put("CREATE_TIME", new Date());
			record.put("EDITOR", "interface");
			record.put("EDIT_TIME", new Date());
			if(!"".equals(USERCENTER)){
				super.afterRecord(rowIndex, record, line);
			}else{
				logger.info("接口1070存在错误数据:"+line);
			}*/
			}
		} catch (Exception e) {
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
		}
	}
	
	/**
	 * 根据工业份额设置BIAOS
	 * @author 贺志国
	 * @date 2013-6-4
	 * @param gongyfe  工业份额
	 * @return BIAOS
	 */
	private String convertBiaos(String gongyfe){
		//当配额为0时标识为0(失效)，当配额非0时标识为1(生效)
		int i_GONGYFE=	Integer.parseInt(gongyfe);
		String biaos = "";
		if(i_GONGYFE==0){
			biaos ="0";
		}else{
			biaos="1";
		}
		return biaos;
	} 
	/**
	 * 将原程序的批量提交改为逐个单独提交
	 * @author 贺志国
	 * @date 2013-6-4
	 * @param record 
	 */
	private void insertLingjgys(Record record) {
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append("insert into ").append(SpaceFinal.spacename_ckx);
		sqlBuf.append(".ckx_lingjgys(usercenter,gongysbh,lingjbh,gongyhth,gongyfe,shengxsj,fayd,");
		sqlBuf.append("creator,create_time,editor,edit_time,biaos) values('");
		sqlBuf.append(record.getString("USERCENTER")).append("','").append(record.getString("GONGYSBH")).append("','");
		sqlBuf.append(record.getString("LINGJBH")).append("','").append(record.getString("GONGYHTH")).append("','");
		sqlBuf.append(record.getString("GONGYFE")).append("',").append("to_date('"+record.getString("SHENGXSJ")).append("','yyyy-MM-dd'),'");
		sqlBuf.append(record.getString("FAYD")).append("','");
		sqlBuf.append(record.getString("creator")).append("',to_date('").append(DateTimeUtil.getAllCurrTime()).append("','yyyy-MM-dd HH24:mi:ss'),'");
		sqlBuf.append(record.getString("editor")).append("',to_date('").append(DateTimeUtil.getAllCurrTime()).append("','yyyy-MM-dd HH24:mi:ss'),'");
		sqlBuf.append(record.getString("BIAOS")).append("')");
		try{
			super.execute(sqlBuf.toString());
		}catch(RuntimeException e){
			logger.error("接口1070插入ckx_lingjgys表报错"+e.getMessage());
		}
		super.INSERT_COUNT++;
		setFILE_INSERT_COUNT(INSERT_COUNT);
	}
	
	/**
	 * 如果"用户中心"、"零件号"和"供应商编码"三个维度，查到数据则更新
	 * @author 贺志国
	 * @date 2013-6-4
	 * @param record
	 */
	private void updateLingjgys(Record record) {
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append("update ").append(SpaceFinal.spacename_ckx).append(".ckx_lingjgys set GONGYHTH = '").append(record.getString("GONGYHTH")).append("',");
		sqlBuf.append("GONGYFE ='").append(record.getString("GONGYFE")).append("',");
		sqlBuf.append("SHENGXSJ=").append("to_date('"+record.getString("SHENGXSJ")).append("','yyyy-MM-dd'),");
		sqlBuf.append("FAYD='").append(record.getString("FAYD")).append("',");
		sqlBuf.append("BIAOS='").append(record.getString("BIAOS")).append("',").append("EDIT_TIME=");
		sqlBuf.append("to_date('").append(DateTimeUtil.getAllCurrTime()).append("','yyyy-MM-dd HH24:mi:ss')");
		sqlBuf.append("where USERCENTER = '" + StrNull(record.getString("USERCENTER").trim()) + "' and ");
		sqlBuf.append("LINGJBH = '" + StrNull(record.getString("LINGJBH").trim()) +"' and ");
		sqlBuf.append("GONGYSBH = '" + StrNull(record.getString("GONGYSBH").trim()) + "'");
		try{
			super.execute(sqlBuf.toString());
		}catch(RuntimeException e){
			logger.error("接口1070更新ckx_lingjgys表报错"+e.getMessage());
		}
		super.UPDATE_COUNT++;
		setFILE_UPDATE_COUNT(UPDATE_COUNT);
	}

	/**
	 * 查询零件供应商信息
	 * @author 贺志国
	 * @date 2012-10-21
	 * @param USERCENTER 用户中心
	 * @param LINGJBH 零件编号
	 * @param GONGYSBH 供应商编号
	 * @return Map<String,String>零件供应商集合
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> queryLingjGYS(String USERCENTER,String LINGJBH,String GONGYSBH){
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("select t.gongyhth, t.gongyfe,to_char(t.shengxsj,'yyyy-MM-dd') as shengxsj,t.biaos"); 
		sqlBuf.append(" from "+SpaceFinal.spacename_ckx+".CKX_LINGJGYS t ");
		sqlBuf.append("where USERCENTER = '" + StrNull(USERCENTER) + "' and ");
		sqlBuf.append("LINGJBH = '" + StrNull(LINGJBH) +"' and ");
		sqlBuf.append("GONGYSBH = '" + StrNull(GONGYSBH) + "'");
		Map<String,String> t_map = super.selectOne(sqlBuf.toString());
		return t_map;
		
	}
	
	/**
	 * 获取供应商参考系"发运地"
	 * @author 贺志国
	 * @date 2012-10-21
	 * @param USERCENTER 用户中心
	 * @param GONGYSBH 供应商编号
	 * @return Map<String,String> map集合
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> queryFaydOfGongys(String USERCENTER ,String GONGYSBH){
		StringBuffer sqlBuf=new StringBuffer();
		sqlBuf.append("select t.fayd as fayd ");
		sqlBuf.append("from "+SpaceFinal.spacename_ckx+".ckx_gongys t ");
		sqlBuf.append("left join "+SpaceFinal.spacename_ckx+".ckx_lingjgys y ");
		sqlBuf.append("on y.usercenter = t.usercenter ");
		sqlBuf.append("and y.gongysbh = t.gcbh ");
		sqlBuf.append("where y.usercenter = '"+StrNull(USERCENTER)+"' and ");
		sqlBuf.append("y.gongysbh = '"+StrNull(GONGYSBH)+"'");
		Map<String,String> map_fayd = super.selectOne(sqlBuf.toString());
		return map_fayd;
	}
	
	/**
	 * 查询零件供应商判断份额
	 * @author 贺志国
	 * @date 2012-10-21
	 * @param USERCENTER 用户中心
	 * @param LINGJBH 零件编号
	 * @return Map<String,String> map集合
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> queryLingjGYSFE(String USERCENTER ,String LINGJBH){
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("select * ");
		strbuf.append("from "+SpaceFinal.spacename_ckx+".CKX_LINGJGYS ");
		strbuf.append("where USERCENTER = '" + StrNull(USERCENTER)+"' and ");
		strbuf.append("LINGJBH = '" + StrNull(LINGJBH) + "' and ");
		strbuf.append("GONGYHTH is not null and ");
		strbuf.append("BIAOS = '1'");
		Map<String,String> y_map = super.selectOne(strbuf.toString());
		return y_map;
	}


	/**
	 * 判断字符为空时
	 * @param objstr
	 * @return
	 */
	private String StrNull(Object objstr){
		return objstr==null?"":objstr.toString();
	}
}
