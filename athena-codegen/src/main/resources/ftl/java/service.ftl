/**
 * 代码声明
 */
package ${package};

import com.athena.component.service.BaseService;
import com.toft.core3.container.annotation.Component;
import ${modelPath}.${modelClassName};

@Component
public class ${modelClassName}Service extends BaseService<${modelClassName}>{
	
	@Override
	protected String getNamespace() {
		return "${moduleName}";
	}
	
}