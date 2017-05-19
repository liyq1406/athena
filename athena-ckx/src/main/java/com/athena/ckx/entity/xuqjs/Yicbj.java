package com.athena.ckx.entity.xuqjs;

import com.toft.core3.support.PageableSupport;

/**
* 异常报警实体bean
* @author Xiahui
* @date   2012-1-11
*/
public class Yicbj extends PageableSupport{
		private static final long serialVersionUID = 1L;
		private String  usercenter;//用户中心
		private String  lingjbh;   //零件编号
		private String  cuowlx;    //错误类型
		private String  cuowxxxx;  //错误详细信息
		private String  jihyz;     //计划员组
		private String  jihydm;    //计划员代码
		private String  jismk;     //计算模块
		/**
		 * 创建人
		 */
		private String creator = null;

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

		public String getEdit_time() {
			return edit_time;
		}

		public void setEdit_time(String edit_time) {
			this.edit_time = edit_time;
		}

		public String getEditor() {
			return editor;
		}

		public void setEditor(String editor) {
			this.editor = editor;
		}
		/**
		 * 创建时间
		 */
		private String create_time = null;

		/**
		 * 修改时间
		 */
		private String edit_time = null;

		/**
		 * 修改人
		 */
		private String editor = null;
		public String getUsercenter() {
			return usercenter;
		}
		public String getJihydm() {
			return jihydm;
		}
		public void setJihydm(String jihydm) {
			this.jihydm = jihydm;
		}
		public String getJismk() {
			return jismk;
		}
		public void setJismk(String jismk) {
			this.jismk = jismk;
		}
		public void setUsercenter(String usercenter) {
			this.usercenter = usercenter;
		}
		public String getLingjbh() {
			return lingjbh;
		}
		public void setLingjbh(String lingjbh) {
			this.lingjbh = lingjbh;
		}
		public String getCuowlx() {
			return cuowlx;
		}
		public void setCuowlx(String cuowlx) {
			this.cuowlx = cuowlx;
		}
		public String getCuowxxxx() {
			return cuowxxxx;
		}
		public void setCuowxxxx(String cuowxxxx) {
			this.cuowxxxx = cuowxxxx;
		}
		public String getJihyz() {
			return jihyz;
		}
		public void setJihyz(String jihyz) {
			this.jihyz = jihyz;
		}

}
