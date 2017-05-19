/**
 * 
 */
package com.athena.component.exchange.db;

import java.util.Map;

import com.athena.component.exchange.AbstractRowParser;
import com.athena.component.exchange.DataWriter;
import com.athena.component.exchange.ParseLineCommand;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.RowParser;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.runner.RunnerService;

/**
 * <p>Title:</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright:Copyright (c) 2011.9</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author Administrator
 * @version 1.0
 */
public class DbRowParser extends AbstractRowParser<Map<String,Object>>  implements RowParser<Map<String,Object>>{
	
	public DbRowParser(DataParserConfig config, DataWriter dataWriter,
			RunnerService runnerService) {
		super(config, dataWriter, runnerService);
	}
	
	@Override
	protected ParseLineCommand<Map<String, Object>> createParseRowCommand(
			DataWriter dataWriter, int rowIndex, Map<String, Object> rowObject) {	
		return new ParseResultSetCommand(dataWriter,rowIndex,rowObject);		
	}

	class ParseResultSetCommand extends ParseLineCommand<Map<String,Object>>{

		public ParseResultSetCommand(DataWriter dataWriter, int rowIndex,
				Map<String,Object> rowObject) {
			super(dataWriter, rowIndex, rowObject);
		}

		@Override
		public Record buildRecord() {
			Record record = new Record();
			record.putAll(rowObject);
			return record;
		}
		
	}
}

