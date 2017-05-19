package com.athena.xqjs.module.utils.xls.kanbyhl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jxl.read.biff.BiffException;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.kanbyhl.Kanbxhgm;
import com.athena.xqjs.module.common.Const;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * 按照XML文件处理
 * @author chenlei
 * @vesion 1.0
 * @date 2012-7-26
 */

public class XmlSheetHandlertiaozxhgm extends SheetHandlertiaozxhgm {
	private AbstractIBatisDao baseDao;
	private static Properties prop;
	private String editor;
	private String nwb;
	private boolean zbcZxc;
	private int countErr = 0;
	protected static Logger logger = Logger.getLogger(XmlSheetHandlertiaozxhgm.class);	//定义日志方法
	public XmlSheetHandlertiaozxhgm(Document document, String sheet, String table,
			String datasourceId, String clazz, String keys, String dateColumns,
			String dateFormats) {
		super(document, sheet, table, datasourceId, clazz, keys, dateColumns,
				dateFormats);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String doSheetHandler(String editor,String nwb,AbstractIBatisDao baseDao,boolean zbcZxc) throws BiffException, IOException, ParseException {
		this.editor = editor;
		this.nwb = nwb;
		this.baseDao = baseDao;
		this.zbcZxc = zbcZxc;
		// 对sheet处理 保存入库
		Element sheetElment = getSheetElment(sheet);
		int rowNum = getRowNumber(sheetElment);
		//导入的数据超过5000行 提示不让插入
		if(rowNum>5002){
			String  message = "导入数据超过5000行,请调整数据行数";
			return message;
			//throw new ServiceException("导入数据超过5000行,请调整数据行数");
		}
		int coluNum = getCellNumber(sheetElment);
		ArrayList<String> fieldList = new ArrayList<String>(); // 字段名 集合
		ArrayList<String> fieldValue = new ArrayList<String>(); // 一列的值
		List<Kanbxhgm> datas = new ArrayList<Kanbxhgm>();	//用于存储可以插入数据库的对象的集合
		StringBuffer wronginfo=new StringBuffer("");
		boolean flag = true;	//用来校验每一行数据的正误
		List<String> mosList = new ArrayList<String>();
		mosList = this.getGonghms(nwb);
		List<Element> list = sheetElment.selectNodes("xmlns:Table/xmlns:Row");
		for (int i = 2; i < list.size(); i++) {
			flag = true;	//初始化正误
			Kanbxhgm xhgm = new Kanbxhgm();
			xhgm.setEditor(editor);
			Element rowElement =  (Element) list.get(i);
			fieldValue=getCellValue(rowElement,fieldList.size());
			StringBuffer err = new StringBuffer("");
			for (int j = 0; j < coluNum; j++) {
				if( j == 0){//用户中心必须为2位大写字母
					if(null!=fieldValue.get(j)&& !"".equals(fieldValue.get(j))&&fieldValue.get(j).matches("^[A-Za-z]{2}$")){
						xhgm.setUsercenter(fieldValue.get(j).toUpperCase());
						err.append("用户中心"+fieldValue.get(j));
					}else {
						flag = false;
						err.append("用户中心"+fieldValue.get(j)+"不能为空且必须为2位字母或数字 ");
					}
				}else if( j == 1  && j< fieldValue.size()){//零件编号必须为10位大写字母或数字
					if(null!=fieldValue.get(j)&& !"".equals(fieldValue.get(j))&&fieldValue.get(j).matches("^[A-Za-z0-9]{10}$")){
						xhgm.setLingjbh(fieldValue.get(j).toUpperCase());
						err.append("零件编号"+fieldValue.get(j));
					}else {
						flag = false;
						err.append("零件编号"+fieldValue.get(j)+"不能为空且必须为10位字母或数字 ");
					}
				}else if(j == 2 && j< fieldValue.size()){//供货模式不能为空且必须符合内外部
					if(null!=fieldValue.get(j)&& !"".equals(fieldValue.get(j))&&mosList.contains(fieldValue.get(j).toUpperCase())){
						xhgm.setGonghms(fieldValue.get(j).toUpperCase());
						err.append("供货模式"+fieldValue.get(j));
					}else {
						flag = false;
						err.append("供货模式"+fieldValue.get(j)+"不能为空且必须符合内/外部模式 ");
					}
				}else if(j == 3 && j< fieldValue.size()){//客户编号不能为空
					if(null!=fieldValue.get(j)&& !"".equals(fieldValue.get(j))){
						xhgm.setKehd(fieldValue.get(j).toUpperCase());
						err.append("客户编号"+fieldValue.get(j));
					}else {
						flag = false;
						err.append("客户编号"+fieldValue.get(j)+"不能为空 ");
					}
				}else if(j == 4 && j< fieldValue.size()){//循环规模可以为空，如果不为，必须为1-999正整数
					if(null!=fieldValue.get(j)&&!"".equals(fieldValue.get(j))){
						if (fieldValue.get(j).matches("^[1-9][0-9]{0,2}$")) {
							xhgm.setXiafxhgm(BigDecimal.valueOf(Double.valueOf((fieldValue.get(j)))));
						}else {
							flag = false;
							err.append("循环规模"+fieldValue.get(j)+"必须为1-999正整数");
						}
					}
				}else if(j == 5 && j< fieldValue.size()){//冻结解冻状态可以为空
					if(null!=fieldValue.get(j)&&!"".equals(fieldValue.get(j))){
						xhgm.setDongjjdzt(fieldValue.get(j).toUpperCase());
					}else {
						xhgm.setDongjjdzt("");
					}
				}
			}
			if (flag) {
				datas.add(xhgm);
			}else {
				//第 i + 1 行 
				countErr ++;
				if (countErr<=5) {
					wronginfo.append("第"+(i+1)+"行：");
					wronginfo.append(err.toString());
					wronginfo.append("\n");
				}
			}
		}
		
		String updateMessage = sqlUpdate(datas);
		
		wronginfo.append(updateMessage);
		
		return wronginfo.toString();
	}
  
	/**
	 * 根据集合执行更新操作
	 * @param datasnew
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public String sqlUpdate(List<Kanbxhgm> datasnew){
		//先查看当前数据库中的所有看板循环规模
		List<Kanbxhgm> listKB = getAllKanbxhgm();
		//将查出的看板循环放入map
		Map<String, Kanbxhgm> mapKB = new HashMap<String, Kanbxhgm>();
		if (null != listKB && listKB.size()>0) {
			for (int i = 0; i < listKB.size(); i++) {
				mapKB.put(listKB.get(i).getUsercenter()+listKB.get(i).getLingjbh()+listKB.get(i).getGonghms()+listKB.get(i).getKehd(), listKB.get(i));
			}
			listKB.clear();
			StringBuffer message = new StringBuffer("");
			//StringBuffer dongjjd = new StringBuffer("");
			int countAll = 0;
			int count = 0;
			//需要三个集合，一个存储不进行冻结解冻操作的，一个存储解冻操作的，一个存储冻结操作的
			List<Kanbxhgm> ndjList = new ArrayList<Kanbxhgm>();	//不进行冻结解冻操作
			List<Kanbxhgm> jList = new ArrayList<Kanbxhgm>();	//解冻操作
			List<Kanbxhgm> dList = new ArrayList<Kanbxhgm>();	//冻结操作
			for (int i = 0; i < datasnew.size(); i++) {
				String dongjjdzt = datasnew.get(i).getDongjjdzt();		//冻结解冻状态
				if (dongjjdzt != null && dongjjdzt.equals("正常")) {			//解冻操作集合
					jList.add(datasnew.get(i));
				}else if (dongjjdzt != null && dongjjdzt.equals("冻结")) {	//冻结操作集合
					dList.add(datasnew.get(i));
				}else {									//不冻结解冻集合
					ndjList.add(datasnew.get(i));		
				}
			}
			//冻结操作
			int xdCount = 0;
			int dCount = 0;
			if (null != dList && dList.size()>0) {
				logger.info("要执行冻结操作的行数："+dList.size());
				xdCount = dList.size();
				for (int i = 0; i < dList.size(); i++) {
					//需要查看冻结解冻状态
					Kanbxhgm kb = new Kanbxhgm();
					logger.info("用户中心"+dList.get(i).getUsercenter()+",零件编号"+dList.get(i).getLingjbh()+",供货模式"+dList.get(i).getGonghms()+",客户编号"+dList.get(i).getKehd()+"开始执行冻结操作\n");
					if (mapKB.containsKey(dList.get(i).getUsercenter()+dList.get(i).getLingjbh()+dList.get(i).getGonghms()+dList.get(i).getKehd())) {
						kb = mapKB.get(dList.get(i).getUsercenter()+dList.get(i).getLingjbh()+dList.get(i).getGonghms()+dList.get(i).getKehd());
						kb.setEditor(editor);
						//直接冻结
						if (kb.getDongjjdzt().equals("1")) {//未冻结，可以进行冻结操作
							dCount += this.updateDongj(kb);
						}else {
							countErr ++;
							if (countErr<=5) {
								message.append("用户中心"+dList.get(i).getUsercenter()+",零件编号"+dList.get(i).getLingjbh()+",供货模式"+dList.get(i).getGonghms()+",客户编号"+dList.get(i).getKehd()+"已冻结，不再次冻结\n");
							}
							logger.info("用户中心"+dList.get(i).getUsercenter()+",零件编号"+dList.get(i).getLingjbh()+",供货模式"+dList.get(i).getGonghms()+",客户编号"+dList.get(i).getKehd()+"已冻结，不再次冻结\n");
						}
						//不更新循环规模
					}else {
						countErr ++;
						if (countErr<=5) {
							message.append("用户中心"+dList.get(i).getUsercenter()+",零件编号"+dList.get(i).getLingjbh()+",供货模式"+dList.get(i).getGonghms()+",客户编号"+dList.get(i).getKehd()+"数据不存在，不更新\n");
						}
						logger.info("用户中心"+dList.get(i).getUsercenter()+",零件编号"+dList.get(i).getLingjbh()+",供货模式"+dList.get(i).getGonghms()+",客户编号"+dList.get(i).getKehd()+"数据不存在，不更新\n");
					}
				}
				logger.info("全部冻结操作执行完毕，共冻结的行数："+dCount);
			}
			
			//解冻操作
			int xjCount = 0;
			int jCount = 0;
			if (null != jList && jList.size()>0) {
				logger.info("要执行解冻操作的行数："+jList.size());
				xjCount = jList.size();
				for (int i = 0; i < jList.size(); i++) {
					//需要查看冻结解冻状态
					Kanbxhgm kb = new Kanbxhgm();
					logger.info("用户中心"+jList.get(i).getUsercenter()+",零件编号"+jList.get(i).getLingjbh()+",供货模式"+jList.get(i).getGonghms()+",客户编号"+jList.get(i).getKehd()+"开始执行解冻操作\n");
					if (mapKB.containsKey(jList.get(i).getUsercenter()+jList.get(i).getLingjbh()+jList.get(i).getGonghms()+jList.get(i).getKehd())) {
						kb = mapKB.get(jList.get(i).getUsercenter()+jList.get(i).getLingjbh()+jList.get(i).getGonghms()+jList.get(i).getKehd());
						kb.setEditor(editor);
						if (kb.getDongjjdzt().equals("0")) {//冻结状态，需要先解冻
							if (!zbcZxc) {
								//先更新要货令表
								this.updateCkYaohl(kb, this.nwb);
							}
							//再进行解冻
							jCount += this.updateJied(kb);
						}
						//如果解冻成功，再根据是否有循环规模判断是否更新
						Kanbxhgm newKB = this.getKanbxhgm(jList.get(i));
						if (newKB.getDongjjdzt().equals("1")) {
							BigDecimal tiaozxugm = jList.get(i).getXiafxhgm();
							if (tiaozxugm != null && !tiaozxugm.equals("")) {
								count =updateTiaozxhgm(jList.get(i));
								if (count == 0) {
									//更新行数为0
									countErr ++;
									if (countErr<=5) {
										message.append("用户中心"+jList.get(i).getUsercenter()+",零件编号"+jList.get(i).getLingjbh()+",供货模式"+jList.get(i).getGonghms()+",客户编号"+jList.get(i).getKehd()+"更新失败\n");
									}
									logger.info("用户中心"+jList.get(i).getUsercenter()+",零件编号"+jList.get(i).getLingjbh()+",供货模式"+jList.get(i).getGonghms()+",客户编号"+jList.get(i).getKehd()+"更新失败\n");
								}else {
									/*if (!zbcZxc) {
										//更新看板循环交付总量
										this.doUpdate(kb);
									}*/
								}
								countAll += count;
							}
						}else {
							countErr ++;
							if (countErr<=5) {
								message.append("用户中心"+jList.get(i).getUsercenter()+",零件编号"+jList.get(i).getLingjbh()+",供货模式"+jList.get(i).getGonghms()+",客户编号"+jList.get(i).getKehd()+"解冻操作失败，不更新\n");
							}
							logger.info("用户中心"+jList.get(i).getUsercenter()+",零件编号"+jList.get(i).getLingjbh()+",供货模式"+jList.get(i).getGonghms()+",客户编号"+jList.get(i).getKehd()+"解冻操作失败，不更新\n");
						}
					}else {
						countErr ++;
						if (countErr<=5) {
							message.append("用户中心"+jList.get(i).getUsercenter()+",零件编号"+jList.get(i).getLingjbh()+",供货模式"+jList.get(i).getGonghms()+",客户编号"+jList.get(i).getKehd()+"数据不存在，不更新\n");
						}
						logger.info("用户中心"+jList.get(i).getUsercenter()+",零件编号"+jList.get(i).getLingjbh()+",供货模式"+jList.get(i).getGonghms()+",客户编号"+jList.get(i).getKehd()+"数据不存在，不更新\n");
					}
				}
				logger.info("全部解冻操作执行完毕，共解冻的行数："+jCount);
			}
			
			//不冻结解冻
			if (null != ndjList && ndjList.size()>0) {
				logger.info("不执行冻结解冻操作的行数："+ndjList.size());
				for (int i = 0; i < ndjList.size(); i++) {
					//需要查看冻结解冻状态
					Kanbxhgm kb = new Kanbxhgm();
					if (mapKB.containsKey(ndjList.get(i).getUsercenter()+ndjList.get(i).getLingjbh()+ndjList.get(i).getGonghms()+ndjList.get(i).getKehd())) {
						kb = mapKB.get(ndjList.get(i).getUsercenter()+ndjList.get(i).getLingjbh()+ndjList.get(i).getGonghms()+ndjList.get(i).getKehd());
						kb.setEditor(editor);
						if (kb.getDongjjdzt().equals("1")) {//解冻状态，可以更新
							//根据是否有循环规模判断是否更新
							BigDecimal tiaozxugm = ndjList.get(i).getXiafxhgm();
							if (tiaozxugm != null && !tiaozxugm.equals("")) {
								count =updateTiaozxhgm(ndjList.get(i));
								if (count == 0) {
									//更新行数为0
									countErr ++;
									if (countErr<=5) {
										message.append("用户中心"+ndjList.get(i).getUsercenter()+",零件编号"+ndjList.get(i).getLingjbh()+",供货模式"+ndjList.get(i).getGonghms()+",客户编号"+ndjList.get(i).getKehd()+"更新失败\n");
									}
									logger.info("用户中心"+ndjList.get(i).getUsercenter()+",零件编号"+ndjList.get(i).getLingjbh()+",供货模式"+ndjList.get(i).getGonghms()+",客户编号"+ndjList.get(i).getKehd()+"更新失败\n");
								}else {
									/*if (!zbcZxc) {
										//更新看板循环交付总量
										this.doUpdate(kb);
									}*/
								}
								countAll += count;
							}else {
								countErr ++;
								if (countErr<=5) {
									message.append("用户中心"+ndjList.get(i).getUsercenter()+",零件编号"+ndjList.get(i).getLingjbh()+",供货模式"+ndjList.get(i).getGonghms()+",客户编号"+ndjList.get(i).getKehd()+"没有循环规模，不更新\n");
								}
								logger.info("用户中心"+ndjList.get(i).getUsercenter()+",零件编号"+ndjList.get(i).getLingjbh()+",供货模式"+ndjList.get(i).getGonghms()+",客户编号"+ndjList.get(i).getKehd()+"没有循环规模，不更新\n");
							}
						}else {
							countErr ++;
							if (countErr<=5) {
								message.append("用户中心"+ndjList.get(i).getUsercenter()+",零件编号"+ndjList.get(i).getLingjbh()+",供货模式"+ndjList.get(i).getGonghms()+",客户编号"+ndjList.get(i).getKehd()+"已冻结，需要先解冻才能更新\n");
							}
							logger.info("用户中心"+ndjList.get(i).getUsercenter()+",零件编号"+ndjList.get(i).getLingjbh()+",供货模式"+ndjList.get(i).getGonghms()+",客户编号"+ndjList.get(i).getKehd()+"已冻结，需要先解冻才能更新\n");
						}
					}else {
						countErr ++;
						if (countErr<=5) {
							message.append("用户中心"+ndjList.get(i).getUsercenter()+",零件编号"+ndjList.get(i).getLingjbh()+",供货模式"+ndjList.get(i).getGonghms()+",客户编号"+ndjList.get(i).getKehd()+"数据不存在，不更新\n");
						}
						logger.info("用户中心"+ndjList.get(i).getUsercenter()+",零件编号"+ndjList.get(i).getLingjbh()+",供货模式"+ndjList.get(i).getGonghms()+",客户编号"+ndjList.get(i).getKehd()+"数据不存在，不更新\n");
					}
				}
			}
			if (message.length()==0) {
				message.append("更新了调整循环规模的数据共有" + countAll + "条\n");
			}else {
				message.append("更新了调整循环规模的数据共有" + countAll + "条\n");
			}
			message.append("模板中需要冻结的数据共有"+xdCount+"条，实际冻结"+dCount+"条\n");
			message.append("模板中需要解冻的数据共有"+xjCount+"条，实际解冻"+jCount+"条\n");
			return message.toString();
		}
		return "没有看板循环数据，无法进行导入更新操作！";
		
	}
	
	/**
	 * 根据条件查询出看板循环规模，条件为用户中心、零件编号、供货模式、客户编号
	 */
	public Kanbxhgm getKanbxhgm(Kanbxhgm param){
		return (Kanbxhgm) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("kanbyhl.getKBXHByParam", param);
	}
	
	/**
	 * 根据内外部查询模式
	 * @return
	 */
	public List<String> getGonghms(String nwb){
		List<String> list = new ArrayList<String>();
		if (null != nwb && nwb.equals("n")) {
			list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kanbyhl.getAllNGHMS");
		}else if (null != nwb && nwb.equals("w")) {
			list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kanbyhl.getAllWGHMS");
		}
		return list;
	}
	
	/**
	 * 查询出所有看板循环规模
	 */
	public List<Kanbxhgm> getAllKanbxhgm(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kanbyhl.getAllKBXH");
	}
	
	/**
	 * 逐行更新看板循环规模
	 * @param kanbxhgm
	 * @return
	 */
	public Integer updateTiaozxhgm(Kanbxhgm kanbxhgm){
		kanbxhgm.setShengxzt(Const.KANBXH_SHENGX);
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kanbyhl.updateTiaozxhgm",
				kanbxhgm);
		return count;
	} 
	
	/**
	 * 更新看板循环交付总量
	 * @param kanbxhgm
	 * @return
	 */
	public Integer doUpdate(Kanbxhgm kanbxhgm){
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kanbyhl.updateYjfzl",
				kanbxhgm);
		return count;
	}
	
	/**
	 * 逐行更新要货令，内部更新内部，外部更新外部
	 * @param kanbxhgm
	 * @return
	 */
	public Integer updateCkYaohl(Kanbxhgm kanbxhgm, String nwb){
		int count = 0;
		if (nwb.equals("n")) {
			count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kanbyhl.updateCkYaohln",kanbxhgm);
		}else if (nwb.equals("w")) {
			count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kanbyhl.updateCkYaohl",kanbxhgm);
		}
		
		return count;
	} 
	
	/**
	 * 逐行冻结
	 * @param kanbxhgm
	 * @return
	 */
	public Integer updateDongj(Kanbxhgm kanbxhgm){
		kanbxhgm.setShengxzt(Const.KANBXH_SHENGX);
		kanbxhgm.setShifgxxfgm("YN");
		kanbxhgm.setDongjjdzt(Const.KANBXH_YIDONGJ);
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kanbyhl.updateDongj",
				kanbxhgm);
		return count;
	} 
	
	/**
	 * 逐行解冻
	 * @param kanbxhgm
	 * @return
	 */
	public Integer updateJied(Kanbxhgm kanbxhgm){
		kanbxhgm.setDongjjdzt(Const.KANBXH_WEIDONGJ);
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kanbyhl.updateJied",
				kanbxhgm);
		return count;
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

	/*private String resultMessage(int index, String message) 
	{
		StringBuffer flagSb = new StringBuffer();
		flagSb.append("  导入文件中第").append(index).append("行,").append(message);
		return flagSb.toString();
	}*/
	
}
