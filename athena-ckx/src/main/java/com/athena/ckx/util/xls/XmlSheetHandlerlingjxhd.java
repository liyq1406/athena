package com.athena.ckx.util.xls;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.athena.ckx.entity.xuqjs.CkxLingjxhd;
import com.athena.ckx.entity.xuqjs.CkxLingjxhds;
import com.athena.ckx.entity.xuqjs.Fenpq;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.db.ConstantDbCode;
import com.toft.core3.dao.DataAccessException;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * 按照XML文件处理
 * @author chenlei
 * @vesion 1.0
 * @date 2012-7-26
 */
public class XmlSheetHandlerlingjxhd extends SheetHandlerlingjxhd {
	private AbstractIBatisDao baseDao;
	private static Properties prop;
	private String editor;
	private Map<String,String> mapfenpq;
	private Map<String,String> gongysmap;
	private Map<String,String> maplingj;
	private Map<String,String> gyxhdmap;
	private Map<String,String> xiaohcmap;
	private Map<String,String> xiaohcbhmap;
	protected static Logger logger = Logger.getLogger(XmlSheetHandlerlingjxhd.class);	//定义日志方法
	public XmlSheetHandlerlingjxhd(Document document, String sheet, String table,
			String datasourceId, String clazz, String keys, String dateColumns,
			String dateFormats) {
		super(document, sheet, table, datasourceId, clazz, keys, dateColumns,
				dateFormats);
	}
	
