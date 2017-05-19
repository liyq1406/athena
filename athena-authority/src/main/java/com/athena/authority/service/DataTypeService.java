/**
 * 代码声明
 */
package com.athena.authority.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.entity.DataType;
import com.athena.authority.util.PropertyLoader;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;
import com.toft.ui.tags.tree.DefaultHtmlTreeNode;
import com.toft.ui.tags.tree.HtmlTreeNode;

@Component
public class DataTypeService extends BaseService{
	/**
	 * 获取模块空间
	 */
	 //log4j日志初始化
	private final Log log = LogFactory.getLog(DataTypeService.class); 
	
	
	@Override
	protected String getNamespace() {
		return "authority";
	}

	/**
	 * @param code
	 * @return
	 */
	public HtmlTreeNode getDataItemTree(String code) {
		DataType dataType = null;
		dataType = getDataTypeByCode(code);
		if(dataType==null){
			return new DefaultHtmlTreeNode("tree_root_"+code,"还未配置数据项");
		}
		
		//sql类型数据权限 //TODO 其他类型
		String itemSql = dataType.getDataParam();
		
		HtmlTreeNode tree = 
			new DefaultHtmlTreeNode(dataType.getDataId(),dataType.getDataName());
		if(dataType!=null){
			tree = buildTreeBySql(tree,itemSql);
		}
		return tree;
	}
	
	@SuppressWarnings("unchecked")
	private HtmlTreeNode buildTreeBySql(HtmlTreeNode tree,String itemSql){
		Map<String,String> params = new HashMap<String,String>();
		params.put("itemSql", itemSql);
		List<Map<String, String>> items = new ArrayList<Map<String, String>>();
		try {
			items = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(getNamespace()+".getDataItems",
					params);
		} catch (Exception e) {
			//TODO
			log.error("获取数据类型失败");
			
		}
		for(Map<String,String> item:items){
			tree.addChild(new DefaultHtmlTreeNode(item.get("value"),item.get("text")));
		}
		return tree;
	}
	/**
	 * 
	 * @param code
	 * @return
	 */
	public DataType getDataTypeByCode(String code){
		Map<String,String> params = new HashMap<String,String>();
		params.put("dataCode", code);
		Object result = 
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject(getNamespace()+".getDataTypeByCode", params);
		return result==null?null:(DataType)result;
	}

	public Object validateOnlyDataType(DataType bean) {
		Integer count = (Integer)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject(getNamespace()+".validateOnlyDataType", bean);
		String msg = "";
		if(count>0){
			msg = "1";
		}else if(count==0){
			msg = "0";
		}
		return msg; 
	}

	public Object refreshAllData() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("authority.getAllData");
	}

	public void validatedataParam(String dataParam){
		PropertyLoader pl = new PropertyLoader();
		String newParam = pl.getPropertyInfo(dataParam);
		//gswang 2012-09-27 mantis 0004307
		newParam = pl.getNewDataSql(newParam,"");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("authority.validatedataParam",newParam);
	}
}