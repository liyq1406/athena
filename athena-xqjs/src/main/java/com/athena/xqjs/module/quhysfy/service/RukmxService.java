package com.athena.xqjs.module.quhysfy.service;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.common.Xitcsdy;
import com.athena.xqjs.entity.quhyuns.Baoz;
import com.athena.xqjs.entity.quhyuns.JinjjQuhYuns;
import com.athena.xqjs.entity.quhyuns.QuhYuns;
import com.athena.xqjs.entity.quhyuns.Rukmx;
import com.athena.xqjs.entity.quhyuns.Tuopbzdygx;
import com.athena.xqjs.entity.quhyuns.WarnMessage;
import com.athena.xqjs.entity.quhyuns.Yunsfyhz;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.util.GetPostOnly;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;


/**
 * 入库明细
 * @author denggq
 * 2012-3-19
 */
@Component
public class RukmxService extends BaseService<Rukmx>{

	
	private final Logger log = Logger.getLogger(RukmxService.class);
	
	@Override
	public String getNamespace() {
		return "ts_quhysfy";
	}
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	
	public Yunsfyhz gettotalyunf(Rukmx bean) {
		Object obj=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("ts_quhysfy.totalrukmx", bean);
		if (Integer.valueOf(obj.toString())==0) {
			throw new ServiceException("预审条数不能为零");
		}
		return  (Yunsfyhz) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("ts_quhysfy.totalyunf", bean);
	}
	
	public void checkShenhkssj(Rukmx bean) {
		Yunsfyhz yunsfyhz=new Yunsfyhz();
		yunsfyhz.setUsercenter(bean.getUsercenter());
		yunsfyhz.setDanjlx(bean.getDanjlx());
		Yunsfyhz yunsfyhz2=(Yunsfyhz)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("ts_quhysfy.getMaxShenhjssj", yunsfyhz);
		if (yunsfyhz2!=null && !yunsfyhz2.getShenhjssj().equalsIgnoreCase(bean.getQisruksj())) {
			throw new ServiceException("入库开始日期应该为"+yunsfyhz2.getShenhjssj()+"才能使审核时间段连上");
		}
	}
	
	public int getprinttotalyunf(Rukmx bean,
			Map<String, List<Yunsfyhz>> zcmap,
			Map<String, List<Yunsfyhz>> jjmap,
			Map<String, List<Yunsfyhz>> lxmap, Map<String, Object> lingxmc) {
	
		int page=0;
		Xitcsdy xitcsdy=new Xitcsdy();
		xitcsdy.setZidlx("LXJLB");
		//查询零星件类别和名称
		List<Xitcsdy> zidbm= baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ts_quhysfy.queryzdbm", xitcsdy);
		StringBuffer sb=new StringBuffer();
		for (int i = 0; i < zidbm.size(); i++) { 
			sb.append(",sum( CASE lxjlb  WHEN '"+zidbm.get(i).getZidbm()+"'THEN  xinlxfy ELSE   0 END)  lingxing"+(i+1));
		//	sb.append(",sum(decode(LXJLB,'"+zidbm.get(i).getZidbm()+"',lingxfy,0))  lingxing"+(i+1));
			lingxmc.put("lingxing"+(i+1), zidbm.get(i).getZidmc());
		}
		//查询所有的承运商
		List<Rukmx> chengysdmList= baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ts_quhysfy.querychengysdm", bean);
		for (Rukmx rukmx : chengysdmList) {
			bean.setChengysdm(rukmx.getChengysdm());
			//正常件运费
			List<Yunsfyhz> list1= baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ts_quhysfy.getprinttotalyunf", bean);
			List<Yunsfyhz> list3=new ArrayList<Yunsfyhz>();//零星件
			if(list1!=null && list1.size()>0){
				for (Yunsfyhz yunsfyhz : list1) {
					if(yunsfyhz.getLingxfy()!=0.0){
						Yunsfyhz lingxing=new Yunsfyhz();
						lingxing.setUsercenter(yunsfyhz.getUsercenter());
						lingxing.setChengysdm(rukmx.getChengysdm());
						lingxing.setGongysdm(yunsfyhz.getGongysdm()==null ? "DEFAULT" : yunsfyhz.getGongysdm());
						lingxing.setLingxing1(sb.toString());
						lingxing.setDanjh(bean.getDanjh());//0013567  取货运输结算单据打印中零星件统计错误 2017.4.13
						Yunsfyhz lingxfy= (Yunsfyhz) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("ts_quhysfy.querylingxfy", lingxing);
						list3.add(lingxfy);
					}
				}
			}
			//紧急件运费
			List<Yunsfyhz> list2=new ArrayList<Yunsfyhz>();
			if(!bean.getJinjjdjh().equalsIgnoreCase("undefined")){
				Rukmx rukmx2=new Rukmx();
				rukmx2.setUsercenter(bean.getUsercenter());
				rukmx2.setChengysdm(rukmx.getChengysdm());
				rukmx2.setDanjh(bean.getJinjjdjh());
				list2= baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ts_quhysfy.getprinttotalyunfjinj", rukmx2);
			
			}
			if(list1.size()>0 || list2.size()>0 || list3.size()>0){//有一种类型有值就必须在打印展示
				zcmap.put(rukmx.getChengysdm(), list1);
				jjmap.put(rukmx.getChengysdm(), list2);
				lxmap.put(rukmx.getChengysdm(), list3);	
			}
		//	int zc=list1.size();
			//int jj=list2.size()+(list2.size() % 30 ==0 ? list2.size() / 30 : list2.size() /30 + 1);
			//int lx=list3.size()+(list3.size() % 30 ==0 ? list3.size() / 30 : list3.size() /30 + 1)*2;
			//int nowpage= (zc+jj+lx) % 30 ==0 ? (zc+jj+lx)  / 30 :  (zc+jj+lx)  /30 + 1;
			int nowpage=(list1.size()+(list2.size()==0?list2.size():list2.size()+1)+(list3.size()==0?list3.size():list3.size()+2));//总页数
			 nowpage= nowpage % 26 ==0 ? nowpage / 26 : nowpage /26 + 1;
			 //总页数为奇数必须加一。
			if((nowpage%2)==1){
				nowpage++;
			}
			page+=nowpage;
		
		}
	

		return page;
	}

	