	/**
	 * 获取分配区
	 * @return
	 */
	private Map<String,Fenpq> getFenpqMap(){
		Fenpq param = new Fenpq();
		param.setBiaos("1");
		List<Fenpq> fenpqs =baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryFenpq",param);
		Map<String,Fenpq> fenpqMap = new HashMap<String,Fenpq>();
		for (Fenpq fenpq : fenpqs) {
			fenpqMap.put(fenpq.getUsercenter()+fenpq.getFenpqh(), fenpq);
		}
		return fenpqMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String doSheetHandler(String editor,Map<String, String>  mapfenpq,Map<String,String>  gongysmap,Map<String, String>  maplingj,Map<String, String>  gyxhdmap,Map<String, String>  xiaohcmap,Map<String, String>  xiaohcbhmap,AbstractIBatisDao baseDao) {
		try{
			this.editor = editor;
			this.mapfenpq=mapfenpq;
			this.gongysmap=gongysmap;
			this.maplingj=maplingj;
			this.gyxhdmap=gyxhdmap;
			this.xiaohcbhmap=xiaohcbhmap;
			this.xiaohcmap=xiaohcmap;
			this.baseDao = baseDao;
			// 对sheet处理 保存入库
			// 对sheet处理 保存入库
			Element sheetElment = getSheetElment(sheet);
			int rowNum = getRowNumber(sheetElment);
			int coluNum = getCellNumber(sheetElment);
			String result = "";
			// 导入的数据超过5000行 提示不让插入
			if (rowNum > 5002) {
				String message = "导入数据超过5000行,请调整数据行数";
				return message;
				// throw new ServiceException("导入数据超过5000行,请调整数据行数");
			}
			ArrayList<String> fieldList = new ArrayList<String>(); // 字段名 集合
			ArrayList<String> fieldValue = new ArrayList<String>(); // 一列的值
			List<CkxLingjxhd> datas = new ArrayList<CkxLingjxhd>();
			StringBuffer errorBuffer = new StringBuffer(); //错误信息
			StringBuffer wronginfo=new StringBuffer("");
			int index = 3;
			List<Element> list = sheetElment.selectNodes("xmlns:Table/xmlns:Row");
			Map<String,Fenpq> fenpqs = getFenpqMap();
			for (int i = 2; i < list.size(); i++) {
				CkxLingjxhd lingjxhd = new CkxLingjxhd();
				//Element rowElement = (Element) sheetElment.selectNodes("xmlns:Table/xmlns:Row").get(i+2);
				Element rowElement =  (Element) list.get(i);
				fieldList=getCellValue((Element) list.get(0));
				fieldValue=getCellValue(rowElement,fieldList.size());
				for (int j = 0; j < coluNum; j++) {
					if( j == 0)
					{
						//用户中心
						if(null!=fieldValue.get(j)&& fieldValue.get(j).matches("^[A-Za-z]+$")){
							lingjxhd.setUsercenter(fieldValue.get(j).toUpperCase());
						}
					}
					else if( j == 1)
					{
						//零件编号
						if(null!=fieldValue.get(j)&& fieldValue.get(j).matches("^[A-Za-z0-9_]+$")){
							lingjxhd.setLingjbh(fieldValue.get(j).toUpperCase());
						}
					}
					else if (j==2){
						//消耗点编号
						if(null!=fieldValue.get(j)&& fieldValue.get(j).matches("^[A-Za-z0-9_]+$")){
							lingjxhd.setXiaohdbh(fieldValue.get(j).toUpperCase());
						}
					}
					else if (j==3){
						//物流描述编号
						if(null!=fieldValue.get(j)&& !StringUtils.isBlank(fieldValue.get(j))){
							lingjxhd.setWulbh(fieldValue.get(j));
						}
					}
					else if (j==4){
						//消耗点起始日
						if(null!=fieldValue.get(j)&& !StringUtils.isBlank(fieldValue.get(j))){
							lingjxhd.setShengxr(fieldValue.get(j));
						}
					}
					else if (j==5){
						//结束日
						if(null!=fieldValue.get(j)&& !StringUtils.isBlank(fieldValue.get(j))){
							lingjxhd.setJiesr(fieldValue.get(j));
						}
					}
					
					else if (j==6){
						//管理员编号 数字 大小写字母 下划线
						if(null!=fieldValue.get(j)){
							lingjxhd.setGuanlybh(fieldValue.get(j).toUpperCase());
						}
					}
					
					else if (j==7){
						//指定供应商 10位  数字 大小写字母  下划线
						if(null!=fieldValue.get(j)&& fieldValue.get(j).matches("^[A-Za-z0-9_]+$")){
							lingjxhd.setGongysbh(fieldValue.get(j).toUpperCase());
						}
					}
					
					else if (j==8){
						//顺序管理标志
						if(null!=fieldValue.get(j)&& !StringUtils.isBlank(fieldValue.get(j))){
							lingjxhd.setShunxglbz(fieldValue.get(j).toUpperCase());
						}
					}
					else if (j==9){
						//自动发货标志
						if(null!=fieldValue.get(j)&& !StringUtils.isBlank(fieldValue.get(j))){
							lingjxhd.setZidfhbz(fieldValue.get(j).toUpperCase());
						}
					}
					else if (j==10){
						//线边要货类型
						if(null!=fieldValue.get(j)&& !StringUtils.isBlank(fieldValue.get(j))){
							lingjxhd.setXianbyhlx(fieldValue.get(j).toUpperCase());
						}
					}
					else if (j==11){
						//是否延迟上线
						if(null!=fieldValue.get(j)&&  !StringUtils.isBlank(fieldValue.get(j))){
							lingjxhd.setYancsxbz(fieldValue.get(j).toUpperCase());
						}
					}
					else if (j==12){
						//是否产生备货单
						if(null!=fieldValue.get(j)&& !StringUtils.isBlank(fieldValue.get(j))){
							lingjxhd.setBeihdbz(fieldValue.get(j).toUpperCase());
						}
					}
					else if (j==13){
						//小火车编号 数字 大小写字母  下划线
						if(null!=fieldValue.get(j)&& !StringUtils.isBlank(fieldValue.get(j))){
							lingjxhd.setXiaohcbh(fieldValue.get(j));
						}
					}
					else if (j==14){
						//小火车车厢号 数字 大小写字母  下划线
						if(null!=fieldValue.get(j)&& !StringUtils.isBlank(fieldValue.get(j))){
							lingjxhd.setXiaohccxbh(fieldValue.get(j));
						}
					}
					else if (j==15){
						//层   数字 大小写字母  下划线
						if(null!=fieldValue.get(j)&& !StringUtils.isBlank(fieldValue.get(j))){
							lingjxhd.setCeng(fieldValue.get(j));
						}
					}
					else if (j==16){
						//安全库存天数
						if(null!=fieldValue.get(j)&& !StringUtils.isBlank(fieldValue.get(j))){
							if(fieldValue.get(j).matches("^[1-9][0-9]{0,2}(?:[.][0-9]{1,3})?$|0[.][0-9]{1,3}$|^0$")){
								lingjxhd.setAnqkcts(Double.valueOf(fieldValue.get(j)));
							}else{
								lingjxhd.setAnqkcts(9999.999);
							}
						}
					}
					else if (j==17){
						//安全库存数
						if(null!=fieldValue.get(j)&& !StringUtils.isBlank(fieldValue.get(j))){
							if(fieldValue.get(j).matches("^[1-9][0-9]{0,6}(?:[.][0-9]{1,3})?$|0[.][0-9]{1,3}$|^0$")){
								lingjxhd.setAnqkcs(Double.valueOf(fieldValue.get(j)));
							}else{
								lingjxhd.setAnqkcs(999999999.999);
							}
						}
					}
			   }
			    datas.add(lingjxhd);
			}		
			
			//校验有误的条数
			int wrongcoun=0;
			
			//用一个新集合接收读取的数据，并且把错误信息放进去
			ArrayList<CkxLingjxhd> wrongdatas =  new ArrayList<CkxLingjxhd>();
			//用一个新集合接收读取的数据，并且把错误信息放进去
			ArrayList<CkxLingjxhd> datasnew =  new ArrayList<CkxLingjxhd>();
			
			//校验每个字段
			for (int i=0;i<datas.size();i++) {
				CkxLingjxhd xhd = datas.get(i);
				String s="";
				//校验用户中心
				if(null==xhd.getUsercenter() || StringUtils.isBlank(xhd.getUsercenter()) || xhd.getUsercenter().length()!=2){
					if(null==xhd.getUsercenter()){
						xhd.setUsercenter("");
					}
					result = resultMessage(index,"用户中心："+xhd.getUsercenter()+" 必填 只能由数字、字母以及下划线组成并且长度必须为2位");
					wronginfo=wronginfo.append(result);
				}
				//校验零件编号必填
				if(null==xhd.getLingjbh() || StringUtils.isBlank(xhd.getLingjbh()) || xhd.getLingjbh().length()!=10){
					if(null==xhd.getLingjbh()){
						xhd.setLingjbh("");
					}
					result = resultMessage(index,"零件编号："+xhd.getLingjbh()+"  必填 只能由数字、字母以及下划线组成并且长度必须为10位");
					wronginfo=wronginfo.append(result);
				}else{
					//校验零件编号的存在有效性
					 if (null==maplingj.get(xhd.getUsercenter()+xhd.getLingjbh())){
						 result = resultMessage(index,"用户中心："+xhd.getUsercenter() +" 零件编号："+xhd.getLingjbh()+" 不存在或已失效");
						wronginfo=wronginfo.append(result);
					 }
				}
				//校验   消耗点编号必填且存在有效性   (消耗点 在工艺消耗点表中存在即可) 
				if(null==xhd.getXiaohdbh() || StringUtils.isBlank(xhd.getXiaohdbh()) || xhd.getXiaohdbh().length()!=9){
					if(null==xhd.getXiaohdbh()){
						xhd.setXiaohdbh("");
					}
					result = resultMessage(index,"消耗点编号："+xhd.getXiaohdbh()+" 必填 只能由数字、字母以及下划线组成 并且长度必须为9位");
					wronginfo=wronginfo.append(result);
				}else{
					//在校验 消耗点编号的存在有效性
					if(!xhd.getXiaohdbh().equals(gyxhdmap.get(xhd.getXiaohdbh()))){
						result = resultMessage(index,"消耗点编号："+xhd.getXiaohdbh()+" 不存在或已失效");
						wronginfo=wronginfo.append(result);
					}else{
						//获得分配区 的值
						String f = xhd.getXiaohdbh().substring(0,5);//取消耗点的前5为对应分配区
	//					s =mapfenpq.get(xhd.getUsercenter()+f);//根据用户中心+分配区编号，取产线
						Fenpq fenpq = fenpqs.get(xhd.getUsercenter()+f);
						if(null==fenpq){
							result = resultMessage(index,"用户中心："+xhd.getUsercenter()+"分配区："+f+"没有对应有效产线");
							wronginfo=wronginfo.append(result);
						}else{
							s = fenpq.getShengcxbh();
							xhd.setShengcxbh(s);
							xhd.setFenzxh(fenpq.getFenzxh());
							xhd.setFenpqbh(f);
						}
					}
				}
				//物流描述编号 必填 比且为4位编号
				if(null==xhd.getWulbh() || StringUtils.isBlank(xhd.getWulbh()) || xhd.getWulbh().length()!=4){
					if(null==xhd.getWulbh()){
						xhd.setWulbh("");
					}
					result = resultMessage(index,"物流描述编号："+xhd.getWulbh()+" 必填 并且长度必须为4位");
					wronginfo=wronginfo.append(result);
				}else{
					if(!xhd.getWulbh().matches("^[A-Za-z0-9_]+$")){
						result = resultMessage(index,"物流描述编号："+xhd.getWulbh()+" 只能为数字、字母以及下划线");
						wronginfo=wronginfo.append(result);
					}
				}
				//消耗点起始日是否要校验???
				//消耗点解释日是否要校验？？？
				//只需要校验 消耗点起始日不能大于结束日
				if((null!=xhd.getUsercenter() && !StringUtils.isBlank(xhd.getUsercenter())) && (null!=xhd.getLingjbh() && !StringUtils.isBlank(xhd.getLingjbh())) 
						&& (null!=xhd.getXiaohdbh() && !StringUtils.isBlank(xhd.getXiaohdbh()))){
					if((null==xhd.getShengxr() || StringUtils.isBlank(xhd.getShengxr())) || (null==xhd.getJiesr() || StringUtils.isBlank(xhd.getJiesr()))){
						if(null==xhd.getShengxr()){
							xhd.setShengxr("");
						}
						if(null==xhd.getJiesr()){
							xhd.setJiesr("");
						}
						result = resultMessage(index,"消耗点起始日: "+xhd.getShengxr()+ "和结束日："+xhd.getJiesr()+" 必填并且格式为 YYYY-MM-DD");
						wronginfo=wronginfo.append(result);
					}else{
						if(DateTimeUtil.strToDate(xhd.getShengxr()).getTime()>=DateTimeUtil.strToDate(xhd.getJiesr()).getTime()){
							result = resultMessage(index,"用户中心"+xhd.getUsercenter()+"下零件编号为"+xhd.getLingjbh()+"的消耗点"+xhd.getXiaohdbh()+" 起始日期不能大于结束日期");
							wronginfo=wronginfo.append(result);
						}
					}
				}
				//管理员编号 和 指定供应商 的校验
				//管理员编号不为空
				if(null!=xhd.getGuanlybh() && !StringUtils.isBlank(xhd.getGuanlybh())){
					if(!xhd.getGuanlybh().matches("^[A-Za-z0-9_]+$")){
						result = resultMessage(index,"管理员编号："+xhd.getGuanlybh()+" 只能为数字、字母以及下划线");
						wronginfo=wronginfo.append(result);
					}
				}
				//指定供应商不为空  要校验存在有效性
				if(null!=xhd.getGongysbh() && !StringUtils.isBlank(xhd.getGongysbh())){
					if(null==gongysmap.get(xhd.getUsercenter()+xhd.getGongysbh())){
						result = resultMessage(index,"指定供应商编号："+xhd.getGongysbh()+" 不存在或已失效");
						wronginfo=wronginfo.append(result);
					}
				}
				
				//顺序管理标志 (Y/N)
				if(null==xhd.getShunxglbz() || " ".equals(xhd.getShunxglbz()) || (!"Y".equals(xhd.getShunxglbz()) && !"N".equals(xhd.getShunxglbz()))){
					if(null==xhd.getShunxglbz()){
						xhd.setShunxglbz("");
					}
					result = resultMessage(index,"顺序管理标志："+xhd.getShunxglbz()+" 必填并且只能为 Y/N中的一种");
					wronginfo=wronginfo.append(result);
				}
				//自动发货标志 (Y/N)
				if(null==xhd.getZidfhbz() || " ".equals(xhd.getZidfhbz()) || (!"Y".equals(xhd.getZidfhbz()) && !"N".equals(xhd.getZidfhbz()))){
					if(null==xhd.getZidfhbz()){
						xhd.setZidfhbz("");
					}
					result = resultMessage(index,"自动发货标志："+xhd.getZidfhbz()+" 必填并且只能为 Y/N中的一种");
					wronginfo=wronginfo.append(result);
				}
				
				//线边要货类型 (K/R/P/S/G)
				if(null==xhd.getXianbyhlx() || " ".equals(xhd.getXianbyhlx()) || (!"K".equals(xhd.getXianbyhlx()) && !"R".equals(xhd.getXianbyhlx())) && !"P".equals(xhd.getXianbyhlx()) && !"S".equals(xhd.getXianbyhlx()) && !"G".equals(xhd.getXianbyhlx())){
					if(null==xhd.getXianbyhlx()){
						xhd.setXianbyhlx("");
					}
					result = resultMessage(index,"线边要货类型："+xhd.getXianbyhlx()+" 必填并且只能为 K/R/P/S/G中的一种");
					wronginfo=wronginfo.append(result);
				}
				
				//是否延迟上线 (I/P)
				if(null==xhd.getYancsxbz() || " ".equals(xhd.getYancsxbz()) || (!"I".equals(xhd.getYancsxbz()) && !"P".equals(xhd.getYancsxbz()))){
					if(null==xhd.getYancsxbz()){
						xhd.setYancsxbz("");
					}
					result = resultMessage(index,"是否延迟上线："+xhd.getYancsxbz()+" 必填并且只能为 I/P中的一种");
					wronginfo=wronginfo.append(result);
				}
				
				//是否产生备货单 (Y/N)
				if(null==xhd.getBeihdbz() || " ".equals(xhd.getBeihdbz()) || (!"Y".equals(xhd.getBeihdbz()) && !"N".equals(xhd.getBeihdbz()))){
					if(null==xhd.getBeihdbz()){
						xhd.setBeihdbz("");
					}
					result = resultMessage(index,"是否产生备货单："+xhd.getBeihdbz()+" 必填并且只能为 Y/N中的一种");
					wronginfo=wronginfo.append(result);
				}
				
				
				//校验小火车编号 用户中心+产线+小火车号
				if(null!=s && !"".equals(s) && null!=xhd.getXiaohcbh() && !StringUtils.isBlank(xhd.getXiaohcbh())){
					if(xhd.getXiaohcbh().length()>5){
						result = resultMessage(index,"小火车编号："+xhd.getXiaohcbh()+" 必填并且长度最大为5位");
						wronginfo=wronginfo.append(result);
					}else{
						//校验存在性
						if(null==xiaohcmap.get(xhd.getUsercenter()+xhd.getShengcxbh()+xhd.getXiaohcbh())){
							result = resultMessage(index,"小火车编号："+xhd.getXiaohcbh()+" 不存在或已失效");
							wronginfo=wronginfo.append(result);
						}
					}
				}
				
				//校验小火车车厢编号   用户中心+产线+小火车号+小火车车厢号  必须产线要存在
				if(null!=s && !"".equals(s) && null!=xhd.getXiaohccxbh() && !StringUtils.isBlank(xhd.getXiaohccxbh())){
					if(xhd.getXiaohccxbh().length()>4){
						result = resultMessage(index,"小火车车厢编号："+xhd.getXiaohccxbh()+" 必填并且长度最大为4位");
						wronginfo=wronginfo.append(result);
					}else{
						//校验存在性
						if(null==xiaohcbhmap.get(xhd.getUsercenter()+xhd.getShengcxbh()+xhd.getXiaohcbh()+xhd.getXiaohccxbh())){
							result = resultMessage(index,"小火车车厢编号："+xhd.getXiaohccxbh()+" 不存在或已失效");
							wronginfo=wronginfo.append(result);
						}
					}
				}
				//校验层  可填
				if(null!=xhd.getCeng()&& !StringUtils.isBlank(xhd.getCeng())){
					if(xhd.getCeng().length()>2 || !xhd.getCeng().matches("^[A-Za-z0-9_]+$")){
						result = resultMessage(index,"层："+xhd.getCeng()+" 长度最大为2位并且只能为数字、字母以及下划线");
						wronginfo=wronginfo.append(result);
					}
				}
				//安全库存天数
				if(null!=xhd.getAnqkcts()&& !StringUtils.isBlank(xhd.getAnqkcts().toString())){
					if(9999.999==xhd.getAnqkcts()){
						result = resultMessage(index,"安全库存天数   只能为 0-999.99");
						wronginfo=wronginfo.append(result);
					}
				}
				//安全库存数
				if(null!=xhd.getAnqkcs()&& !StringUtils.isBlank(xhd.getAnqkcs().toString())){
					if(999999999.999==xhd.getAnqkcs()){
						result = resultMessage(index,"安全库存数   只能为 0-9999999.999");
						wronginfo=wronginfo.append(result);
					}
				}
				//xhd.setJiesr("9999-12-31");
				xhd.setBiaos("1");
				xhd.setCreator(editor);
				xhd.setCreate_time(DateTimeUtil.getAllCurrTime());
				xhd.setEditor(editor);
				xhd.setEdit_time(DateTimeUtil.getAllCurrTime());
				index ++;
				//如果有错误信息将会返回
				if(!wronginfo.toString().equals("")){
					wronginfo.append("\n");
					wrongcoun=wrongcoun+1;
					wrongdatas.add(xhd);
					errorBuffer.append(wronginfo);
					wronginfo= new StringBuffer("");
					//return wronginfo.toString();
				}
				datasnew.add(xhd);
				if(wrongdatas.size()>0 && wrongcoun==5){
					break;
				}
			}
			
			
				//如果没有错误信息，正常操作，如果有则需要返回
				if(wrongdatas.size()<=0){
					//在判断是插入 还是更新
					//先判断导入的数据是否存在 如果存在则更新
					//sqlInsertOrUpdate(datasnew);
					return sqlInsertOrUpdate(datasnew);
				}
				
				  return errorBuffer.toString();
		}finally{
			//关闭连接
			if(conn!=null){
				try {
					if(!conn.isClosed())
						conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	
	public  String  sqlInsertOrUpdate(ArrayList<CkxLingjxhd> datasnew){
		try {
			for (CkxLingjxhd lingjxhd : datasnew) {
				//判断导入的数据是新增还是更新后 在来做操作
				if(DBUtil.checkCount(baseDao,"ts_ckx.getCountLingjxhd", lingjxhd)){
					//如果是更新的话,则需要判断是否有并发的情况,采用 乐观锁  即增加 版本字段来控制
					//先查询出对应的记录存储的版本号
					//更新前线查询对应的零件消耗点 查询出他的 VERSION 版本的值
					CkxLingjxhd versionxhd = (CkxLingjxhd)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryCkxLjxhdsx", lingjxhd);
					//当条记录对应的version版本号
					String versionmax = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCkxLjxhdversion",lingjxhd);
					if (Integer.valueOf(versionmax) == 999999999) {
						//版本号自动循环 如果超过了最大的上限  则在从 1 开始
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateversion",lingjxhd);
						lingjxhd.setVersion(1);
						//versionxhd.setVersion(1);
					}else{
						lingjxhd.setVersion(versionxhd.getVersion());
					}
					int num = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkxLingjxhdImport",lingjxhd);
					//如果更新失败，则表示同时有人在操作该条数据  应该提示给用户
					if(0==num){
						return  "数据中有某一行数据正在被多个用户同时操作，请稍后在更新!";
					}
				}else{
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkxLingjxhdImport",lingjxhd);
				}
			}
		} catch (DataAccessException e) {
			logger.info(e.getMessage());
			return "插入或更新失败";
		}
		return "";
	}

	
	
	
	
	/**
	 * 创建查找sql
	 * 
	 * @param fieldList
	 * @param keys
	 * @return
	 */
	private String makeSelectSql(ArrayList<String> fieldList, String keys) {
		// select count(1) from sys_bus_log l where
		// l.id='8a9a2b26354157d601354157d64f0000'
		String[] keyStrs = keys.split(",");
		StringBuffer sb = new StringBuffer();

		sb.append("select count(1) from ");
		sb.append(replacetable(table));
		sb.append(" where ");

		for (int j = 0; j < keyStrs.length; j++) {
			if (j == keyStrs.length - 1) {
				// 不加 逗号
				sb.append(keyStrs[j] + "=?");
			} else {
				sb.append(keyStrs[j] + "=? and ");
			}
		}

		return sb.toString();
	}

	/**
	 * 创建修改sql
	 * 
	 * @param fieldList
	 * @param keys
	 *            主键
	 * @return
	 */
	private String makeUpdateSql(ArrayList<String> fieldList,String keys,ArrayList<String> fieldValue) {
		String[] keyStrs = keys.split(",");

		// update sys_bus_log set OPERATOR='2',OPERATOR_IP='22' where id='333'
		StringBuffer sb = new StringBuffer();
		sb.append("update ");
		sb.append(replacetable(table));
		sb.append(" set ");
		
		for (int i = 0; i < fieldList.size(); i++) {
			if(fieldValue==null){
				if (i == fieldList.size() - 1) {
					// 不加 逗号
					sb.append(fieldList.get(i) + "=" + createPlaceholder(fieldList.get(i)));
				} else {
					sb.append(fieldList.get(i) + "=" + createPlaceholder(fieldList.get(i))+",");
				}
			}else{
				if(fieldValue.get(i)!=null){
					if (i == fieldList.size() - 1) {
						// 不加 逗号
						sb.append(fieldList.get(i) + "=" + createPlaceholder(fieldList.get(i)));
					} else {
						sb.append(fieldList.get(i) + "=" + createPlaceholder(fieldList.get(i))+",");
					}
				}
			}
		}
		
		//剔除最后的逗号
		if(sb.toString().endsWith(",")){
			sb= sb.deleteCharAt(sb.length()-1);
		}
		//0007177  将修改人编辑为当前登录人  编辑时间写入数据库
		sb.append(",SHENGCXBH=?,biaos='1',");
		sb.append("jiesr=to_date('9999-12-31','yyyy-MM-dd'),");
		sb.append("editor='" + editor+"',");
		sb.append("edit_time = sysdate ");
		sb.append(" where ");

		for (int j = 0; j < keyStrs.length; j++) {
			if (j == keyStrs.length - 1) {
				// 不加 逗号
				sb.append(keyStrs[j] + "=?");
			} else {
				sb.append(keyStrs[j] + "=? and ");
			}
		}

		return sb.toString();
	}

	/**
	 * 将fieldValue保存入库 如果库有此条记录，则执行更新操作； 没有，则执行新增操作
	 * 
	 * @param fieldValue
	 * @param fieldList
	 * @param insertSql
	 * @param updateSql
	 * @param selectSql
	 * @throws SQLException
	 */
	private void addOrUpdateToTable(ArrayList<String> fieldValue,
			ArrayList<String> fieldList, String insertSql, String updateSql,
			String selectSql,ArrayList<String> fieldErrorValue) throws SQLException {

		if (getFiledByKey(fieldValue, fieldList, selectSql) < 1) {
			insertValue(fieldValue, fieldList, insertSql,fieldErrorValue);
//			throw new ServiceException("错误数据");		
		} else {
			// 数据库有 则执行 修改
			if(fieldValue.contains(null)){
				//Value中有空值  则重新生成sql 踢出null
				String insertSqlNNull = makeUpdateSql(fieldList,keys,fieldValue);
				fieldValue = removeNull(fieldValue);
				updateValue(fieldValue, fieldList, insertSqlNNull,fieldErrorValue);
			}else{
				updateValue(fieldValue, fieldList, updateSql,fieldErrorValue);
			}	
		}

	}
	
	/**
	 * 移除fieldValue为Null的元素
	 * @param fieldValue
	 * @return
	 */
	private ArrayList<String> removeNull(ArrayList<String> fieldValue) {
		for(int i=0;i<fieldValue.size();){
			if(fieldValue.get(i)==null){
				fieldValue.remove(i);
			}else{
				i++;
			}
		}
		return fieldValue;
	}

	/**
	 * 修改操作
	 * 
	 * @param fieldValue
	 * @param fieldList
	 * @param updateSql
	 * @throws SQLException
	 */
	private void updateValue(ArrayList<String> fieldValue,
			ArrayList<String> fieldList, String updateSql,ArrayList<String> fieldErrorValue) throws SQLException {
		String[] keyStrs = keys.split(",");
		
		//将keyValues的值保存到fieldValue中
		for (int i = 0; i < keyStrs.length; i++) {
			//keyValues[i] = fieldValue.get(fieldList.indexOf(keyStrs[i]));
			fieldValue.add(fieldValue.get(fieldList.indexOf(keyStrs[i])));
		}
	
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(updateSql);
			if(fieldValue.get(4).length()==9){
				//原消耗点编号  必须要提前存在 
				Map map1 = new HashMap();
				map1.put("usercenter", fieldValue.get(0));
				map1.put("lingjbh", fieldValue.get(1));
				map1.put("xiaohdbh",fieldValue.get(4));
				map1.put("biaos", "1");
				CkxLingjxhd Lingjxhds = (CkxLingjxhd)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryLingjxhd",map1);
				if(null==Lingjxhds){
					throw new SQLException("原消耗点编号："+fieldValue.get(4)+" 不存在");
				}
			}
			// 将占位符替换
			for (int i = 0; i < fieldValue.size(); i++) {
				if(i+1==3){
					String f=fieldValue.get(i).substring(0,5);//取消耗点的前5为对应分配区
					String s=this.mapfenpq.get(fieldValue.get(i-2)+f);//根据用户中心+分配区编号，取产线
//					st.setObject(n, f);
					if(null==s){
						throw new SQLException("用户中心："+fieldValue.get(i-2)+"分配区："+f+"没有对应有效产线");
					}
					
					//根据用户中心+分配区编号，取产线  ,将值存入list
					fieldValue.add(i+7, s);
//					st.setObject(n+1, s);
				}
				
				st.setObject(i+1, fieldValue.get(i));
			}
			st.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			//错误信息
			StringBuffer esb = new StringBuffer();
			for (int i = 0; i < keyStrs.length; i++) {
				esb.append(keyStrs[i]+"=");
				esb.append(fieldErrorValue.get(fieldList.indexOf(keyStrs[i]))+ "   ");
			}
			esb.append(" 此条数据修改失败.");
			esb.append(" : 错误信息   ");
			esb.append(e.getMessage());
	
			throw new SQLException(esb.toString());
		}finally{
			if(st!=null){
				st.close();
			}			
		}

	}


	/**
	 * 添加操作
	 * 
	 * @param fieldValue
	 * @param fieldList
	 * @param insertSql
	 * @throws SQLException
	 */
	private void insertValue(ArrayList<String> fieldValue,
			ArrayList<String> fieldList, String insertSql,ArrayList<String> fieldErrorValue) throws SQLException {
		String[] keyStrs = keys.split(",");
	
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(insertSql);
			if(fieldValue.get(4).length()==9){
				//原消耗点编号  必须要提前存在 
				Map map1 = new HashMap();
				map1.put("usercenter", fieldValue.get(0));
				map1.put("lingjbh", fieldValue.get(1));
				map1.put("xiaohdbh",fieldValue.get(4));
				map1.put("biaos", "1");
				CkxLingjxhd Lingjxhds = (CkxLingjxhd)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryLingjxhd",map1);
				if(null==Lingjxhds){
					throw new SQLException("原消耗点编号："+fieldValue.get(4)+" 不存在");
				}
			}
			// 将占位符替换
			for (int i = 0; i < fieldValue.size(); i++) {
				if(i+1==3){
					
					
					String f=fieldValue.get(i).substring(0,5);//取消耗点的前5为对应分配区
					String s=this.mapfenpq.get(fieldValue.get(i-2)+f);//根据用户中心+分配区编号，取产线
					st.setObject(i+8, f);
					if(null==s){
						throw new SQLException("用户中心："+fieldValue.get(i-2)+"分配区："+f+"没有对应有效产线");
					}
					//根据用户中心+分配区编号，取产线
					st.setObject(i+9, s);
				}
				st.setObject(i+1, fieldValue.get(i));
			}
			st.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			//错误信息
			StringBuffer esb = new StringBuffer();
			for (int i = 0; i < keyStrs.length; i++) {
				esb.append(keyStrs[i]+"=");
				esb.append(fieldErrorValue.get(fieldList.indexOf(keyStrs[i]))+ "   ");
			}
			esb.append(" 此条数据添加失败.");
			esb.append(" : 错误信息   ");
			esb.append(e.getMessage());
	
			throw new SQLException(esb.toString());
		}finally{
			if(st!=null){
				st.close();
			}			
		}

	}

	@Transactional
	private void biangLingjxhd(CkxLingjxhd ckxLingjxhd)  throws SQLException{
		//根据新老消耗点去查询对应的PDS拆分标识
		Map map = new HashMap();
		map.put("usercenter", ckxLingjxhd.getUsercenter());
		map.put("lingjbh", ckxLingjxhd.getLingjbh());
		map.put("xiaohdbh", ckxLingjxhd.getXiaohdbh());
		map.put("biaos", "1");
		CkxLingjxhd Lingjxhd = (CkxLingjxhd)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryLingjxhd",map);
		Map map1 = new HashMap();
		map1.put("usercenter", ckxLingjxhd.getUsercenter());
		map1.put("lingjbh", ckxLingjxhd.getLingjbh());
		map1.put("xiaohdbh", ckxLingjxhd.getYuanxhdbh());
		map1.put("biaos", "1");
		CkxLingjxhd Lingjxhds = (CkxLingjxhd)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryLingjxhd",map1);
//		if(null==Lingjxhd){
//			throw new SQLException("消耗点编号："+ckxLingjxhd.getXiaohdbh()+" 不存在");
//		}
//		if(null==Lingjxhds){
//			throw new SQLException("原消耗点编号："+ckxLingjxhd.getYuanxhdbh()+" 不存在");
//		}
		//根据新老消耗点去查询对应的PDS拆分标识  如果标识为 按需  则 对应的插入表ckx_lingjxhd_s
		//插入的时候 需要注意判断 如果 ckx_lingjxhd_s 表中不存在此条记录 则 直接插入 初始化为 未生效状态,并且 shengxr 和 jiesr 默认都是空.待用户去该页面手动生效时填写
		//如ckx_lingjxhd_s表中存在此条记录,则清空 shengxr 和 jiesr  并且标识为 未生效
		//只要PDS拆分标识中的值不为空 就可以替换。新消耗点或老消耗点 中的任意一个
		if((!"".equals(Lingjxhd.getPdsbz())&& null!=Lingjxhd.getPdsbz())||(!"".equals(Lingjxhds.getPdsbz())&& null!=Lingjxhds.getPdsbz())){
			//if(Lingjxhd.getPdsbz().equals("按需")||Lingjxhds.getPdsbz().equals("按需")){
				CkxLingjxhds ckxLingjxhds = new CkxLingjxhds();
				ckxLingjxhds.setUsercenter(ckxLingjxhd.getUsercenter());
				ckxLingjxhds.setLingjbh(ckxLingjxhd.getLingjbh());
				ckxLingjxhds.setXiaohd(ckxLingjxhd.getXiaohdbh());
				ckxLingjxhds.setYuanxhd(ckxLingjxhd.getYuanxhdbh());
				ckxLingjxhds.setBiaos("0");
				ckxLingjxhds.setCreator(ckxLingjxhd.getCreator());
				ckxLingjxhds.setEditor(ckxLingjxhd.getEditor());
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.mergelingjXhds",ckxLingjxhds);
			//}
		}
//		else{
//			throw new ServiceException("零件消耗点变更中的 新老消耗点的PDS拆分标识不能全部为空,并且PDS拆分标识只能为  按需");
//		}
	}
	
	
	/**
	 * 添加操作
	 * 
	 * @param fieldValue
	 * @param fieldList
	 * @param insertSql
	 * @throws SQLException
	 */
	private void insertXhdbgValue(ArrayList<String> fieldValue,
			ArrayList<String> fieldList, String insertSql,ArrayList<String> fieldErrorValue) throws SQLException {
		String[] keyStrs = keys.split(",");
	
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(insertSql);

			// 将占位符替换
			for (int i = 0; i < fieldValue.size()-4; i++) {
				st.setObject(i+1, fieldValue.get(i));
			}
			st.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			//错误信息
			StringBuffer esb = new StringBuffer();
			for (int i = 0; i < keyStrs.length; i++) {
				esb.append(keyStrs[i]+"=");
				esb.append(fieldErrorValue.get(fieldList.indexOf(keyStrs[i]))+ "   ");
			}
			esb.append("YUANXHDBH=");
			esb.append(fieldErrorValue.get(4)+ "   ");
			esb.append(" 零件消耗点变更 数据添加失败.");
			esb.append(" : 错误信息   ");
			esb.append(e.getMessage());
	
			throw new SQLException(esb.toString());
		}finally{
			if(st!=null){
				st.close();
			}			
		}

	}
	
	
	
	
	
	/**
	 * 根据key做查找
	 * 
	 * @param fieldValue
	 * @param selectSql
	 * @return
	 * @throws SQLException
	 */
	private int getFiledByKey(ArrayList<String> fieldValue,
			ArrayList<String> fieldList, String selectSql) throws SQLException {
		int result = 0;
		String[] keyStrs = keys.split(",");
		String[] keyValues = new String[keyStrs.length]; // key值

		for (int i = 0; i < keyStrs.length; i++) {
			keyValues[i] = fieldValue.get(fieldList.indexOf(keyStrs[i]));
		}
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(selectSql);
			
			// 将占位符替换
			for (int i = 0; i < keyValues.length; i++) {
				st.setObject(i+1, keyValues[i]);
			}

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				result = rs.getInt(1);
				return result;
			}
		
		} catch (SQLException e) {
			throw e;
		}finally{
			if(st!=null){
				st.close();
			}
		}

		return result;
	}

