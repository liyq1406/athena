<?xml version="1.0" encoding="UTF-8"?>
<toft>
	<package name="${moduleName}" extend="toft-default" namespace="/${moduleName}">
		<#list models as model>
		<!-- ${model.caption} -->
		<action name="${model.name}" 
			class="${model.name}Action" method="execute">
			<result name="select">/WEB-INF/pages/<#if childProject??>${childProject}/</#if>${moduleName}/${model.name}/${model.name}.jsp</result>
		</action>
		
		<action name="query${model.simpleClassName}" class="${model.name}Action" method="query"/>
		<action name="list${model.simpleClassName}" class="${model.name}Action" method="list"/>
		<action name="get${model.simpleClassName}" class="${model.name}Action" method="get"/>
		<action name="save${model.simpleClassName}" class="${model.name}Action" method="save"/>
		<action name="remove${model.simpleClassName}" class="${model.name}Action" method="remove"/>
		</#list>
	</package>
</toft>