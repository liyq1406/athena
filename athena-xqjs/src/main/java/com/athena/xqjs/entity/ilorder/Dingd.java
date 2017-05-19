package com.athena.xqjs.entity.ilorder;

import com.athena.xqjs.module.common.Const;
import com.toft.core3.support.PageableSupport;

/**
 * 实体: 订单
 * 
 * @author LIMING
 * @version
 * 
 */
public class Dingd extends PageableSupport {

	private static final long serialVersionUID = 1L;

	private Dingdlj dingdlj;

	/**
	 * 订单类型
	 */
	private String dingdlx = "";

	/**
	 * 计算周期
	 */
	private String jiszq = "";

	/**
	 * 用户中心
	 */
	private String usercenter = "";

	/**
	 * 供应商代码
	 */
	private String gongysdm = "";

	/**
	 * 处理类型
	 */
	private String chullx = "";

	/**
	 * 订单生效操作人
	 */
	private String dingdsxczr = "";

	/**
	 * 合同号
	 */
	private String heth = "";

	/**
	 * 毛需求版次
	 */
	private String maoxqbc = "";

	/**
	 * 订单生效时间
	 */
	private String dingdsxsj = "";

	/**
	 * 发货周期
	 */
	private String fahzq = "";

	/**
	 * 是否发送供应商
	 */
	private String shiffsgys = "";

	/**
	 * 订单计算时间
	 */
	private String dingdjssj = "";

	/**
	 * 计算类型
	 */
	private String jislx = "";

	/**
	 * 备注
	 */
	private String beiz = "";

	/**
	 * 资源获取日期
	 */
	private String ziyhqrq = "";

	/**
	 * 订单发送时间
	 */
	private String dingdfssj = "";

	/**
	 * 是否只发送要货令
	 */
	private String shifzfsyhl = "";

	/**
	 * 订单状态
	 */
	private String dingdzt = "";

	/**
	 * 供应商类型
	 */
	private String gongyslx = "";

	/**
	 * 订单内容
	 */
	private String dingdnr = "";

	/**
	 * 订单号
	 */
	private String dingdh = "";

	private String shifyjsyhl = "";

	/**
	 * 创建人
	 */
	private String creator = "";

	/**
	 * 创建时间
	 */
	private String create_time = "";

	/**
	 * 修改时间
	 */
	private String edit_time = "";

	/**
	 * 修改人
	 */
	private String editor = "";

	/**
	 * 当前修改时间
	 */
	private String newEditTime = "";

	/**
	 * 当前修改人
	 */
	private String newEditor = "";

	/**
	 * 删除标示
	 */
	private String active = "";

	// 增加字段，方便计算
	private String dingdzzsj = "";

	private String mdd = "";
	///////////////wuyichao   0010339 生效订单的时候查询订单明细下的用户中心，与订单的用户中心进行匹配/////////////
	private String nUsercenter = "";
	
	public String getnUsercenter() {
		return nUsercenter;
	}

	public void setnUsercenter(String nUsercenter) {
		this.nUsercenter = nUsercenter;
	}
	///////////////wuyichao   0010339 生效订单的时候查询订单明细下的用户中心，与订单的用户中心进行匹配/////////////
	/**
	 * @return the dingdztName
	 */
	public String getDingdztName() {
		if (this.dingdzt != null) {
			if (this.dingdzt.equals(Const.DINGD_STATUS_YDY)) {
				return Const.DINGD_STATUS_YDY_NAME;
			} else if (this.dingdzt.equals(Const.DINGD_STATUS_ZZZ)) {
				return Const.DINGD_STATUS_ZZZ_NAME;
			} else if (this.dingdzt.equals(Const.DINGD_STATUS_DSX)) {
				return Const.DINGD_STATUS_DSX_NAME;
			} else if (this.dingdzt.equals(Const.DINGD_STATUS_JUJ)) {
				return Const.DINGD_STATUS_JUJ_NAME;
			} else if (this.dingdzt.equals(Const.DINGD_STATUS_YSX)) {
				return Const.DINGD_STATUS_YSX_NAME;
			}
		}
		return "";
	}