	/**
	 * 生成保存sql
	 * 
	 * @param fieldList
	 *            要保存的sql字段名称
	 * @return
	 */
	private String makeInsertxhdbgSql(ArrayList<String> fieldList,ArrayList<String> fieldValue) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sb_value = new StringBuffer();
		sb.append("INSERT INTO ");
		sb.append("ZBC_TEST.CKX_LINGJXHD_S");
		sb.append(" ( ");
		
		for (int i = 0; i < fieldList.size()-4; i++) {
			if (i == fieldList.size() - 1) {
				// 不加 逗号
				if(fieldValue!=null){
					if(fieldValue.get(i)!=null){
						sb.append(fieldList.get(i));
						sb_value.append(createPlaceholder(fieldList.get(i)));
					}else{
						//去除逗号
						 if(sb.toString().endsWith(",")){
							 sb = sb.deleteCharAt(sb.length()-1);
							 sb_value = sb_value.deleteCharAt(sb_value.length()-1);
						 }
					}
				}else{
					sb.append(fieldList.get(i));
					sb_value.append(createPlaceholder(fieldList.get(i)));
				}
			} else {
				if(fieldValue!=null){
					if(fieldValue.get(i)!=null){
						sb.append(fieldList.get(i) + ",");
						sb_value.append(createPlaceholder(fieldList.get(i))+",");
					}
				}else{
					sb.append(fieldList.get(i) + ",");
					sb_value.append(createPlaceholder(fieldList.get(i))+",");
				}
			}
		}
		sb.append("BIAOS,CREATOR,CREATE_TIME,EDITOR,EDIT_TIME");
		sb.append(") values (");
		sb_value.append("'0'");
		sb_value.append(",'"+editor+"'");
		sb_value.append(",sysdate");
		sb_value.append(",'"+editor+"'");
		sb_value.append(",sysdate");
		sb.append(sb_value.toString() + ")");

