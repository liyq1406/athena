/**
 * 代码声明
 */
package com.athena.ckx.module.workCalendar.service;

import java.util.ArrayList;
import java.util.List;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.workCalendar.CkxCalendarCenter;
import com.athena.ckx.entity.workCalendar.CkxCalendarGongyzq;
import com.athena.ckx.entity.workCalendar.CkxCalendarGongyzx;
import com.athena.ckx.entity.workCalendar.CkxXiuxr;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.component.service.Message;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.dao.DataAccessException;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * 中心日历
 * @author kong
 * 2012-02-14
 */
@Component
public class CkxCalendarCenterService extends BaseService<CkxCalendarCenter>{
	
	//命名空间
	@Override
	protected String getNamespace() {
		return "workCalendar";
	}
	/**
	 * 添加中心日历
	 * @param usercenter 用户中心
	 * @param year 年份
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String addYear(String usercenter,String year,LoginUser user){
		String nianzqTemp="";//保存前一天的业周期序号，用于判断夸工业周期
		int biaoji=0,zhouxu=0,nianzhouxu=1;
		List<CkxCalendarCenter>  list= new ArrayList<CkxCalendarCenter>();
		//获取休息日集合
		CkxXiuxr dayOff=new CkxXiuxr();
		dayOff.setBiaos("1");//获取有效数据
		List<CkxXiuxr> xList= baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("workCalendar.queryCkxXiuxr",dayOff);
		for (int m = 1; m <=12; m++) {//月循环
			String month=m<10?"0"+m:String.valueOf(m);
    		int days=DateTimeUtil.getDaysInAMonth(year,month);
    		for (int d = 1; d <= days; d++) {//日循环
        		String day=d<10?"0"+d:String.valueOf(d);
        		String date=year+"-"+month+"-"+day;//拼接日期
        		String week=DateTimeUtil.getWeek(date);//星期序号
        		/*-----------计算年周序号------------*/
        		if(week.equals("1")&&(m!=1||d!=1)){//如果星期一不是元旦
        			nianzhouxu++;
        		}
        		//自己计算
        		int w=nianzhouxu;
        		//(java计算)
//        		int w=DateTimeUtil.getWeekSeqNo(date);
        		String weekNo=w<10?"0"+w:String.valueOf(w);//年周序号
        		CkxCalendarCenter cen=new CkxCalendarCenter();
        		cen.setUsercenter(usercenter);
        		cen.setRiq(date);//日期
        		cen.setXingq(week);//星期
        		cen.setNianzx(year+weekNo);//年周序根据万年历获取(有问题)
        		cen.setXis(new Double(1));//系数默认为1
        		cen.setBiaos("1");
        		//-----------设置年周期--------------
        		if(biaoji!=0){//紧跟1号所在周剩余几天的年周期设置
        			int newmonth=Integer.valueOf(month)-1;
        			String m1=newmonth<10?"0"+newmonth:String.valueOf(newmonth);//截取新的月份
        			cen.setNianzq(year+m1);
        			biaoji--;
        		}else{
        			cen.setNianzq(year+month);//年周期设置
        		}
        		//-----------1号年周期设置------------
        		if(d==1&&m!=1){//一月一号不参加活动
        			if(Integer.valueOf(week)<=3){//1号所在周归本月的周期
        				for (CkxCalendarCenter c : list) {
        					if(c.getNianzx().substring(4, 6).equals(weekNo)){//将本周已设置的周期改过来（一号前几天的）
        						c.setNianzq(year+month);
        					}
        				}
        			}else{//当前月1号年周期减1(归属上一周期)
        				int newmonth=Integer.valueOf(cen.getNianzq().substring(4, 6))-1;
        				String m1=newmonth<10?"0"+newmonth:String.valueOf(newmonth);
        				cen.setNianzq(year+m1);//年月
        				biaoji=7-Integer.valueOf(cen.getXingq());
            		}
        		}
        		//--------------设置周序-----------------
	       		if("1".equals(week)){zhouxu++;}//周一序号加1
        		if(!nianzqTemp.equals(cen.getNianzq())){zhouxu=1;}//夸工业周期序号恢复到1
        		String w1=zhouxu<10?"0"+zhouxu:String.valueOf(zhouxu);
        		cen.setZhoux(w1);//设置周序
				if (d == 1 && m != 1) {// 一月一号不参加活动
					if (Integer.valueOf(week) <= 3) {//如果一号在星期3之前，还要改掉本周已设置的周序
						for (CkxCalendarCenter c : list) {if (c.getNianzx().substring(4, 6).equals(weekNo)) {c.setZhoux(w1);}}
					}
				}
				nianzqTemp=cen.getNianzq();//保存前一天的工业周期
        		//-------------根据双休判断休息日---------
        		if("6".equals(week)||"7".equals(week)){//休息日
        			cen.setShifgzr("0");
        			cen.setShifjfr("0");
        		}else{
        			//--------根据休息日表判断休息日--------
            		for (CkxXiuxr ckxXiuxr : xList) {
    					if(DateTimeUtil.isEqualDate(ckxXiuxr.getRiq(), date)){
    						cen.setShifgzr("0");
    	        			cen.setShifjfr("0");
    	        			break;
    					}
    				}
            		if(cen.getShifgzr()==null||"".equals(cen.getShifgzr())){
	        			cen.setShifgzr("1");
	        			cen.setShifjfr("1");
            		}
        		}
        		//把计算好的日历放入集合
        		cen.setCreateTime(DateTimeUtil.getAllCurrTime());
        		cen.setCreator(user.getUsername());
        		cen.setEditor(user.getUsername());
        		cen.setEditTime(DateTimeUtil.getAllCurrTime());
        		list.add(cen);
        	}
		}
		addGongyzq(list,user);
		addGongyzx(list,user);
		for (CkxCalendarCenter c : list) {
			try {
				super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("workCalendar.insertCkxCalendarCenter", c);
			} catch (DataAccessException e) {//中心日历已存在
				throw new ServiceException(new Message("thisCenterExistent","i18n.ckx.workCalendar.i18n_workCalendar").getMessage());
			}
		}
		//添加成功
		return new Message("addSuccess","i18n.ckx.workCalendar.i18n_workCalendar").getMessage();
	}
	/**
	 * 添加年周期
	 * @param list
	 */
	private void addGongyzq(List<CkxCalendarCenter>  list,LoginUser user){
		//添加之前首先删除已生成的数据，避免主键冲突
		CkxCalendarGongyzq cond=new CkxCalendarGongyzq();
		cond.setGongyzq(list.get(0).getNianzq().substring(0, 4));
		super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("workCalendar.deleteCkxCalendarGongyzq", cond);
		String zq="",riq="",firstDate="";//存放第一天
		List<CkxCalendarGongyzq> gList=new ArrayList<CkxCalendarGongyzq>();
		CkxCalendarGongyzq gong=null;
		for (CkxCalendarCenter c : list) {
			//每个周期的第一天存放上个周期数据
			if(!zq.equals(c.getNianzq())){
				gong=new CkxCalendarGongyzq();
				gong.setGongyzq(zq);//工业周期
				gong.setKaissj(firstDate);//开始时间
				gong.setJiessj(riq);//日期
				gong.setCreateTime(DateTimeUtil.getAllCurrTime());//创建时间
				gong.setCreator(user.getUsername());//创建人
				gong.setEditor(user.getUsername());//修改人
				gong.setEditTime(DateTimeUtil.getAllCurrTime());//修改时间
				if(!"".equals(zq)){gList.add(gong);}
				firstDate=c.getRiq();
			}
			riq=c.getRiq();
			zq=c.getNianzq();
		}
		//最后一周期的数据
		gong=new CkxCalendarGongyzq();
		gong.setGongyzq(zq);//工业周期
		gong.setKaissj(firstDate);//开始时间
		gong.setJiessj(riq);
		gong.setCreateTime(DateTimeUtil.getAllCurrTime());//创建时间
		gong.setCreator(user.getUsername());//创建人
		gong.setEditor(user.getUsername());//修改人
		gong.setEditTime(DateTimeUtil.getAllCurrTime());//修改时间
		gList.add(gong);
		for (CkxCalendarGongyzq q : gList) {
			super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("workCalendar.insertCkxCalendarGongyzq", q);
		}
	}
	
	
	/**
	 * 添加年周序
	 * @param list
	 */
	private void addGongyzx(List<CkxCalendarCenter>  list,LoginUser user){
		//添加之前首先删除已生成的数据，避免主键冲突
		CkxCalendarGongyzx cond=new CkxCalendarGongyzx();
		cond.setGongyzx(list.get(0).getNianzx().substring(0, 4));
		super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("workCalendar.deleteCkxCalendarGongyzx", cond);
		String zx="",riq="",firstDate="";//存放第一天
		List<CkxCalendarGongyzx> gList=new ArrayList<CkxCalendarGongyzx>();
		CkxCalendarGongyzx gong=null;
		for (CkxCalendarCenter c : list) {
			//每个周期的第一天存放上个周期数据
			if(!zx.equals(c.getNianzx())){
				gong=new CkxCalendarGongyzx();
				gong.setGongyzx(zx);//工业周期
				gong.setKaissj(firstDate);//开始时间
				gong.setJiessj(riq);
				gong.setCreateTime(DateTimeUtil.getAllCurrTime());//创建时间
				gong.setCreator(user.getUsername());//创建人
        		gong.setEditor(user.getUsername());//修改人
        		gong.setEditTime(DateTimeUtil.getAllCurrTime());//修改时间
				if(!"".equals(zx)){gList.add(gong);}
				firstDate=c.getRiq();
			}
			riq=c.getRiq();
			zx=c.getNianzx();
		}
		//最后一周期的数据
		gong=new CkxCalendarGongyzx();
		gong.setGongyzx(zx);//工业周期
		gong.setKaissj(firstDate);//开始时间
		gong.setJiessj(riq);
		gong.setCreateTime(DateTimeUtil.getAllCurrTime());//创建时间
		gong.setCreator(user.getUsername());//创建人
		gong.setEditor(user.getUsername());//修改人
		gong.setEditTime(DateTimeUtil.getAllCurrTime());//修改时间
		gList.add(gong);
		for (CkxCalendarGongyzx q : gList) {
			super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("workCalendar.insertCkxCalendarGongyzx", q);
		}
	}
	/**
	 * 修改中心日历
	 * @param editList  需要修改的对象集合
	 * @return
	 */
	@Transactional
	public String edit(List<CkxCalendarCenter> editList,LoginUser user){
		for (CkxCalendarCenter ckxCalendarCenter : editList) {
			ckxCalendarCenter.setEditor(user.getUsername());
			ckxCalendarCenter.setEditTime(DateTimeUtil.getAllCurrTime());
			super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("workCalendar.updateCkxCalendarCenter", ckxCalendarCenter);
		}
		//保存成功
		return new Message("saveSuccess","i18n.ckx.workCalendar.i18n_workCalendar").getMessage();
	}
	/**
	 * 根据年份删除中心日历
	 * @param year
	 * @param center
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String delYear(String year,String center){
		CkxCalendarCenter ckxCalendarCenter =new CkxCalendarCenter();
		ckxCalendarCenter.setRiq(year);
		ckxCalendarCenter.setUsercenter(center);
		List<CkxCalendarCenter> list=super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("workCalendar.queryCkxCalendarCenterForVersion", ckxCalendarCenter);
		if(list==null||list.size()==0){
			//未找到对应的中心日历
			return new Message("notFindCenter","i18n.ckx.workCalendar.i18n_workCalendar").getMessage();
		}
		//物理删除
		super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("workCalendar.deleteCkxCalendarCenter", ckxCalendarCenter);
		//删除成功
		return new Message("deleteSuccess","i18n.ckx.workCalendar.i18n_workCalendar").getMessage();
	}
}