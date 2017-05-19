package com.athena.xqjs.util;

import java.util.List;

import javax.jws.WebService;

import com.athena.xqjs.entity.ilorder.Dingdmx;



@WebService
public interface AXlsddWebservice 
{
	//按需临时订单校验
	public String checkAXlldd(List<Dingdmx> list);

}
