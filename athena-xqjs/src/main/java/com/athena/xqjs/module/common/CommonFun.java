package com.athena.xqjs.module.common;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.xqjs.entity.anxorder.UsercenterSet;
import com.athena.xqjs.entity.ycbj.Yicbj;
import com.athena.xqjs.module.diaobl.service.DiaobshService;
import com.athena.xqjs.module.ycbj.service.YicbjService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * 公共方法类
 * 
 * @author WL
 * @date 2011-10-24
 */
@SuppressWarnings("rawtypes")
@Component
public class CommonFun {

	/**
	 * new SimpleDateFormat("yyyy-MM-dd")
	 */
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public static final SimpleDateFormat yyyyMMddHHmm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	/**
	 * new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 */
	public static final SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static final Logger logger = Logger.getLogger(CommonFun.class);

	@Inject
	private DiaobshService diaobshService;
	@Inject
	private YicbjService yicbjservice;
	
	/**
	 * 获取在指定日期后加上天数，跨月跨年
	 * 
	 * @throws ParseException
	 */
	public static String getDayTime(String time, int index,String format) throws Exception {
		DateFormat dateFormat = new SimpleDateFormat(format);
		Calendar calendar = Calendar.getInstance();
		try {
			Date date = dateFormat.parse(time);
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_MONTH, index);
		} catch (ParseException e) {
			// e.printStackTrace();
			throw new Exception("com.athena.xqjs.module.common.CommonFun.getDayTime() Error!!!");
		}
		return dateFormat.format(calendar.getTime());
	}

	/**
	 * 获取程序时间
	 * 
	 * @author Niesy
	 * 
	 * @return time
	 */
	public static String getJavaTime() {
		Date date = new Date(System.currentTimeMillis());
		Format df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:sss");
		return df.format(date);
	}

	
	public static String getJavaTime(Date date) {
		Format df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}
	
	/**
	 * 获取程序时间 String format 格式
	 * 
	 * @author 李智
	 * 
	 * @return time
	 */
	public static String getJavaTime(String format) {
		Date date = new Date(System.currentTimeMillis());
		Format df = new SimpleDateFormat(format);
		return df.format(date);
	}

	/**
	 * 获取数据库时间
	 * 
	 * @author Niesy
	 * 
	 * @return time
	 */
	public String getDBTime() {
		return diaobshService.getSysdate();
	}

	/**
	 * 获取两个日期天数差值(日期,不包含时分秒)
	 * 
	 * @param date1
	 *            日期1
	 * @param date2
	 *            日期2
	 * @return 天数差值
	 */
	public static int daysOfDate(Date date1, Date date2) {
		return (int) ((date1.getTime() - date2.getTime()) / 1000 / 60 / 60 / 24);
	}
		
	
	/**
	 * BigDecimal非空判断
	 * 
	 * @param obj
	 *            参数对象
	 * @return BigDecimal结果,非空转换,为空返回ZERO
	 */
	public static BigDecimal getBigDecimal(Object obj) {
		if (obj != null) {
			return new BigDecimal(CommonFun.strNull(obj));
		} else {
			return BigDecimal.ZERO;
		}
	}
	
	/**
	 * BigDecimal非空判断
	 * @param obj参数对象
	 * @param num 为空时默认值
	 * @return BigDecimal结果,非空转换,为空返回默认值num
	 */
	public static BigDecimal getBigDecimal(Object obj,int num) {
		if (obj != null) {
			return new BigDecimal(CommonFun.strNull(obj));
		} else {
			return new BigDecimal(num);
		}
	}

	/**
	 * 获取map中value和的方法
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-09
	 * @参数说明：TreeMap map，传入一个TreeMap集合
	 */
	public static BigDecimal sumValue(Map map) {
		Collection value = map.values();
		BigDecimal sum = BigDecimal.ZERO;
		if (null != value) {
			Iterator<BigDecimal> iterm = value.iterator();
			while (iterm.hasNext()) {
				sum = sum.add(iterm.next());
			}
		}
		return sum;
	}

	/**
	 * 汇总map中的值
	 * 
	 * @version v1.0
	 * @date 2011-12-09
	 * @参数说明：TreeMap map
	 */
	public static Map<String, BigDecimal> sumMapValue(Map<String, BigDecimal> cangkMap, Map<String, BigDecimal> zongMap) {
		Map<String, BigDecimal> map = new TreeMap<String, BigDecimal>();
		// 将仓库map中的值汇总到总map中
		Set<String> keys = cangkMap.keySet();// 得到map的键值集合
		for (int i = 0; i < keys.toArray().length; i++) {
			if (zongMap.containsKey(keys.toArray()[i])) {
				zongMap.put((String) keys.toArray()[i],
						zongMap.get(keys.toArray()[i]).add(cangkMap.get(keys.toArray()[i])));
			} else {
				zongMap.put((String) keys.toArray()[i], cangkMap.get(keys.toArray()[i]));
			}
		}
		map = zongMap;
		return map;
	}

	public static String getDate(String key) {
		Date date = new Date();
		Map<String, String> map = new HashMap<String, String>();
		map.put("yyyy-MM-dd", "yyyy-MM-dd");
		map.put("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
		map.put("yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd hh:mm:ss");
		map.put("yyyy/MM/dd", "yyyy/MM/dd");
		map.put("yyyy", "yyyy");
		map.put("MM", "MM");
		map.put("dd", "dd");
		map.put("HH", "HH");
		map.put("hh", "hh");
		map.put("mm", "mm");
		map.put("ss", "ss");
		if (null == map.get(key)) {
			return "none";
		} else {
			SimpleDateFormat dateFormate = new SimpleDateFormat(map.get(key).toString());
			return dateFormate.format(date);
		}
	}

	/**
	 * 去除map中重复的value值
	 * 
	 * @version v1.0
	 * @date 2011-12-20 参数说明：treemap集合
	 */
	public static Map<Integer, String> clearRepeatValue(Map<Integer, String> map) {
		Map<Integer, String> maps = new TreeMap<Integer, String>();
		if (!map.isEmpty()) {
			int count = 0;
			for (int i = 0; i <= map.size(); i++) {		
				if (!maps.containsValue(map.get(i)) && null != map.get(i)) {
					maps.put(count, map.get(i));
					count++;	
				}
			}
		}
		return maps;
	}

    /**
     * 时间向后推指定日期的月份
     * @param startDate
     * @param monthNum
     * @return
     * @throws Exception
     */
    public  static  String   getEndDate(String   startDate,   int   monthNum)   throws   Exception   {
        String   resultDate;
        resultDate   =   " ";
        try   {
            Calendar   calendar   =   Calendar.getInstance();
            SimpleDateFormat   sdf   =   new   SimpleDateFormat("yyyy-MM-dd");
            calendar.setTime(sdf.parse(startDate));
            calendar.add(Calendar.MONTH,   monthNum);
            calendar.add(Calendar.DATE,   -1);
            Date   date   =   calendar.getTime();
            resultDate   =   sdf.format(date);
        }   catch   (Exception   ex)   {
        	ex.printStackTrace();
        }   finally   {

        }
        return   resultDate;
    }
	/**
	 * 空处理
	 * 
	 * @param obj
	 *            对象
	 * @return 空对象返回空串
	 */
	public static String strNull(Object obj) {
		if (obj != null) {
			return obj.toString();
		} else {
			return "";
		}
	}

	/**
	 * list拼接map方法
	 * 
	 * @param list
	 *            要拼接的list
	 * @return 拼接的map
	 */
	public static Map<String, Object> listToMap(List list) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("total", list.size());
		map.put("rows", list);
		return map;
	}

	/**
	 * 跨年周期 max为后面两位数字
	 * **/
	public static String addNianzq(String zhouq, String max, int index) {
		CommonFun.logger.debug("跨年周期addNianzq方法参数为zhouq="+zhouq+";max="+max+";index="+index);
		DecimalFormat data = new DecimalFormat("00");
		String front = zhouq.substring(0, 4);
		CommonFun.logger.debug("跨年周期addNianzq方法front="+front);
		String back = zhouq.substring(4, zhouq.length());
		CommonFun.logger.debug("跨年周期addNianzq方法back="+back);
		if (Integer.parseInt(zhouq) + index > Integer.parseInt(front + max)) {
			front = (Integer.parseInt(front) + 1) + "";
			CommonFun.logger.debug("跨年周期addNianzq方法在(Integer.parseInt(zhouq) + index > Integer.parseInt(front + max)情况下)front="+front);
			back = data.format((Integer.parseInt(back) + index - Integer.parseInt(max)));
			CommonFun.logger.debug("跨年周期addNianzq方法在(Integer.parseInt(zhouq) + index > Integer.parseInt(front + max)情况下)back="+back);
		} else {
			back = data.format(Integer.parseInt(back) + index);
			CommonFun.logger.debug("跨年周期addNianzq方法不在(Integer.parseInt(zhouq) + index > Integer.parseInt(front + max)情况下)back="+back);
		}
		return front + back;
	}

	/**
	 * 设置用户中心
	 * 
	 * @param map
	 * @return
	 */
	public static Map<String, String> setUsercenter(Map<String, String> map) {
		map.put("usercenter", AuthorityUtils.getSecurityUser().getUsercenter());
		return map;
	}

	public static Map<String, String> getPartten(String year, String month, String day) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Const.PP, Const.PP.substring(1) + year + month + day);
		map.put(Const.PM, Const.PM.substring(1) + year + month + day);
		map.put(Const.PJ, Const.PJ.substring(1) + year + month + day);
		map.put(Const.PS, Const.PS.substring(1) + year + month + day);
		map.put(Const.ANX_MS_CD, Const.CD + year + month + day);
		map.put(Const.ANX_MS_C1, Const.C1 + year + month + day);
		map.put(Const.ANX_MS_MD, Const.MD + year + month + day);
		map.put(Const.ANX_MS_M1, Const.M1 + year + month + day);
		map.put(Const.LS, Const.LS + year + month + day);
		map.put(Const.BJ, Const.BJ_LS + year + month + day);
		//0012820
		//map.put(Const.VJ, Const.VJ.substring(1) + year + month + day); 
		//map.put(Const.MJ, Const.MJ.substring(1) + year + month + day); 
		map.put(Const.VJ, "A" + year + month + day); 
		map.put(Const.MJ, "B" + year + month + day); 
		return map;
	}
	
	public static Map<String, String> getPartten(String year, String month, String day,UsercenterSet uset) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Const.PP, Const.PP.substring(1) + year + month + day);
		map.put(Const.PM, Const.PM.substring(1) + year + month + day);
		map.put(Const.PJ, Const.PJ.substring(1) + year + month + day);
		map.put(Const.PS, Const.PS.substring(1) + year + month + day);
		//0012819
		/*map.put(uset.getCD(), Const.CD + year + month + day);
		map.put(uset.getC1(), Const.C1 + year + month + day);
		map.put(uset.getMD(), Const.MD + year + month + day);
		map.put(uset.getM1(), Const.M1 + year + month + day);*/
		map.put(uset.getCD(), "W" + year + month + day);
		map.put(uset.getC1(), "X" + year + month + day);
		map.put(uset.getMD(), "Y" + year + month + day);
		map.put(uset.getM1(), "Z" + year + month + day);
		map.put(Const.LS, Const.LS + year + month + day);
		map.put(Const.BJ, Const.BJ_LS + year + month + day);
		return map;
	}

	/**
	 * 去掉数组中的的null以及“”和重复值
	 * **/
	public static String[] getArray(String[] array) {
		// 定义一个set集合，利用其不能重复的属性
		Set<String> listArray = new HashSet<String>();
		// 将不为null和“”的值放到集合中
		for (String string : array) {
			if (string != null && !"".equals(string.trim())) {
				listArray.add(string);
			}
		}
		// 定义个新的数组
		String[] newArray = new String[listArray.size()];
		// 迭代集合，将集合中的值付给数组
		Iterator<String> iter = listArray.iterator();
		int i = 0;
		while (iter.hasNext()) {
			newArray[i] = iter.next();
			i++;
		}
		return newArray;
	}

	/**
	 * 年周期，周序相减
	 * **/
	public static int subValue(String firstTime, String endTime, String maxTime) {
		String firstYear = firstTime.substring(0, 4);
		CommonFun.logger.debug("年周期，周序相减subValue方法firstYear="+firstYear);
		String endYear = endTime.substring(0, 4);
		CommonFun.logger.debug("年周期，周序相减subValue方法endYear="+endYear);
		String firstNian = firstTime.substring(4, firstTime.length());
		CommonFun.logger.debug("年周期，周序相减subValue方法firstNian="+firstNian);
		String endNian = endTime.substring(4, endTime.length());
		CommonFun.logger.debug("年周期，周序相减subValue方法firstNian="+firstNian);
		int value = 0;
		if (Integer.parseInt(firstTime) > Integer.parseInt(endTime)) {
		} else {
			if (firstYear.equals(endYear)) {
				value = Integer.parseInt(endNian) - Integer.parseInt(firstNian);
			} else {
				value = Integer.parseInt(maxTime) - Integer.parseInt(firstNian) + Integer.parseInt(endNian);
			}
		}
		CommonFun.logger.debug("年周期，周序相减subValue方法value="+value);
		return value;
	}

	public static String checkStringValue(String obj){
		String value = obj;
		if(obj==null||"".equals(obj)){
			value = "0" ;
		}
		return value ;
	}
	
	/**
	 * 截断拼接字符串
	 * @param str 原字符串
	 * @return 拼接后字符串
	 */
	public static String splitString(String str){
		StringBuilder sb = new StringBuilder("");
		String [] strs = str.split(",");
		for (int i = 0; i < strs.length; i++) {
			sb.append("'" + strs[i] + "',");
		}
		if(!sb.equals("")){
			sb.deleteCharAt(sb.lastIndexOf(","));
		}
		return sb.toString();
	}
	/**
	 * 异常报警批量信息
	 * @param 错误类型，错误信息，异常报警List，计划员组，替换字符数组，用户中心，零件编号，登录者类
	 * @return 异常报警list
	 * 陈骏
	 */
	
	public static List<Yicbj> insertError(String cuowlx,String message,List<Yicbj> errorList,String zu,String [] paramStr,String usercenter,String lingjbh,LoginUser loginuser,String jismk){
		try
		{
			for(int i=0;i<paramStr.length;i++){
				message = message.replace("%" + (i + 1), paramStr[i]);
			}
			Yicbj yicbj = new Yicbj();
			yicbj.setUsercenter(usercenter);
			yicbj.setLingjbh(lingjbh);
			yicbj.setCuowlx(cuowlx);
			yicbj.setCuowxxxx(message);
			yicbj.setJihyz(zu);
			String username = loginuser == null ? "SYSTEM" : loginuser.getUsername();
			yicbj.setJihydm(username);
			yicbj.setCreator(username);
			yicbj.setCreate_time(CommonFun.getJavaTime());
			errorList.add(yicbj);
			yicbj.setJismk(jismk);
		}
		catch (Exception e) {
			CommonFun.logger.error(e);
		}
		return errorList;
	}

	public static List<Yicbj> insertError(String cuowlx, String message, List<Yicbj> errorList, String zu, List<String> paramStr, String usercenter, String lingjbh, LoginUser loginuser, String jismk) {
		for (int i = 0; i < paramStr.size(); i++) {
			message = message.replace("%" + (i + 1), paramStr.get(i));
		}
		Yicbj yicbj = new Yicbj();
		yicbj.setUsercenter(usercenter);
		yicbj.setLingjbh(lingjbh);
		yicbj.setCuowlx(cuowlx);
		yicbj.setCuowxxxx(message);
		yicbj.setJihyz(zu);
		System.out.println();
		String username = loginuser == null ? "SYSTEM" : loginuser.getUsername();
		yicbj.setJihydm(username);
		yicbj.setCreator(username);
		yicbj.setCreate_time(CommonFun.getJavaTime());
		errorList.add(yicbj);
		yicbj.setJismk(jismk);
		return errorList;
	}
	/**
	 * 异常报警单个信息插入
	 * @param 错误类型，错误信息，计划员组，替换字符数组，用户中心，零件编号，登录者类
	 * @return
	 * 陈骏
	 */
	public  void  insertError(String cuowlx,String message,String zu,String [] paramStr,String usercenter,String lingjbh,LoginUser loginuser,String jismk){
		for(int i=0;i<paramStr.length;i++){
			message = message.replace("%" + (i + 1), paramStr[i]);
		}
		Yicbj yicbj = new Yicbj();
		yicbj.setUsercenter(usercenter);
		yicbj.setLingjbh(lingjbh);
		yicbj.setCuowlx(cuowlx);
		yicbj.setCuowxxxx(message);
		yicbj.setJihyz(zu);
		String username = loginuser == null ? "SYSTEM" : loginuser.getUsername();
		yicbj.setJihydm(username);
		yicbj.setCreator(username);
		yicbj.setCreate_time(CommonFun.getJavaTime());
		yicbj.setJismk(jismk);
		yicbjservice.insert(yicbj);
	}
	
	public void insertError(String cuowlx, String message, String zu, List<String> paramStr, String usercenter, String lingjbh, LoginUser loginuser, String jismk) {
		for (int i = 0; i < paramStr.size(); i++) {
			message = message.replace("%" + (i + 1), paramStr.get(i));
		}
		Yicbj yicbj = new Yicbj();
		yicbj.setUsercenter(usercenter);
		yicbj.setLingjbh(lingjbh);
		yicbj.setCuowlx(cuowlx);
		yicbj.setCuowxxxx(message);
		yicbj.setJihyz(zu);
		String username = loginuser == null ? "SYSTEM" : loginuser.getUsername();
		yicbj.setJihydm(username);
		yicbj.setCreator(username);
		yicbj.setCreate_time(CommonFun.getJavaTime());
		yicbj.setJismk(jismk);
		yicbjservice.insert(yicbj);
	}
	
	public void insertError(String cuowlx, String message, String zu, List<String> paramStr, String usercenter, String lingjbh, String loginuser, String jismk) {
		for (int i = 0; i < paramStr.size(); i++) {
			message = message.replace("%" + (i + 1), paramStr.get(i));
		}
		Yicbj yicbj = new Yicbj();
		yicbj.setUsercenter(usercenter);
		yicbj.setLingjbh(lingjbh);
		yicbj.setCuowlx(cuowlx);
		yicbj.setCuowxxxx(message);
		yicbj.setJihyz(zu);
		yicbj.setJihydm(loginuser);
		yicbj.setCreator(loginuser);
		yicbj.setCreate_time(CommonFun.getJavaTime());
		yicbj.setJismk(jismk);
		yicbjservice.insert(yicbj);
	}
	
	public static void listPrint(List list,String listName){
		 logger.debug("list循环打印开始：list名称为:"+listName);
		 for(int i=0;i<list.size();i++){
			 logger.debug("名称为"+listName+"的list中第"+i+"个变量为："+list.get(i));
		 }
	}
	
	/**
	 * list的log循环打印其中的object属性及其值
	 * @param list及其名称
	 * @return
	 * 陈骏
	 */
	public static void objListPrint(List list,String listName){
		 logger.debug("list循环打印开始：list名称为:"+listName);
		for(int i=0;i<list.size();i++){
			  Object obj = list.get(i);
			  String fieldName = obj.getClass().toString();
			  Field[] field = obj.getClass().getDeclaredFields(); 
			  logger.setLevel(( Level ) Level.DEBUG );
			  logger.debug("名称为"+listName+"内的变量类型为"+fieldName+"属性名称及其值如下：");
			  for(int j=0 ; j<field.length ; j++){ 
				  String name = field[j].getName();//获取属性的名字
				  if(!name.equals("serialVersionUID")){
				  if(name!=null && name!=""){
					  name  = name.substring(0,1).toUpperCase()+name.substring(1);
			         }
				  String type = field[j].getGenericType().toString(); //获取属性的类型
				  Object value = null;
				  try {
					  Method m;
						m = obj.getClass().getMethod("get"+name);
				  if(type.equals("class java.lang.String")){   //如果type是类类型，则前面包含"class "，后面跟类名
	                    value = (String) m.invoke(obj);    //调用getter方法获取属性值
	                }
	                if(type.equals("class java.lang.Integer")){
	                    value = (Integer) m.invoke(obj);
	                }
	                if(type.equals("class java.lang.Short")){
	                    value = (Short) m.invoke(obj);
	                }      
	                if(type.equals("class java.lang.Double")){
	                    value = (Double) m.invoke(obj);
	                }                 
	                if(type.equals("class java.lang.Boolean")){
	                    value = (Boolean) m.invoke(obj);
	                  
	                }
	                if(type.equals("class java.util.Date")){                  
	                    value = (Date) m.invoke(obj);
	                }
	                logger.debug("属性名称为"+name+"类型为"+type+"值为："+value);
				  } catch (SecurityException e) {
						// TODO Auto-generated catch block
					  throw new RuntimeException("CommonFun.objListPrint,SecurityException");
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						throw new RuntimeException("CommonFun.objListPrint,NoSuchMethodException");
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						throw new RuntimeException("CommonFun.objListPrint,IllegalArgumentException");
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						throw new RuntimeException("CommonFun.objListPrint,IllegalAccessException");
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						throw new RuntimeException("CommonFun.objListPrint,InvocationTargetException");
					}
			  }
			  }
		}
	}
	/**
	 * 打印单个的object属性及其值
	 * @param obj
	 * @return
	 * 陈骏
	 */
	public static void objPrint(Object obj,String objName){
	}
	/**
	 * map的log循环打印其中的key及其值
	 * @param map
	 * @return
	 * 陈骏
	 */
	public static void mapPrint(Map map,String mapName){
	}
	
	/**
	 * undefined转换
	 * @param str 字符串
	 * @return 转换后字符串
	 */
	public static String strUndefined(String str){
		if(CommonFun.strNull(str).equals("undefined")){
			return "";
		}
		return str;
	}
	
	/**
	 * 按包装向上取整
	 * @param shul 数量
	 * @param pack 包装容量
	 * @return 取整后数量
	 */
	public static BigDecimal roundingByPack(BigDecimal shul,BigDecimal pack){
		//比较数量和包装容量
		if(shul.compareTo(pack) > 0){
			//数量大于包装容量,需要多个包装
			//计算数量除包装容量,需要包装个数和余数
			BigDecimal[] shuls = shul.divideAndRemainder(pack); 
			//如果余数不为0,则表示数量无法整除,需要按包装取整
			if(shuls[1].compareTo(BigDecimal.ZERO) != 0){
				//计算取整后数量(包装个数+1)
				shul = (shuls[0].add(BigDecimal.ONE)).multiply(pack);
			}
		}else{
			//数量小于包装容量,则取整为包装容易,即一个包装
			shul = pack;
		}
		return shul;
	}
	
	/**
	 * 按包装向下取整
	 * @param shul 数量
	 * @param pack 包装容量
	 * @return 取整后数量
	 */
	public static BigDecimal roundingByPackDown(BigDecimal shul,BigDecimal pack){
		//比较数量和包装容量
		if(shul.compareTo(pack) > 0){
			//数量大于包装容量,需要多个包装
			//计算数量除包装容量,需要包装个数和余数
			BigDecimal[] shuls = shul.divideAndRemainder(pack); 
			//如果余数不为0,则表示数量无法整除,需要按包装取整
			if(shuls[1].compareTo(BigDecimal.ZERO) != 0){
				//计算取整后数量,向下取整(包装个数)
				shul = shuls[0].multiply(pack);
			}
		}else{
			//数量小于包装容量,则不要货
			shul = BigDecimal.ZERO;
		}
		return shul;
	}
	/**
	 * 转换字符串，把%20 替换成 空格 ,把%3A替换成:
	 * @param string
	 * @return
	 */
	public static String getStrByAsc(String string){
		if(null != string){
			//把%20 替换成 空格 ,把%3A替换成:
			string = string.replace("%20", " ").replace("%3A", ":");
		}
		return string;
	}
	
    public static  String sdfAxformat(Date date)throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
       return sdf.format(date);
   }
   
   public static Date sdfAxparse(String strDate) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
       return sdf.parse(strDate);
   }
	
    public static  String yyyyMMddHHmmssAxformat(Date date)throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       return sdf.format(date);
   }
   
   public static Date yyyyMMddHHmmssAxparse(String strDate) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       return sdf.parse(strDate);
   }
	
    public static  String yyyyMMddHHmmAxformat(Date date)throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
       return sdf.format(date);
   }
   
   public static Date yyyyMMddHHmmAxparse(String strDate) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
       return sdf.parse(strDate);
   }
}
