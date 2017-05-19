/**
 * 代码声明
 */
package ${package};

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

import ${modelPath}.${modelClassName};

public class ${modelClassName}ServiceTests extends AbstractCompomentTests{
	
	@Inject
	private ${modelClassName}Service ${modelName}Service;
	/**
	 * 多记录查询
	 */
	@Test
	public void testList() {
		
	}
	/**
	 * 单记录查询
	 */
	@Test
	public void testGet() {
		
	}
	/**
	 * 保存
	 */
	@Test
	public void testSave() {
		
	}
	/**
	 * 分页查询
	 */
	@Test
	public void testSelect() {
		${modelClassName} bean = new ${modelClassName}();
		${modelName}Service.select(bean);
	}
}