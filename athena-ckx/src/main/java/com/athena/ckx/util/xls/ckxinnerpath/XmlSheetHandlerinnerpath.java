package com.athena.ckx.util.xls.ckxinnerpath;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import jxl.read.biff.BiffException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.cangk.Cangk;
import com.athena.ckx.entity.carry.CkxInnerPathAndModle;
import com.athena.ckx.entity.carry.CkxShengcxXianb;
import com.athena.ckx.entity.carry.CkxWaibms;
import com.athena.ckx.entity.carry.CkxXianbDingh;
import com.athena.ckx.entity.xuqjs.CkxLingj;
import com.athena.ckx.entity.xuqjs.Fenpq;
import com.athena.ckx.entity.xuqjs.Lingjck;
import com.athena.ckx.entity.xuqjs.Lingjgys;
import com.athena.ckx.entity.xuqjs.Xitcsdy;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.db.ConstantDbCode;

import com.toft.core3.dao.DataAccessException;
import com.toft.core3.ibatis.support.AbstractIBatisDao;


/**
 * 按照XML文件处理
 * @author lc
 * @date 2016-10-24
 */
public class XmlSheetHandlerinnerpath extends SheetHandlerinnerpath {
	private AbstractIBatisDao baseDao;
	private static Properties prop;
	private String editor;
	private Map<String,String> cmap;
	protected static Logger logger = Logger.getLogger(XmlSheetHandlerinnerpath.class);	//定义日志方法
	public XmlSheetHandlerinnerpath(Document document, String sheet, String table,
			String datasourceId, String clazz, String keys, String dateColumns,
			String dateFormats) {
		super(document, sheet, table, datasourceId, clazz, keys, dateColumns,
				dateFormats);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String doSheetHandler(String editor,Map<String,String> cmap,AbstractIBatisDao baseDao) throws BiffException, IOException, ParseException {
		try{
			
		this.editor = editor;
		this.cmap=cmap;
		this.baseDao = baseDao;
		String result = "";
		// 对sheet处理 保存入库
		Element sheetElment = getSheetElment(sheet);
		int rowNum = getRowNumber(sheetElment);
		//导入的数据超过5000行 提示不让插入
		if(rowNum>5002){
			String  message = "导入数据超过5000行,请调整数据行数";
			return message;
		}
		int coluNum = getCellNumber(sheetElment);
		ArrayList<String> fieldList = new ArrayList<String>(); // 字段名 集合
		ArrayList<String> fieldValue = new ArrayList<String>(); // 一列的值
		ArrayList<String> fieldErrorValue = new ArrayList<String>(); // 一列的值 为错误使用
		//String insertSql = null; // 新增sql语句
		//String updateSql = null; // 修改sql语句
		//String selectSql = null; // 查找sql语句
		List<CkxInnerPathAndModle> datas = new ArrayList<CkxInnerPathAndModle>();
		StringBuffer errorBuffer = new StringBuffer(); //错误信息
		StringBuffer wronginfo=new StringBuffer("");
		int num = 0;
		int index = 3;
		List<Element> list = sheetElment.selectNodes("xmlns:Table/xmlns:Row");
		for (int i = 2; i < list.size(); i++) {
			CkxInnerPathAndModle nbwl = new CkxInnerPathAndModle();
			Element rowElement =  (Element) list.get(i);
			fieldValue=getCellValue(rowElement,fieldList.size());
			for (int j = 0; j < coluNum; j++) {
				if( j == 0)
				{
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						nbwl.setUsercenter(fieldValue.get(j).toUpperCase());
					}
				}else if(j == 1 && j< fieldValue.size()){
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						nbwl.setLingjbh(fieldValue.get(j).toUpperCase());
					}
				}else if( j == 2  && j< fieldValue.size())
				{
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						nbwl.setFenpqh(fieldValue.get(j).toUpperCase());
					}
				}else if(j == 3 && j< fieldValue.size()){
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						nbwl.setXianbk(fieldValue.get(j).toUpperCase() );
					}
				}else if( j == 4  && j< fieldValue.size())
				{
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						nbwl.setMos(fieldValue.get(j).toUpperCase() );
					}
				}else if(j == 5 && j< fieldValue.size()){
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						nbwl.setJianglms(fieldValue.get(j).toUpperCase());
					}
				}else if(j == 6 && j< fieldValue.size()){
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						nbwl.setJianglmssxsj(fieldValue.get(j).toUpperCase());
					}
				}else if(j == 7 && j< fieldValue.size()){
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						nbwl.setWms(fieldValue.get(j).toUpperCase());
					}
				}else if( j == 8  && j< fieldValue.size())
				{
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						nbwl.setWjlms(fieldValue.get(j).toUpperCase());
					}
				}else if(j == 9 && j< fieldValue.size()){
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						nbwl.setShengxsj(fieldValue.get(j).toUpperCase() );
					}
				}else if( j == 10  && j< fieldValue.size())
				{
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						nbwl.setZhidgys(fieldValue.get(j).toUpperCase() );
					}
				}else if(j == 11 && j< fieldValue.size()){
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						nbwl.setDinghk(fieldValue.get(j).toUpperCase());
					}
				}else if(j == 12 && j< fieldValue.size()){
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						nbwl.setMos2(fieldValue.get(j).toUpperCase());
					}
				}else if(j == 13 && j< fieldValue.size()){
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						nbwl.setJianglms2(fieldValue.get(j).toUpperCase());
					}
				}else if(j == 14 && j< fieldValue.size()){
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						nbwl.setJianglmssxsj2(fieldValue.get(j).toUpperCase());
					}
				}

			}
			datas.add(nbwl);
		}
		
		//校验有误的条数
		int wrongcoun=0;
		
		//用一个新集合接收读取的数据，并且把错误信息放进去
		ArrayList<CkxInnerPathAndModle> wrongdatas =  new ArrayList<CkxInnerPathAndModle>();
		//用一个新集合接收读取的数据，并且把错误信息放进去
		ArrayList<CkxInnerPathAndModle> datasnew =  new ArrayList<CkxInnerPathAndModle>();
		//校验每个字段
		for (int i=0;i<datas.size();i++) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date currdate = new Date();
			Date shengxTime = null;
			CkxInnerPathAndModle bean = datas.get(i);
			//对主键进行校验 只需要验证该数据存不存在且有效
			if(null==bean.getUsercenter() || StringUtils.isBlank(bean.getUsercenter())){
				if(null==bean.getUsercenter()){
					bean.setUsercenter("");
				}
				result = resultMessage(index,"用户中心："+bean.getUsercenter()+"  必填并且长度必须为2位");
				wronginfo=wronginfo.append(result);
			}else if(!bean.getUsercenter().matches("^[A-Z]{2}$")){
				bean.setUsercenter("");
				result = resultMessage(index,"用户中心：必须为2位只能由字母组成");
				wronginfo=wronginfo.append(result);
			}else{
				//用户中心权限校验
				if(AuthorityUtils.getSecurityUser().getUcList()!=null && AuthorityUtils.getSecurityUser().getUcList().size()>0){
					List<String> uclist=AuthorityUtils.getSecurityUser().getUcList();
					if(!uclist.contains(bean.getUsercenter())){
						result = resultMessage(index,"用户中心：您没有权限操作用户中心为"+bean.getUsercenter()+"的数据");
						wronginfo=wronginfo.append(result);
					}
				}
			}
			if(null==bean.getLingjbh() || StringUtils.isBlank(bean.getLingjbh())){
				if(null==bean.getLingjbh()){
					bean.setLingjbh("");
				}
				result = resultMessage(index,"零件编号："+bean.getLingjbh()+" 必填并且长度必须为10位");
				wronginfo=wronginfo.append(result);
			}else {
			    if(!bean.getLingjbh().matches("^[A-Za-z0-9_]{10}$")){
			    	bean.setLingjbh("");
					result = resultMessage(index,"零件编号：必须为10位只能由数字、字母以及下划线组成");
					wronginfo=wronginfo.append(result);
			     }else{
			    	 CkxLingj lingj=new CkxLingj();
			 		 lingj.setUsercenter(bean.getUsercenter());
			 		 lingj.setLingjbh(bean.getLingjbh());
			 		 lingj.setBiaos("1");
	    			 if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountLingj", lingj)){
	    				 result = resultMessage(index,"零件表中不存在零件编号："+bean.getLingjbh()+"的数据或数据已失效");
	    				 wronginfo=wronginfo.append(result);	    				
	    			 }
			     }
			}
			if(null==bean.getFenpqh() || StringUtils.isBlank(bean.getFenpqh())){
				if(null==bean.getFenpqh()){
					bean.setFenpqh("");
				}
				result = resultMessage(index,"分配区："+bean.getFenpqh()+" 必填并且长度必须为5位");
				wronginfo=wronginfo.append(result);
			}else if(!bean.getFenpqh().matches("^[A-Za-z0-9]{5}$")){
				bean.setFenpqh("");
				result = resultMessage(index,"分配区：必须为5位只能由数字、字母组成");
				wronginfo=wronginfo.append(result);
			}else{
				Fenpq fenpq=new Fenpq();
				fenpq.setUsercenter(bean.getUsercenter());
				fenpq.setFenpqh(bean.getFenpqh());
				fenpq.setBiaos("1");
				fenpq.setWulgyyz(cmap.get("wulgyyz"));
				fenpq = (Fenpq) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getFenpq_Inner", fenpq);				
                if(null == fenpq){
                	result = resultMessage(index,"分配区表中不存在用户中心："+bean.getUsercenter()+"分配区编号："+bean.getFenpqh()+"的数据或数据已失效或无该分配区数据权限");
    				wronginfo=wronginfo.append(result);	
                }else{
                	bean.setShengcxbh(fenpq.getShengcxbh());
                }                                
			}
			if(null==bean.getXianbk() || StringUtils.isBlank(bean.getXianbk())){
				if(null==bean.getXianbk()){
					bean.setXianbk("");
				}
				result = resultMessage(index,"线边库："+bean.getXianbk()+" 必填并且长度必须为3位");
				wronginfo=wronginfo.append(result);
			}else if(!bean.getXianbk().matches("^[A-Za-z0-9]{3}$")){
				bean.setXianbk("");
				result = resultMessage(index,"线边库：必须为3位只能由数字、字母组成");
				wronginfo=wronginfo.append(result);
			}else{
				Lingjck lingjck=new Lingjck();
				lingjck.setUsercenter(bean.getUsercenter());
				lingjck.setLingjbh(bean.getLingjbh());
				lingjck.setCangkbh(bean.getXianbk());//仓库是循环的起点（线边库）
				if(!DBUtil.checkCount(baseDao,"ts_ckx.getCheckCountLingjCK", lingjck)){
					result = resultMessage(index,"零件["+bean.getLingjbh()+"]的循环起点仓库不存在");
   				    wronginfo=wronginfo.append(result);
				}
				
				Cangk cangk=new Cangk();
				cangk.setUsercenter(bean.getUsercenter());
				cangk.setCangkbh(bean.getXianbk());
				cangk = (Cangk) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCangk_Inner", cangk);
				if (cangk == null) {
					result = resultMessage(index,"不存在此仓库编号："+bean.getXianbk()+"的数据或数据已失效！");
   				    wronginfo=wronginfo.append(result);
				}else{
					bean.setQidlx(cangk.getCangklx());					
					if ("2".equals(cangk.getCangklx()) || "4".equals(cangk.getCangklx())) {
						result = resultMessage(index,"内部物流路径不能选择不合格品库");
	   				    wronginfo=wronginfo.append(result);
					}
				}
			}
			if(null==bean.getMos() || StringUtils.isBlank(bean.getMos())){
				if(null==bean.getMos()){
					bean.setMos("");
				}
				result = resultMessage(index,"上线模式："+bean.getMos()+" 必填");
				wronginfo=wronginfo.append(result);
			}else{
				Xitcsdy xitcsdy=new Xitcsdy();
				xitcsdy.setZidlx("SXMS");
				xitcsdy.setZidbm(bean.getMos());
				Object obj=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryXitcsdy", xitcsdy);
				if (obj == null) {
					result = resultMessage(index,"上线模式["+bean.getMos()+"]在系统参数定义表中未维护！");
   				    wronginfo=wronginfo.append(result);
				}
			}
			if(null==bean.getJianglms() || StringUtils.isBlank(bean.getJianglms())){
				if(null==bean.getJianglms()){
					bean.setJianglms("");
				}
			}else if(null != bean.getJianglms()&& !"".equals(bean.getJianglms())){
				Xitcsdy xitcsdy=new Xitcsdy();
				xitcsdy.setZidlx("SXMS");
				xitcsdy.setZidbm(bean.getJianglms());
				Object obj=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryXitcsdy", xitcsdy);
				if (obj == null) {
					result = resultMessage(index,"上线将来模式["+bean.getJianglms()+"]在系统参数定义表中未维护！");
   				    wronginfo=wronginfo.append(result);
				}else if(bean.getMos().equals(bean.getJianglms())){
					result = resultMessage(index,"上线模式与上线将来模式不能相同!");
					wronginfo=wronginfo.append(result);
				}
			}
			if(null==bean.getJianglmssxsj() || StringUtils.isBlank(bean.getJianglmssxsj())){
				if(null==bean.getJianglmssxsj()){
					bean.setJianglmssxsj("");
					if(null != bean.getJianglms()&& !"".equals(bean.getJianglms())){
						result = resultMessage(index,"上线将来模式和它对应的生效时间必须同时存在！");
						wronginfo=wronginfo.append(result);
					}
				}								
			}else if(!bean.getJianglmssxsj().matches("^[0-9]{4}[-][0-9]{2}[-][0-9]{2}$")){
				bean.setJianglmssxsj("");
				result = resultMessage(index,"上线生效时间：格式必须为 YYYY-MM-DD");
				wronginfo=wronginfo.append(result);
			}else{
				if(null==bean.getJianglms() || StringUtils.isBlank(bean.getJianglms())){
					result = resultMessage(index,"上线将来模式和它对应的生效时间必须同时存在！");
					wronginfo=wronginfo.append(result);
				}
				
				shengxTime = sf.parse(bean.getJianglmssxsj()+" 23:59:59");				 
				if(shengxTime.getTime()< currdate.getTime()){
					result = resultMessage(index,"上线生效时间必须大于当前时间！");
					wronginfo=wronginfo.append(result);
				}
			}
			if(null==bean.getWms() || StringUtils.isBlank(bean.getWms())){
				if(null==bean.getWms()){
					bean.setWms("");
				}
				result = resultMessage(index,"外部模式："+bean.getWms()+" 必填");
				wronginfo=wronginfo.append(result);
			}else{
				Xitcsdy xitcsdy=new Xitcsdy();
				xitcsdy.setZidlx("WBMS");
				xitcsdy.setZidbm(bean.getWms());
				Object obj=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryXitcsdy", xitcsdy);
				if (obj == null) {
					result = resultMessage(index,"外部模式["+bean.getWms()+"]在系统参数定义表中未维护！");
   				    wronginfo=wronginfo.append(result);
				}
			}
			if(null==bean.getWjlms() || StringUtils.isBlank(bean.getWjlms())){
				if(null==bean.getWjlms()){
					bean.setWjlms("");
				}
			}else if(null != bean.getWjlms()&& !"".equals(bean.getWjlms())){
				Xitcsdy xitcsdy=new Xitcsdy();
				xitcsdy.setZidlx("WBMS");
				xitcsdy.setZidbm(bean.getWjlms());
				Object obj=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryXitcsdy", xitcsdy);
				if (obj == null) {
					result = resultMessage(index,"外部将来模式["+bean.getWjlms()+"]在系统参数定义表中未维护！");
   				    wronginfo=wronginfo.append(result);
				}else if(bean.getWms().equals(bean.getWjlms())){
					result = resultMessage(index,"外部模式与外部将来模式不能相同！");
					wronginfo=wronginfo.append(result);
				}
			}
			if(null==bean.getShengxsj() || StringUtils.isBlank(bean.getShengxsj())){
				if(null==bean.getShengxsj()){
					bean.setShengxsj("");
					if(null != bean.getWjlms()&& !"".equals(bean.getWjlms())){
						result = resultMessage(index,"外部将来模式和它对应的生效时间必须同时存在！");
						wronginfo=wronginfo.append(result);
					}
				}								
			}else if(!bean.getShengxsj().matches("^[0-9]{4}[-][0-9]{2}[-][0-9]{2}$")){
				bean.setShengxsj("");
				result = resultMessage(index,"外部生效时间：格式必须为 YYYY-MM-DD");
				wronginfo=wronginfo.append(result);
			}else{
				if(null==bean.getWjlms() || StringUtils.isBlank(bean.getWjlms())){
					result = resultMessage(index,"外部将来模式和它对应的生效时间必须同时存在！");
					wronginfo=wronginfo.append(result);
				}
				
				shengxTime = sf.parse(bean.getShengxsj()+" 23:59:59");				 
				if(shengxTime.getTime()< currdate.getTime()){
					result = resultMessage(index,"外部生效时间必须大于当前时间！");
					wronginfo=wronginfo.append(result);
				}
			}
			if(null != bean.getZhidgys()&& !"".equals(bean.getZhidgys())){
				String newchengysbh=((bean.getZhidgys()).trim()).replace(" ", "1");
			    if(!newchengysbh.matches("^[A-Za-z0-9]{10}$")){
			    	bean.setZhidgys("");
					result = resultMessage(index,"指定供应商：必须为10位只能由数字、字母、空格组成");
					wronginfo=wronginfo.append(result);
			     }else{
			    	 Lingjgys lingjgys = new Lingjgys();
					 lingjgys.setUsercenter(bean.getUsercenter());
					 lingjgys.setLingjbh(bean.getLingjbh());
					 lingjgys.setGongysbh(bean.getZhidgys());
					 lingjgys.setBiaos("1");
					 Object lingjobj=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryCountLingjgys", lingjgys);
					 if (lingjobj == null) {
						 result = resultMessage(index,"零件："+bean.getLingjbh()+"供应商："+bean.getZhidgys()+"零件供应商不存在此对应关系！");
		    			 wronginfo=wronginfo.append(result);
						}						
					}
			}
			if("R1".equals(bean.getMos()) || "CD".equals(bean.getMos())){
				if(!bean.getMos().equals(bean.getWms())){
					result = resultMessage(index,"若上线模式为CD或R1，则外部模式和上线模式必须相同！");
					wronginfo=wronginfo.append(result);
				}
				if(null != bean.getJianglms()&& !"".equals(bean.getJianglms())){
					if(!bean.getJianglmssxsj().equals(bean.getShengxsj())){
						result = resultMessage(index,"若上线模式为CD或R1，且上线将来模式不为空，则上线生效时间和外部生效时间必须相同！");
						wronginfo=wronginfo.append(result);
					}
					if(null==bean.getWjlms() || StringUtils.isBlank(bean.getWjlms())){
						result = resultMessage(index,"若上线模式为CD或R1，则上线将来模式和外部将来模式必须都存在值或都不存在！");
						wronginfo=wronginfo.append(result);
					}
				}else{
					if(null != bean.getWjlms()&& !"".equals(bean.getWjlms())){
						result = resultMessage(index,"若上线模式为CD或R1，则上线将来模式和外部将来模式必须都存在值或都不存在！");
						wronginfo=wronginfo.append(result);
					}
				}
			}
			if("R1".equals(bean.getJianglms()) || "CD".equals(bean.getJianglms())){
				if(!bean.getJianglms().equals(bean.getWjlms())){
					result = resultMessage(index,"若上线将来模式为CD或R1，则外部将来模式和上线将来模式必须相同！");
					wronginfo=wronginfo.append(result);
				}
				if(!bean.getJianglmssxsj().equals(bean.getShengxsj())){
					result = resultMessage(index,"若上线将来模式为CD或R1，且上线将来模式不为空，则上线生效时间和外部生效时间必须相同！");
					wronginfo=wronginfo.append(result);
				}
			}
			if("R1".equals(bean.getWms()) || "CD".equals(bean.getWms()) || "SY".equals(bean.getWms())){
				if(!bean.getMos().equals(bean.getWms())){
					result = resultMessage(index,"若外部模式为CD或R1或SY，则外部模式和上线模式必须相同！");
					wronginfo=wronginfo.append(result);
				}				
			}
			if("R1".equals(bean.getWjlms()) || "CD".equals(bean.getWjlms()) || "SY".equals(bean.getWjlms())){
				if(!bean.getJianglms().equals(bean.getWjlms())){
					result = resultMessage(index,"若外部将来模式为CD或R1或SY，则外部将来模式和上线将来模式必须相同！");
					wronginfo=wronginfo.append(result);
				}				
			}
			if(bean.getDinghk()!=null&&!"".equals(bean.getDinghk())){
				if(!bean.getDinghk().matches("^[A-Za-z0-9]{3}$")){
					bean.setDinghk("");
					result = resultMessage(index,"订货库：必须为3位只能由数字、字母组成");
					wronginfo=wronginfo.append(result);
				}else if(bean.getDinghk().equals(bean.getXianbk())){
					result = resultMessage(index,"当起始位置为订货库，线边库与订货库不能相同");
					wronginfo=wronginfo.append(result);
				}else{
					Lingjck lingjck=new Lingjck();
					lingjck.setUsercenter(bean.getUsercenter());
					lingjck.setLingjbh(bean.getLingjbh());
					lingjck.setCangkbh(bean.getDinghk());//仓库是线边库的起点（订货库）
					if(!DBUtil.checkCount(baseDao,"ts_ckx.getCheckCountLingjCK", lingjck)){
						result = resultMessage(index,"零件["+bean.getLingjbh()+"]的线边库起点仓库不存在");
	   				    wronginfo=wronginfo.append(result);
					}
					
					Cangk cangk=new Cangk();
					cangk.setUsercenter(bean.getUsercenter());
					cangk.setCangkbh(bean.getDinghk());
					cangk = (Cangk) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCangk_Inner", cangk);
					if (cangk == null) {
						result = resultMessage(index,"不存在此仓库编号："+bean.getDinghk()+"的数据或数据已失效！");
	   				    wronginfo=wronginfo.append(result);
					}else{
						bean.setQidlx2(cangk.getCangklx());					
						if ("2".equals(cangk.getCangklx()) || "4".equals(cangk.getCangklx())) {
							result = resultMessage(index,"内部物流路径不能选择不合格品库");
		   				    wronginfo=wronginfo.append(result);
						}
					}
				}
				if(null==bean.getMos2() || StringUtils.isBlank(bean.getMos2())){
					if(null==bean.getMos2()){
						bean.setMos2("");
					}
					result = resultMessage(index,"中间模式："+bean.getMos2()+" 必填");
					wronginfo=wronginfo.append(result);
				}else{
					Xitcsdy xitcsdy=new Xitcsdy();
					xitcsdy.setZidlx("XDMS");
					xitcsdy.setZidbm(bean.getMos2());
					Object obj=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryXitcsdy", xitcsdy);
					if (obj == null) {
						result = resultMessage(index,"中间模式["+bean.getMos2()+"]在系统参数定义表中未维护！");
	   				    wronginfo=wronginfo.append(result);
					}else if("MD".equals(bean.getMos2())){
						if(!"MD".equals(bean.getMos())){
							result = resultMessage(index,"若中间模式为MD，则起始位置必须是订货库，且上线模式为MD！");
							wronginfo=wronginfo.append(result);
						}				
					}
				}
				if(null==bean.getJianglms2() || StringUtils.isBlank(bean.getJianglms2())){
					if(null==bean.getJianglms2()){
						bean.setJianglms2("");
					}
				}else if(null != bean.getJianglms2()&& !"".equals(bean.getJianglms2())){
					Xitcsdy xitcsdy=new Xitcsdy();
					xitcsdy.setZidlx("XDMS");
					xitcsdy.setZidbm(bean.getJianglms2());
					Object obj=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryXitcsdy", xitcsdy);
					if (obj == null) {
						result = resultMessage(index,"中间将来模式["+bean.getJianglms2()+"]在系统参数定义表中未维护！");
	   				    wronginfo=wronginfo.append(result);
					}else{
						if(bean.getMos2().equals(bean.getJianglms2())){
							result = resultMessage(index,"中间模式与中间将来模式不能相同！");
							wronginfo=wronginfo.append(result);
						}
						if("MD".equals(bean.getJianglms2())){
							if(!"MD".equals(bean.getJianglms())){
								result = resultMessage(index,"若中间将来模式为MD，则起始位置必须是订货库，且上线将来模式为MD！");
								wronginfo=wronginfo.append(result);
							}				
						}
					}
				}
				if(null==bean.getJianglmssxsj2() || StringUtils.isBlank(bean.getJianglmssxsj2())){
					if(null==bean.getJianglmssxsj2()){
						bean.setJianglmssxsj2("");
						if(null != bean.getJianglms2()&& !"".equals(bean.getJianglms2())){
							result = resultMessage(index,"中间将来模式和它对应的生效时间必须同时存在！");
							wronginfo=wronginfo.append(result);
						}
					}									
				}else if(!bean.getJianglmssxsj2().matches("^[0-9]{4}[-][0-9]{2}[-][0-9]{2}$")){
					bean.setJianglmssxsj2("");
					result = resultMessage(index,"上线生效时间：格式必须为 YYYY-MM-DD");
					wronginfo=wronginfo.append(result);
				}else{
					if(null==bean.getJianglms2() || StringUtils.isBlank(bean.getJianglms2())){
						result = resultMessage(index,"中间将来模式和它对应的生效时间必须同时存在！");
						wronginfo=wronginfo.append(result);
					}
					
					shengxTime = sf.parse(bean.getJianglmssxsj2()+" 23:59:59");				 
					if(shengxTime.getTime()< currdate.getTime()){
						result = resultMessage(index,"中间生效时间必须大于当前时间！");
						wronginfo=wronginfo.append(result);
					}
				}
			}else if((null != bean.getMos2()&& !"".equals(bean.getMos2())) || (null != bean.getJianglms2()&& !"".equals(bean.getJianglms2())) || (null != bean.getJianglmssxsj2()&& !"".equals(bean.getJianglmssxsj2()))){
				result = resultMessage(index,"当起始位置为线边库时，订货库、中间模式、中间将来模式、中间生效时间必须为空！");
				wronginfo=wronginfo.append(result);
			}
			if("MD".equals(bean.getMos())){
				if(null==bean.getDinghk() || StringUtils.isBlank(bean.getDinghk()) || !"MD".equals(bean.getMos2())){
					result = resultMessage(index,"若上线模式为MD，则起始位置必须是订货库，且中间模式为MD！");
					wronginfo=wronginfo.append(result);
				}
				if(null != bean.getJianglms()&& !"".equals(bean.getJianglms())){
					if(!bean.getJianglmssxsj().equals(bean.getJianglmssxsj2())){
						result = resultMessage(index,"若上线模式为MD，且上线将来模式不为空，则上线生效时间和中间生效时间必须相同！");
						wronginfo=wronginfo.append(result);
					}
					if(null==bean.getJianglms2() || StringUtils.isBlank(bean.getJianglms2())){
						result = resultMessage(index,"若上线模式为MD，则上线将来模式和中间将来模式必须都存在值或都不存在！");
						wronginfo=wronginfo.append(result);
					}
				}else{
					if(null != bean.getJianglms2()&& !"".equals(bean.getJianglms2())){
						result = resultMessage(index,"若上线模式为MD，则上线将来模式和中间将来模式必须都存在值或都不存在！");
						wronginfo=wronginfo.append(result);
					}
				}
			}
			if("MD".equals(bean.getJianglms())){
				if(null==bean.getDinghk() || StringUtils.isBlank(bean.getDinghk()) || !"MD".equals(bean.getJianglms2())){
					result = resultMessage(index,"若上线将来模式为MD，则起始位置必须是订货库，且中间将来模式为MD！");
					wronginfo=wronginfo.append(result);
				}				
			}
			index ++;
			bean.setCreator(editor);
			bean.setCreateTime(DateTimeUtil.getAllCurrTime());
			bean.setEditor(editor);
			bean.setEditTime(DateTimeUtil.getAllCurrTime());
			//如果有错误信息将会返回
			if(!wronginfo.toString().equals("")){
				wronginfo.append("\n");
				wrongcoun=wrongcoun+1;
				wrongdatas.add(bean);
				errorBuffer.append(wronginfo);
				wronginfo= new StringBuffer("");
				//return wronginfo.toString();
			}
			
			datasnew.add(bean);
			
			if(wrongdatas.size()>0 && wrongcoun==5){
				break;
			}
		}
		
		    //如果没有错误信息，正常操作，如果有则需要返回						
			if(wrongdatas.size()<=0){
				return sqlInsertOrUpdate(datasnew);
			}else{
				return errorBuffer.toString();
			}
		
		}finally{
			//关闭数据库连接
			closeConnection();
		}
	}
	
	/**
	 * 关闭数据库连接
	 */
	private void closeConnection(){
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
  
	/**
	 * 插入数据到表
	 */
	public String sqlInsertOrUpdate(ArrayList<CkxInnerPathAndModle> datasnew){
		try {
			for (CkxInnerPathAndModle inner : datasnew) {
				//判断导入的数据是新增还是更新后再来做操作
				//产线-线边库
				CkxShengcxXianb inner1 = new CkxShengcxXianb();
				inner1.setUsercenter(inner.getUsercenter());//用户中心
				inner1.setLingjbh(inner.getLingjbh());//零件编号
				inner1.setFenpqh(inner.getFenpqh());//循环
				inner1.setShengcxbh(inner.getShengcxbh());//产线编号
				inner1.setQid(inner.getXianbk());//循环起点				
				inner1.setQidlx(inner.getQidlx());//起点类型
				inner1.setMos(inner.getMos());//模式
				inner1.setJianglms(inner.getJianglms());//将来模式
				inner1.setJianglmssxsj(inner.getJianglmssxsj());//将来模式生效时间
				inner1.setCreator(inner.getCreator());
				inner1.setCreateTime(inner.getCreateTime());
				inner1.setEditor(inner.getEditor());
				inner1.setEditTime(inner.getEditTime());
				CkxShengcxXianb ckxsx = new CkxShengcxXianb();
				ckxsx.setUsercenter(inner.getUsercenter());//用户中心
				ckxsx.setLingjbh(inner.getLingjbh());//零件编号
				ckxsx.setFenpqh(inner.getFenpqh());//循环
				//更新ckx_shengcx_xianb表
				if(null == inner.getDinghk()||"".equals(inner.getDinghk())){
					if("R1".equals(inner.getWms()) || "CD".equals(inner.getWms())){//更新上线模式
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateSXMos", inner);
						if(null==inner.getWjlms() || StringUtils.isBlank(inner.getWjlms())){
							baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateSXJLMos", inner);
						}
					}
					if("R1".equals(inner.getWjlms()) || "CD".equals(inner.getWjlms())){//更新上线将来模式
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateSXJLMos", inner);
					}					
				}else{
					if("R1".equals(inner.getWms()) || "CD".equals(inner.getWms())){//更新上线模式
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateXDMos", inner);
						if(null==inner.getWjlms() || StringUtils.isBlank(inner.getWjlms())){
							baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateXDJLMos", inner);
						}
					}
					if("R1".equals(inner.getWjlms()) || "CD".equals(inner.getWjlms())){//更新上线将来模式
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateXDJLMos", inner);
					}					
				}
				if(DBUtil.checkCount(baseDao,"carry.getCountCkxShengcxXianb",ckxsx)){//如果存在此线边库的路径1，则更新路径1					
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateCkxShengcxXianb",inner1);
				}else{//如果不存在此线边库的路径1，则添加 
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.insertCkxShengcxXianb",inner1);
				}
				//外部模式
				CkxWaibms wm = new CkxWaibms();
				wm.setUsercenter(inner.getUsercenter());//用户中心
				wm.setLingjbh(inner.getLingjbh());//零件编号
				wm.setFenpqh(inner.getFenpqh());//循环
				wm.setZhidgys(inner.getZhidgys());//指定供应商
				wm.setMos(inner.getWms());//外部模式
				wm.setJianglms(inner.getWjlms());//将来模式
				wm.setShengxsj(inner.getShengxsj());//生效时间
				wm.setCreator(inner.getCreator());
				wm.setCreateTime(inner.getCreateTime());
				wm.setEditor(inner.getEditor());
				wm.setEditTime(inner.getEditTime());
				//更新外部模式
				if(null == inner.getDinghk()||"".equals(inner.getDinghk())){
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateCkxShengcxXianbMos", inner);
				}else{
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateCkxXianbDinghMos", inner);
				}
				Object obj=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("carry.qureyCkxWaibms",wm);
				if (obj == null){//如果不存在此线边库的外部模式，则添加					
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.insertCkxWaibms",wm);
				}else{//如果存在此线边库的外部模式，则更新		
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateCkxWaibms",wm);
				}
				if(inner.getDinghk()!=null&&!"".equals(inner.getDinghk())){
					CkxXianbDingh inner2 = new CkxXianbDingh();
					inner2.setUsercenter(inner.getUsercenter());//用户中心
					inner2.setLingjbh(inner.getLingjbh());//零件编号
					inner2.setXianbck(inner.getXianbk());//线边库
					inner2.setQid(inner.getDinghk());//订货库
					inner2.setQidlx(inner.getQidlx2());//起点类型
					inner2.setMos(inner.getMos2());//模式
					inner2.setJianglms(inner.getJianglms2());//将来模式
					inner2.setJianglmssxsj(inner.getJianglmssxsj2());//将来模式生效时间
					inner2.setCreator(inner.getCreator());
					inner2.setCreateTime(inner.getCreateTime());
					inner2.setEditor(inner.getEditor());
					inner2.setEditTime(inner.getEditTime());
					obj=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("carry.getCkxXianbDingh",inner2);
					if(obj==null){//如果不存在此线边库的路径2，则添加 						
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.insertCkxXianbDingh", inner2);
					}else{//如果存在此线边库的路径2，则更新路径2
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateCkxXianbDingh", inner2);
					}
				}
			}
		} catch (DataAccessException e) {
			logger.info(e.getMessage());
			return "插入或更新失败";
		}
		return "";
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
