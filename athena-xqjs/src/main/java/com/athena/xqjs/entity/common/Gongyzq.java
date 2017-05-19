/**
 *
 */
package com.athena.xqjs.entity.common;
import com.toft.core3.support.PageableSupport;


/**
 * <p>
 * Title:工业周期实体类
 * </p>
 * <p>
 * Description:工业周期实体类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 李明
 * @version v1.0
 * @date 2011-12-27
 */
public class Gongyzq extends PageableSupport{
	
	private static final long serialVersionUID = 6807755220652558230L;
	
	private String gongyzq;//工业周期
	private String kaissj;//开始时间
	private String jiessj;//结束时间
	private String creator ;
	private String create_time ;
	private String editor ;
	private String edit_time ;

	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getEdit_time() {
		return edit_time;
	}
	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}
	public String getGongyzq() {
		return gongyzq;
	}
	public void setGongyzq(String gongyzq) {
		this.gongyzq = gongyzq;
	}
	public String getKaissj() {
		return kaissj;
	}
	public void setKaissj(String kaissj) {
		this.kaissj = kaissj;
	}
	public String getJiessj() {
		return jiessj;
	}
	public void setJiessj(String jiessj) {
		this.jiessj = jiessj;
	}
	
	
}