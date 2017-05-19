package com.athena.xqjs.entity.ilorder;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;


/**
 * 实体: 毛需求明细
 * @author
 * @version
 * 
 */
public class Maoxqmx extends PageableSupport{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 使用车间
	 */
	private String shiycj;
	/**
	 * 需求数量
	 */
	private BigDecimal xuqsl;
	/**
	 * 需求日期
	 */
	private String xuqrq;
	/**
	 * 起始周序号
	 */
	private String qiszxh;
	/**
	 * 需求所属周期
	 */
	private String xuqsszq;
	/**
	 * 制造路线
	 */
	private String zhizlx;
	/**
	 * 单位
	 */
	private String danw;
	/**
	 * 用户中心
	 */
	private String usercenter;
	/**
	 * 需求所属周
	 */
	private String xuqz;
	/**
	 * 零件号
	 */
	private String lingjbh;
	/**
	 * ID
	 */
	private String id;
	/**
	 * 产线
	 */
	private String chanx;
	/**
	 * 需求版次
	 */
	private String xuqbc;
	/**
	 * 创建人
	 */
	private String creator;
	/**
	 * 修改人
	 */
	private String editor;
	/**
	 * 创建时间
	 */
	private String crete_time;
	/**
	 * 修改时间
	 */
	private String edit_time;
	
	private String xuqksrq ;
	
	private String xuqjsrq ;
	
	private String xuqcfsj;
	
	private String xuqly;

	private String starttime;
	
	private String  endtime;
	
	private  String  cangk;
	
	private String cangklx;
	
	/**
	 * 消耗点
	 */
	private String xiaohd;
	
	

	public String getXiaohd() {
		return xiaohd;
	}

	public void setXiaohd(String xiaohd) {
		this.xiaohd = xiaohd;
	}

	public String getCangklx() {
		return cangklx;
	}

	public void setCangklx(String cangklx) {
		this.cangklx = cangklx;
	}

	public String getCangk() {
		return cangk;
	}

	public void setCangk(String cangk) {
		this.cangk = cangk;
	}

	public String getXuqcfsj() {
		return xuqcfsj;
	}

	public void setXuqcfsj(String xuqcfsj) {
		this.xuqcfsj = xuqcfsj;
	}

	public String getXuqly() {
		return xuqly;
	}

	public void setXuqly(String xuqly) {
		this.xuqly = xuqly;
	}

	public String getXuqksrq() {
		return xuqksrq;
	}

	public void setXuqksrq(String xuqksrq) {
		this.xuqksrq = xuqksrq;
	}

	public String getXuqjsrq() {
		return xuqjsrq;
	}

	public void setXuqjsrq(String xuqjsrq) {
		this.xuqjsrq = xuqjsrq;
	}

	/**
	 * 删除标识
	 */
	private String active;

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

	public String getCrete_time() {
		return crete_time;
	}

	public void setCrete_time(String crete_time) {
		this.crete_time = crete_time;
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

	
	/**
	 * 取得 - 使用车间.
	 *
	 * @return the 使用车间
	 */
	public String getShiycj() {
		return shiycj;
	}
	
	/**
	 * 设置 - 使用车间.
	 *
	 * @param shiycj 使用车间
	 */
	public void setShiycj(String shiycj) {
		this.shiycj = shiycj;
	}
	
	/**
	 * 取得 - 需求数量.
	 *
	 * @return the 需求数量
	 */
	public BigDecimal getXuqsl() {
		return xuqsl;
	}
	
	/**
	 * 设置 - 需求数量.
	 *
	 * @param xuqsl 需求数量
	 */
	public void setXuqsl(BigDecimal xuqsl) {
		this.xuqsl = xuqsl;
	}
	
	/**
	 * 取得 - 需求日期.
	 *
	 * @return the 需求日期
	 */
	public String getXuqrq() {
		return xuqrq;
	}
	
	/**
	 * 设置 - 需求日期.
	 *
	 * @param xuqrq 需求日期
	 */
	public void setXuqrq(String xuqrq) {
		this.xuqrq = xuqrq;
	}
	
	/**
	 * 取得 - 起始周序号.
	 *
	 * @return the 起始周序号
	 */
	public String getQiszxh() {
		return qiszxh;
	}
	
	/**
	 * 设置 - 起始周序号.
	 *
	 * @param qiszxh 起始周序号
	 */
	public void setQiszxh(String qiszxh) {
		this.qiszxh = qiszxh;
	}
	
	/**
	 * 取得 - 需求所属周期.
	 *
	 * @return the 需求所属周期
	 */
	public String getXuqsszq() {
		return xuqsszq;
	}
	
	/**
	 * 设置 - 需求所属周期.
	 *
	 * @param xuqsszq 需求所属周期
	 */
	public void setXuqsszq(String xuqsszq) {
		this.xuqsszq = xuqsszq;
	}
	
	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	/**
	 * 取得 - 制造路线.
	 *
	 * @return the 制造路线
	 */
	public String getZhizlx() {
		return zhizlx;
	}
	
	/**
	 * 设置 - 制造路线.
	 *
	 * @param zhizlx 制造路线
	 */
	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}
	
