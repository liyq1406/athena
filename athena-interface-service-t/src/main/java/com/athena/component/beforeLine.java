package com.athena.component;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.athena.component.exchange.config.DataParserConfig;

public class beforeLine {

	/**
	 * 统计所有行数之和
	 * @param dataParserConfig
	 * @return int
	 */
	public static int LineNum(DataParserConfig dataParserConfig) {
		FileInputStream file = null;
		InputStreamReader is = null;
		BufferedReader br = null;
		int i = 0;
		try {
			file = new FileInputStream(dataParserConfig.getReaderConfig()
					.getFilePath());
			is = new InputStreamReader(file, dataParserConfig.getReaderConfig()
					.getEncoding());
			br = new BufferedReader(is);
			while (null != br.readLine()) {
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

}
