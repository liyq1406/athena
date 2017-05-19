package com.athena.component.exchange;

import com.athena.component.exchange.config.DataParserConfig;
import com.athena.util.exception.ServiceException;

/**
 * Created with IntelliJ IDEA.
 * User: yufei
 * Date: 12-12-13
 * Time: 上午11:57
 * 输入接口
 */
public interface InputDataService {
    /**
     * 输入数据
     */
    public boolean read(DataParserConfig dataParserConfig) throws ServiceException;

    /**
     * 数据文件解析前回调
     * @author Hezg
     * @date 2013-1-31
     */
    public void before();
    
    /**
     * 行记录解析前回调
     * @author Hezg
     * @date 2013-1-31
     * @param line
     * @param fileName
     * @param lineNum
     * @return 
     */
    public boolean beforeRecord(String line, String fileName, int lineNum);
}