	/**
	 * 取得 - 单位.
	 *
	 * @return the 单位
	 */
	public String getDanw() {
		return danw;
	}
	
	/**
	 * 设置 - 单位.
	 *
	 * @param danw 单位
	 */
	public void setDanw(String danw) {
		this.danw = danw;
	}
	
	/**
	 * 取得 - 用户中心.
	 *
	 * @return the 用户中心
	 */
	public String getUsercenter() {
		return usercenter;
	}
	
	/**
	 * 设置 - 用户中心.
	 *
	 * @param usercenter 用户中心
	 */
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	
	/**
	 * 取得 - 需求所属周.
	 *
	 * @return the 需求所属周
	 */
	public String getXuqz() {
		return xuqz;
	}
	
	/**
	 * 设置 - 需求所属周.
	 *
	 * @param xuqz 需求所属周
	 */
	public void setXuqz(String xuqz) {
		this.xuqz = xuqz;
	}
	
	/**
	 * 取得 - 零件号.
	 *
	 * @return the 零件号
	 */
	public String getLingjbh() {
		return lingjbh;
	}
	
	/**
	 * 设置 - 零件号.
	 *
	 * @param lingjbh 零件号
	 */
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	
	/**
	 * 取得 - iD.
	 *
	 * @return the iD
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * 设置 - iD.
	 *
	 * @param id iD
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 取得 - 产线.
	 *
	 * @return the 产线
	 */
	public String getChanx() {
		return chanx;
	}
	
