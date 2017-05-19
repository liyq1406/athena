<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/include.jsp"%>
<t:html title="${modelDescription}管理">
	<head>
		<%@ include file="/WEB-INF/pages/common/commonScriptAndCss.jsp"%>
		<script type="text/javascript">
			function initPage(){
				//页面初始化
			}
		</script>
	</head>
	<t:page>
		
		<!-- ${modelDescription}列表 -->
		<t:grid id="grid_${modelName}" 
			idKeys="${modelName}Id"
			dataFormId="form_${modelName}"
			caption="${modelDescription}列表" 
			src="query${modelClassName}.ajax"
			editSrc="get${modelClassName}.ajax"
			removeSrc="remove${modelClassName}.ajax">
			<t:fieldLayout>
			<#list attributes as attribute>
				<t:fieldText property="${attribute.name}" caption="${attribute.description}"/>
			</#list>
			</t:fieldLayout>
			<#list attributes as attribute>
				<t:gridCol property="${attribute.name}" caption="${attribute.description}"/>
			</#list>
		</t:grid>
		
		<!-- ${modelDescription}表单 -->
		<t:form dialog="true" id="form_${modelName}" caption="${modelDescription}" action="save${modelClassName}.ajax">
			<t:fieldLayout prefix="record">
				<#list attributes as attribute>
					<t:fieldText property="${attribute.name}" caption="${attribute.description}"/>
				</#list>
			</t:fieldLayout>
		</t:form>
	</t:page>
</t:html>