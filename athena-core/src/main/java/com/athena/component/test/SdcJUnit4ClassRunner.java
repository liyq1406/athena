/**
 * 
 */
package com.athena.component.test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Rule;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.statements.Fail;
import org.junit.rules.MethodRule;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.toft.core3.container.ComponentsException;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.context.SdcContext;
import com.toft.core3.context.support.ClassPathSdcContext;
import com.toft.core3.dao.DataAccessException;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.core3.util.ReflectionUtils;

/**
 * @author Administrator
 *
 */
public class SdcJUnit4ClassRunner extends BlockJUnit4ClassRunner{
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private ClassPathSdcContext sdcContext;
	
	public SdcJUnit4ClassRunner(Class<?> klass) throws InitializationError {
		super(klass);
		try {
			sdcContext = new ClassPathSdcContext("classpath:config/sdc-config.xml");
		} catch (Exception e) {
			throw new InitializationError("sdc context error!"+e.getMessage());
		}
	}
	
	/*
	 * 生成初始化数据
	 */
	private void dataGenerator(TestData testData){
		if(testData==null)return;
		TestDataGenerator dataGenerator = null;
		final AbstractIBatisDao dao = 
			sdcContext.getComponent(AbstractIBatisDao.class);
		
		try {
			String dbUserName = 
				dao.getDataSource().getConnection().getMetaData().getUserName();
			if(!dbUserName.toUpperCase().endsWith("_TEST")){
				throw new RuntimeException("请使用测试数据库！");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(testData!=null){
			GeneratorType type = testData.type();
			if(type.equals(GeneratorType.XLS)){
				dataGenerator = new XlsTestDataGenerator(testData.locations());
			}else if(type.equals(GeneratorType.SQL)){
				dataGenerator = new SqlTestDataGenerator(testData.locations());
			}
			
			if(dataGenerator!=null){
				dataGenerator.generate();
				//删除表数据
				for(String tableName:dataGenerator.getClearTables()){
					Map<String,String> params = new HashMap<String,String>();
					params.put("tableName", tableName);
					try {
						dao.execute("component.clearTestData",params);
					} catch (DataAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				//插入数据
				//dao.execute("component.insertTestData",dataGenerator.getInsertParams());
				dataGenerator.visitTable(new RowCallBack(){
					/* (non-Javadoc)
					 * @see com.athena.component.test.RowCallBack#doRow(com.athena.component.test.InsertParameter)
					 */
					public void doRow(InsertParameter insertParameter){
						List<RowValues> rowValues = insertParameter.getRowValues();
						if(rowValues!=null){
							for(RowValues rowValue:rowValues){
								dao.execute("component.insertTestRowData",
										getParam(insertParameter.getTableName(),insertParameter.getHeaders(),
												rowValue));
							}
						}
					}
					
					/**
					 * 
					 * 组织插入参数
					 * @param tableName
					 * @param headers
					 * @param rowValue
					 * @return
					 */
					private Map<String,Object> getParam(String tableName, List<String> headers, RowValues rowValue){
						Map<String,Object> params = new HashMap<String,Object>();
						params.put("tableName", tableName);
						params.put("headers", headers);
						params.put("values", rowValue.getValues());
						return params;
					}
				});
			}
		}
	}

	@Override
	protected Statement withBeforeClasses(Statement statement) {
		Statement junitBeforeClasses = super.withBeforeClasses(statement);
		Annotation[] classAnnotations
			= getTestClass().getAnnotations();
		for(Annotation annotation:classAnnotations){
			if(annotation instanceof TestData){
				dataGenerator((TestData) annotation);
			}
		}
		return junitBeforeClasses;
	}
	
	@Override
	protected Statement withAfterClasses(Statement statement) {
		Statement junitAfterClasses = super.withAfterClasses(statement);
		return junitAfterClasses;
	}
	
	@Override
	protected Statement methodBlock(FrameworkMethod frameworkMethod) {
		Object testInstance;
		try {
			testInstance = new ReflectiveCallable() {
				@Override
				protected Object runReflectiveCall() throws Throwable {
					return createTest();
				}
			}.run();
		}
		catch (Throwable e) {
			return new Fail(e);
		}
		//构建测试的准备数据
		this.dataGenerator(frameworkMethod.getAnnotation(TestData.class));
		
		Field[] fields = testInstance.getClass().getDeclaredFields();
		
		for(Field field:fields){
			Inject inject = field.getAnnotation(Inject.class);
			
			if(field.getType().equals(SdcContext.class)){
				field.setAccessible(true);
				
				ReflectionUtils.setField(field, testInstance, sdcContext);
			}else if(inject!=null){
				Object component = null;
				try {
					component = sdcContext.getComponent(field.getType());
				} catch (ComponentsException e) {
					logger.warn("未找到组件："+e.getMessage());
				}
				
				field.setAccessible(true);
				ReflectionUtils.setField(field, testInstance, component);
			}
		}
		Statement statement = methodInvoker(frameworkMethod, testInstance);
		
//		statement = possiblyExpectingExceptions(frameworkMethod, testInstance, statement);
//		statement = withBefores(frameworkMethod, testInstance, statement);
//		statement = withAfters(frameworkMethod, testInstance, statement);
//		statement = withRulesReflectively(frameworkMethod, testInstance, statement);
//		statement = withPotentialRepeat(frameworkMethod, testInstance, statement);
//		statement = withPotentialTimeout(frameworkMethod, testInstance, statement);
		statement = withRules(frameworkMethod,testInstance,statement);
		return statement;
	}
	
	private Statement withRules(FrameworkMethod method, Object target,
			Statement statement) {
		Statement result= statement;
		for (MethodRule each : getTestClass().getAnnotatedFieldValues(target,
				Rule.class, MethodRule.class))
			result= each.apply(result, method, target);
		return result;
	}
}