	/**
	 * 设置 - 产线.
	 *
	 * @param chanx 产线
	 */
	public void setChanx(String chanx) {
		this.chanx = chanx;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Maoxqmx [shiycj=" + shiycj + ", xuqsl=" + xuqsl + ", xuqrq="
				+ xuqrq + ", qiszxh=" + qiszxh + ", xuqsszq=" + xuqsszq
				+ ", zhizlx=" + zhizlx + ", danw=" + danw + ", usercenter="
				+ usercenter + ", xuqz=" + xuqz + ", lingjbh=" + lingjbh
				+ ", id=" + id + ", chanx=" + chanx + ", xuqbc=" + xuqbc + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result + ((cangk == null) ? 0 : cangk.hashCode());
		result = prime * result + ((cangklx == null) ? 0 : cangklx.hashCode());
		result = prime * result + ((chanx == null) ? 0 : chanx.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result
				+ ((crete_time == null) ? 0 : crete_time.hashCode());
		result = prime * result + ((danw == null) ? 0 : danw.hashCode());
		result = prime * result
				+ ((edit_time == null) ? 0 : edit_time.hashCode());
		result = prime * result + ((editor == null) ? 0 : editor.hashCode());
		result = prime * result + ((endtime == null) ? 0 : endtime.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lingjbh == null) ? 0 : lingjbh.hashCode());
		result = prime * result + ((qiszxh == null) ? 0 : qiszxh.hashCode());
		result = prime * result + ((shiycj == null) ? 0 : shiycj.hashCode());
		result = prime * result
				+ ((starttime == null) ? 0 : starttime.hashCode());
		result = prime * result
				+ ((usercenter == null) ? 0 : usercenter.hashCode());
		result = prime * result + ((xuqbc == null) ? 0 : xuqbc.hashCode());
		result = prime * result + ((xuqcfsj == null) ? 0 : xuqcfsj.hashCode());
		result = prime * result + ((xuqjsrq == null) ? 0 : xuqjsrq.hashCode());
		result = prime * result + ((xuqksrq == null) ? 0 : xuqksrq.hashCode());
		result = prime * result + ((xuqly == null) ? 0 : xuqly.hashCode());
		result = prime * result + ((xuqrq == null) ? 0 : xuqrq.hashCode());
		result = prime * result + ((xuqsl == null) ? 0 : xuqsl.hashCode());
		result = prime * result + ((xuqsszq == null) ? 0 : xuqsszq.hashCode());
		result = prime * result + ((xuqz == null) ? 0 : xuqz.hashCode());
		result = prime * result + ((zhizlx == null) ? 0 : zhizlx.hashCode());
		return result;
	}

	/* (non-Javadoc)
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
		if (!(obj instanceof Maoxqmx)) {
			return false;
		}
		Maoxqmx other = (Maoxqmx) obj;
		if (active == null) {
			if (other.active != null) {
				return false;
			}
		} else if (!active.equals(other.active)) {
			return false;
		}
		if (cangk == null) {
			if (other.cangk != null) {
				return false;
			}
		} else if (!cangk.equals(other.cangk)) {
			return false;
		}
		if (cangklx == null) {
			if (other.cangklx != null) {
				return false;
			}
		} else if (!cangklx.equals(other.cangklx)) {
			return false;
		}
		if (chanx == null) {
			if (other.chanx != null) {
				return false;
			}
		} else if (!chanx.equals(other.chanx)) {
			return false;
		}
		if (creator == null) {
			if (other.creator != null) {
				return false;
			}
		} else if (!creator.equals(other.creator)) {
			return false;
		}
		if (crete_time == null) {
			if (other.crete_time != null) {
				return false;
			}
		} else if (!crete_time.equals(other.crete_time)) {
			return false;
		}
		if (danw == null) {
			if (other.danw != null) {
				return false;
			}
		} else if (!danw.equals(other.danw)) {
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
		if (endtime == null) {
			if (other.endtime != null) {
				return false;
			}
		} else if (!endtime.equals(other.endtime)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (lingjbh == null) {
			if (other.lingjbh != null) {
				return false;
			}
		} else if (!lingjbh.equals(other.lingjbh)) {
			return false;
		}
		if (qiszxh == null) {
			if (other.qiszxh != null) {
				return false;
			}
		} else if (!qiszxh.equals(other.qiszxh)) {
			return false;
		}
		if (shiycj == null) {
			if (other.shiycj != null) {
				return false;
			}
		} else if (!shiycj.equals(other.shiycj)) {
			return false;
		}
		if (starttime == null) {
			if (other.starttime != null) {
				return false;
			}
		} else if (!starttime.equals(other.starttime)) {
			return false;
		}
		if (usercenter == null) {
			if (other.usercenter != null) {
				return false;
			}
		} else if (!usercenter.equals(other.usercenter)) {
			return false;
		}
		if (xuqbc == null) {
			if (other.xuqbc != null) {
				return false;
			}
		} else if (!xuqbc.equals(other.xuqbc)) {
			return false;
		}
		if (xuqcfsj == null) {
			if (other.xuqcfsj != null) {
				return false;
			}
		} else if (!xuqcfsj.equals(other.xuqcfsj)) {
			return false;
		}
		if (xuqjsrq == null) {
			if (other.xuqjsrq != null) {
				return false;
			}
		} else if (!xuqjsrq.equals(other.xuqjsrq)) {
			return false;
		}
		if (xuqksrq == null) {
			if (other.xuqksrq != null) {
				return false;
			}
		} else if (!xuqksrq.equals(other.xuqksrq)) {
			return false;
		}
		if (xuqly == null) {
			if (other.xuqly != null) {
				return false;
			}
		} else if (!xuqly.equals(other.xuqly)) {
			return false;
		}
		if (xuqrq == null) {
			if (other.xuqrq != null) {
				return false;
			}
		} else if (!xuqrq.equals(other.xuqrq)) {
			return false;
		}
		if (xuqsl == null) {
			if (other.xuqsl != null) {
				return false;
			}
		} else if (!xuqsl.equals(other.xuqsl)) {
			return false;
		}
		if (xuqsszq == null) {
			if (other.xuqsszq != null) {
				return false;
			}
		} else if (!xuqsszq.equals(other.xuqsszq)) {
			return false;
		}
		if (xuqz == null) {
			if (other.xuqz != null) {
				return false;
			}
		} else if (!xuqz.equals(other.xuqz)) {
			return false;
		}
		if (zhizlx == null) {
			if (other.zhizlx != null) {
				return false;
			}
		} else if (!zhizlx.equals(other.zhizlx)) {
			return false;
		}
		return true;
	}
}