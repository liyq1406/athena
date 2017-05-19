package com.athena.component.exchange;

import java.io.OutputStreamWriter;

import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.config.ExchangerConfig;
import com.athena.util.exception.ServiceException;

/**
 * Created with IntelliJ IDEA.
 * User: yufei
 * Date: 12-12-13
 * Time: 下午12:00
 * 输出接口
 */
public interface OutputDataService {
    /**
     * 输入数据
     */
    public boolean  write(DataParserConfig dataParserConfig) throws ServiceException;
    public void  afterAllRecords(ExchangerConfig[] ecs);
    public void fileAfter(ExchangerConfig write, ExchangerConfig read,OutputStreamWriter out);
}
