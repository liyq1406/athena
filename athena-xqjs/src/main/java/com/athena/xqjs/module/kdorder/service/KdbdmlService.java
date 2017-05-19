package com.athena.xqjs.module.kdorder.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.athena.authority.service.PostService;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.common.Lingj;
import com.athena.xqjs.entity.common.LingjGongys;
import com.athena.xqjs.entity.kdorder.Kdbdml;
import com.athena.xqjs.entity.kdorder.KdbdmulResult;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.LingjGongysService;
import com.athena.xqjs.module.common.service.LingjService;
import com.athena.xqjs.module.ycbj.service.YicbjService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.dao.DataAccessException;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * <p>
 * Title:比对目录
 * </p>
 * <p>
 * Description:比对目录
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 陈骏
 * @version v1.0
 * @date 2012-1-4
 */
@Component
public class KdbdmlService extends BaseService {
	@Inject
	private KdbdmulResultService kdbdmulresservice;
	@Inject
	private YicbjService yicbjservice;
	@Inject
	private PostService postservice;
	@Inject
	private LingjService lingjservice;
	@Inject
	private LingjGongysService lingjgongysservice;

	/**
	 * <p>
	 * Title:将比对结果插入到结果表
	 * </p>
	 * <p>
	 * Description:比对目录
	 * </p>
	 * <p>
	 * Copyright: Copyright (c) 2011
	 * </p>
	 * <p>
	 * Company: 软通动力
	 * </p>
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2012-1-4
	 */
	@Transactional
	public boolean insertKdbdml(Kdbdml kdbdml) throws ServiceException{
		//xss-0013156
		boolean flag = true;
		int execute = 0;
		try {	
		   execute = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kdorder.insertKdbdml", kdbdml);
		}catch (DataAccessException e) {
			throw new ServiceException("零件编号："+kdbdml.getLingjbh()+" 插入数据库错误,请检查导入文件!"); 
		}
		if (execute < 0) {
			flag = false;
		}
		return flag;
	}

	/**
	 * <p>
	 * Title:清理结果表
	 * </p>
	 * <p>
	 * Description:比对目录
	 * </p>
	 * <p>
	 * Copyright: Copyright (c) 2011
	 * </p>
	 * <p>
	 * Company: 软通动力
	 * </p>
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2012-1-4
	 */
	public void deleteKdbdml() {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kdorder.deleteKdbdml");
	}

