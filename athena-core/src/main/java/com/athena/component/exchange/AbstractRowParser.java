/**
 * 
 */
package com.athena.component.exchange;

import java.util.ArrayList;
import java.util.List;

import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;
import com.athena.component.runner.Command;
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
public abstract class AbstractRowParser<T> implements RowParser<T> {
	public DataWriter getDataWriter() {
		return dataWriter;
	}

	public void setDataWriter(DataWriter dataWriter) {
		this.dataWriter = dataWriter;
	}


	private static final int DEFAULT_POOL_SIZE = 10;//开启的线程数量
	
	private static final int DEFAULT_COMMIT_SIZE = 20;//事务提交数量
	
	private static final int DEFAULT_BATCH_COMMONDS = 100;//每组运行的命令总量
	
	//改造 --> 输出：支持输出 生成多文件
//	private Runner runner = new Runner(DEFAULT_POOL_SIZE);
	
	protected DataWriter dataWriter;
	
	protected RunnerService runnerService;//线程运行服务 
	
	protected DataParserConfig config;
	
	protected List<Command<Record>> commands = new ArrayList<Command<Record>>();
	//文件名
	protected String fileName;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public AbstractRowParser(DataParserConfig config,
			DataWriter dataWriter,
			RunnerService runnerService){
		this.config = config;
		this.dataWriter = dataWriter;
		this.runnerService = runnerService;
		
//		//创建runner对象  Datawriter对象不是txt的 就是默认创建线程池大小为20,否则就创建以writer属性个数为大小的线程池
//		DataParserConfig dc = dataWriter.getDataParserConfig();
//		if(dc!=null && dc.getWriterConfigs()!=null){
//			runner = new Runner(dc.getWriterConfigs().length);
//		}else{
//			runner = new Runner(DEFAULT_POOL_SIZE);
//		}
	}
	
	@Override
	public void parse(int rowIndex, T rowObject) {
		commands.add(createParseRowCommand(dataWriter,rowIndex,rowObject));
		
		//改造 --> 输出：支持输出 生成多文件
		String writer = dataWriter.getDataParserConfig().getGroupConfig().getWriter();
		if(rowIndex%DEFAULT_BATCH_COMMONDS==0){
		//	runnerService.runBlock(DEFAULT_POOL_SIZE,commands.toArray(new Command[commands.size()]))
			
			try{
				if("txt".equals(writer)){
					runnerService.runBlock(1,commands.toArray(new Command[commands.size()]));
				}else{
					runnerService.runBlock(DEFAULT_POOL_SIZE,commands.toArray(new Command[commands.size()]));
				}
			}catch(Exception e){
				runnerService.getRunner().shutdown();
				while(!runnerService.getRunner().isTerminated()){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						e.printStackTrace();
					}
				}
				
				//有一条错误就回滚
				if("db".equals(writer)){
					dataWriter.rollback();
				}
				
				throw new RuntimeException(e.getMessage());
			}finally{
				commands = new ArrayList<Command<Record>>();
				//关闭线程池
				runnerService.getRunner().shutdown();
			}

		}
		
		//不为 db的时候才做 提交操作
		if(!"db".equals(writer)){
			if(rowIndex%DEFAULT_COMMIT_SIZE==0){
				dataWriter.commit();
			}
		}
	}

	@Override
	public void complete() {
		//
		//改造 --> 输出：支持输出 生成多文件
		String writer = dataWriter.getDataParserConfig().getGroupConfig().getWriter();
		
		try{
			if("txt".equals(writer)){
				runnerService.runBlock(1,commands.toArray(new Command[commands.size()]));
			}else{
				runnerService.runBlock(DEFAULT_POOL_SIZE,commands.toArray(new Command[commands.size()]));
			}
		}catch(Exception e){
			runnerService.getRunner().shutdown();
			while(!runnerService.getRunner().isTerminated()){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e.printStackTrace();
				}
			}
			//有一条错误就回滚
			if("db".equals(writer)){
				dataWriter.rollback();
			}
			throw new RuntimeException(e.getMessage());
		}finally{
			//改造 --> 输出：支持输出 生成多文件 关闭线程池
			//runner.shutdown();		
			runnerService.getRunner().shutdown();
			while(!runnerService.getRunner().isTerminated()){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			if(dataWriter instanceof DbDataWriter){
				if(((DbDataWriter)dataWriter).getErrorFileName().get(fileName)!=null){
					//说明解析文件有错误,则回滚此文件对应的记录
					dataWriter.rollback();
				}else{
					dataWriter.commit();
				}
			}else{
				dataWriter.commit();
			}			
			commands.clear();
		}
	}
	
	
	protected abstract ParseLineCommand<T> createParseRowCommand(DataWriter dataWriter, int rowIndex, T rowObject);
}
