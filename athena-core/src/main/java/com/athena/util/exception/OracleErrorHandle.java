package com.athena.util.exception;

import java.util.HashMap;
import java.util.Map;


/**
 * oracle 数据库错误码解析类
 * @author zhangl 2012-04-09
 *
 */
public class OracleErrorHandle {
	
	private static OracleErrorHandle singleton;
	private Map<String,String> errorMap = new HashMap<String,String>();
	
	private OracleErrorHandle(){
		
		errorMap.put("904", "SQL语句错误：无效列名！");
		errorMap.put("942", "数据库错误：表或者视图不存在！");
		errorMap.put("1400", "数据库错误：不能将空值插入！");
		errorMap.put("936", "SQL语句错误：缺少表达式！");
		errorMap.put("933", "SQL语句错误：SQL 命令未正确结束！");
		errorMap.put("1722", "SQL语句错误：无效数字，可能是企图将字符串类型的值填入数字型而造成！");
		errorMap.put("6530", "SQL语句错误：企图将值写入未初化对象的属性！");
		errorMap.put("6592", "SQL语句错误：case语句格式有误，没有分支语句！");
		errorMap.put("6531", "SQL语句错误：企图将集合填入未初始化的嵌套表中！");
		errorMap.put("6511", "SQL语句错误：企图打开已经打开的指针．指针已经打开，要再次打开必须先关闭！");
		
		errorMap.put("1", "对不起，该数据已存在！");
		errorMap.put("1001", "SQL语句错误：无效指针，非法指针操作！");
		errorMap.put("1017", "数据库错误：企图用无效的用户名或密码登录oracle！");
		errorMap.put("1403", "数据库错误：无数据发现！");
		errorMap.put("1012", "对不起，未与数据库建立连接！");
		errorMap.put("6501", "pl/sql系统问题！");
		errorMap.put("06504", "行类型不匹配！");
		errorMap.put("30625", "SELF_IS_NULL！");
		errorMap.put("6500", "PL/SQL运行内存溢出或内存冲突！");
		errorMap.put("6533", "SQL语句错误：子句超出数量！");
		
		errorMap.put("6532", "SQL语句错误：子句超出数量！");
		errorMap.put("6533", "SQL语句错误：子句非法数量！");
		errorMap.put("1410", "SQL语句错误：无效的字段名，sql语句中有不存在的字段！");
		errorMap.put("51", "对不起，资源等待超时！");
		errorMap.put("1422", "SQL语句错误：预期返回一行，但实际返回数据集超过一行！");
		errorMap.put("6502", "对不起，插入或者更新的值与对应的数据库字段约束不符！");
		errorMap.put("1476", "SQL语句错误：除0错误！");
	}
	
	public static OracleErrorHandle getInstance() {
		if( null == singleton ){
			singleton = new OracleErrorHandle();
		}
		return singleton;
	}
	