	public String getDingdzzsj() {
		return dingdzzsj;
	}

	public void setDingdzzsj(String dingdzzsj) {
		this.dingdzzsj = dingdzzsj;
	}

	public String getShifyjsyhl() {
		return shifyjsyhl;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public void setShifyjsyhl(String shifyjsyhl) {
		this.shifyjsyhl = shifyjsyhl;
	}

	public Dingdlj getDingdlj() {
		return dingdlj;
	}

	public void setDingdlj(Dingdlj dingdlj) {
		this.dingdlj = dingdlj;
	}

	/**
	 * 取得-订单类型
	 * 
	 * @return
	 */
	public String getDingdlx() {
		return this.dingdlx;
	}

	/**
	 * 设定-订单类型
	 * 
	 * @param dingdlx
	 */
	public void setDingdlx(String dingdlx) {
		this.dingdlx = dingdlx;
	}

	/**
	 * 取得-供应商代码
	 * 
	 * @return
	 */
	public String getGongysdm() {
		return this.gongysdm;
	}

	/**
	 * 设定-供应商代码
	 * 
	 * @param gongysdm
	 */
	public void setGongysdm(String gongysdm) {
		this.gongysdm = gongysdm;
	}

	/**
	 * 取得-处理类型
	 * 
	 * @return
	 */
	public String getChullx() {
		return this.chullx;
	}

	/**
	 * 设定-处理类型
	 * 
	 * @param chullx
	 */
	public void setChullx(String chullx) {
		this.chullx = chullx;
	}

	/**
	 * 取得-订单生效操作人
	 * 
	 * @return
	 */
	public String getDingdsxczr() {
		return this.dingdsxczr;
	}

	/**
	 * 设定-订单生效操作人
	 * 
	 * @param dingdsxczr
	 */
	public void setDingdsxczr(String dingdsxczr) {
		this.dingdsxczr = dingdsxczr;
	}

	/**
	 * 取得-合同号
	 * 
	 * @return
	 */
	public String getHeth() {
		return this.heth;
	}

	/**
	 * 设定-合同号
	 * 
	 * @param heth
	 */
	public void setHeth(String heth) {
		this.heth = heth;
	}

	/**
	 * 取得-毛需求版次
	 * 
	 * @return
	 */
	public String getMaoxqbc() {
		return this.maoxqbc;
	}

	/**
	 * 设定-毛需求版次
	 * 
	 * @param maoxqbc
	 */
	public void setMaoxqbc(String maoxqbc) {
		this.maoxqbc = maoxqbc;
	}

	/**
	 * 取得-订单生效时间
	 * 
	 * @return
	 */
	public String getDingdsxsj() {
		return this.dingdsxsj;
	}

	/**
	 * 设定-订单生效时间
	 * 
	 * @param dingdsxsj
	 */
	public void setDingdsxsj(String dingdsxsj) {
		this.dingdsxsj = dingdsxsj;
	}

	/**
	 * 取得-发货周期
	 * 
	 * @return
	 */
	public String getFahzq() {
		return this.fahzq;
	}

	/**
	 * 设定-发货周期
	 * 
	 * @param fahzq
	 */
	public void setFahzq(String fahzq) {
		this.fahzq = fahzq;
	}

	/**
	 * 取得-是否发送供应商
	 * 
	 * @return
	 */
	public String getShiffsgys() {
		return this.shiffsgys;
	}

	/**
	 * 设定-是否发送供应商
	 * 
	 * @param shiffsgys
	 */
	public void setShiffsgys(String shiffsgys) {
		this.shiffsgys = shiffsgys;
	}

	/**
	 * 取得-订单计算时间
	 * 
	 * @return
	 */
	public String getDingdjssj() {
		return this.dingdjssj;
	}

	/**
	 * 设定-订单计算时间
	 * 
	 * @param dingdjssj
	 */
	public void setDingdjssj(String dingdjssj) {
		this.dingdjssj = dingdjssj;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	/**
	 * 取得-计算类型
	 * 
	 * @return
	 */
	public String getJislx() {
		return this.jislx;
	}

	/**
	 * 设定-计算类型
	 * 
	 * @param jislx
	 */
	public void setJislx(String jislx) {
		this.jislx = jislx;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
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

	public String getNewEditTime() {
		return newEditTime;
	}

	public void setNewEditTime(String newEditTime) {
		this.newEditTime = newEditTime;
	}

	public String getNewEditor() {
		return newEditor;
	}

	public void setNewEditor(String newEditor) {
		this.newEditor = newEditor;
	}

	/**
	 * 取得-目的地
	 * 
	 * @return
	 */
	public String getMdd() {
		return mdd;
	}

	/**
	 * 设定-目的地
	 * 
	 * @return
	 */
	public void setMdd(String mdd) {
		this.mdd = mdd;
	}

	/**
	 * 取得-备注
	 * 
	 * @return
	 */
	public String getBeiz() {
		return this.beiz;
	}

	/**
	 * 设定-备注
	 * 
	 * @param beiz
	 */
	public void setBeiz(String beiz) {
		this.beiz = beiz;
	}

	/**
	 * 取得-资源获取日期
	 * 
	 * @return
	 */
	public String getZiyhqrq() {
		return this.ziyhqrq;
	}

	/**
	 * 设定-资源获取日期
	 * 
	 * @param ziyhqrq
	 */
	public void setZiyhqrq(String ziyhqrq) {
		this.ziyhqrq = ziyhqrq;
	}

	/**
	 * 取得-订单发送时间
	 * 
	 * @return
	 */
	public String getDingdfssj() {
		return this.dingdfssj;
	}

	/**
	 * 设定-订单发送时间
	 * 
	 * @param dingdfssj
	 */
	public void setDingdfssj(String dingdfssj) {
		this.dingdfssj = dingdfssj;
	}

	/**
	 * 取得-是否只发送要货令
	 * 
	 * @return
	 */
	public String getShifzfsyhl() {
		return this.shifzfsyhl;
	}

	/**
	 * 设定-是否只发送要货令
	 * 
	 * @param shifzfsyhl
	 */
	public void setShifzfsyhl(String shifzfsyhl) {
		this.shifzfsyhl = shifzfsyhl;
	}

	/**
	 * 取得-订单状态
	 * 
	 * @return
	 */
	public String getDingdzt() {
		return this.dingdzt;
	}

	/**
	 * 设定-订单状态
	 * 
	 * @param dingdzt
	 */
	public void setDingdzt(String dingdzt) {
		this.dingdzt = dingdzt;
	}

	/**
	 * 取得-供应商类型
	 * 
	 * @return
	 */
	public String getGongyslx() {
		return this.gongyslx;
	}

	/**
	 * 设定-供应商类型
	 * 
	 * @param gongyslx
	 */
	public void setGongyslx(String gongyslx) {
		this.gongyslx = gongyslx;
	}

	/**
	 * 取得-订单内容
	 * 
	 * @return
	 */
	public String getDingdnr() {
		return this.dingdnr;
	}

	/**
	 * 设定-订单内容
	 * 
	 * @param dingdnr
	 */
	public void setDingdnr(String dingdnr) {
		this.dingdnr = dingdnr;
	}

	/**
	 * 取得-订单号
	 * 
	 * @return
	 */
	public String getDingdh() {
		return this.dingdh;
	}

	/**
	 * 设定-订单号
	 * 
	 * @param dingdh
	 */
	public void setDingdh(String dingdh) {
		this.dingdh = dingdh;
	}

	public String getJiszq() {
		return jiszq;
	}

	public void setJiszq(String jiszq) {
		this.jiszq = jiszq;
	}

	private String lingjbh;

	/**
	 * 取得 lingjbh
	 * 
	 * @return the lingjbh
	 */
	public String getLingjbh() {
		return lingjbh;
	}

	/**
	 * @param lingjbh
	 *            the lingjbh to set
	 */
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result + ((beiz == null) ? 0 : beiz.hashCode());
		result = prime * result + ((chullx == null) ? 0 : chullx.hashCode());
		result = prime * result + ((create_time == null) ? 0 : create_time.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((dingdfssj == null) ? 0 : dingdfssj.hashCode());
		result = prime * result + ((dingdh == null) ? 0 : dingdh.hashCode());
		result = prime * result + ((dingdjssj == null) ? 0 : dingdjssj.hashCode());
		result = prime * result + ((dingdlj == null) ? 0 : dingdlj.hashCode());
		result = prime * result + ((dingdlx == null) ? 0 : dingdlx.hashCode());
		result = prime * result + ((dingdnr == null) ? 0 : dingdnr.hashCode());
		result = prime * result + ((dingdsxczr == null) ? 0 : dingdsxczr.hashCode());
		result = prime * result + ((dingdsxsj == null) ? 0 : dingdsxsj.hashCode());
		result = prime * result + ((dingdzt == null) ? 0 : dingdzt.hashCode());
		result = prime * result + ((edit_time == null) ? 0 : edit_time.hashCode());
		result = prime * result + ((editor == null) ? 0 : editor.hashCode());
		result = prime * result + ((fahzq == null) ? 0 : fahzq.hashCode());
		result = prime * result + ((gongysdm == null) ? 0 : gongysdm.hashCode());
		result = prime * result + ((gongyslx == null) ? 0 : gongyslx.hashCode());
		result = prime * result + ((heth == null) ? 0 : heth.hashCode());
		result = prime * result + ((jislx == null) ? 0 : jislx.hashCode());
		result = prime * result + ((maoxqbc == null) ? 0 : maoxqbc.hashCode());
		result = prime * result + ((newEditTime == null) ? 0 : newEditTime.hashCode());
		result = prime * result + ((newEditor == null) ? 0 : newEditor.hashCode());
		result = prime * result + ((shiffsgys == null) ? 0 : shiffsgys.hashCode());
		result = prime * result + ((shifyjsyhl == null) ? 0 : shifyjsyhl.hashCode());
		result = prime * result + ((shifzfsyhl == null) ? 0 : shifzfsyhl.hashCode());
		result = prime * result + ((usercenter == null) ? 0 : usercenter.hashCode());
		result = prime * result + ((ziyhqrq == null) ? 0 : ziyhqrq.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Dingd)) {
			return false;
		}
		Dingd other = (Dingd) obj;
		if (active == null) {
			if (other.active != null) {
				return false;
			}
		} else if (!active.equals(other.active)) {
			return false;
		}
		if (beiz == null) {
			if (other.beiz != null) {
				return false;
			}
		} else if (!beiz.equals(other.beiz)) {
			return false;
		}
		if (chullx == null) {
			if (other.chullx != null) {
				return false;
			}
		} else if (!chullx.equals(other.chullx)) {
			return false;
		}
		if (create_time == null) {
			if (other.create_time != null) {
				return false;
			}
		} else if (!create_time.equals(other.create_time)) {
			return false;
		}
		if (creator == null) {
			if (other.creator != null) {
				return false;
			}
		} else if (!creator.equals(other.creator)) {
			return false;
		}
		if (dingdfssj == null) {
			if (other.dingdfssj != null) {
				return false;
			}
		} else if (!dingdfssj.equals(other.dingdfssj)) {
			return false;
		}
		if (dingdh == null) {
			if (other.dingdh != null) {
				return false;
			}
		} else if (!dingdh.equals(other.dingdh)) {
			return false;
		}
		if (dingdjssj == null) {
			if (other.dingdjssj != null) {
				return false;
			}
		} else if (!dingdjssj.equals(other.dingdjssj)) {
			return false;
		}
		if (dingdlj == null) {
			if (other.dingdlj != null) {
				return false;
			}
		} else if (!dingdlj.equals(other.dingdlj)) {
			return false;
		}
		if (dingdlx == null) {
			if (other.dingdlx != null) {
				return false;
			}
		} else if (!dingdlx.equals(other.dingdlx)) {
			return false;
		}
		if (dingdnr == null) {
			if (other.dingdnr != null) {
				return false;
			}
		} else if (!dingdnr.equals(other.dingdnr)) {
			return false;
		}
		if (dingdsxczr == null) {
			if (other.dingdsxczr != null) {
				return false;
			}
		} else if (!dingdsxczr.equals(other.dingdsxczr)) {
			return false;
		}
		if (dingdsxsj == null) {
			if (other.dingdsxsj != null) {
				return false;
			}
		} else if (!dingdsxsj.equals(other.dingdsxsj)) {
			return false;
		}
		if (dingdzt == null) {
			if (other.dingdzt != null) {
				return false;
			}
		} else if (!dingdzt.equals(other.dingdzt)) {
			return false;
		}
		if (edit_time == null) {
			if (other.edit_time != null) {
				return false;
			}
		} else if (!edit_time.equals(other.edit_time)) {
			return false;
		}
		if (editor == null) {
			if (other.editor != null) {
				return false;
			}
		} else if (!editor.equals(other.editor)) {
			return false;
		}
		if (fahzq == null) {
			if (other.fahzq != null) {
				return false;
			}
		} else if (!fahzq.equals(other.fahzq)) {
			return false;
		}
		if (gongysdm == null) {
			if (other.gongysdm != null) {
				return false;
			}
		} else if (!gongysdm.equals(other.gongysdm)) {
			return false;
		}
		if (gongyslx == null) {
			if (other.gongyslx != null) {
				return false;
			}
		} else if (!gongyslx.equals(other.gongyslx)) {
			return false;
		}
		if (heth == null) {
			if (other.heth != null) {
				return false;
			}
		} else if (!heth.equals(other.heth)) {
			return false;
		}
		if (jislx == null) {
			if (other.jislx != null) {
				return false;
			}
		} else if (!jislx.equals(other.jislx)) {
			return false;
		}
		if (maoxqbc == null) {
			if (other.maoxqbc != null) {
				return false;
			}
		} else if (!maoxqbc.equals(other.maoxqbc)) {
			return false;
		}
		if (newEditTime == null) {
			if (other.newEditTime != null) {
				return false;
			}
		} else if (!newEditTime.equals(other.newEditTime)) {
			return false;
		}
		if (newEditor == null) {
			if (other.newEditor != null) {
				return false;
			}
		} else if (!newEditor.equals(other.newEditor)) {
			return false;
		}
		if (shiffsgys == null) {
			if (other.shiffsgys != null) {
				return false;
			}
		} else if (!shiffsgys.equals(other.shiffsgys)) {
			return false;
		}
		if (shifyjsyhl == null) {
			if (other.shifyjsyhl != null) {
				return false;
			}
		} else if (!shifyjsyhl.equals(other.shifyjsyhl)) {
			return false;
		}
		if (shifzfsyhl == null) {
			if (other.shifzfsyhl != null) {
				return false;
			}
		} else if (!shifzfsyhl.equals(other.shifzfsyhl)) {
			return false;
		}
		if (usercenter == null) {
			if (other.usercenter != null) {
				return false;
			}
		} else if (!usercenter.equals(other.usercenter)) {
			return false;
		}
		if (ziyhqrq == null) {
			if (other.ziyhqrq != null) {
				return false;
			}
		} else if (!ziyhqrq.equals(other.ziyhqrq)) {
			return false;
		}
		return true;
	}

}