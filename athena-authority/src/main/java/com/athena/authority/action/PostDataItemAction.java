/**
 * 
 */
package com.athena.authority.action;

import java.util.Map;

import com.athena.authority.entity.PostDataItem;
import com.athena.authority.service.PostDataItemService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class PostDataItemAction extends ActionSupport {
	@Inject
	private PostDataItemService postDataItemService;

	/**
	 * 主页面
	 * @param bean
	 * @return
	 */
	public String execute(@Param PostDataItem bean) {
		return  "select";
	}

	/**
	 * 分页查询
	 * @param bean
	 * @return
	 */
	public String query(@Param PostDataItem bean) {
		setResult("result", postDataItemService.select(bean));
		return AJAX;
	}
	
	/**
	 * 集合查询
	 * @param bean
	 * @return
	 */
	public String list(@Param PostDataItem bean) {
		setResult("result", postDataItemService.list(bean));
		return AJAX;
	}
	
	/**
	 * 单记录查询
	 * @param bean
	 */
	public String get(@Param PostDataItem bean) {
		setResult("result", postDataItemService.get(bean));
		return AJAX;
	}
	/**
	 * 数据保存
	 * @param bean
	 */
	public String save(@Param PostDataItem bean) {
		setResult("result", postDataItemService.save(bean));
		return AJAX;
	}
	
	/**
	 * 数据删除
	 * @return
	 */
	public String remove(@Param PostDataItem bean) {
		setResult("result", postDataItemService.doDelete(bean));
		return AJAX;
	}
	/**
	 * 根据dataid获取用户组数据权限组项
	 * @param dataId
	 * @return
	 */
	public String listPostDataItemByDataId(@Param("dataId") String dataId,@Param("postCode") String postCode){
		
		Map map = postDataItemService.listPostDataItemByDataId(dataId,postCode);
		setResult("result",map);
		return AJAX;
	}
}
