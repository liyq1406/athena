package com.athena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.athena.authority.entity.MenuDirectory;
import com.athena.authority.entity.PageButton;
import com.athena.authority.service.MenuDirectoryService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

public class UserIsExistTest extends AbstractCompomentTests{
	
	@Inject
	private UserIsExist isExist;
	
	@Inject
	private MenuDirectoryService menuDirectoryService;
	
	
	@Test
	public void test1(){
		
		
		String username = "root";
		String passwords = "123456";
		
		try{
			System.out.println(isExist.userIsExist(username, passwords));
		}catch( Exception e ){
			System.out.println(e.getMessage());
		}
	}
	
	
	@Test
	public void test2(){
		String username = "root";
		String passwords = "123457";
		
		try {
			System.out.println(isExist.userIsExist(username, passwords));
		} catch (Exception e) {
			
			System.out.println(e.getMessage());
		}
		
	}
	
	
	@Test
	public void test3(){
		String username = "roo1";
		String passwords = "123456";
		
		try{
			System.out.println(isExist.userIsExist(username, passwords));
		}catch( Exception e ){
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void testTree(){
//		List<MenuDirectory> menu= new ArrayList<MenuDirectory>();
		ArrayList<PageButton> button = new ArrayList<PageButton>();
		menuDirectoryService.createTreeId(button);
	}
	
//	@Test
	public void testSysPostGroup(){
		Map<String,String> params = new HashMap<String,String>();
		params.put("treeId", "402882f938b7e4450138b7e445b90000_99040001_8a9a2b42387b644201387b6442f00000_4028826839c2b3070139c2b307470000_4028826839c2b3070139c2b625310001");
//		params.put("parentId", "99040001");
		params.put("ischeck", "1");
		params.put("postGroupId", "pcz");
		params.put("flag", "1");
		menuDirectoryService.updateMenuButtonAuth(params);
	}
}
