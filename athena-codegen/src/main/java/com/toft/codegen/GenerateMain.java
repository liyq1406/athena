/*
 * Copyright (c) 2010-2018, 
 * All rights reserved.
 */
package com.toft.codegen;

import java.util.List;

import org.youi.codegen.model.ModelFactory;
import org.youi.codegen.pdm.PdmReader;

/**
 * 
 * 代码配置文件（generator.properties）关键说明
 *  <p>#项目路径</p>
	<p>generator.home=E:/testCode/code</p>
	<p>#页面路径</p>
	<p>generator.page.home=E:/testCode/page</p>
	<p>#包前缀</p>
	<p>package.prefix=com.athena.demo</p>
	<p>#项目名称,比如 athena-demo = demo</p>
	<p>generator.childProject=demo</p>
 * 
 * @author zhouyi
 * @since 0.0.1
 * @time 2011-7-27 上午10:48:01
 */
public class GenerateMain {
	// 本地模型文件存放地址
	private static final String MODEL_FILE_PATH =  
		"E:\\application\\workspace-athena\\athena\\athena-codegen\\src\\main\\resources\\models\\model-all.xml";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		generateCodeFromPdm("F:\\vfile\\youi-c.pdm");//从PDM生成地址对应的本地模型文件
		generateCode("admin");//从本地模型文件生成模块的代码，初始的模型配置文件
//		generateCode("pd","warehouseStorage");//生成模块下实体对象的全套代码
	}
	
	/**
	 * 根据pdm文件生成模型文件
	 * @param pdmFile
	 */
	public static void generateCodeFromPdm(String pdmFile){
		List<String> modules = parsePdm(pdmFile);
//		for(String moduleName:modules){
//			generateCode(moduleName);
//		}
	}
	
	/**
	 * 解析pdm文件
	 * @param pdmFile
	 * @return
	 */
	public static List<String> parsePdm(String pdmFile){
		PdmReader pdr = new PdmReader(pdmFile);
		pdr.generatorModels(getModelFileName());
		return pdr.getModules();
	}
	
	/**
	 * 生成模块代码
	 * @param moduleName
	 */
	public static void generateCode(String moduleName){
		ModelFactory.getInstance().registerModelFile(getModelFileName());
		GenerateEngine.getInstance().generate(moduleName);
	}
	
	/**
	 * 生成模块中的模型代码
	 * @param moduleName
	 * @param modelName
	 */
	public static void generateCode(String moduleName,String modelName){
		ModelFactory.getInstance().registerModelFile(getModelFileName());
		GenerateEngine.getInstance().generate(moduleName,modelName);
	}
	
	/**
	 * @return
	 */
	private static String getModelFileName(){
		return MODEL_FILE_PATH;
	}
}
