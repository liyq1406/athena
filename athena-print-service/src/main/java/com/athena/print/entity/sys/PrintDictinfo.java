/**
 *
 */
package com.athena.print.entity.sys;


import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 实体: 打印单据类型
 * @author
 * @version
 * 
 */
public class PrintDictinfo extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = -4310436063688027447L;
	private String scode;
	private String scodes;
	private String codedetail;
	private String creator;//creator
	private String editor;//editor
	private String create_time;//create_time
	private String edit_time;//edit_time
	
	public String getScode() {
		return scode;
	}
	public void setScode(String scode) {
		this.scode = scode;
	}
	public String getScodes() {
		return scodes;
	}
	public void setScodes(String scodes) {
		this.scodes = scodes;
	}
	public String getCodedetail() {
		return codedetail;
	}
	public void setCodedetail(String codedetail) {
		this.codedetail = codedetail;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getEdit_time() {
		return edit_time;
	}
	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	


	
}
