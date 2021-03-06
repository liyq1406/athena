package com.athena.excore.template.export;

import java.io.ByteArrayOutputStream;

import java.util.Map;

import com.athena.util.exception.ServiceException;

/**
 * @Author: 王冲
 * @Email:jonw@sina.com
 * @Date: 2012-4-23
 * @Time: 上午10:55:33
 * @Description :将数据根据模板文件处理成文件流
 */
public interface IProcessData {

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-4-23
	 * @Time: 上午11:29:07
	 * @param ftlName
	 *            模板文件
	 * @param dataSource
	 *            数据源
	 * @return OutputStream
	 * @throws ServiceException
	 * @Description: 处理后的文件 流
	 */
	public ByteArrayOutputStream processFileStream(String ftlName,
			Map<String, Object> dataSource) throws ServiceException;

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-4-23
	 * @Time: 上午11:29:07
	 * @param ftlName
	 *            模板文件
	 * @param dataSource
	 *            数据源
	 * @return byte[]
	 * @throws ServiceException
	 * @Description: 处理后的文件 流
	 */
	public byte[] processByteCache(String ftlName,
			Map<String, Object> dataSource) throws ServiceException;


}
