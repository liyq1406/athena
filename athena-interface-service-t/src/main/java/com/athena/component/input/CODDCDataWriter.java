package com.athena.component.input;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

public class CODDCDataWriter extends DbDataWriter {
	protected static Logger logger = Logger.getLogger(CODDCDataWriter.class);	//定义日志方法
	protected List<String> list = new ArrayList<String>();  //总的集合
	protected String compDate = "";  //存放临时比较数据

	public CODDCDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	
	/**
	 * 解析前操作	
	 */
	@Override
	public boolean before() {
		try{
        //String sql="delete from "+SpaceFinal.spacename_ddbh+".ddbh_coddcxhdlj";
		String sql = "truncate table "+SpaceFinal.spacename_ddbh+".ddbh_coddcxhdlj";
        super.execute(sql);
//        String sqlstr="delete from "+SpaceFinal.spacename_ddbh+".in_coddc";
        String sqlstr="truncate table "+SpaceFinal.spacename_ddbh+".in_coddc";
        super.execute(sqlstr);
		}catch(RuntimeException e){
			logger.error(e.getMessage());
		}
		return super.before();
	}
	/**
	 * 行解析前处理
	 */
	@Override
	public boolean beforeRecord(int rowIndex, Object line) {
		if (rowIndex==1) {// 文件第一行不导入表
			return false;
		}
		if(line!=null&&line.toString().contains("PDS—ATHENA")){
			return false;
		}
		return true;
		
	}
	
	@Override
	public void afterRecord(int rowIndex, Record record,Object line){
		
			try{ 
					if(line!=null){
							String readLine = line.toString();
					    	String CODDC=readLine.substring(0, 7).trim(); 
					    	String lingj=readLine.substring(7, 18).trim(); //零件号
					    	String xiaohd=readLine.substring(18, 27).trim(); //消耗点
					    	String sydw=readLine.substring(27, 37).trim();  //单位
					    	String xhdxs=readLine.substring(37, 53).trim(); //消耗点系数
					    	double  d_xhdxs=Double.parseDouble(xhdxs);
					    	String EMON=readLine.substring(53).trim();    //EMON商业化时间
					    	String usercenter="U"+xiaohd.substring(0, 1); //用户中心
				    		String[] emon=EMON.split(",");
				        	for(int y=0;y<emon.length;y++){ 
				        		//list.add(compDate);
				        		String stardateStr="";
				        		String enddateStr="";
				        		String[] dataStr=null;
				        		if(emon[y].indexOf("-")!=-1){
				        			//如果时间为20120501-20120502
				        			dataStr=emon[y].split("-");
				        			stardateStr=DateTimeUtil.DateStr(dataStr[0]);
				        			enddateStr=DateTimeUtil.DateStr(dataStr[1]);
				        			//是否存相同的数据
				        			//compDate = usercenter+CODDC+lingj+xiaohd+ stardateStr;
				        			AddTable(usercenter,CODDC,lingj,xiaohd,sydw,d_xhdxs,stardateStr,enddateStr);
				        		}else{
				        			String em = DateTimeUtil.DateStr(emon[y].toString());
				        			AddTable(usercenter,CODDC,lingj,xiaohd,sydw,d_xhdxs,em, em);
				        		}
								super.INSERT_COUNT++;
								setFILE_INSERT_COUNT(INSERT_COUNT);
				        	}
						}
				
			}catch(Exception e){
					super.makeErrorMessage(rowIndex, record, "消耗点和需件编号不存在！", line);
			}
	
	}
	@Override
	public void after() {
		try{
	        String sql="delete from "+SpaceFinal.spacename_ddbh+".in_coddc where danw is null";
	        super.execute(sql);
	        String sqlStr = "DELETE FROM "+SpaceFinal.spacename_ddbh+".IN_CODDC A WHERE ROWID !=(SELECT MAX(ROWID) "
	        			  + " FROM "+SpaceFinal.spacename_ddbh+".IN_CODDC B WHERE A.USERCENTER=B.USERCENTER AND A.CODDC=B.CODDC "
	        			  + " AND A.LINGJ = B.LINGJ AND A.XIAOHD = B.XIAOHD AND A.ECOMQSSJ = B.ECOMQSSJ) ";
	        super.execute(sqlStr);
	        String sqlInsert = "INSERT INTO "+SpaceFinal.spacename_ddbh+".DDBH_CODDCXHDLJ SELECT USERCENTER,CODDC,LINGJ,XIAOHD,"
                			 + " SHENGCX,ECOMQSSJ,ECOMJSSJ,XIAOHXS,CHULZT,"
						     + " DANW,ZHIZLX,CREATOR,CREATE_TIME from "+SpaceFinal.spacename_ddbh+".IN_CODDC ";
	        super.execute(sqlInsert);
			}catch(RuntimeException e){
				logger.error(e.getMessage());
			}
        //System.out.println("运行结束！");
	}
	
	/**
	 * 在参考系分配区表里取出生产线号
	 * @return
	 */
	public Map GetChanx(String usercenter,String xiaohd){
		String chanx="";
		Map map=null;
		try{
		StringBuffer strbuf=new StringBuffer();
		strbuf.append("select shengcxbh from "+SpaceFinal.spacename_ddbh+".ckx_fenpq ");
		strbuf.append("where usercenter = '"+strNull(usercenter)+"' and fenpqh = substr('"+strNull(xiaohd)+"', 1, 5)");
	    map=selectOne(strbuf.toString());
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return map;
	}
	
	/**
	 * 新增CODDC-消耗点零件表数据
	 * @param dataStr
	 * @param xhdxs
	 * @throws Exception 
	 * @throws SQLException 
	 */
	
	public void AddTable(String usercenter,String CODDC,String lingj,String xiaohd,String sydw,double d_xhdxs,
			String dataStartStr,String dataEndStr) throws SQLException{
            Map map=null;
     		try {

			    map= GetChanx(usercenter,xiaohd); //得到生产线号
				StringBuffer sqlbuf=new StringBuffer();
				sqlbuf.append("insert into "+SpaceFinal.spacename_ddbh+".in_coddc (USERCENTER,CODDC,LINGJ,XIAOHD,DANW,XIAOHXS,SHENGCX,ECOMQSSJ,ECOMJSSJ,CHULZT,CREATOR,CREATE_TIME) values(");
				sqlbuf.append("'"+strNull(usercenter)+"',");
				sqlbuf.append("'"+strNull(CODDC)+"',");
				sqlbuf.append("'"+strNull(lingj)+"',");
				sqlbuf.append("'"+strNull(xiaohd)+"',");
				sqlbuf.append("'"+strNull(sydw)+"',");
				sqlbuf.append(""+d_xhdxs+",");
				sqlbuf.append("'"+strNull(map.get("shengcxbh"))+"',");
				sqlbuf.append("to_date('"+strNull(dataStartStr)+"','yyyy-MM-dd'),");
				sqlbuf.append("to_date('"+strNull(dataEndStr)+"','yyyy-MM-dd'),"); //add by pan.rui
				sqlbuf.append("'0',");
				sqlbuf.append("'interspace',");
				sqlbuf.append("sysdate)");
				execute(sqlbuf.toString());

			} catch(Exception e){
               throw new SQLException (e.getMessage());
			}
    }
	
	
	/**
	 * 空串处理
	 * 
	 * @param obj
	 *            对象
	 * @return 处理后字符串
	 * @author GJ
	 * @date 2011-10-26
	 */
	private String strNull(Object obj) {// 对象为空返回空串,不为空toString
		return obj == null ? "" : obj.toString();
	}
    
    
     

}
