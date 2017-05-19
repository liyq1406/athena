package com.athena;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.athena.authority.AuthorityConstants;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.db.ConstantDbCode;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.core3.web.context.WebSdcContext;
import com.toft.core3.web.context.support.WebSdcContextUtils;
/**
 * 根据value的值：控制跳转按钮的显示
 * 	name:ck 做仓库的权限验证，有仓库权限就显示按钮
 *        ckx 做参考系权限验证，有参考系权限就显示按钮
 * @author chenlei
 * @vesion 1.0
 * @date 2012-7-31
 */
public class ChangeTag extends TagSupport {
	private final String  CK = "CK";
	private final String  CKX = "CKX";
		
	private String name;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int doStartTag() throws JspException {
		WebSdcContext sdcContext = WebSdcContextUtils.getWebSdcContext(pageContext.getServletContext());
		AbstractIBatisDao baseDao = sdcContext.getComponent(AbstractIBatisDao.class);
		LoginUser user = AuthorityUtils.getSecurityUser(); //得到当前用户对象
		
		//是否显示按钮
		boolean doTag = true;
		
		if(CK.equals(name)){
			//name：为仓库   则做仓库权限校验
			if(!isSkip(".isHasCKSkip",user,baseDao)){
				//没有此仓库权限
				doTag= false;
			}
		}else if(CKX.equals(name)){
			//name:为参考系 做参考系权限校验
			if(!isSkip(".isHasCKXSkip",user,baseDao)){
				//没有此仓库权限
				doTag= false;
			}
		}
		
		if(doTag){
			//有权限就执行标签体内容
			return EVAL_BODY_INCLUDE;
		}else{
			//不执行标签体内容
			return TagSupport.SKIP_BODY;
		}		
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 是否有权限 
	 *  有就返回真
	 *  没有就返回假
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean isSkip(String sqlxmlId,LoginUser user,AbstractIBatisDao baseDao){
		boolean isSkip = true;
		
		if(user!=null){
			//查找当前用户对应的用户组，是否有 仓库组 
			//有仓库组  则为真
			//没有  则为假
			List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(AuthorityConstants.MODULE_NAMESPACE+sqlxmlId,user);
			BigDecimal db = (BigDecimal) ((HashMap)(list.get(0))).get("ID");
			int count = db.intValue();
			if(count<1){
				//如果没有对应的菜单，则认为没有权限
				isSkip = false;
			}
		}else{
			isSkip = false;   //没有当前用户登录，则返回false
		}
		
		return isSkip;
	}
	
}
