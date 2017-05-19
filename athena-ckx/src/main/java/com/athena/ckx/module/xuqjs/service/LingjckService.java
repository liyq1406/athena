package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.cangk.Cangk;
import com.athena.ckx.entity.cangk.Kuw;
import com.athena.ckx.entity.cangk.Zick;
import com.athena.ckx.entity.xuqjs.Baoz;
import com.athena.ckx.entity.xuqjs.Biangjlb;
import com.athena.ckx.entity.xuqjs.CkxLingj;
import com.athena.ckx.entity.xuqjs.Lingjck;
import com.athena.ckx.entity.xuqjs.Usercenter;
import com.athena.ckx.module.carry.service.CkxWulljService;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.ckx.util.GetPostOnly;
import com.athena.ckx.util.KanbxhgmCkxService;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
import com.toft.mvc.dispacher.ActionContext;


/**
 * 零件仓库Service
 * @author denggq
 * 2012-4-13
 */
@Component
public class LingjckService  extends BaseService<Lingjck>{

	@Inject
	private KanbxhgmCkxService kanbxhgmService;
	@Inject
	private CkxWulljService ckxWulljService;
	/**
	 * 获取命名空间
	 * @author denggq
	 * @time 2012-4-13
	 * */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	
	/**
	 * 分页查询
	 * @param bean
	 * @author denggq
	 * @date 2012-4-13
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public Map<String, Object> select(Lingjck bean) throws ServiceException {
		Map map = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
		
		String key = GetPostOnly.getPostOnly(map);
		if("WULGYY".equals(key)){
			String value = (String) map.get(key);
			Cangk cangk =new Cangk();
			cangk.setUsercenter(bean.getUsercenter());
			cangk.setWulgyyz(value);
			cangk.setCangkbh(bean.getCangkbh());
			List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCangk",cangk);
			if(0 == list.size()){
				throw new ServiceException(GetMessageByKey.getMessage("nocangkuqx"));
			}else{
				bean.setWulgyyz(value);
			}
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryLingjck",bean,bean);
	}

	

	/**
	 * 单条数据保存
	 * @param bean
	 * @param operant 1-新增  2-修改
	 * @author denggq
	 * @date 2012-4-13
	 */
	@SuppressWarnings("rawtypes")
	@Transactional
	public String save(Lingjck bean,Integer operant, String userId) throws ServiceException{
		
		Map map = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
		
		String key = GetPostOnly.getPostOnly(map);
		String zickNew = bean.getZickbh();
		Lingjck lingjckOld = (Lingjck) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getLingjck", bean);
		String zickOld = lingjckOld == null ? null :lingjckOld.getZickbh(); 
		//当子仓库为 S的时候  校验定置库位的存在性   只在执行层 校验
		if("ZXCPOA".equals(key)&&"S".equals(bean.getZickbh().subSequence(2, 3))&& null==bean.getDingzkw()){
			throw new ServiceException("定置库位不能为空");
		}
		
		//零件编号是否存在
//		String sql1 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_lingj where usercenter = '"+bean.getUsercenter()+"' and lingjbh = '"+bean.getLingjbh()+"' and biaos = '1'";
//		DBUtilRemove.checkBH(sql1, GetMessageByKey.getMessage("lingjianbianhao")+bean.getLingjbh()+GetMessageByKey.getMessage("notexist"));
		CkxLingj lingj = new CkxLingj();
		lingj.setUsercenter(bean.getUsercenter());
		lingj.setLingjbh(bean.getLingjbh());
		lingj.setBiaos("1");
		DBUtil.checkCount(baseDao, "ts_ckx.getCountLingj", lingj, GetMessageByKey.getMessage("lingjianbianhao")+bean.getLingjbh()+ GetMessageByKey.getMessage("notexist"));
		
		//仓库编号是否存在
//		String sql2 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_cangk where usercenter = '"+bean.getUsercenter()+"' and cangkbh = '"+bean.getCangkbh()+"' and biaos = '1'";
//		DBUtilRemove.checkBH(sql2, GetMessageByKey.getMessage("cangkubianhao")+bean.getCangkbh()+GetMessageByKey.getMessage("notexist"));
		Cangk cangk = new Cangk();
		cangk.setUsercenter(bean.getUsercenter());
		cangk.setCangkbh(bean.getCangkbh());
		if("WULGYY".equals(key)){
			cangk.setWulgyyz(map.get(key).toString());
		}
		cangk.setBiaos("1");
		DBUtil.checkCount(baseDao, "ts_ckx.getCountCangk", cangk, "不存在仓库："+cangk.getCangkbh()+",或没有该数据权限或数据已失效");
	
		//子仓库编号是否存在
		if(null != bean.getZickbh()){
//			String sql3 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_zick where usercenter = '"+bean.getUsercenter()+"' and cangkbh = '"+bean.getCangkbh()+"' and zickbh ='"+bean.getZickbh()+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql3, GetMessageByKey.getMessage("zicangkubianhao")+bean.getZickbh()+GetMessageByKey.getMessage("notexist"));
			Zick zick=new Zick();
			zick.setCangkbh(bean.getCangkbh());
			zick.setZickbh(bean.getZickbh());
			zick.setUsercenter(bean.getUsercenter());
			zick.setBiaos("1");
			DBUtil.checkCount(baseDao,"ts_ckx.getCountZick", zick,GetMessageByKey.getMessage("zicangkubianhao")+bean.getZickbh()+GetMessageByKey.getMessage("notexist"));
			//获得卸货站台编号
			Zick z = new Zick();
			z.setUsercenter(bean.getUsercenter());
			z.setCangkbh(bean.getCangkbh());
			z.setZickbh(bean.getZickbh());
			String xiehztbh = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getXiehztbh", z);
			//0007120 对比卸货站台是否发生变化
			bean.setYuanxiehztbh(bean.getXiehztbh());
			if(null != xiehztbh && !xiehztbh.equals(bean.getXiehztbh())){
				bean.setXiehztbh(xiehztbh);
			}
			
		}
		
		//单取库位编号是否存在
		if(null != bean.getDanqkw() && null != bean.getZickbh()){
//			String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_kuw where usercenter = '"+bean.getUsercenter()+"' and cangkbh = '"+bean.getCangkbh()+"' and zickbh ='"+bean.getDanqkbh()+"'and kuwbh = '"+bean.getDanqkw()+"'";
//			DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("danjukubh")+bean.getDanqkw()+GetMessageByKey.getMessage("notexist"));
			Kuw kuw = new Kuw();
			kuw.setUsercenter(bean.getUsercenter());
			kuw.setCangkbh(bean.getCangkbh());
			kuw.setZickbh(bean.getDanqkbh());
			kuw.setKuwbh(bean.getDanqkw());
			DBUtil.checkCount(baseDao, "ts_ckx.getCountKuw", kuw, GetMessageByKey.getMessage("danjukubh")+bean.getDanqkw()+GetMessageByKey.getMessage("notexist"));
		}
		
		//备用子仓库编号是否存在
		if(null != bean.getBeiykbh() ){
//			String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_zick where usercenter = '"+bean.getUsercenter()+"' and cangkbh = '"+bean.getCangkbh()+"' and zickbh = '"+bean.getBeiykbh()+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("beiyongzckbh")+bean.getBeiykbh()+GetMessageByKey.getMessage("notexist"));
			Zick zick=new Zick();
			zick.setCangkbh(bean.getCangkbh());
			zick.setZickbh(bean.getBeiykbh());
			zick.setUsercenter(bean.getUsercenter());
			zick.setBiaos("1");
			DBUtil.checkCount(baseDao,"ts_ckx.getCountZick", zick,GetMessageByKey.getMessage("beiyongzckbh")+bean.getBeiykbh()+GetMessageByKey.getMessage("notexist"));
		
		}
		
		//子仓库与备用子仓库不能相同
		if(null != bean.getBeiykbh() && null != bean.getZickbh() && bean.getBeiykbh().equals(bean.getZickbh())){
			throw new ServiceException( GetMessageByKey.getMessage("zckbutongbyzck"));
		}
		
		//定置库位是否存在
		if("ZXCPOA".equals(key)&&null != bean.getDingzkw() && null != bean.getZickbh()&&!"D".equals(bean.getZickbh().substring(2, 3))){
//			String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_kuw where usercenter = '"+bean.getUsercenter()+"' and cangkbh = '"+bean.getCangkbh()+"' and zickbh ='"+bean.getZickbh()+"'and kuwbh = '"+bean.getDingzkw()+"'";
//			DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("dingzhikuwei")+bean.getDingzkw()+GetMessageByKey.getMessage("notexist"));
			Kuw kuw = new Kuw();
			kuw.setUsercenter(bean.getUsercenter());
			kuw.setCangkbh(bean.getCangkbh());
			kuw.setZickbh(bean.getZickbh());
			kuw.setKuwbh(bean.getDingzkw());
			DBUtil.checkCount(baseDao, "ts_ckx.getCountKuw", kuw, GetMessageByKey.getMessage("dingzhikuwei")+bean.getDingzkw()+GetMessageByKey.getMessage("notexist"));
		
		}
		
		//一个定置库位只对应一种零件编号
		if(null != bean.getDingzkw()){
			Lingjck lingjck1 = new Lingjck();
			lingjck1.setUsercenter(bean.getUsercenter());
			lingjck1.setDingzkw(bean.getDingzkw());
			lingjck1.setZickbh(bean.getZickbh());
			lingjck1.setCangkbh(bean.getCangkbh());
			Lingjck lingjck= (Lingjck)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryLingjck", lingjck1);
			if(null!=lingjck&&!bean.getLingjbh().equals(lingjck.getLingjbh())&&!"D".equals(bean.getZickbh().substring(2, 3))){
				//String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_lingjck where usercenter = '"+bean.getUsercenter()+"' and cangkbh = '"+bean.getCangkbh()+"'and dingzkw = '"+bean.getDingzkw()+"'";
				throw new ServiceException ("定置库位【"+bean.getDingzkw()+"】已被零件编号【"+lingjck.getLingjbh()+"】占用");
			}
		}
		
		//一个单取库位只对应一种零件编号
		if(null != bean.getDanqkw()){
			Lingjck lingjck = new Lingjck();
			lingjck.setUsercenter(bean.getUsercenter());
			lingjck.setDanqkw(bean.getDanqkw());
			lingjck.setCangkbh(bean.getCangkbh());
			Lingjck lingjck1= (Lingjck)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryLingjck", lingjck);
			if(null!=lingjck1&&!bean.getLingjbh().equals(lingjck1.getLingjbh())){
				//String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_lingjck where usercenter = '"+bean.getUsercenter()+"' and cangkbh = '"+bean.getCangkbh()+"'and dingzkw = '"+bean.getDingzkw()+"'";
				throw new ServiceException ("单取库位【"+bean.getDanqkw()+"】已被零件编号【"+lingjck1.getLingjbh()+"】占用");
			}
		}
		
		//9339  设置单取库的上下限时，限制下限必须小于等于上限
		if(null!=bean.getZuixxx()||null!=bean.getZuidsx()){
			if(null==bean.getZuixxx()&& null!=bean.getZuidsx()){
				throw new ServiceException ("请输入最小下限");
			}
			if(null!=bean.getZuixxx()&& null==bean.getZuidsx()){
				throw new ServiceException ("请输入最大上限");
			}
			if(0==bean.getZuixxx()&& 0!=bean.getZuidsx()){
				throw new ServiceException ("请输入最小下限");
			}
			if(0!=bean.getZuixxx()&& 0==bean.getZuidsx()){
				throw new ServiceException ("请输入最大上限");
			}
			if(bean.getZuixxx()>bean.getZuidsx()){
				throw new ServiceException ("最小下限必须小于和等于最大上限");
			}
		}
		
		bean.setEditor(userId);//修改人
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());//修改时间
		
