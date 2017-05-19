<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<c:set var="ctx" value="${'$'}{pageContext.request.contextPath}" />
<%@ taglib uri="http://www.isoftstone.com/sdc/toft3" prefix="t"%>
<%@ taglib uri="http://www.isoftstone.com/swan-base" prefix="base"%>

  <t:page title="" i18n="" borderlayout="false">
					
	<t:grid idField="${id}" id="${modelClassName}Grid" url="/${moduleName}/query${modelClassName}.do" height="300"
		rownumbers="true" searchFormId="" dataFormId="${modelClassName}Dataform"
		onSelect="">
			<#list attributes as attribute>
			<t:column field="${attribute.name}" header="${attribute.description}" align="center" width="110" />
			</#list>
	</t:grid>
	
	<t:dataform actionurl="/${moduleName}/${modelName}.ajax"  dataFormId="${modelClassName}Dataform" idfield="${id}" 
			 afterdelete="" aftersave="">
			<ul class="c2 l120 clearfix">
			<#list attributes as attribute>
				<li><label for="${attribute.name}">${attribute.description}</label><input type="text" id="${attribute.name}" name="${attribute.name}" />
				</li>
			</#list>
			</ul>
		</t:dataform>
		<div id="buttons" class=" clearfix buttons">
		<input type="button" id="btnAdd" value="${'$'}{i18n.add }" class="sysBtn"/> 
		<input type="button" id="btnCancel" value="${'$'}{i18n.cancel }" class="sysBtn"  /> 
		<input type="button" id="btnSave" value="${'$'}{i18n.save }" class="sysBtn"  />  
   	 	<input type="button" id="btnDel" value="${'$'}{i18n.delete}" class="sysBtn" /> 
  	</div>
</t:page>