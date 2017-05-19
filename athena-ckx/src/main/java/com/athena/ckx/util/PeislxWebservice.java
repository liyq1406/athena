package com.athena.ckx.util;

import java.util.List;

import javax.jws.WebService;

import com.athena.ckx.entity.xuqjs.CkxLingjxhd;
import com.athena.ckx.entity.xuqjs.Peislb;



@WebService
public interface PeislxWebservice 
{

	//查询有效存在的集合
	public List<Peislb> queryPeislx();
	
	//查询同步时配送类型是否存在
	public Integer querypeislxTB(CkxLingjxhd bean);

}