		//单取库位编号存在则单取库编号必须存在    20150312 wangliang

		if(bean.getDanqkbh()!=null && bean.getDanqkw()==null){
			throw new ServiceException("单取库位和单取库编号应该同时存在或者同时不存在！");
		}
		if(bean.getDanqkbh()==null && bean.getDanqkw()!=null){
			throw new ServiceException("单取库位和单取库编号应该同时存在或者同时不存在！");
		}
		
		if (1 == operant){//增加
			//根据默认值设置库存天数 Bug 002465
			Cangk c = new Cangk();
			c.setUsercenter(bean.getUsercenter());
			c.setCangkbh(bean.getCangkbh());
			c = (Cangk) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCangk",c);
			if(null != c && null == bean.getAnqkcts()){
				bean.setAnqkcts(c.getAnqkctsmrz());//安全库存天数默认值
			}
			if(null != c &&  null == bean.getZuidkcts()){
				bean.setZuidkcts(c.getZuidkctsmrz());//最大库存天数默认值
			}
			bean.setCreator(userId);//增加人
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());//增加时间
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertLingjck", bean);//增加数据库
		}else if(2 == operant){//修改
			//执行层“取消单取库”  0010894
			if(!getzbcZxc()){
				Lingjck oldlck = (Lingjck) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getLingjck", bean);
				if(oldlck.getDanqkw() != null && !oldlck.getDanqkw().equals(bean.getDanqkw())) {
					int sfcz= (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.querySfcz", oldlck);
					
					if(sfcz!=0){
						int kucsl= (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryKcsl", oldlck);
						if(kucsl >0){
							throw new ServiceException("该单取库下有库存，不能取消");
						}else{
							//更新该单取库下要货令的子仓库为原子仓库。
							oldlck.setEditor(userId);//修改人
							oldlck.setEdit_time(DateTimeUtil.getAllCurrTime());//修改时间
							baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateYaohlzck", oldlck);
						}
					}else{
						//更新该单取库下要货令的子仓库为原子仓库。
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateYaohlzck", oldlck);
					}
				}
			}
			
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateLingjck", bean);//修改数据库
		}
		if(null ==  bean.getYuanckbh() && 2 == operant){
//			checkXiehztbhChange(bean.getUsercenter(),bean.getYuanxiehztbh(),bean.getXiehztbh(), bean.getCangkbh(), bean.getZickbh(), bean.getLingjbh(), userId);
			if(zickOld != null && !zickNew.equals(zickOld)){
				if(getzbcZxc()){
					biang(bean.getUsercenter(), bean.getLingjbh(), bean.getCangkbh(), bean.getCangkbh(),userId);//变更仓库
				}
			}
		}
		return "success";
	}
	
	@SuppressWarnings("rawtypes")
	@Transactional
	public String replace(Lingjck bean,String userId){
		Map map = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
		
		String key = GetPostOnly.getPostOnly(map);
		Cangk cangk = new Cangk();
		cangk.setUsercenter(bean.getUsercenter());
		cangk.setCangkbh(bean.getCangkbh());
		if("WULGYY".equals(key)){
			cangk.setWulgyyz(map.get(key).toString());
		}
		cangk.setBiaos("1");
		DBUtil.checkCount(baseDao, "ts_ckx.getCountCangk", cangk, "不存在仓库："+cangk.getCangkbh()+",或没有该数据权限或数据已失效");
	
		//cangkbh:新的仓库编号，yuanckbh:原仓库编号
		Zick z = new Zick();
		z.setUsercenter(bean.getUsercenter());
		z.setCangkbh(bean.getCangkbh());
		z.setZickbh(bean.getZickbh());
		z.setBiaos("1");
		z = (Zick) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryZick", z);
		if(null == z){
			throw new ServiceException("不存在用户中心："+bean.getUsercenter()+
					",仓库编号："+bean.getCangkbh()+",子仓库："+bean.getZickbh()+"的数据或数据已失效！");
		}
		Lingjck lingjck = (Lingjck) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getLingjck", bean);
		Lingjck YuanCangk = new Lingjck();//根据原仓库获取原仓库的对象、
		YuanCangk.setUsercenter(bean.getUsercenter());
		YuanCangk.setLingjbh(bean.getLingjbh());
		YuanCangk.setCangkbh(bean.getYuanckbh());
		YuanCangk = (Lingjck) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getLingjck", YuanCangk);
		if(null != lingjck ){
			if(!bean.getZickbh().equals(lingjck.getZickbh())){
				throw new ServiceException("对不起，零件仓库关系已存在，但子仓库不为"+bean.getZickbh());
			}
			//合并仓库的
			bean.setDingdbdzl(getZL(YuanCangk.getDingdbdzl())+getZL(lingjck.getDingdbdzl()));
			bean.setDingdzzzl(getZL(YuanCangk.getDingdzzzl())+getZL(lingjck.getDingdzzzl()));
			bean.setYijfzl(getZL(YuanCangk.getYijfzl())+getZL(lingjck.getYijfzl()));
			bean.setXittzz(getZL(YuanCangk.getXittzz())+getZL(lingjck.getXittzz()));
			bean.setEditor(userId);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateLingjckYuanck", bean);//修改数据库
		}else {
			bean.setCreator(userId);
			bean.setEditor(userId);
			bean.setAnqkcts(YuanCangk.getAnqkcts());
			bean.setAnqkcsl(YuanCangk.getAnqkcsl());
			bean.setZuidkcts(YuanCangk.getZuidkcts());
			bean.setZuidkcsl(YuanCangk.getZuidkcsl());		
			bean.setZuixqdl(YuanCangk.getZuixqdl());
			bean.setDingdbdzl(YuanCangk.getDingdbdzl());
			bean.setDingdzzzl(YuanCangk.getDingdzzzl());
			bean.setYijfzl(YuanCangk.getYijfzl());
			bean.setXittzz(YuanCangk.getXittzz());
			bean.setJistzz(YuanCangk.getJistzz());
			bean.setShifxlh(YuanCangk.getShifxlh());
			bean.setFifo(YuanCangk.getFifo());
			bean.setZidfhbz(YuanCangk.getZidfhbz());
		
			//新增时将卸货站台写入
			bean.setXiehztbh(z.getZhantbh());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertLingjck", bean);//增加数据库
		}
		//清空原仓库的
		//YuanCangk.setDingdbdzl(0.0);
		//YuanCangk.setDingdzzzl(0.0);
		//YuanCangk.setYijfzl(0.0);
		//YuanCangk.setXittzz(0.0);
		//YuanCangk.setEditor(userId);
		//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateLingjck", YuanCangk);//修改数据库
		biang(bean.getUsercenter(), bean.getLingjbh(), bean.getYuanckbh(), bean.getCangkbh(),userId);//变更仓库
		if(ckxWulljService.checkWulljXhdCk(bean.getUsercenter(), bean.getLingjbh(), bean.getYuanckbh(), "C")){
			kanbxhgmService.getKanbxhgm(bean.getUsercenter(), bean.getLingjbh(), bean.getYuanckbh());	
		}
		return "替换成功！请维护相关物流路径";
	}
	/**
	 * 避免为null相加报错
	 * @param d
	 * @return
	 */
	private Double getZL(Double d){
		return d == null?0.0:d;
	}
	/**
	 * 单条记录删除
	 * @param bean
	 * @return String
	 * @author qizhongtao
	 * @time 2012-4-13
	 * @update lc 2016.10.24
	 */
	@Transactional
	public String doDelete(ArrayList<Lingjck> lingjck, String editor, String edit_time) throws ServiceException{
		Lingjck bean = new Lingjck();
		for (int i = 0; i < lingjck.size(); i++){
			bean.setUsercenter(lingjck.get(i).getUsercenter());//用户中心
			bean.setLingjbh(lingjck.get(i).getLingjbh());//零件编号
			bean.setCangkbh(lingjck.get(i).getCangkbh());//仓库编号
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteLingjck",bean);//删除数据库
			Biangjlb b = new Biangjlb();
			b.setUsercenter(lingjck.get(i).getUsercenter());//用户中心
			b.setLingjbh(lingjck.get(i).getLingjbh());//零件编号
			b.setXianbh(lingjck.get(i).getCangkbh());//仓库编号
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteBiangjlb", b);//删除变更
		}		
		return "success";
	}
	
	
	/**
	 * @description记录零件仓库变更
	 * @author denggq
	 * @date 2012-4-28
	 */
	private void biang(String usercenter,String lingjbh, String yuanbh,String xianbh,String userId) throws ServiceException {
		if(null != yuanbh ){
			Biangjlb bean = new Biangjlb();
			bean.setUsercenter(usercenter);//用户中心
			bean.setLingjbh(lingjbh);//零件编号
			bean.setBianglx("1");//变更类型:仓库
			bean.setYuanbh(yuanbh);//原编号
			bean.setXianbh(xianbh);//现编号
			bean.setCreator(userId);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userId);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			bean.setShifsy(yuanbh.equals(xianbh)?"1":"0");
//			List<Biangjlb> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryBiangjlb", bean);
//			if(0 == list.size()){//若表中无此数据
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertBiangjlb", bean);
//			}else{//有此数据
//				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateBiangjlb", bean);
//			}
		}
	}
	
	
	/**
	 * 获得多个CMJ
	 * @param bean
	 * @return List
	 * @author denggq
	 * @date 2012-8-3
	 */
	@Transactional
	@SuppressWarnings("rawtypes")
	public List list(Lingjck bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryLingjck",bean);
	}
	/**0007120
	 * 判断是准备层还是执行层，准备层返回true，执行层返回false
	 * @return
	 */
	public boolean getzbcZxc(){
		HttpServletRequest request = ActionContext.getActionContext().getRequest();
		HttpSession session = request.getSession();
		String zbczxc = (String) session.getAttribute("zbcZxc");
		boolean flag = false;
		if("ZBC".equals(zbczxc)){
			flag = true;
		}
		return flag;
	}
	/**
	 * 0007120
	 * 根据卸货站台是否改变，是否记录变更记录
	 * @param usercenter
	 * @param yuanxiehzt
	 * @param xiehzt
	 * @param cangk
	 * @param zick
	 * @param lingjbh
	 * @param userId
	 */
	@SuppressWarnings("unchecked")
	public void checkXiehztbhChange(String usercenter,String  yuanxiehzt,String xiehzt,String cangk,String zick,String lingjbh,String userId){
		if(!StringUtils.defaultIfEmpty(yuanxiehzt, "").equals(xiehzt)){
			if( null == lingjbh){
				updateLingjckByXiehzt(usercenter, cangk, zick, xiehzt, userId);
				//查出所有该仓库子仓库对应的零件-仓库,并记录变更记录
				Lingjck bean = new Lingjck();
				bean.setUsercenter(usercenter);
				bean.setCangkbh(cangk);
				bean.setZickbh(zick);
				List<Lingjck> list = list(bean);
				for (Lingjck lingjck : list) {
					if(getzbcZxc()){
						biang(usercenter, lingjck.getLingjbh(), cangk, cangk,userId);//变更仓库
					}
				}
			}else{
				if(getzbcZxc()){
					biang(usercenter, lingjbh, cangk, cangk,userId);//变更仓库
				}
			}
		}
	}
	/**
	 * 0007120
	 * 如果是仓库对应的子仓库的卸货站台发生变化，则更新所有该仓库对应的子仓库
	 * @param usercenter
	 * @param cangkbh
	 * @param zickbh
	 * @param xiehztbh
	 * @param userId
	 */
	public void updateLingjckByXiehzt(String usercenter,String cangkbh,String zickbh,String xiehztbh,String userId){
		Lingjck bean = new Lingjck();
		bean.setUsercenter(usercenter);
		bean.setCangkbh(cangkbh);
		bean.setZickbh(zickbh);
		bean.setXiehztbh(xiehztbh);
		bean.setEditor(userId);//修改人
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());//修改时间
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateLingjckXiehzt", bean);//修改数据库
	}
	
	/**
	 * 根据用户中心+零件
	 * @return map<key:usercent+fenppx,value:chanx>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getLingjsMap(){
		CkxLingj ckxlj = new CkxLingj();
		ckxlj.setBiaos("1");
		List<CkxLingj>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCkxLingj",ckxlj);
		Map<String,String> map=new HashMap<String,String>();
		for (CkxLingj lingj : list) {
			map.put(lingj.getUsercenter()+lingj.getLingjbh(),lingj.getUsercenter()+lingj.getLingjbh());
		}
		return map;
	}
	
	/**
	 * 根据用户中心+零件
	 * @return map<key:usercent+fenppx,value:chanx>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getCangksMap(){
		Cangk ck = new Cangk();
		ck.setBiaos("1");
		List<Cangk>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryLingjCangk",ck);
		Map<String,String> map=new HashMap<String,String>();
		for (Cangk cangk : list) {
			map.put(cangk.getUsercenter()+cangk.getCangkbh(),cangk.getUsercenter()+cangk.getCangkbh());
		}
		return map;
	}
	
	/**
	 * 根据用户中心+零件
	 * @return map<key:usercent+fenppx,value:chanx>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getZickMap(){
		Zick zick = new Zick();
		zick.setBiaos("1");
		List<Zick>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryLingjZick",zick);
		Map<String,String> map=new HashMap<String,String>();
		for (Zick zck : list) {
			map.put(zck.getUsercenter()+zck.getCangkbh()+zck.getZickbh(),zck.getUsercenter()+zck.getCangkbh()+zck.getZickbh());
		}
		return map;
	}
	
	/**
	 * 根据用户中心+零件
	 * @return map<key:usercent+fenppx,value:chanx>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getBaozMap(){
		Baoz bz = new Baoz();
		bz.setBiaos("1");
		List<Baoz>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryLingjBaoz",bz);
		Map<String,String> map=new HashMap<String,String>();
		for (Baoz baoz : list) {
			map.put(baoz.getBaozlx(),baoz.getBaozlx());
		}
		return map;
	}
	
	/**
	 * 根据用户中心+零件
	 * @return map<key:usercent+fenppx,value:chanx>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getXiehztMap(){
		Zick zick = new Zick();
		zick.setBiaos("1");
		List<Zick>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryLingjZick",zick);
		Map<String,String> map=new HashMap<String,String>();
		for (Zick zck : list) {
			map.put(zck.getUsercenter()+zck.getCangkbh()+zck.getZickbh(),zck.getZhantbh());
		}
		return map;
	}
	
	/**
	 * 查询用户中心
	 * @date 20161107
	 * @author CSY
	 * @return map<key:usercenter,value:usercenter>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getUsercenterMap(){
		Usercenter usercenter = new Usercenter();
		usercenter.setBiaos("1");
		List<Usercenter>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryUsercenter",usercenter);
		Map<String,String> map=new HashMap<String,String>();
		for (Usercenter uc : list) {
			map.put(uc.getUsercenter(),uc.getUsercenter());
		}
		return map;
	}
	
	/**
	 * 查询零件仓库
	 * @date 20161107
	 * @author CSY
	 * @return map<key:usercenter+lingjbh+cangkbh+zickbh,value:lingjck>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Lingjck> getLingjckMap(){
		List<Lingjck>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryLingjck");
		Map<String,Lingjck> map=new HashMap<String,Lingjck>();
		for (Lingjck lc : list) {
			map.put(lc.getUsercenter()+lc.getLingjbh()+lc.getCangkbh()+lc.getZickbh(),lc);
		}
		return map;
	}
	
	/**
	 * 查询定置库位
	 * @date 20161107
	 * @author CSY
	 * @return map<key:usercenter+cangkbh+zickbh+kuwbh,value:kuw>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Kuw> getKuwMap(){
		List<Kuw>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getKuwForDR");
		Map<String,Kuw> map=new HashMap<String,Kuw>();
		for (Kuw kw : list) {
			map.put(kw.getUsercenter()+kw.getCangkbh()+kw.getZickbh()+kw.getKuwbh(),kw);
		}
		return map;
	}
	
	/**
	 * 查询地面库
	 * @date 20161107
	 * @author CSY
	 * @return map<key:usercenter+cangkbh+zickbh,value:zick>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Zick> getDimMap(){
		List<Zick>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getDimForDR");
		Map<String,Zick> map=new HashMap<String,Zick>();
		for (Zick zck : list) {
			map.put(zck.getUsercenter()+zck.getCangkbh()+zck.getZickbh(),zck);
		}
		return map;
	}
}