	//预审修改明细
	public void updateRukmx(Rukmx bean) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ts_quhysfy.updateRukmxYushen", bean);		
	}
	
	/**	创建人 */
	private String creator = "";
	
	public String calcjisyf() throws ServiceException{
		String result = "";
		try{
			result = calcjisyf("4800");	//计算取货运输费用
		}catch(Exception e){
			log.error("计算取货运费异常结束...",e);
			result = "fail";
			throw new ServiceException(e.getMessage());
		}
		return result;
	}
	
	public String calcjisyf(String creator) {
		this.creator = creator;
		String result = "success";
		//订单计算时间
		log.info("---------计算取货运输费用开始------时间:"+CommonFun.getJavaTime(Const.TIME_FORMAT_HH_MM_SS));
		Rukmx bean=new Rukmx();
		bean.setJiszt("0");
		bean.setLingjlx("1");
		List<Rukmx>  rukmxs=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ts_quhysfy.queryRukmx", bean);
		//运费计算每条明细
		yunfcs(rukmxs,1); 
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ts_quhysfy.updateRukmxAllJiszt", bean);//计算结束后统一修改计算状态
		log.info("---------计算取货运输费用结束------时间:"+CommonFun.getJavaTime(Const.TIME_FORMAT_HH_MM_SS));
		return result;
	}
	public String   chongsuan(List<Rukmx> rukmxs,Yunsfyhz bean){
		String flag="";
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		log.info("---------取货运费重算开始------时间:"+CommonFun.getJavaTime(Const.TIME_FORMAT_HH_MM_SS));
		creator=getLoginUser().getUsername();
		bean.setFlag("1");
		try {
			
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ts_quhysfy.updatejiszt",bean );
			if(bean.getDanjlx().equals("1")){
				//更新明细的费用
			 yunfcs(rukmxs,2);
			}
			else if(bean.getDanjlx().equals("2")){
				//急件重算
			 chongsuaneditsJinjj(rukmxs,getLoginUser().getUsername());
			}
		Rukmx rukmx=new Rukmx();
		rukmx.setUsercenter(bean.getUsercenter());
		rukmx.setDanjh(bean.getDanjh());
		//把更新的明细费用汇总获取新的总费用
		Yunsfyhz  newyunsfyhz	= (Yunsfyhz) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("ts_quhysfy.totalyunf", rukmx);
		newyunsfyhz.setUsercenter(bean.getUsercenter());
		newyunsfyhz.setDanjh(bean.getDanjh());
		String banch=bean.getBanch().replace("第", "");
		banch=banch.replace("版", "");
		newyunsfyhz.setBanch("第"+(Integer.parseInt(banch)+1)+"版");
		newyunsfyhz.setEditor(getLoginUser().getUsername());
		newyunsfyhz.setEdit_time(formatter.format(new Date()));
		newyunsfyhz.setFlag("0");
		//更新主单的总费用和版次号
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ts_quhysfy.shougcsupdateYunsfyhz",newyunsfyhz );
		flag="成功";
		log.info("---------取货运费重算结束------时间:"+CommonFun.getJavaTime(Const.TIME_FORMAT_HH_MM_SS));
		} catch (Exception e) {
			bean.setFlag("0");
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ts_quhysfy.updatesuo",bean );//重算如果异常把锁打开
			flag="重算出现异常，请联系管理员";
			log.error("取货运费重算异常"+e);
		}
		return flag;
	}


	public void yunfcs(List<Rukmx> rukmxs,int flag) {
		
		for (Rukmx rukmx : rukmxs) {
		 try {
		if(rukmx.getLingjlx().equalsIgnoreCase("1")){
			//初始化运费
			rukmx.setXinlfmdj(0.0);
			rukmx.setXintpfkfy(0.0);
			rukmx.setXintpfy(0.0);
			rukmx.setTuoptj(0.0);
			rukmx.setTuopfktj(0.0);
			rukmx.setXinljdj(0.0);
			rukmx.setXinysfy(0.0);
			rukmx.setXinfkfy(0.0);
			rukmx.setZhengcjfktj(0.0);
			rukmx.setZhengctj(0.0);
			rukmx.setChengysdm("DEFAULT");
			QuhYuns ckxyuns = getckxyuns(rukmx);
			if(ckxyuns!=null){  //参考系找不到
				rukmx.setChengysdm(ckxyuns.getChengysdm());//把参考系的承运商代码赋值给入库明细
				Baoz bz = getckxbaoz(rukmx);	
				Tuopbzdygx ckxTuopbzdygx = getckxtuobzdygx(rukmx);
				double zhedlfmtj=0;
				double lifmtj=0;
		//		0013563  参数有误时，计入异常报警，然后跳过算下一条。 王朋
			  try {
				if(bz!=null){
				//折叠立方米体积
				 zhedlfmtj=(double)bz.getZhedgd()*bz.getChangd()*bz.getKuand()/1000000000;
				//参考系立方米体积
				 lifmtj=(double)bz.getChangd()*bz.getKuand()*bz.getGaod()/1000000000;
				}
			  } catch (Exception e) {
				e.printStackTrace();
				log.error(e);
				WarnMessage message = new WarnMessage(rukmx.getUsercenter(),"运费计算错误",
						"包装类型为"+bz.getBaozlx()+"包装参考系有空值参数","940",rukmx.getLingjbh(), creator);
				addWarnMessage(message);
			  }
				
				//立方米单价
		//		rukmx.setLifmdj((double)ckxyuns.getZhixqhdj()+ckxyuns.getCangcpsdj()+ckxyuns.getGanxysdj()+ckxyuns.getQuyjpdj());
				rukmx.setXinlfmdj((double)ckxyuns.getZhixqhdj()+ckxyuns.getCangcpsdj()+ckxyuns.getGanxysdj()+ckxyuns.getQuyjpdj());
				if(ckxTuopbzdygx!=null){//托盘参考系为空
					//托盘型号对应包装参考系
					Baoz tuopbaoz =getckxbaoz(ckxTuopbzdygx,rukmx);
					if(tuopbaoz!=null){
						//托盘折叠立方米体积
						double	 tuopzhedlfmtj=(double)tuopbaoz.getZhedgd()*tuopbaoz.getChangd()*tuopbaoz.getKuand()/1000000000;
						//托盘立方米体积
						double	 tuoplifmtj=(double)tuopbaoz.getChangd()*tuopbaoz.getKuand()*tuopbaoz.getGaod()/1000000000;
						//托盘个数
						double tuopgs=(double)rukmx.getBaozgs()/ckxTuopbzdygx.getBaozgs();		
						//托盘费用
						rukmx.setXintpfy((double)tuopgs*tuoplifmtj*rukmx.getXinlfmdj());
						if(tuopbaoz!=null && StringUtils.isNotBlank(bz.getShifhs()) && tuopbaoz.getShifhs().equals("1")){
							//托盘返空费用
							rukmx.setXintpfkfy((double)tuopzhedlfmtj*tuopgs*ckxyuns.getFankdj());
							
							//托盘返空体积
							rukmx.setTuopfktj((double)tuopzhedlfmtj*tuopgs);
						}
						//托盘体积
						rukmx.setTuoptj((double)tuopgs*tuoplifmtj);

					}
					
				}	
				//零件单价	
				rukmx.setXinljdj((double)lifmtj*rukmx.getXinlfmdj()/rukmx.getBaozrl());
				//运输费用	
				rukmx.setXinysfy((double)lifmtj*rukmx.getXinlfmdj()/rukmx.getBaozrl()*rukmx.getLingjsl());
				if(bz!=null && StringUtils.isNotBlank(bz.getShifhs()) &&  bz.getShifhs().equals("1")){				
					//返空费用	
					rukmx.setXinfkfy((double)zhedlfmtj*rukmx.getBaozgs()*ckxyuns.getFankdj());				
					//正常件返空体积	
					rukmx.setZhengcjfktj((double)zhedlfmtj*rukmx.getBaozgs());			
				}
				//正常件体积	
				rukmx.setZhengctj((double)rukmx.getBaozgs()* lifmtj);
			}
				if(flag==1){
					rukmx.setLifmdj(rukmx.getXinlfmdj()); 			//原立方米单价
					rukmx.setTuopfkfy(rukmx.getXintpfkfy()); 		//原托盘返空费用
					rukmx.setTuopfy(rukmx.getXintpfy());			//原托盘费用
					rukmx.setYtuoptj(rukmx.getTuoptj());			//原托盘体积
					rukmx.setYtuopfktj(rukmx.getTuopfktj());		//原托盘返空体积
					rukmx.setLingjdj(rukmx.getXinljdj());			//原零件单价
					rukmx.setYunsfy(rukmx.getXinysfy());			//原运输费用
					rukmx.setFankfy(rukmx.getXinfkfy());			//原返空费用
					rukmx.setYzhengcjfktj(rukmx.getZhengcjfktj());	//原正常件返空体积
					rukmx.setYzhengctj(rukmx.getZhengctj());		//原正常件体积
			//		rukmx.setJiszt("1");  改为计算完后统一更改计算状态
				
				}
				rukmx.setEditor(creator);
				Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				rukmx.setEdit_time(formatter.format(new Date()));//计算时间
			
				//修改计算状态，计算时间，计算费用
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ts_quhysfy.updateRukmxJiszt", rukmx);
			}
//		0013563  参数有误时，计入异常报警，然后跳过算下一条。 王朋
			} catch (Exception e) {
				e.printStackTrace();
				log.error("用户中心"+rukmx.getUsercenter()+"供应商"+rukmx.getGongysdm()+"承运商"
						+rukmx.getChengysdm()+"零件编号"+rukmx.getLingjbh()+"包装型号"+rukmx.getBaozxh()+"入库日期"+rukmx.getRuksj()+"计算异常"+e);
				continue;
			}
		}
	
	}

	
	/**
	 * 添加报警信息
	 * @param message 报警信息
	 */
	private void addWarnMessage(WarnMessage message){
		log.error(message.toString());	//打印报警信息
		message.setCreate_time(CommonFun.getJavaTime());	//创建时间
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ts_quhysfy.insertWarnMessage", message);		//保存报警信息
	}
	private Tuopbzdygx getckxtuobzdygx(Rukmx rukmx) {
		Tuopbzdygx tuopbzdygx=new Tuopbzdygx();
		tuopbzdygx.setUsercenter(rukmx.getUsercenter());
		tuopbzdygx.setBiaos("1");
		tuopbzdygx.setBaozxh(rukmx.getBaozxh());
		//托盘包装对应关系
		Tuopbzdygx ckxTuopbzdygx=(Tuopbzdygx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("ts_quhysfy.queryTuopbzdygx", tuopbzdygx);
		if(ckxTuopbzdygx==null){
			//添加报警信息
			WarnMessage message = new WarnMessage(rukmx.getUsercenter(),"托盘包装对应关系参考系",
					"用户中心"+rukmx.getUsercenter()+"供应商"+rukmx.getGongysdm()+"承运商"
					+rukmx.getChengysdm()+"零件编号"+rukmx.getLingjbh()+"包装型号"+rukmx.getBaozxh()+"入库日期"+rukmx.getRuksj()+"包装型号对应托盘包装系不存在","920",rukmx.getLingjbh(), creator);
			addWarnMessage(message);
		//	throw new ServiceException(message.getXiaox());
		}
		return ckxTuopbzdygx;
	}

	private Baoz getckxbaoz(Rukmx rukmx) {
		Baoz baoz=new Baoz();
		baoz.setBiaos("1");
		baoz.setBaozlx(rukmx.getBaozxh());
		//包装参考系
		Baoz bz=(Baoz) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("ts_quhysfy.queryBaoz", baoz);
		if(bz==null){
			//添加报警信息
			WarnMessage message = new WarnMessage(rukmx.getUsercenter(),"包装参考系",
					"用户中心"+rukmx.getUsercenter()+"供应商"+rukmx.getGongysdm()+"承运商"
					+rukmx.getChengysdm()+"零件编号"+rukmx.getLingjbh()+"包装型号"+rukmx.getBaozxh()+"入库日期"+rukmx.getRuksj()+"包装型号对应包装参考系不存在","910",rukmx.getLingjbh(), creator);
			addWarnMessage(message);
		//	throw new ServiceException(message.getXiaox());
		}
		return bz;
	}
	
	private Baoz getckxbaoz(Tuopbzdygx ckxTuopbzdygx,Rukmx rukmx) {
		Baoz baoz=new Baoz();
		baoz.setBiaos("1");
		baoz.setBaozlx(ckxTuopbzdygx.getTuopxh());
		//包装参考系
		Baoz bz=(Baoz) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("ts_quhysfy.queryBaoz", baoz);
		if(bz==null){
			//添加报警信息
			WarnMessage message = new WarnMessage(rukmx.getUsercenter(),"包装参考系",
					"用户中心"+rukmx.getUsercenter()+"供应商"+rukmx.getGongysdm()+"承运商"
					+rukmx.getChengysdm()+"零件编号"+rukmx.getLingjbh()+"包装型号"+rukmx.getBaozxh()+"入库日期"+rukmx.getRuksj()+"托盘型号对应包装参考系不存在","910",rukmx.getLingjbh(), creator);
			addWarnMessage(message);
	//		throw new ServiceException(message.getXiaox());
		}
		return bz;
	}

	private QuhYuns getckxyuns(Rukmx rukmx) {
		QuhYuns quhYuns=new QuhYuns();
		quhYuns.setBiaos("1");
		quhYuns.setUsercenter(rukmx.getUsercenter());
		quhYuns.setGongysdm(rukmx.getGongysdm());
	//	quhYuns.setChengysdm(rukmx.getChengysdm());
		quhYuns.setYouxsj(rukmx.getRuksj());
		QuhYuns ckxyuns1=new QuhYuns();
		try {		
			 ckxyuns1 = youxjcz(rukmx, quhYuns);
			if(ckxyuns1==null){  //参考系找不到
				//添加报警信息
				WarnMessage message = new WarnMessage(rukmx.getUsercenter(),"运费单价参考系",
						"用户中心"+rukmx.getUsercenter()+"供应商"+rukmx.getGongysdm()+"承运商"
						+rukmx.getChengysdm()+"零件编号"+rukmx.getLingjbh()+"包装型号"+rukmx.getBaozxh()+"入库日期"+rukmx.getRuksj()+"取货运费参考系不存在","900",rukmx.getLingjbh(),creator);
				addWarnMessage(message);
			}
//			0013563  参数有误时，计入异常报警，然后跳过算下一条。 王朋
		} catch (Exception e) {
			WarnMessage message = new WarnMessage(rukmx.getUsercenter(),"运费计算错误",
					"用户中心"+rukmx.getUsercenter()+"供应商"+rukmx.getGongysdm()+"承运商"
					+rukmx.getChengysdm()+"零件编号"+rukmx.getLingjbh()+"包装型号"+rukmx.getBaozxh()+"入库日期"+rukmx.getRuksj()+"对应取货运费参考系存在多条","940",rukmx.getLingjbh(),creator);
			addWarnMessage(message);
		}
		return ckxyuns1;
	}

	private QuhYuns youxjcz(Rukmx rukmx, QuhYuns quhYuns) {
		//运费参考系
		QuhYuns ckxyuns1=(QuhYuns) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("ts_quhysfy.queryQuhYuns", quhYuns);
		if(ckxyuns1==null){
				//查找最近的时间段
			//	 quhYuns.setChengysdm(rukmx.getChengysdm());
				 quhYuns.setGongysdm(rukmx.getGongysdm());
				 QuhYuns  ckxyuns2=(QuhYuns) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("ts_quhysfy.queryzuijsjd", quhYuns);  
			        if(ckxyuns2!=null){
						ckxyuns2.setBiaos("1");
						ckxyuns1=(QuhYuns) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("ts_quhysfy.queryQuhYuns", ckxyuns2);
					 }

		}
		return ckxyuns1;
	}
	
	/**
	 * 批量保存方法
	 * @author denggq
	 * @date 2012-3-21
	 * @param insert,edit,delete,userID
	 * @return ""
	 */
	@Transactional
	public String save(ArrayList<Rukmx> insert,
	           ArrayList<Rukmx> edit,
	   		   ArrayList<Rukmx> delete,String userID) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){//无操作
			return "null";
		}

			Yunsfyhz yunsfyhz=new Yunsfyhz();
			yunsfyhz.setUsercenter(edit.get(0).getUsercenter());
			yunsfyhz.setDanjh(edit.get(0).getDanjh());
			yunsfyhz=(Yunsfyhz) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("ts_quhysfy.getYunsfyhz", yunsfyhz);
			if(StringUtils.isNotBlank(yunsfyhz.getFlag()) && yunsfyhz.getFlag().equals("1")){
				throw new ServiceException("单据号"+yunsfyhz.getDanjh()+"目前在重算中，不能操作该单据");
			}

		String msg="";
		 msg=edits(edit,userID);//修改
		return msg;
	}
	
	
	public String saveJinjj(ArrayList<Rukmx> insert,
	           ArrayList<Rukmx> edit,
	   		   ArrayList<Rukmx> delete,String userID) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){//无操作
			return "null";
		}

		for(Rukmx bean:edit){
			if(!bean.getShenhzt().equalsIgnoreCase("1")){
				 throw new ServiceException("只有未审核的单据才能修改");
			}
		}
		String msg="";
		 msg=insertsJinjj(insert,userID);//增加
		 if(!msg.equalsIgnoreCase("success")){
			 return msg; 
		 }
		 msg=editsJinjj(edit,userID);//修改
		 if(!msg.equalsIgnoreCase("success")){
			 return msg; 
		 }
		 msg=deletesJinjj(delete,userID);//修改
		return msg;
	}
	
	//急件重算页面修改趟次
	public String updateJinjj(ArrayList<Rukmx> insert,
	           ArrayList<Rukmx> edit,
	   		   ArrayList<Rukmx> delete,String userID) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){//无操作
			return "null";
		}
		
		Yunsfyhz yunsfyhz=new Yunsfyhz();
		yunsfyhz.setUsercenter(edit.get(0).getUsercenter());
		yunsfyhz.setDanjh(edit.get(0).getDanjh());
		yunsfyhz=(Yunsfyhz) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("ts_quhysfy.getYunsfyhz", yunsfyhz);
		if(StringUtils.isNotBlank(yunsfyhz.getFlag()) && yunsfyhz.getFlag().equals("1")){
			throw new ServiceException("单据号"+yunsfyhz.getDanjh()+"目前在重算中，不能操作该单据");
		}
		
		String msg="";
		 msg=saveeditsJinjj(edit,userID);//修改
	
		return msg;
	}
	
	
	/**
	 * 私有批量删除方法
	 * @author denggq
	 * @date 2012-3-21
	 * @param delete,userID
	 * @return ""
	 */
	@Transactional
	public String deletesJinjj(List<Rukmx> delete,String userID)throws ServiceException{
		for(Rukmx bean:delete){
			bean.setLingjlx("2");
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ts_quhysfy.delrukmx",bean);//失效数据库
		}
		return "success";
	}
	
	
	@Transactional
	public String inserts(List<Rukmx> insert,String userID)throws ServiceException{
		   Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(Rukmx bean:insert){
			bean.setCreator(userID);
			bean.setCreate_time(formatter.format(new Date()));
			bean.setEditor(userID);
			bean.setEdit_time(formatter.format(new Date()));
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ts_quhysfy.insertRukmxBaoz",bean);//增加数据库
		}
		return "";
	}
	
	@Transactional
	public String insertsJinjj(List<Rukmx> insert,String userID)throws ServiceException{
		   Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String danjlx="2";
		for(Rukmx bean:insert){
			GetPostOnly.checkqhqx(bean.getUsercenter());
			if(bean.getRuksj().compareTo(formatter.format(new Date()))>0){
				throw new ServiceException("入库日期"+bean.getRuksj()+"大于系统当前时间");
			}
			Rukmx rukmx=new Rukmx();
			rukmx.setUsercenter(bean.getUsercenter());
			rukmx.setBiaos("1");
			rukmx.setGongysdm(bean.getChengysdm());
			Object obj2=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_quhysfy.getCountGongys", rukmx);
			if (Integer.valueOf(obj2.toString())==0) {
				 throw new ServiceException("承运商编码:"+bean.getChengysdm()+"在参考系供应商不存在！");
			}
			rukmx.setGongysdm(bean.getGongysdm());
			rukmx.setLingjlx("3");
			Object obj1=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_quhysfy.getCountGongys", rukmx);
			if (Integer.valueOf(obj1.toString())==0) {
				 throw new ServiceException("供应商编码:"+bean.getGongysdm()+"在参考系供应商不存在！");
			}
		
			checkRukrq(bean, danjlx);
			 JinjjQuhYuns jinjjQuhYuns=new JinjjQuhYuns();
			 jinjjQuhYuns.setUsercenter(bean.getUsercenter());
			 jinjjQuhYuns.setGongysdm(bean.getGongysdm());
			 jinjjQuhYuns.setChengysdm(bean.getChengysdm());
			 jinjjQuhYuns.setYouxsj(bean.getRuksj());
			 jinjjQuhYuns.setBiaos("1");
			 JinjjQuhYuns yuns=		youxjczJinjj(bean,jinjjQuhYuns);
			 if(yuns!=null){
				 bean.setXinjjfy((double)yuns.getTangcdj()*bean.getLingjsl());	 
				 bean.setJinjfy((double)yuns.getTangcdj()*bean.getLingjsl());	
				 bean.setXinljdj((double)yuns.getTangcdj());
				 bean.setLingjdj((double)yuns.getTangcdj());
			 }else{
				 throw new ServiceException("供应商"+bean.getGongysdm()+"承运商"+bean.getChengysdm()+"对应的急件参考系不存在");
			 }
			bean.setDanjlx(danjlx);
			bean.setLingjlx("2");
			bean.setCreator(userID);
			bean.setCreate_time(formatter.format(new Date()));
			bean.setEditor(userID);
			bean.setEdit_time(formatter.format(new Date()));
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ts_quhysfy.insertRukmxJinjj",bean);//增加数据库
		}
		return "success";
	}
	
	public String editsJinjj(List<Rukmx> edit,String userID) throws ServiceException{
		   Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String danjlx="2";
		for(Rukmx bean:edit){
			//用户中心权限校验
			if((List) AuthorityUtils.getSecurityUser().getDicCodeUcMap().get("QUHYFJHY")!=null && ((List) AuthorityUtils.getSecurityUser().getDicCodeUcMap().get("QUHYFJHY")).size()>0){
				List<String> uclist=(List) AuthorityUtils.getSecurityUser().getDicCodeUcMap().get("QUHYFJHY");
				if(!uclist.contains(bean.getUsercenter())){
					throw new ServiceException("您没有权限操作用户中心为"+bean.getUsercenter()+"的数据");
				}
			}
			if(bean.getGongysdm()!=null){
				bean.setGongysdm(bean.getGongysdm().replace(" ", " "));
			}
			if(bean.getChengysdm()!=null){
				bean.setChengysdm(bean.getChengysdm().replace(" ", " "));
			}
		
			
			checkRukrq(bean, danjlx);
			
			 JinjjQuhYuns jinjjQuhYuns=new JinjjQuhYuns();
			 jinjjQuhYuns.setUsercenter(bean.getUsercenter());
			 jinjjQuhYuns.setGongysdm(bean.getGongysdm());
			 jinjjQuhYuns.setChengysdm(bean.getChengysdm());
			 jinjjQuhYuns.setYouxsj(bean.getRuksj());
			 jinjjQuhYuns.setBiaos("1");
			 JinjjQuhYuns yuns=	youxjczJinjj(bean,jinjjQuhYuns);
			 //不为空更新紧急费用和单价
			 if(yuns!=null){
				 bean.setXinjjfy((double)yuns.getTangcdj()*bean.getLingjsl());	 
				 bean.setJinjfy((double)yuns.getTangcdj()*bean.getLingjsl());	
				 bean.setXinljdj((double)yuns.getTangcdj());
				 bean.setLingjdj((double)yuns.getTangcdj());
			 }else{
					throw new ServiceException("供应商"+bean.getGongysdm()+"承运商"+bean.getChengysdm()+"对应的急件参考系不存在");
			 }
			bean.setYtangc(bean.getLingjsl());
			bean.setEditor(userID);
			bean.setEdit_time(formatter.format(new Date()));
			bean.setLingjlx("2");
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ts_quhysfy.updateRukmxJinjj",bean);//修改数据库
	
		}
		return "success";
	}
	//急件重算
	public void chongsuaneditsJinjj(List<Rukmx> edit,String userID) throws ServiceException{
		   Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(Rukmx bean:edit){
			if(bean.getGongysdm()!=null){
				bean.setGongysdm(bean.getGongysdm().replace(" ", " "));
			}
			if(bean.getChengysdm()!=null){
				bean.setChengysdm(bean.getChengysdm().replace(" ", " "));
			}
			
			JinjjQuhYuns yuns=getJinjjQuhYuns(bean);
			 if(yuns!=null){
				 bean.setXinjjfy((double)yuns.getTangcdj()*bean.getLingjsl());	 	
				 bean.setXinljdj((double)yuns.getTangcdj());
			 }else{
				 //找不到参考系就给0
				 bean.setXinjjfy(0.0);	
				 bean.setXinljdj(0.0);
			 }
			bean.setEditor(userID);
			bean.setEdit_time(formatter.format(new Date()));
			bean.setLingjlx("2");
			bean.setCreate_time(formatter.format(new Date()));
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ts_quhysfy.chongsuanupdateRukmxJinjj",bean);//修改数据库
	
		}
	}
	
	private JinjjQuhYuns getJinjjQuhYuns(Rukmx rukmx) {
		 JinjjQuhYuns jinjjQuhYuns=new JinjjQuhYuns();
		 jinjjQuhYuns.setUsercenter(rukmx.getUsercenter());
		 jinjjQuhYuns.setGongysdm(rukmx.getGongysdm());
		 jinjjQuhYuns.setChengysdm(rukmx.getChengysdm());
		 jinjjQuhYuns.setYouxsj(rukmx.getRuksj());
		 jinjjQuhYuns.setBiaos("1");
		 JinjjQuhYuns ckxyuns1=new JinjjQuhYuns();
		 try{
		    ckxyuns1 = youxjczJinjj(rukmx,jinjjQuhYuns);
			if(ckxyuns1==null){  //参考系找不到
				//添加报警信息
				WarnMessage message = new WarnMessage(rukmx.getUsercenter(),"紧急件运费参考系",
						"用户中心"+rukmx.getUsercenter()+"供应商"+rukmx.getGongysdm()+"承运商"
						+rukmx.getChengysdm()+"入库日期"+rukmx.getRuksj()+"紧急件运费参考系不存在","930","", creator);
				addWarnMessage(message);
			}
//			0013563  参数有误时，计入异常报警，然后跳过算下一条。 王朋
		} catch (Exception e) {
			WarnMessage message = new WarnMessage(rukmx.getUsercenter(),"运费计算错误",
					"用户中心"+rukmx.getUsercenter()+"供应商"+rukmx.getGongysdm()+"承运商"
					+rukmx.getChengysdm()+"入库日期"+rukmx.getRuksj()+"对应紧急件运费参考系存在多条","940","",creator);
			addWarnMessage(message);
		}
		return ckxyuns1;
	}
	
	//急件重算页面修改趟次
	public String saveeditsJinjj(List<Rukmx> edit,String userID) throws ServiceException{
		   Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   int i=0;
		for(Rukmx bean:edit){
			if(i==0){
				Yunsfyhz yunsfyhz=new Yunsfyhz();
				yunsfyhz.setUsercenter(bean.getUsercenter());
				yunsfyhz.setDanjh(bean.getDanjh());
				yunsfyhz=(Yunsfyhz) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("ts_quhysfy.getYunsfyhz", yunsfyhz);
				if(yunsfyhz!=null && !yunsfyhz.getShenhzt().equalsIgnoreCase("2")){
					throw new ServiceException("单据已经不是预审状态，不能修改");
				}
				if(yunsfyhz!=null && !yunsfyhz.getBiaos().equalsIgnoreCase("1")){
					throw new ServiceException("单据已经被撤回，不能修改");
				}
			}
			if(bean.getGongysdm()!=null){
				bean.setGongysdm(bean.getGongysdm().replace(" ", " "));
			}
			if(bean.getChengysdm()!=null){
				bean.setChengysdm(bean.getChengysdm().replace(" ", " "));
			}
			 JinjjQuhYuns jinjjQuhYuns=new JinjjQuhYuns();
			 jinjjQuhYuns.setUsercenter(bean.getUsercenter());
			 jinjjQuhYuns.setGongysdm(bean.getGongysdm());
			 jinjjQuhYuns.setChengysdm(bean.getChengysdm());
			 jinjjQuhYuns.setYouxsj(bean.getRuksj());
			 jinjjQuhYuns.setBiaos("1");
			 JinjjQuhYuns yuns=		youxjczJinjj(bean,jinjjQuhYuns);
			 if(yuns!=null){
				 bean.setXinjjfy((double)yuns.getTangcdj()*bean.getLingjsl());	 	
				 bean.setXinljdj((double)yuns.getTangcdj());
			 }else{
					throw new ServiceException("供应商"+bean.getGongysdm()+"承运商"+bean.getChengysdm()+"对应的急件参考系不存在");
			 }
			bean.setEditor(userID);
			bean.setEdit_time(formatter.format(new Date()));
			bean.setLingjlx("2");
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ts_quhysfy.chongsuanupdateRukmxJinjj",bean);//修改数据库
	
		}
		if(edit!=null && edit.size()>0){
			Yunsfyhz yunsfyhz=new Yunsfyhz();
			yunsfyhz.setDanjh(edit.get(0).getDanjh());
			yunsfyhz.setUsercenter(edit.get(0).getUsercenter());
			yunsfyhz.setEdit_time(formatter.format(new Date()));
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ts_quhysfy.updateYunsfyhzSaveTime",yunsfyhz);//重算保存时间
		}
		return "success";
	}
	
	private JinjjQuhYuns youxjczJinjj(Rukmx rukmx, JinjjQuhYuns quhYuns) {
		//运费参考系
		JinjjQuhYuns ckxyuns1=(JinjjQuhYuns) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("ts_quhysfy.queryJinjjQuhYuns", quhYuns);
		if(ckxyuns1==null){	
			 //查找最近的时间段
		 	    quhYuns.setChengysdm(rukmx.getChengysdm());
				quhYuns.setGongysdm(rukmx.getGongysdm());
				 JinjjQuhYuns	 ckxyuns2=(JinjjQuhYuns) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("ts_quhysfy.queryzuijsjdJinjj", quhYuns);  
				if(ckxyuns2!=null){
					 ckxyuns2.setBiaos("1");
					 ckxyuns1=(JinjjQuhYuns) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("ts_quhysfy.queryJinjjQuhYuns", ckxyuns2);
			}
					     
		  }
		
		return ckxyuns1;
	}
	/**
	 * 私有批量update方法
	 * @author denggq
	 * @date 2012-3-21
	 * @param edit,userID
	 * @return ""
	 */
	@Transactional
	public String edits(List<Rukmx> edit,String userID) throws ServiceException{
		   Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   int i=0;
		   Boolean flag=false;
		for(Rukmx bean:edit){
			if(i==0){
			Yunsfyhz yunsfyhz=new Yunsfyhz();
			yunsfyhz.setUsercenter(bean.getUsercenter());
			yunsfyhz.setDanjh(bean.getDanjh());
			yunsfyhz=(Yunsfyhz) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("ts_quhysfy.getYunsfyhz", yunsfyhz);
			if(yunsfyhz!=null && !yunsfyhz.getShenhzt().equalsIgnoreCase("2")){
				throw new ServiceException("单据已经不是预审状态，不能修改");
			}
			if(yunsfyhz!=null && !yunsfyhz.getBiaos().equalsIgnoreCase("1")){
				throw new ServiceException("单据已经被撤回，不能重算");
			}
			}
			//零星件不需要修改
			if(bean.getLingjlx().equalsIgnoreCase("3")){
				continue;
			}else if(bean.getLingjlx().equalsIgnoreCase("1")){
				flag=true;  //有正常件才需要计算保存时间
			}
			Baoz baoz=new Baoz();
			baoz.setBiaos("1");
			baoz.setBaozlx(bean.getBaozxh());
			//包装参考系
			Baoz bz=(Baoz) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("ts_quhysfy.queryBaoz", baoz);
			if(bz==null){
				throw new ServiceException("包装型号"+bean.getBaozxh()+"对应包装参考系不存在");
			}
			if(bean.getGongysdm()!=null){
				bean.setGongysdm(bean.getGongysdm().replace(" ", " "));
			}
			if(bean.getChengysdm()!=null){
				bean.setChengysdm(bean.getChengysdm().replace(" ", " "));
			}
	
			bean.setEditor(userID);
			bean.setEdit_time(formatter.format(new Date()));
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ts_quhysfy.updateRukmxBaoz",bean);//修改数据库
			i++;
		}
		if(edit!=null && edit.size()>0 && flag){
			Yunsfyhz yunsfyhz=new Yunsfyhz();
			yunsfyhz.setDanjh(edit.get(0).getDanjh());
			yunsfyhz.setUsercenter(edit.get(0).getUsercenter());
			yunsfyhz.setEdit_time(formatter.format(new Date()));
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ts_quhysfy.updateYunsfyhzSaveTime",yunsfyhz);//重算保存时间
		}
		return "success";
	}
	
	
	
	
	public Map<String, Object> queryRukmxChus(Rukmx bean) {
		Map<String, Object> map= baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectPages("ts_quhysfy.queryRukmxChus", bean,bean);

		return map;
	}
	
	
	
	/**
	 * 分页查询零星件运费管理
	 * @param bean
	 * @author lc
	 * @date 2016-11-29
	 */
	public Map<String, Object> queryLxjfygl(Rukmx bean) {
		Map<String, Object> map= baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectPages("ts_quhysfy.queryLxjfygl", bean,bean);

		return map;
	}
	
	/**
	 * 批量保存方法
	 * @author lc
	 * @date 2016-11-29
	 * @param insert,edit,delete,userID
	 * @return ""
	 */
	@Transactional
	public String savesLxjfygl(ArrayList<Rukmx> insert,ArrayList<Rukmx> edit,
	   	   ArrayList<Rukmx> delete,String userID) throws ServiceException{
		
		for(Rukmx bean:edit){
			if(!bean.getShenhzt().equalsIgnoreCase("1")){
				throw new ServiceException("只有未审核的单据才能修改");
			}
		}
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){//无操作
			return "null";
		}
		String result="success";
		result=	insertslxj(insert,userID);//增加			
		 if(!result.equalsIgnoreCase("success")){
			 return result; 
		 }
		result=	editslxj(edit,userID);//修改
		 if(!result.equalsIgnoreCase("success")){
			 return result; 
		 }
		result=	 deleteslxj(delete,userID);//删除
		return result;
	}
	
	/**
	 * 私有批量删除方法
	 * @author denggq
	 * @date 2012-3-21
	 * @param delete,userID
	 * @return ""
	 */
	@Transactional
	public String deleteslxj(List<Rukmx> delete,String userID)throws ServiceException{
		for(Rukmx bean:delete){
			bean.setLingjlx("3");
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ts_quhysfy.delrukmx",bean);//失效数据库
		}
		return "success";
	}
	
	
	/**
	 * 私有批量insert方法
	 * @author lc
	 * @date 2016-11-29
	 * @param insert,userID
	 * @return  ""
	 */
	@Transactional
	public String insertslxj(List<Rukmx> insert,String userID)throws ServiceException{
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String danjlx="1";
		for(Rukmx bean:insert){
			GetPostOnly.checkqhqx(bean.getUsercenter());
			if(bean.getRuksj().compareTo(formatter.format(new Date()))>0){
				throw new ServiceException("入库日期"+bean.getRuksj()+"大于系统当前时间");
			}

			if(bean.getLxjlb().equalsIgnoreCase("LINGXING")){
				if(StringUtils.isBlank(bean.getGongysdm())){
					throw new ServiceException("零星件类别为零星时，供应商代码不能为空");
				}
				if(StringUtils.isBlank(bean.getLingjbh())){
					throw new ServiceException("零星件类别为零星时，零件编号不能为空");
				}
			}
			//  0013569 只用校验供应商、承运商存在性即可，不校验单价参考系有没有  王朋
		
		/*	Object wullj=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_quhysfy.checkWullj", bean);
			if (Integer.valueOf(wullj.toString())==0) {
				throw new ServiceException("取货运输单价参考系无对应承运商信息");
			}*/      
		
			checkRukrq(bean, danjlx);
			
			Rukmx rukmx=new Rukmx();
			rukmx.setUsercenter(bean.getUsercenter());
			rukmx.setGongysdm(bean.getChengysdm());
			rukmx.setBiaos("1");
			Object obj2=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_quhysfy.getCountGongys", rukmx);
			if (Integer.valueOf(obj2.toString())==0) {
				throw new ServiceException("承运商编码:"+bean.getChengysdm()+"在参考系供应商不存在！");
			}
			rukmx.setGongysdm(bean.getGongysdm());
			rukmx.setLingjlx("3");
			if(StringUtils.isNotBlank(rukmx.getGongysdm())){
				Object obj1=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_quhysfy.getCountGongys", rukmx);
				if (Integer.valueOf(obj1.toString())==0) {
					throw new ServiceException("供应商编码:"+bean.getGongysdm()+"在参考系供应商不存在！");
				}
			}else{
				bean.setGongysdm("DEFAULT");
			}
			rukmx.setGongysdm(bean.getChengysdm());
		
			rukmx.setLingjbh(bean.getLingjbh());
			if(StringUtils.isNotBlank(rukmx.getLingjbh())){
				Object obj3=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_quhysfy.getCountLingj", rukmx);
				if (Integer.valueOf(obj3.toString())==0) {
					throw new ServiceException("零件表中不存在零件编号："+bean.getLingjbh()+"的数据或数据已失效");
				}
			}else{
				bean.setLingjbh("DEFAULT");
			}
			bean.setCreator(userID);
			bean.setCreate_time(formatter.format(new Date()));
			bean.setEditor(userID);
			bean.setEdit_time(formatter.format(new Date()));
			Object obj=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("ts_quhysfy.queryLxj",bean);
			if (Integer.valueOf(obj.toString())==0) {			
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ts_quhysfy.insertRukmxLingxj",bean);//增加数据库
			}else{		
				throw new ServiceException("该数据在数据库中已存在！");
			}			
		}
		return "success";
	}
	
	/**
	 * 私有批量edit方法
	 * @author lc
	 * @date 2016-11-29
	 * @param edit,userID
	 * @return  ""
	 */
	public String editslxj(List<Rukmx> edit,String userID) throws ServiceException{
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String danjlx="1";
		for(Rukmx bean:edit){
		//	Baoz bz = getckxbaoz(bean);	
			if(bean.getGongysdm()!=null){
				bean.setGongysdm(bean.getGongysdm().replace(" ", " "));	
			}
			if(bean.getChengysdm()!=null){
				bean.setChengysdm(bean.getChengysdm().replace(" ", " "));
			}
			
			checkRukrq(bean, danjlx);
			
			if(bean.getLxjlb().equalsIgnoreCase("LINGXING")){
				if(StringUtils.isBlank(bean.getGongysdm())){
					throw new ServiceException("零星件类别为零星时，供应商代码不能为空");
				}
				if(StringUtils.isBlank(bean.getLingjbh())){
					throw new ServiceException("零星件类别为零星时，零件编号不能为空");
				}
			}else{
				if(StringUtils.isBlank(bean.getGongysdm())){
					bean.setGongysdm("DEFAULT");
				}
				if(StringUtils.isBlank(bean.getLingjbh())){
					bean.setLingjbh("DEFAULT");
				}
			}
			bean.setEditor(userID);
			bean.setEdit_time(formatter.format(new Date()));
			bean.setLingjlx("3");
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ts_quhysfy.updateRukmxLingxj",bean);//修改数据库
	
		}
		return "success";
	}
	//检查入库日期是否大于当前日期
	private void checkRukrq(Rukmx bean, String danjlx) {
		Yunsfyhz yunsfyhz=new Yunsfyhz();
		yunsfyhz.setUsercenter(bean.getUsercenter());
		yunsfyhz.setDanjlx(danjlx);
		yunsfyhz.setShenhkssj(bean.getRuksj());
		yunsfyhz.setBiaos("0");//不为失效的单据
		Object obj4= baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_quhysfy.checkRukrq", yunsfyhz);
		if(Integer.valueOf(obj4.toString())>0){
			 throw new ServiceException("入库日期:"+bean.getRuksj()+"已在审核时间段内,无法保存");
		}
	}
	
	@SuppressWarnings("rawtypes")
	public List queryPostLxjlb(Map<String,String> params) throws ServiceException {
		if(params.get("lingjlx") ==null || params.get("lingjlx").equalsIgnoreCase("3")){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_quhysfy.queryPostLxjlb",params);
		}else{
			return new ArrayList();
		}
	}

}

