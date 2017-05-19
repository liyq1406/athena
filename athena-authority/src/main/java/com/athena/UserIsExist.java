package com.athena;

import org.apache.shiro.crypto.hash.Md5Hash;

import com.athena.authority.entity.User;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

/**
 * 判定用户是否存在 
 * @author Zhangl
 *
 */

@Component
public class UserIsExist {
	
	@Inject
	protected AbstractIBatisDao baseDao;
	
	private String username;
	private String passwords;
	
	/**
	 * 空构造, 注入对象的时候自动使用
	 */
	public UserIsExist(){
		
	}
	
	/**
	 * 构造函数 new 对象时候使用
	 * @param username
	 * @param passwords
	 * @param baseDao
	 */
	public UserIsExist( String username, String passwords, AbstractIBatisDao baseDao ){
		this.baseDao = baseDao;
		this.username = username;
		this.passwords = passwords;
	}
	
	/**
	 * 判定用户是否存在：构造对象后的使用方法
	 * @return boolean
	 * @throws RuntimeException
	 */
	public  boolean userIsExist() throws RuntimeException {
		
		boolean flag = true;
		
		try{
			
			if( null == baseDao ){
				flag = false;
				throw new Exception("Data Connection is null! ");
			}
			
			flag = isExist(username, passwords);
		
			return flag;
			
		}catch( Exception e){
			throw new RuntimeException(e.getMessage());
		}

	}
	
	/**
	 * 判定用户是否存在：注入类后的使用方法
	 * @param username
	 * @param passwords
	 * @return
	 * @throws RuntimeException
	 */
	public  boolean userIsExist( String username, String passwords ) throws RuntimeException {

		boolean flag = true;
		try{
			
			if( null == baseDao ){
				flag = false;
				throw new Exception("Data Connection is null! ");
			}
			
			flag = isExist(username, passwords);
			
			return flag;
			
		}catch( Exception e){
			throw new RuntimeException(e.getMessage());
		}

	}
	

	private boolean isExist( String username, String passwords ) throws Exception {
		
		boolean flag = true;
		
		User param = new User();
		param.setLoginname(username);
		
		User foundedUser = null;//存储登录的用户
		String dataPasswords = "";
		
		foundedUser = (User)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("authority.getUserByLoginname",param);
		
		if( null == foundedUser){
			throw new Exception("sorry, username is not exist!");
		}else{
			
			dataPasswords = foundedUser.getPassword();				
			passwords = new Md5Hash(passwords).toHex();
			
			if( !dataPasswords.equals(passwords)){
				throw new Exception("sorry, passwords is wrong!");
			}
			
		}
		
		return flag;
		

	}

}
