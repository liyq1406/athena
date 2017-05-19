/**
 *
 */
package com.athena.xqjs.entity.ilorder;

import com.toft.core3.support.PageableSupport;


/**
 * 实体: 毛需求
 * @author
 * @version
 * 
 */
public class Maoxq extends PageableSupport{
	
	private static final long serialVersionUID = 1L;
	/**
	 * 备注
	 */
	private String beiz;
	/**
	 * 需求拆分时间
	 */
	private String xuqcfsj;
	/**
	 * 需求类型
	 */
	private String xuqlx;
	/**
	 * 生效标识
	 */
	private String shengxbz;
	/**
	 * 需求版次
	 */
	private String xuqbc;
	/**
	 * 需求来源
	 */
	private String xuqly;
	/**
	 * 需求来源说明
	 */
	private String xuqlymc;
	/**
	 * 创建人
	 */
	private String creator;
	
	/**
	 * 修改人
	 */
	private String editor;
	
	/**
	 * 新修改人
	 */
	private String newEditor;
	
	/**
	 * 创建时间
	 */
	private String create_time;
	/**
	 * 修改时间
	 */
	private String edit_time;
	/**
	 * 新修改时间
	 */
	private String newEdit_time;
	
	/**
	 * 删除标识
	 */
	private String active;

	/**
	 * 是否指定运输时刻
	 */
	private String shifzdyssk;

	/**
	 * 指定工业周期FROM
	 */
	private String zdgyzqfrom;

	/**
	 * 指定工业周期TO
	 */
	private String zdgyzqto;

	/**
	 * 是否计算CMJ版本
	 */
	private String shifjscmj;
	
	/**
	 * 用户中心
	 */
	private String usercenter;

	public String getShifzdyssk() {
		return shifzdyssk;
	}

	public void setShifzdyssk(String shifzdyssk) {
		this.shifzdyssk = shifzdyssk;
	}

	public String getZdgyzqfrom() {
		return zdgyzqfrom;
	}

	public void setZdgyzqfrom(String zdgyzqfrom) {
		this.zdgyzqfrom = zdgyzqfrom;
	}

	public String getZdgyzqto() {
		return zdgyzqto;
	}

	public void setZdgyzqto(String zdgyzqto) {
		this.zdgyzqto = zdgyzqto;
	}

	/**
	 * 取得 - 备注.
	 *
	 * @return the 备注
	 */
	public String getBeiz() {
		return beiz;
	}
	
	/**
	 * 设置 - 备注.
	 *
	 * @param beiz 备注
	 */
	public void setBeiz(String beiz) {
		this.beiz = beiz;
	}
	
	/**
	 * 取得 - 需求拆分时间.
	 *
	 * @return the 需求拆分时间
	 */
	public String getXuqcfsj() {
		return xuqcfsj;
	}
	
	/**
	 * 设置 - 需求拆分时间.
	 *
	 * @param xuqcfsj 需求拆分时间
	 */
	public void setXuqcfsj(String xuqcfsj) {
		this.xuqcfsj = xuqcfsj;
	}
	
	/**
	 * 取得 - 需求类型.
	 *
	 * @return the 需求类型
	 */
	public String getXuqlx() {
		return xuqlx;
	}
	
	/**
	 * 设置 - 需求类型.
	 *
	 * @param xuqlx 需求类型
	 */
	public void setXuqlx(String xuqlx) {
		this.xuqlx = xuqlx;
	}
	
	/**
	 * 取得 - 生效标识.
	 *
	 * @return the 生效标识
	 */
	public String getShengxbz() {
		return shengxbz;
	}
	
	/**
	 * 设置 - 生效标识.
	 *
	 * @param shengxbz 生效标识
	 */
	public void setShengxbz(String shengxbz) {
		this.shengxbz = shengxbz;
	}
	
	/**
	 * 取得 - 需求版次.
	 *
	 * @return the 需求版次
	 */
	public String getXuqbc() {
		return xuqbc;
	}
	
	/**
	 * 设置 - 需求版次.
	 *
	 * @param xuqbc 需求版次
	 */
	public void setXuqbc(String xuqbc) {
		this.xuqbc = xuqbc;
	}
	
	/**
	 * 取得 - 需求来源.
	 *
	 * @return the 需求来源
	 */
	public String getXuqly() {
		return xuqly;
	}
	
	/**
	 * 设置 - 需求来源.
	 *
	 * @param xuqly 需求来源
	 */
	public void setXuqly(String xuqly) {
		this.xuqly = xuqly;
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

	

	public String getEdit_time() {
		return edit_time;
	}

	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getNewEdit_time() {
		return newEdit_time;
	}

	public String getShifjscmj() {
		return shifjscmj;
	}

	public void setShifjscmj(String shifjscmj) {
		this.shifjscmj = shifjscmj;
	}

	public void setNewEdit_time(String newEdit_time) {
		this.newEdit_time = newEdit_time;
	}
	public String getNewEditor() {
		return newEditor;
	}

	public void setNewEditor(String newEditor) {
		this.newEditor = newEditor;
	}

	public String getXuqlymc() {
		return xuqlymc;
	}

	public void setXuqlymc(String xuqlymc) {
		this.xuqlymc = xuqlymc;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((beiz == null) ? 0 : beiz.hashCode());
		result = prime * result
				+ ((shengxbz == null) ? 0 : shengxbz.hashCode());
		result = prime * result + ((xuqbc == null) ? 0 : xuqbc.hashCode());
		result = prime * result + ((xuqcfsj == null) ? 0 : xuqcfsj.hashCode());
		result = prime * result + ((xuqlx == null) ? 0 : xuqlx.hashCode());
		result = prime * result + ((xuqly == null) ? 0 : xuqly.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			
			return true;
		}
		if (obj == null){
			
			return false;
		}
		if (getClass() != obj.getClass()){
			
			return false;
		}
		Maoxq other = (Maoxq) obj;
		if (beiz == null) {
			if (other.beiz != null){
				return false;
			}
		} else if (!beiz.equals(other.beiz)){
			
			return false;
		}
		if (shengxbz == null) {
			if (other.shengxbz != null){
				
				return false;
			}
		} else if (!shengxbz.equals(other.shengxbz)){
			
			return false;
		}
		if (xuqbc == null) {
			if (other.xuqbc != null){
				
				return false;
			}
		} else if (!xuqbc.equals(other.xuqbc)){
			
			return false;
		}
		if (xuqcfsj == null) {
			if (other.xuqcfsj != null){
				
				return false;
			}
		} else if (!xuqcfsj.equals(other.xuqcfsj)){
			
			return false;
		}
		if (xuqlx == null) {
			if (other.xuqlx != null){
				
				return false;
			}
		} else if (!xuqlx.equals(other.xuqlx)){
			
			return false;
		}
		if (xuqly == null) {
			if (other.xuqly != null){
				
				return false;
			}
		} else if (!xuqly.equals(other.xuqly)){
			
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Maoxq [beiz=" + beiz + ", xuqcfsj=" + xuqcfsj + ", xuqlx="
				+ xuqlx + ", shengxbz=" + shengxbz + ", xuqbc=" + xuqbc
				+ ", xuqly=" + xuqly + "]";
	}


	
}