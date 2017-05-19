/**
 * 
 */
package ${package};

import com.toft.core2.ToftConst;
import com.toft.core2.UserInfo;
import com.toft.core2.dao.tablemap.TableOperator;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

import ${modelClassPath};
import ${serviceClassPath};

@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class ${modelClassName}Action extends ActionSupport {
	@Inject
	private ${modelClassName}Service ${modelName}Service;

	public UserInfo getUserInfo() {
		return (UserInfo) getSessionAttribute(ToftConst.Toft_SessionKey_UserData);
	}

	/**
	 * 主页面
	 * @param bean
	 * @return
	 */
	public String execute(@Param ${modelClassName} bean) {
		return  "select";
	}

	/**
	 * 分页查询
	 * @param bean
	 * @return
	 */
	public String query(@Param ${modelClassName} bean) {
		setResult("result", ${modelName}Service.select(bean));
		return AJAX;
	}
	
	/**
	 * 集合查询
	 * @param bean
	 * @return
	 */
	public String list(@Param ${modelClassName} bean) {
		setResult("result", ${modelName}Service.list(bean));
		return AJAX;
	}
	
	/**
	 * 单记录查询
	 * @param bean
	 */
	public String get(@Param ${modelClassName} bean) {
		setResult("result", ${modelName}Service.get(bean));
		return AJAX;
	}
	/**
	 * 数据保存
	 * @param bean
	 */
	public String save(@Param ${modelClassName} bean) {
		setResult("result", ${modelName}Service.save(bean));
		return AJAX;
	}
	
	/**
	 * 数据删除
	 * @return
	 */
	public String remove(@Param ${modelClassName} bean) {
		setResult("result", ${modelName}Service.doDelete(bean));
		return AJAX;
	}
}