		return sb.toString();
	}
	
	
	
	/**
	 * 生成保存sql
	 * 
	 * @param fieldList
	 *            要保存的sql字段名称
	 * @return
	 */
	private String makeInsertSql(ArrayList<String> fieldList,ArrayList<String> fieldValue) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sb_value = new StringBuffer();
		sb.append("INSERT INTO ");
		sb.append(replacetable(table));
		sb.append(" ( ");
		
		for (int i = 0; i < fieldList.size(); i++) {
			if (i == fieldList.size() - 1) {
				// 不加 逗号
				if(fieldValue!=null){
					if(fieldValue.get(i)!=null){
						sb.append(fieldList.get(i));
						sb_value.append(createPlaceholder(fieldList.get(i)));
					}else{
						//去除逗号
						 if(sb.toString().endsWith(",")){
							 sb = sb.deleteCharAt(sb.length()-1);
							 sb_value = sb_value.deleteCharAt(sb_value.length()-1);
						 }
					}
				}else{
					sb.append(fieldList.get(i));
					sb_value.append(createPlaceholder(fieldList.get(i)));
				}
			} else {
				if(fieldValue!=null){
					if(fieldValue.get(i)!=null){
						sb.append(fieldList.get(i) + ",");
						sb_value.append(createPlaceholder(fieldList.get(i))+",");
					}
				}else{
					sb.append(fieldList.get(i) + ",");
					sb_value.append(createPlaceholder(fieldList.get(i))+",");
				}
			}
		}
		sb.append(",FENPQBH,SHENGCXBH,BIAOS,JIESR,CREATOR,CREATE_TIME,EDITOR,EDIT_TIME");
		sb.append(") values (");
		sb_value.append(",?,?,'1'");
		sb_value.append(",to_date('9999-12-31','yyyy-MM-dd')");
		sb_value.append(",'"+editor+"'");
		sb_value.append(",sysdate");
		sb_value.append(",'"+editor+"'");
		sb_value.append(",sysdate");
		sb.append(sb_value.toString() + ")");

		return sb.toString();
	}
	
	/**
	 * 生成占位符 
	 * 	时间类型的 生成to_date() ?
	 * @return
	 */
	private String createPlaceholder(String field){
		String result = "?";
		
		if(dateColumns!=null || !"null".equals(dateColumns)){
			//看此字段是否为时间类型
			if(dateColumns.contains(field)){
				//包含此字段，此字段为时间类型
				String[] dateColumn = dateColumns.split(",");
				String[] dateType = dateFormats.split(",");
				for(int i=0;i<dateColumn.length;i++){
					if(field.equals(dateColumn[i])){
						result = "to_date(?,'"+dateType[i]+"')";
					}
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 替换表空间名 ${dbSchemal3}
	 * 
	 * @param sql
	 * @return
	 */
	public String replacetable(String sql) {

		String result = sql;

		if (sql.contains("{")) {
			String regex = "\\{\\w+\\}";
			List<String> li = new ArrayList<String>();
			final Pattern pa = Pattern.compile(regex);
			final Matcher ma = pa.matcher(sql);
			while (ma.find()) {
				li.add(ma.group());
			}

			// 将占位符替换成真命名空间名
			for (String key : li) {
				String realValue = getValueByKey(key);
				sql = sql.replace(key, realValue);
			}

			result = sql;
		}
		return result;

	}

	/**
	 * 对Key 最处理后 再依据处理后的key到 properties文件中拿值 key:{xxxx} 处理后 xxx
	 * 
	 * @param key
	 * @return
	 */
	private String getValueByKey(String key) {
		String result = null;
		String realKey = key.substring(1, key.length() - 1);
		result = prop.getProperty(realKey);
		return result;
	}

	// 加载sqlmap.properties文件
	static {
		try {
			logger.info("加载了......");
			InputStream in = BaseSheetHandler.class.getClassLoader()
					.getResourceAsStream(
							"com/athena/component/config/sqlmap.properties");
			prop = new Properties();
			prop.load(in);

		} catch (Exception e) {
			logger.error("加载sqlmap.properties文件出错...");
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 根据sheet的名字去从XML拿出此sheet的对象
	 * @param sheetName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Element getSheetElment(String sheetName){
		Element sheetElement = null;
		Node node = document.selectSingleNode("Workbook");
		List list = node.selectNodes("xmlns:Worksheet");
		for(int i=0;i<list.size();i++){
			Element ele = (Element) list.get(i);
			if(sheetName.equals(ele.attributeValue("Name"))){
				sheetElement = ele;
				break;
			}
		}
		return sheetElement;
	}
	
	/**
	 * 拿出此sheet中包含的列数  默认取 第一个行的包含列数
	 * @param sheetElement sheet对象
	 * @return
	 */
	private int getCellNumber(Element sheetElement){
		Element firstRowElment = (Element) sheetElement.selectNodes("xmlns:Table/xmlns:Row").get(0);
		return firstRowElment.selectNodes("xmlns:Cell").size();
	}
	/**
	 * 
	 * @param sheetElement sheet对象
	 * @return
	 */
	private int getRowNumber(Element sheetElement){
		return sheetElement.selectNodes("xmlns:Table/xmlns:Row").size();
	}
	
	/**
	 * 得到此行的 列的值
	 * @param rowElement
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private ArrayList<String> getCellValue(Element rowElement){
		int top = 0;
		ArrayList<String> resultList = new ArrayList<String>();
		//拿出此行包含的列
		List cellList = rowElement.selectNodes("xmlns:Cell");
		for(int i=0;i<cellList.size();i++){
			Element cellElement = (Element) cellList.get(i);
			
			if(top==0){
				if(cellElement.attributeValue("Index")==null){
					//没有序列号 则认为此列为第一列
					String value = getDataValue((Element)(cellElement.selectSingleNode("xmlns:Data")));
					resultList.add(value);
					top++;
				}else{
					//补上前面的值 统一补null
					int b = Integer.parseInt(cellElement.attributeValue("Index"));
					for(int j=0;j<b-1;j++){
						resultList.add(null);
						top++;
					}
					//再将本身添加进去
					String value = getDataValue((Element)(cellElement.selectSingleNode("xmlns:Data")));
					resultList.add(value);
					top++;
				}
			}else{
				if(cellElement.attributeValue("Index")==null){
					//就是默认后面的
					String value = getDataValue((Element)(cellElement.selectSingleNode("xmlns:Data")));
					resultList.add(value);
					top++;
				}else{
					//补上 Integer.parseInt(cellElement.attributeValue("Index"))-top-1 个值
					int b = Integer.parseInt(cellElement.attributeValue("Index"));
					int top2 = top;
					for(int j=0;j<b-1-top2;j++){
						resultList.add(null);
						top++;
					}
					String value = getDataValue((Element)(cellElement.selectSingleNode("xmlns:Data")));
					resultList.add(value);
					top++;
				}
			}
		}
		
		return resultList;
	}
	/**
	 * 
	 * @param rowElement
	 * @param fieldSize
	 * @return如果最后列为空 则要放入null
	 */
	private ArrayList<String> getCellValue(Element rowElement,int fieldSize){
		ArrayList<String> resultList = getCellValue(rowElement);
		if(resultList.size()<fieldSize){
			for(int i=0;i<fieldSize-resultList.size();){
				resultList.add(null);
			}
		}
		return resultList;
	}
	
	/**
	 * 如果data标签不为空 则返回此value  为空则返回Null
	 * @param dataElement
	 * @return
	 */

	@SuppressWarnings("unchecked")
	private String getDataValue(Element dataElement){
		String result = null;
		if(dataElement!=null){
			//0007177 如果日期存了两层，则取到最里层的值，外面一层为空
			List<Branch> list = dataElement.content();
//			if(list.size() > 1){
//				Element e = (Element) list.get(1);
//				dataElement = e;
//			}			
			result = dataElement.getText();
		}
		return result;
	}
	
	private String resultMessage(int index, String message) 
	{
		StringBuffer flagSb = new StringBuffer();
		flagSb.append("  导入文件中第").append(index).append("行,").append(message);
		return flagSb.toString();
	}


}