	/**
	 * 转换错误码成通俗语言
	 * @param errorCode
	 * @param message
	 * @return
	 */
	public String translateError( int errorCode, String message ){
		
		String msg = errorMap.get(String.valueOf(errorCode));
		
		if( null == msg || "".equals(msg)){
			msg = message;
		}

		return msg;
	}
	
/**
ORA-00904:  invalid column name 无效列名
ORA-00942:  table or view does not exist 表或者视图不存在
ORA-01400:  cannot insert NULL into () 不能将空值插入
ORA-00936:　缺少表达式
ORA-00933:　SQL 命令未正确结束
ORA-01722:　无效数字：（一般可能是企图将字符串类型的值填入数字型而造成）
ORA-06530:  ACCESS_INTO_NULL　
      Your program attempts to assign values to the attributes of an uninitialized (atomically  null) object.
企图将值写入未初化对象的属性
ORA-06592:  CASE_NOT_FOUND
     None of th  choice  in the WHEN clauses of a CASE statement is selected,  and there is no  ELSE clause.
case语句格式有误，没有分支语句
ORA-06531:  COLLECTION_IS_NULL
     Your program attempts to apply collection methods othe  than EXIST  to an uninitialized(atomically  null) nested table or varray, or th  program attempts to assign values to the elements of an  uninitialized nested table  or  varray.
企图将集合填入未初始化的嵌套表中
ORA-06511:  CURSOR_ALREADY_OPEN
     Your program attempts to open an already open cursor. A cursor must be closed before it can  be reopened.  A cursor FOR loop automatically opens the cursor to which it refers. So, your  program cannot open that cursor inside  the  loop.
企图打开已经打开的指针．指针已经打开，要再次打开必须先关闭．
ORA-00001:  DUP_VAL_ON_INDEX
     Your program attempts to store duplicate values in a database column that is constrained by  a unique index.
数据库字段存储重复，主键唯一值冲突
ORA-01001:  INVALID_CURSOR　无效指针
    Your program attempts an illegal cursor operation such as closing an unopened cursor.
非法指针操作，例如关闭未打开的指针
ORA-01017:  LOGIN_DENIED　拒绝访问
     Your program attempts to log on to Oracle with an invalid username and/or password.
企图用无效的用户名或密码登录oracle
ORA-01403:  NO_DATA_FOUND  无数据发现
     A SELECT INTO statement returns no rows, or your program references a deleted element in a  nested table  or an  uninitialized  element  in  an  index-by  table.  SQL  aggregate  functions  such  as  AVG  and  SUM  always  return  a value or a null.  So, a SELECT INTO statement that calls an aggregate function  never  raises  NO_DATA_FOUND. The FETCH statement is  expected to return no rows eventually, so when that happens, no exception is raised.
ORA-01012:  NOT_LOGGED_ON   未登录
     Your program issues  a database call without being connected to Oracle.
程序发送数据库命令，但未与oracle建立连接
ORA-06501:  PROGRAM_ERROR  程序错误
     PL/SQL  has  an  internal  problem.
pl/sql系统问题
ORA-06504:  ROWTYPE_MISMATCH  行类型不匹配
    The  host  cursor  variable  and  PL/SQL  cursor  variable  involved  in  an  assignment  have  incompatible  return  types. 
For example, when an open host cursor variable is passed to a stored  subprogram, the return types of the actual and formal parameters must be compatible. 
ORA-30625:  SELF_IS_NULL  
     Your  program  attempts  to  call  a  MEMBER  method  on  a  null  instance.  That  is,  the  built-in  parameter  SELF (which  is  always  the  first  parameter  passed  to  a  MEMBER  method)  is  null.
ORA-06500:  STORAGE_ERROR  存储错误
     PL/SQL runs out of memory  or memory has been corrupted.
PL/SQL运行内存溢出或内存冲突
ORA-06533:  SUBSCRIPT_BEYOND_COUNT   子句超出数量
      Your program references a nested table or varray element using an index number larger than the number of elements in  the collection.
ORA-06532:  SUBSCRIPT_OUTSIDE_LIMIT   子句非法数量
Your program references a nested table or varray element using an index number (-1  for  example)  that  is outside  the  legal  range.
ORA-01410:  SYS_INVALID_ROWID   无效的字段名
   The conversion  of  a  character  string  into  a  universal  rowid  fails  because  the  character  string  does  not represent  a  valid  rowid. 
ORA-00051:  TIMEOUT_ON_RESOURCE    资源等待超时
A time-out occurs while Oracle is waiting for a resource. 
ORA-01422:  TOO_MANY_ROWS    返回超过一行
A SELECT INTO statement returns more  han one row.
ORA-06502:  VALUE_ERROR   值错误
An arithmetic, conversion, truncation, or size-constraint error occurs. For example, when  your program selects a column value into a character  variable, if the value is  longer  than  the  declared  length  of  the  variable, PL/SQL aborts the assignment and raises VALUE_ERROR.  In procedural statements,  VALUE_ERROR  is raised if the conversion of a character string into a number fails.  (In SQL statements, INVALID_NUMBER is raised.) 
ORA-01476:  ZERO_DIVIDE  除0错误
Your program attempts to divide a number by zero. 
 */

}
