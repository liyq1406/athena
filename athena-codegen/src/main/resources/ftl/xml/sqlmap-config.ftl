<?xml version="1.0" encoding="UTF-8" ?>

<!DOCTYPE sqlMapConfig      
    PUBLIC "-//ibatis.apache.org//DTD SQL Map Config 2.0//EN"      
    "http://ibatis.apache.org/dtd/sql-map-config-2.dtd">
<sqlMapConfig>
	<settings cacheModelsEnabled="true" enhancementEnabled="true"
		lazyLoadingEnabled="true" maxRequests="32" maxSessions="10"
		maxTransactions="5" useStatementNamespaces="true" />
		
	<#list models as model>
		<sqlMap resource="${moduleRelativePath}/entity/${moduleName}/${model.simpleClassName}.xml" />
	</#list>
</sqlMapConfig>