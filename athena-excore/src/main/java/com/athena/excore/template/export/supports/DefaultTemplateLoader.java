package com.athena.excore.template.export.supports;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.athena.excore.template.export.ITemplateLoader;
import com.athena.excore.template.export.LoaderTemplateProperties;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;

/**
 * @Author: 王冲
 * @Email:jonw@sina.com
 * @Date: 2012-4-23
 * @Time: 上午10:46:11
 * @Description :模板加载
 */
public class DefaultTemplateLoader implements ITemplateLoader {
	
	static Logger logger = Logger.getLogger(DefaultTemplateLoader.class);

	/* description: 加载器路经 */
	private List<TemplateLoader> fileLoaders = new ArrayList<TemplateLoader>();

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-4-23
	 * @Time: 上午10:52:43
	 * @throws IOException
	 * @Description: 初始化加载路经
	 * @throws IOException
	 */
	public void initTemplateLoader() throws IOException {

		
		//得到所有的要加载模板路经
		Set<String> tempPath  = LoaderTemplateProperties.loadTemplate();
		//迭代加载
		for(Iterator<String> it = tempPath.iterator();it.hasNext();){
			//模板路经
			String _path = it.next();
//			//去掉前面的'/'
//			if(_path.startsWith("/")){
//				_path = _path.substring(1) ;
//			}
			//增加前面的'/'
			if(!_path.startsWith("/")){
				_path = "/"+_path ;
			}
			//增加后面的'/'
			if(!_path.endsWith("/")){
				_path = _path+"/" ;
			}
			
			
			ClassTemplateLoader templateFile = new ClassTemplateLoader(getClass(),_path) ;
			
			fileLoaders.add(templateFile);
			
		}
		

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ahena.util.export.ITemplateLoader#getTemplateLoader()
	 */
	public MultiTemplateLoader getTemplateLoader() {

		TemplateLoader[] temp = new TemplateLoader[fileLoaders.size()];
		for (int i = 0; i < fileLoaders.size(); i++) {
			temp[i] = fileLoaders.get(i);
		}

		/** 初始化模板加载器 **/
		MultiTemplateLoader multi = new MultiTemplateLoader(temp);

		return multi;
	}

}
