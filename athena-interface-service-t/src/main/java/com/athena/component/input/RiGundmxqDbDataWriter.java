package com.athena.component.input;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;
import com.athena.component.exchange.utils.ConvertUtils;



/**
 * 零件日毛需求输入类
 * @author GJ
 */
public class RiGundmxqDbDataWriter extends DbDataWriter {
	public RiGundmxqDbDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}

	protected static Logger logger = Logger.getLogger(GundmxqDbDataWriter.class);	//定义日志方法
	public static final SimpleDateFormat yyyyMMddHHmmssSSS = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	public static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	public static final SimpleDateFormat d_format = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 需求版次
	 */
	private static int liush = 1;
	
	  

	
	/**
	 * 文本中出现非法字符并且标识不为"1"时，跳过解析
	 */
	@Override
	public boolean beforeRecord(int rowIndex, Object line) {
		if("".equals(line)){
			return false;
		}
		String biaos=line.toString().substring(75,76).trim();
		boolean isFlag = "1".equals(biaos)?true:false;
		return isFlag;
	}

	/**
	 * 数据解析之前清空毛需求表
	 */
	@Override
	public boolean before() {
		 //根据需求来源和拆分日期查询毛需求版次表版次记录，如果没有则添加一条新纪录
	    String sql = "select count(xuqbc) as liush from "+SpaceFinal.spacename_xqjs+".xqjs_maoxq where to_char(create_time,'yyyyMMdd') = '"+yyyyMMdd.format(new Date())+"'";
	    String liushString = ConvertUtils.strNull(super.selectValue(sql));
	    if(liushString.equals("0")){
	    	liush = 1;
	    }else{
	    	liush = Integer.parseInt(liushString);
	    	liush++;
	    }
		return super.before();
	}
	
	/**
	 * 根据需求来源和拆分日期查询毛需求版次表版次记录，如果没有则添加一条新纪录
	 * @author 贺志国
	 * @date 2012-10-15
	 * @param xuqbc 需求版次
	 * @param xuqcfsj 需求拆分时间
	 * @param xuqly 需求来源
	 * @return String 需求版次号 
	 */
	public synchronized String queryXuqbcOfmaoxq(Map<String, String> params){
		String xuqbc = params.get("xuqbc");
		String sql = "select xuqbc from "+SpaceFinal.spacename_xqjs+".xqjs_maoxq where xuqbc = '"+xuqbc+"'";
		String str = ConvertUtils.strNull(super.selectValue(sql));
		logger.debug("执行取毛需求版次号sql语句:" + sql);
		//毛需求表插入版次信息
		if("".equals(str)){
			this.insertMaoxq(xuqbc, params.get("xuqcfsj"), params.get("xuqly"));
		}
		return xuqbc;
	} 
	
	/**
	 * 行解析之后处理方法
	 * @param rowIndex 行标
	 * @param record 行数据集合
	 * @author GJ
	 */
	@Override
	public  void afterRecord(int rowIndex, Record record,Object line) {
		try {
			logger.debug("记录数：" + rowIndex);
			boolean flag = true;
			if(null!=record && record.size()> 0){
				if(!"".equals(record.get("xuqrq"))){
					Map<String, String> params = parserParam(rowIndex,record,line);
					//如果来源为CLV，处理CLV数据
					if(params.get("xuqly").equals("CLV")){
						flag = this.dealCLVData(rowIndex,params.get("xuqcfsj"));
						if(flag==false){
							return;
						}
					}
					//过滤作用域
					flag = this.filtrateZuoyy(params.get("xuqly"),params.get("chejh"),params.get("lingjbh"),params.get("usercenter"));
					if(flag==false){
						return;
					}

					synchronized(this){
						//单位换算
						this.conversionDanw(record, params);
						
						//根据相关条件查询毛需求明细ID,未查询到数据,新增毛需求明细信息  
						if(isUpdateMaoxqmxOfId(params)>0)return;
						//需求所属周期  ,在进行周、周期转换的时候已经放到record中，这里不需要再进行截取 hzg 2013-3-30
						/*String xqrq=record.get("xuqrq").toString().substring(0,6);
						record.put("xuqsszq", xqrq);*/
						
						//根据需求来源和拆分日期查询毛需求版次表版次记录，如果没有则添加一条新纪录
						String xuqbcString = this.queryXuqbcOfmaoxq(params);
						record.put("xuqbc", xuqbcString);
						//毛需求明细ID
						record.put("id", getUUID());
						//创建人
						//record.put("creator", "interface");
						record.put("creator", super.dataExchange.getCID());
						//修改人
						//record.put("editor", "interface");
						record.put("editor", super.dataExchange.getCID());
						//毛需求明细插入版次信息
						insertMaoxqmx(record,params.get("xuqrq"));
					}
				}
			}
		} catch (RuntimeException e) {
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
		} 
	}
	
	/**
	 * 查询毛需求ID
	 * @author 贺志国
	 * @date 2012-10-15
	 * @param params 查询参数
	 * @return String 毛需求ID
	 */
	public int isUpdateMaoxqmxOfId(Map<String,String> params){
		logger.debug("查询毛需求ID");
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("update ").append(SpaceFinal.spacename_xqjs).append(".xqjs_maoxqmx set xuqsl = (xuqsl + '");
		sqlBuf.append(params.get("xuqsl")).append("') where xuqbc ='");
		sqlBuf.append(params.get("xuqbc")).append("' and usercenter = '").append(params.get("usercenter")).append("' and shiycj = '");
		sqlBuf.append(params.get("chejh")).append("' and lingjbh = '").append(params.get("lingjbh")).append("' and danw = '").append(params.get("danw"));
		sqlBuf.append("' and zhizlx = '").append(params.get("zhizlx")).append("' and xuqrq = to_date('").append(params.get("xuqrq"));
		sqlBuf.append("','yyyyMMdd') and chanx ='").append(params.get("chanx")).append("'");
		logger.debug("update合并需求数量sql：" + sqlBuf.toString());
		int num = super.execute(sqlBuf.toString());
		super.UPDATE_COUNT++;
		setFILE_UPDATE_COUNT(UPDATE_COUNT);
		return num;
	}
	
	/**
	 * 合并毛需求数量
	 * @author 贺志国
	 * @date 2012-10-18
	 * @param id 毛需求ID
	 * @param params 参数map
	 */
	public void mergeMaoxqsl(String id,Map<String,String> params){
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("update ").append(SpaceFinal.spacename_xqjs).append(".xqjs_maoxqmx set xuqsl = (xuqsl + '");
		sqlBuf.append(params.get("xuqsl")).append("') where id ='").append(id).append("'");
		logger.debug("毛需求ID相同，update合并需求数量sql：" + sqlBuf.toString());
		super.execute(sqlBuf.toString());
		super.UPDATE_COUNT++;
		setFILE_UPDATE_COUNT(UPDATE_COUNT);
	}
	
	/**
	 * 参数解析
	 * @author 贺志国
	 * @date 2012-10-15
	 * @param rowIndex
	 * @param record
	 * @param line
	 * @return Map<String,String>
	 */
	private Map<String,String> parserParam(int rowIndex,Record record,Object line) {
		logger.debug("参数解析");
		Calendar calendar = Calendar.getInstance();
		//获取用户中心,为文本前三位
		String usercenter = record.getString("usercenter");		
		//获得需求来源
		String xuqly = line.toString().substring(72,75);
		//需求拆分时间
		String xuqcfsj = line.toString().substring(76,84);
		//需求日期
		String xuqrq = record.get("xuqrq").toString();
		record.put("xuqksrq", xuqrq);
		//版次格式化
		String xuqbc = this.formateXuqbc(xuqly, calendar);
	    //获取车间号,线号前三位
	    String chejh = line.toString().substring(0,3);
	    record.put("shiycj", chejh);
	    //获得线号
		String chanx = chejh + record.getString("chanx");
	    //零件编号
	    String lingjbh = record.getString("lingjbh");
	    //制造路线
	    String zhizlx = record.getString("zhizlx");
	    //单位
	    String danw = record.getString("danw");
	    //需求数量
	    BigDecimal xuqsl = BigDecimal.valueOf((Double) record.get("xuqsl"));
		// 周,周期转换
		this.transformZhouq(record, xuqrq);
		//查询目标产线
		String mubcx = this.queryMubChanx(chanx, usercenter);
		//产线转换
		if(!mubcx.equals("")){
			chanx = mubcx;
		}
		record.put("chanx", chanx);
		//查询制造路线
		String zhizlxx = this.queryZhizxl(zhizlx, usercenter);
		//制造路线转换查询无数据,保存异常报警
		if("".equals(zhizlxx)){
			saveYicbj("现制造路线查询无数据,原制造路线"+zhizlx, lingjbh, usercenter);
		}else{
			//订货路线转换零件查询
			List<String> list = this.queryLingjOfDinghlxzh(usercenter);
			if(list.contains(lingjbh)){
				//订货路线转换，用户中心，零件编号，制造路线查询
				String dinghlx = this.queryDinghlx(usercenter, lingjbh, zhizlxx);
				if("".equals(dinghlx)){
					saveYicbj("订货路线查询无数据,现制造路线"+zhizlxx, lingjbh, usercenter);
					zhizlx = zhizlxx;
				}else{
					zhizlx = dinghlx;
				}
			}else{
				zhizlx = zhizlxx;
			}
		}
		record.put("zhizlx", zhizlx);
		//数据合并,根据相关信息查询数据,查询到则进行合并,否则添加毛需求明细信息
		Map<String,String> params = new HashMap<String,String>();
		params.put("xuqsl", xuqsl.toString());
		params.put("xuqcfsj", xuqcfsj);
		params.put("xuqly", xuqly);
		params.put("xuqbc", xuqbc);
		params.put("usercenter", usercenter);
		params.put("chejh", chejh);
		params.put("lingjbh", lingjbh);
		params.put("danw", danw);
		params.put("zhizlx", zhizlx);
		params.put("xuqrq", xuqrq);
		params.put("chanx", chanx);
		return params;
	}

	/**
	 * 订货路线转换表零件查询
	 * @author 贺志国
	 * @date 2012-11-23
	 * @param usercenter 用户中心
	 * @return List<String> list用户中心下所有的订货路线转换零件集合
	 */
	@SuppressWarnings("unchecked")
	public List<String> queryLingjOfDinghlxzh(String usercenter){
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("select lingjbh from ").append(SpaceFinal.spacename_ckx);
		sqlBuf.append(".ckx_dinghlxzh where usercenter = '");
		sqlBuf.append(usercenter).append("'");
		List<Map<String,String>> listDhlj = super.select(sqlBuf.toString());
		List<String> listStr = new ArrayList<String>();
		for(Map<String,String> map:listDhlj){
			listStr.add(map.get("LINGJBH"));
		}
		return listStr;
	}
	
	/**
	 * 查询订货路线
	 * @author 贺志国
	 * @date 2012-10-18
	 * @param usercenter 用户中心
	 * @param lingjbh 零件编号
	 * @param zhizlxx 现制造路线
	 * @return String 订货路线
	 */
	public String queryDinghlx(String usercenter,String lingjbh,String zhizlxx){
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("select dinghlx from ").append(SpaceFinal.spacename_ckx);
		sqlBuf.append(".ckx_dinghlxzh where usercenter = '");
		sqlBuf.append(usercenter).append("' and lingjbh = '");
		sqlBuf.append(lingjbh).append("' and zhizlx = '");
		sqlBuf.append(zhizlxx).append("'");
		String dinghlx = ConvertUtils.strNull(super.selectValue(sqlBuf.toString()));
		return dinghlx;
	}
	
	/**
	 * 格式化需求版次
	 * @author 贺志国
	 * @date 2012-10-16
	 * @param xuqly 需求来源
	 * @param calendar 当前日历对象
	 * @return String 需求版次号
	 */
	public String formateXuqbc(String xuqly,Calendar calendar){
		logger.debug("格式化需求版次");
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(2); 
		SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd"); 
		String xuqbc = xuqly+yyyyMMdd.format(calendar.getTime())+nf.format(liush);
		return xuqbc;
	}

	/**
	 * CLV数据处理
	 * @author 贺志国
	 * @date 2012-10-15
	 * @param rowIndex 行序号
	 * @param xuqly 需求来源
	 * @param xuqcfsj 需求拆分时间
	 * @param calendar 日历对象
	 */
	public boolean dealCLVData(int rowIndex,String xuqcfsj){
			logger.debug("CLV数据处理");
			Calendar calendar = Calendar.getInstance();
			//如果有CLV数据,进行清除
			if(rowIndex == 1){
				//清除毛需求明细表数据
				String sql = "delete "+SpaceFinal.spacename_xqjs+".xqjs_maoxqmx where xuqbc in (select xuqbc from "+SpaceFinal.spacename_xqjs+".xqjs_maoxq  where xuqly='CLV')";
				super.execute(sql);
				//清除毛需求表数据
				sql = "delete "+SpaceFinal.spacename_xqjs+".xqjs_maoxq where xuqly ='CLV'";
				super.execute(sql);
			}
			//如果当前时间在中午12点之前（取昨天的数据，即跳过不为昨天的数据）
			SimpleDateFormat d_format = new SimpleDateFormat("yyyy-MM-dd"); 
			if(calendar.get(calendar.AM_PM) == calendar.AM){
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				//如果拆分日期不为昨天,跳过该条 
				
				if(!DateTimeUtil.DateFormat(xuqcfsj).equals(d_format.format(calendar.getTime()))){
					return false;
				}
				//如果当前时间在中午12点之后（取今天的数据，即跳过不为今天的数据）
			}else{
				//如果拆分日期不为今天,跳过该条
				if(!DateTimeUtil.DateFormat(xuqcfsj).equals(d_format.format(calendar.getTime()))){
					return false;
				}
			}
			return true;
	}
	
	/**
	 * 过滤作用域
	 * @author 贺志国
	 * @date 2012-10-15
	 * @param xuqly 需求来源
	 * @param chejh 车间号
	 * @param lingjbh 零件编号
	 * @param usercenter 用户中心
	 */
	public boolean filtrateZuoyy(String xuqly,String chejh,String lingjbh,String usercenter){
		logger.debug("过滤作用域");
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("select zuoyy from ").append(SpaceFinal.spacename_ckx).append(".ckx_xuqly where xuqly = '");
		sqlBuf.append(xuqly).append("' and zuoyy = '").append(chejh).append("'");
		logger.debug("作用域查询sql：" + sqlBuf.toString());
		//查询到作用域为空,过滤该数据
		if(ConvertUtils.strNull(super.selectValue(sqlBuf.toString())).equals("")){
			saveYicbj("作用域查询为空,需求来源"+xuqly+"车间号"+chejh, lingjbh, usercenter);
			return false;
		}
		return true;
	}
	
	/**
	 * 周,周期转换
	 * @author 贺志国
	 * @date 2012-10-15
	 * @param record 文本读取集合
	 * @param xuqrq 需求日期
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void transformZhouq(Record record,String xuqrq){
		logger.debug("周,周期转换");
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("select nianzx,nianzq from ");
		sqlBuf.append(SpaceFinal.spacename_ckx);
		sqlBuf.append(".ckx_calendar_center where ");
		sqlBuf.append("usercenter ='"+record.getString("usercenter")+"'");
		sqlBuf.append(" and riq = to_char(to_date('");
		sqlBuf.append(xuqrq+"','yyyyMMdd'),'yyyy-MM-dd')");
		//周,周期转换
    	Map<String,String>map = super.selectOne(sqlBuf.toString());
    	//如果查询不到相关周期,周序信息,异常报警
    	if(map.isEmpty()){
    		saveYicbj("中心日历表周,周期信息查询无数据,日期"+xuqrq, record.getString("lingjbh"), record.getString("usercenter"));
    	}else{
	    	//年周期为需求所属周期
	    	record.put("xuqsszq", ConvertUtils.strNull(map.get("nianzq")));
	    	//年周序为需求所属周
	    	record.put("xuqz", ConvertUtils.strNull(map.get("nianzx")));
    	} 	
	}
	
	/**
	 * 查询目标产线
	 * @author 贺志国
	 * @date 2012-10-15
	 * @param chanx 文本读取产线
	 * @param usercenter 用户中心
	 * @return String 目标产线
	 */
	public String queryMubChanx(String chanx,String usercenter){
		logger.debug("查询目标产线");
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("select mubcx from ").append(SpaceFinal.spacename_ckx).append(".ckx_chanxhb where usercenter ='");
		sqlBuf.append(usercenter).append("' and yuancx = '").append(chanx).append("'");
		logger.debug("目标产线查询sql：" + sqlBuf.toString());
		return ConvertUtils.strNull(super.selectValue(sqlBuf.toString()));
	}
	
	/**
	 * 查询制造路线
	 * @author 贺志国
	 * @date 2012-10-15
	 * @param zhizlx 文本读取制造路线
	 * @param usercenter 用户中心
	 * @return String 制造路线
	 */
	public String queryZhizxl(String zhizlx,String usercenter){
		logger.debug("查询制造路线");
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("select zhizlxx from  ").append(SpaceFinal.spacename_ckx).append(".ckx_zhizlxzh where usercenter = '");
		sqlBuf.append(usercenter).append("' and zhizlxy = '").append(zhizlx).append("'");
		logger.debug("制造路线查询sql：" + sqlBuf.toString());
		return ConvertUtils.strNull(super.selectValue(sqlBuf.toString()));
	}
	
	/**
	 * 查询毛需求ID
	 * @author 贺志国
	 * @date 2012-10-15
	 * @param params 查询参数
	 * @return String 毛需求ID
	 */
	public String queryMaoxqmxOfId(Map<String,String> params){
		logger.debug("查询毛需求ID");
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("select id from ").append(SpaceFinal.spacename_xqjs).append(".xqjs_maoxqmx where xuqbc ='");
		sqlBuf.append(params.get("xuqbc")).append("' and usercenter = '").append(params.get("usercenter")).append("' and shiycj = '");
		sqlBuf.append(params.get("chejh")).append("' and lingjbh = '").append(params.get("lingjbh")).append("' and danw = '").append(params.get("danw"));
		sqlBuf.append("' and zhizlx = '").append(params.get("zhizlx")).append("' and xuqrq = to_date('").append(params.get("xuqrq"));
		sqlBuf.append("','yyyyMMdd') and chanx ='").append(params.get("chanx")).append("'");
		logger.debug("毛需求ID查询sql：" + sqlBuf.toString());
		return ConvertUtils.strNull(super.selectValue(sqlBuf.toString()));
	}
	
	/**
	 * 单位换算
	 * @author 贺志国
	 * @date 2012-10-15
	 * @param record  文本读取记录集
	 * @param lingjbh 零件编号
	 * @param usercenter 用户中心
	 * @param danw 单位
	 * @param xuqsl 需求数量
	 */
	public void conversionDanw(Record record,Map<String, String> params){
		String danw = params.get("danw");
		logger.debug("单位换算：" + danw);
		BigDecimal xuqsl = new BigDecimal(params.get("xuqsl"));
		//零件表单位字段查询
		String lingjdw = this.queryLingjDW(params.get("usercenter"), params.get("lingjbh"));
		//零件单位信息查询不到,保存异常报警
		if(lingjdw.equals("")){
			saveYicbj("零件单位信息查询无数据,零件号"+params.get("lingjbh"), params.get("lingjbh"), params.get("usercenter"));
		}else{
			//如果单位不同,进行换算
			if(!lingjdw.equals(danw)){
				//单位换算表换算比例查询
				BigDecimal huansbl = this.queryHuansbl(params.get("usercenter"), danw, lingjdw);
				//换算比例为空,保存异常报警
				if(huansbl == null){
					saveYicbj("换算比例查询为空,单位"+danw+",被换单位"+lingjdw, params.get("lingjbh"), params.get("usercenter"));
				}else{
					record.put("xuqsl", xuqsl.multiply(huansbl) );
					params.put("xuqsl", xuqsl.multiply(huansbl).toString());
					record.put("danw", lingjdw);
					params.put("danw", lingjdw);
				}
			}
		}
	}
	/**
	 * 零件单位查询
	 * @author 贺志国
	 * @date 2012-10-18
	 * @param usercenter 用户中心
	 * @param lingjbh 零件编号
	 * @return String 零件单位
	 */
	public String queryLingjDW(String usercenter,String lingjbh){
		StringBuffer sqlBufDW = new StringBuffer();
		sqlBufDW.append("select danw from ").append(SpaceFinal.spacename_ckx).append(".ckx_lingj where usercenter = '");
		sqlBufDW.append(usercenter).append("' and lingjbh = '").append(lingjbh).append("'");
		String lingjdw = ConvertUtils.strNull(super.selectValue(sqlBufDW.toString()));
		return lingjdw;
	}
	
	/**
	 * 
	 * @author 贺志国
	 * @date 2012-10-18
	 * @param usercenter
	 * @param danw
	 * @param lingjdw
	 * @return
	 */
	public BigDecimal queryHuansbl(String usercenter,String danw,String lingjdw){
		StringBuffer sqlBufBL = new  StringBuffer();
		sqlBufBL.append("select huansbl from ").append(SpaceFinal.spacename_ckx);
		sqlBufBL.append(".ckx_danwhs where usercenter = '");
		sqlBufBL.append(usercenter).append("' and beihdw = '");
		sqlBufBL.append(danw).append("' and mubdw = '").append(lingjdw).append("'");
		return (BigDecimal) super.selectValue(sqlBufBL.toString());
		
	}
	

	/**
	 * 毛需求明细表插入版次信息
	 * @author 贺志国
	 * @date 2012-10-15
	 * @param record 文本读取记录集
	 * @param xuqrq 需求日期
	 */
	public void insertMaoxqmx(Record record,String xuqrq){
		logger.debug("毛需求明细表插入版次信息");
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("insert into ").append(SpaceFinal.spacename_xqjs);
		sqlBuf.append(".xqjs_maoxqmx(id,xuqbc,usercenter,shiycj,");
		sqlBuf.append("chanx,xuqz,xuqrq,lingjbh,xuqsl,danw,zhizlx,xuqsszq,xuqksrq,xuqjsrq,cangkdm,editor,edit_time,creator,create_time,active) ");
		sqlBuf.append("values('").append(record.getString("id")).append("','").append(record.getString("xuqbc")).append("','");
		sqlBuf.append(record.getString("usercenter")).append("','").append(record.getString("shiycj")).append("','");
		sqlBuf.append(record.getString("chanx")).append("','").append(record.getString("xuqz")).append("',to_date('").append(xuqrq).append("','yyyy-MM-dd'),'");
		sqlBuf.append(record.getString("lingjbh")).append("','").append(record.getString("xuqsl")).append("','").append(record.getString("danw")).append("','");
		sqlBuf.append(record.getString("zhizlx")).append("','").append(record.getString("xuqsszq")).append("','").append(record.getString("xuqksrq")).append("','");
		sqlBuf.append(record.getString("xuqjsrq")).append("','").append(record.getString("cangkdm")).append("','").append(record.getString("editor"));
		sqlBuf.append("',to_timestamp('").append(DateTimeUtil.getAllCurrTime()).append("','yyyy-MM-dd HH24:mi:ss'),'").append(record.getString("creator"));
		sqlBuf.append("',to_timestamp('").append(DateTimeUtil.getAllCurrTime()).append("','yyyy-MM-dd HH24:mi:ss'),'1')");
		super.execute(sqlBuf.toString());
		super.INSERT_COUNT++;
		setFILE_INSERT_COUNT(INSERT_COUNT);
	}

	/**
	 * 毛需求表插入版次信息
	 * @author 贺志国
	 * @date 2012-10-15
	 * @param xuqbc 需求版次
	 * @param xuqcfsj 需求拆分时间
	 * @param xuqly 需求来源
	 */
	public void insertMaoxq(String xuqbc,String xuqcfsj,String xuqly){
		logger.debug("毛需求表插入版次信息");
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("insert into ").append(SpaceFinal.spacename_xqjs);
		sqlBuf.append(".xqjs_maoxq(xuqbc,xuqcfsj,xuqly,shengxbz,");
		sqlBuf.append("creator,create_time,editor,edit_time,active) values('");
		sqlBuf.append(xuqbc).append("',to_date('").append(xuqcfsj).append("','yyyyMMdd'),'");
		sqlBuf.append(xuqly).append("','0','1052',to_date('").append(DateTimeUtil.getAllCurrTime());
		sqlBuf.append("','yyyy-MM-dd HH24:mi:ss'),'1052',to_date('").append(DateTimeUtil.getAllCurrTime()).append("','yyyy-MM-dd HH24:mi:ss'),'1')");
		super.execute(sqlBuf.toString());
	}
	
	
	/**
	 * 保存异常报警
	 * @param cuowxxxx 错误详细信息
	 * @param lingjbh 零件编号
	 * @param usercenter 用户中心
	 */
	public void saveYicbj(String cuowxxxx,String lingjbh,String usercenter){
		logger.debug(" 保存异常报警");
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("insert into ").append(SpaceFinal.spacename_xqjs);
		sqlBuf.append(".xqjs_yicbj(jismk,lingjbh,usercenter,cuowlx,");
		sqlBuf.append("cuowxxxx,jihyz,jihydm,creator,create_time,editor,edit_time) values('10','").append(lingjbh).append("','");
		sqlBuf.append(usercenter).append("','200','").append(cuowxxxx).append("','1052','1052','1052',to_timestamp('");
		sqlBuf.append(DateTimeUtil.getAllCurrTime()).append("','yyyy-MM-dd HH24:mi:ss'),'1052',to_timestamp('");
		sqlBuf.append(DateTimeUtil.getAllCurrTime()).append("','yyyy-MM-dd HH24:mi:ss'))");
		super.execute(sqlBuf.toString());

	}
	
	
}