	/**
	 * <p>
	 * Title:查询结果表
	 * </p>
	 * <p>
	 * Description:比对目录
	 * </p>
	 * <p>
	 * Copyright: Copyright (c) 2011
	 * </p>
	 * <p>
	 * Company: 软通动力
	 * </p>
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2012-1-4
	 */
	public List<Kdbdml> queryKdbdml() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kdorder.queryKdbdml");
	}

	/**
	 * <p>
	 * Title:文件导入
	 * </p>
	 * <p>
	 * Description:比对目录
	 * </p>
	 * <p>
	 * Copyright: Copyright (c) 2011
	 * </p>
	 * <p>
	 * Company: 软通动力
	 * </p>
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2012-1-4
	 */
	public String readMulu(String lujing) throws BiffException, IOException, ParseException {
		String message = "";// 导入后消息输出
		boolean lock = true;// 计算锁
		InputStream is = new FileInputStream(new File(lujing));// 文件输入流
		jxl.Workbook rwb = Workbook.getWorkbook(is);// 打开指定的excel文件
		Sheet st = rwb.getSheet(Const.MULUBIDUI_SHEETMING);// 读取指定的sheet
		int i = st.getRows();// 得到行
		int j = st.getColumns();// 得到列
		List list = new ArrayList();
		for (int x = 1; x < i; x++) {// 做行列循环读取
			Kdbdml kl = new Kdbdml();
			for (int y = 0; y < j; y++) {
				switch (y) {
				case Const.SHEETCELL1:
					kl.setUsercenter(st.getCell(y, x).getContents().trim());
					break;
				case Const.SHEETCELL2:
					kl.setLingjbh(st.getCell(y, x).getContents().trim());
					break;
				case Const.SHEETCELL3:
					kl.setUclx(st.getCell(y, x).getContents().trim());
					break;
				case Const.SHEETCELL4:
					BigDecimal bd = BigDecimal.ZERO;
					if(st.getCell(y, x).getContents().trim()!=null && !st.getCell(y, x).getContents().trim().equals("")){
						bd = new BigDecimal(st.getCell(y, x).getContents().trim());
					}
					
					kl.setUcrl(bd);
					break;
				case Const.SHEETCELL5:
					kl.setUalx(st.getCell(y, x).getContents().trim());
					break;
				case Const.SHEETCELL6:
					BigDecimal bd2 = BigDecimal.ZERO;
					if(st.getCell(y, x).getContents().trim()!=null && !st.getCell(y, x).getContents().trim().equals("")){
						bd2 = new BigDecimal(st.getCell(y, x).getContents().trim());
					}
					
					kl.setUarl(bd2);
					break;
				case Const.SHEETCELL7:
					kl.setXiehd(st.getCell(y, x).getContents().trim());
					break; 
				}
			}
			list.add(kl);// 读取到的数据保存到list
		}
		for (int z = 0; z < list.size(); z++) {// 循环list将数据插入到表
			Kdbdml kl = (Kdbdml) list.get(z);
			boolean flag = this.insertKdbdml(kl);
			if (flag) {
				message = Const.MULUBIDUI_INSERTSUCCESS;
			}else {
				// 检查到数据异常则向异常报警表输出数据
				/*
				lock = false;
				LoginUser lu = AuthorityUtils.getSecurityUser();
				Yicbj yicbj = new Yicbj();
				yicbj.setLingjbh(kl.getLingjbh());
				yicbj.setCuowlx(Const.YICHANG_LX5);
				yicbj.setCuowxxxx("插入编号为" + kl.getLingjbh() + "的零件时发生错误，请检查导入文件。");
				yicbj.setJihyz(postservice.getQueryPostCode("yicbj"));// 计划员组信息目前没有方法得到，等周毅提供方法
				yicbj.setJihydm(lu.getUsername());
				yicbj.setJismk(Const.JISUANMOKUAI_MULUBIDUI);
				*/
				message = "插入编号为" + kl.getLingjbh() + "的零件时发生错误，请检查导入文件。";
			}
		}
		return message;
	}

	/**
	 * <p>
	 * Title:字符串比较
	 * </p>
	 * <p>
	 * Description:比对目录
	 * </p>
	 * <p>
	 * Copyright: Copyright (c) 2011
	 * </p>
	 * <p>
	 * Company: 软通动力
	 * </p>
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2012-1-4
	 */
	public boolean stringCompare(String compare, String compare1) {
		boolean flag = true;
		if (null == compare || null == compare1) {
			flag = false;
		} else {
			flag = compare.equals(compare1);
		}
		return flag;
	}

	// public boolean bigDecimalCompare(BigDecimal compare,BigDecimal compare1){
	// boolean flag = true;
	// if(null == compare||null == compare1){
	// flag = false;
	// }else{
	// flag=compare.equals(compare1);
	// }
	// return flag;
	// }
	/**
	 * <p>
	 * Title:参考系已有数据和客户导入数据比较
	 * </p>
	 * <p>
	 * Description:比对目录
	 * </p>
	 * <p>
	 * Copyright: Copyright (c) 2011
	 * </p>
	 * <p>
	 * Company: 软通动力
	 * </p>
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2012-1-4
	 */
	public String MuluCompared() {
		this.kdbdmulresservice.deleteKdbdmlResult();
		Lingj lingj = new Lingj();
		//LingjGongys lingjgongys = new LingjGongys();
		ArrayList muluList = (ArrayList) this.queryKdbdml();
		List<KdbdmulResult> list = new ArrayList();// 查询用户导入的数据
		
		String res = "";
		if (muluList.size() > 0) {
			for (int i = 0; i < muluList.size(); i++) {// 循环数据并和参考系的相关数据比较
				StringBuffer chayi = new StringBuffer();//差异
				
				Kdbdml kdbdml = (Kdbdml) muluList.get(i);
				//0013186 			
				lingj = this.lingjservice.select_all(kdbdml.getUsercenter(), kdbdml.getLingjbh());
				ArrayList<LingjGongys> glList = (ArrayList)this.lingjgongysservice.selectLingjGongys(kdbdml.getUsercenter(), kdbdml.getLingjbh(),kdbdml.getZhizlx());
				if (null == lingj || glList.size()==0) {// 如果参考系数据为空
					KdbdmulResult kds = new KdbdmulResult();
					kds.setUsercenter(kdbdml.getUsercenter());
					kds.setLingjbh(kdbdml.getLingjbh());
					kds.setUclx(kdbdml.getUclx());
					kds.setUcrl(kdbdml.getUcrl());
					kds.setUalx(kdbdml.getUalx());
					kds.setUarl(kdbdml.getUarl()); 
					kds.setXiehd(kdbdml.getXiehd());
										
					////xss -v4_014
					// 参考系中不存在该零件，则插入空，
					kds.setCkxualx("");
					kds.setCkxuarl(BigDecimal.ZERO);
					kds.setCkxuclx("");
					kds.setCkxucrl(BigDecimal.ZERO);  
					kds.setCkxXiehd("");	 
					
					String cy = " 参考系为空  ";
					chayi.append(cy) ;	
					
					kds.setChayi(chayi.toString());
					
					list.add(kds);
				} else {// 如果参考系数据不为空，但是两边数据不相等
					for(LingjGongys lingjgongys :glList){
						
						if( kdbdml.getUalx()==null && lingjgongys.getUabzlx()==null ){  
							
						}else{
							if (!this.stringCompare(kdbdml.getUalx(), lingjgongys.getUabzlx())){
								String cy = " UA包装类型 ";
								chayi.append(cy) ;
							}
						}
						
						if( kdbdml.getUarl()==null && lingjgongys.getUcrl()==null ){ 
							
						}else{
							if(!this.stringCompare(kdbdml.getUarl().toString(),
									lingjgongys.getUaucgs().multiply(lingjgongys.getUcrl()).toString())
							){
								String cy = " UA包装容量 ";
								chayi.append(cy) ;
							} 
						}
						
						
						if( kdbdml.getUclx()==null && lingjgongys.getUcbzlx()==null ){ 
							
						}else{
							if (!this.stringCompare(kdbdml.getUclx(), lingjgongys.getUcbzlx())
							){
								String cy = " UC包装类型 ";
								chayi.append(cy) ;	
							}
						}
						
						
						if( kdbdml.getUcrl()==null && lingjgongys.getUcrl()==null ){
								
						}else{
							if(!this.stringCompare(kdbdml.getUcrl().toString(),lingjgongys.getUcrl().toString())
							){
								String cy = " UC包装容量 ";
								chayi.append(cy) ;	
							} 
						}
						
						
						if(kdbdml.getXiehd()==null && lingj.getAnjmlxhd()==null ){ 
							
						}else{
							if(!this.stringCompare(kdbdml.getXiehd(), lingj.getAnjmlxhd())
							) {
								String cy = " 按件目录卸货点 ";
								chayi.append(cy) ;	
							} 
						}
							 
						
																		
						KdbdmulResult kds = new KdbdmulResult();
						kds.setUsercenter(kdbdml.getUsercenter());
						kds.setLingjbh(kdbdml.getLingjbh());  
						kds.setUclx(kdbdml.getUclx());
						kds.setUcrl(kdbdml.getUcrl());
						kds.setUalx(kdbdml.getUalx());
						kds.setUarl(kdbdml.getUarl());    
						kds.setCkxualx(lingjgongys.getUabzlx());
						kds.setCkxuarl(lingjgongys.getUaucgs().multiply(lingjgongys.getUcrl()));
						kds.setCkxuclx(lingjgongys.getUcbzlx());
						kds.setCkxucrl(lingjgongys.getUcrl());  
						kds.setCkxXiehd(lingj.getAnjmlxhd());
						
						kds.setXiehd(kdbdml.getXiehd());
						 						
						kds.setChayi(chayi.toString()); 
						
						list.add(kds);
						}
					}
				}
				res = "对比完成！";
			} else {
				res = "未导入数据！";
			}
		for (KdbdmulResult kdbdmulresult : list) {
			this.kdbdmulresservice.insertRes(kdbdmulresult);
		}
		return res;
	}
}
