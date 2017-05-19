<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE sqlMap PUBLIC "-//ibatis.apache.org//DTD SQL Map 2.0//EN" "http://ibatis.apache.org/dtd/sql-map-2.dtd">

<sqlMap namespace="${moduleName}">
	
	<resultMap class="${package}.${className}" id="${className}"><#list attributes as attribute>
		<result column="${attribute.column}" property="${attribute.name}" /></#list>
	</resultMap>
	<!-- 多记录查询 -->
	<select id="query${className}" resultMap="${className}">select <#list attributes as attribute>
		${attribute.column}<#if attribute.isLast!=true>,</#if></#list> from ${tableName} where 1=1
	</select>
	<!-- 单记录查询 -->
	<select id="get${className}" resultMap="${className}">select <#list attributes as attribute>
		${attribute.column}<#if attribute.isLast!=true>,</#if></#list> from ${tableName} where ${idAttribute.name}=#${idAttribute.column}#
	</select>
	<!-- 插入记录 -->
	<insert id="insert${className}" parameterClass="${package}.${className}">insert into
		${tableName} (<#list attributes as attribute>${attribute.column}<#if attribute.isLast!=true>,</#if></#list>)
		values(<#list attributes as attribute>
			#${attribute.name}#<#if attribute.isLast!=true>,</#if></#list>)
	</insert>
	<!-- 修改记录 -->
	<update id="update${className}" parameterClass="${package}.${className}">
		update ${tableName} set <#list attributes as attribute>
		${attribute.column}=#${attribute.name}#<#if attribute.isLast!=true>,</#if></#list> where
		1=1
		<dynamic>
			<isNotEmpty prepend="  and " property="${idAttribute.name}">${idAttribute.column} = #${idAttribute.name}#
			</isNotEmpty>
		</dynamic>
	</update>
	<!-- 删除记录 -->
	<delete id="delete${className}" parameterClass="${package}.${className}">
		delete from ${tableName} where 1=1
		<dynamic>
			<isNotEmpty prepend="  and " property="${idAttribute.name}">${idAttribute.column} = #${idAttribute.name}#
			</isNotEmpty>
		</dynamic>
	</delete>
</sqlMap>
