package exchange;

import org.junit.Test;

import com.athena.component.exchange.DataExchange;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

public class OUTPUT_DateExchangeTest extends AbstractCompomentTests{
	@Inject
	private DataExchange dataEchange;
	
	
	/**
	 * 总成排产
	 */
	@Test
	public void OUT_26(){
		dataEchange.doExchange("OUT_26");
	}
	
	/**
	 * 零件库存快照
	 */
	@Test
	public void OUT_2(){
		dataEchange.doExchange("OUT_2");
	}
	
	
	/**
	 * 包装参考系
	 */
	@Test
	public void OUT_13(){
		dataEchange.doExchange("OUT_13");
	}
	
	
	/**
	 * 成品库存快照
	 */
	@Test
	public void OUT_1(){
		dataEchange.doExchange("OUT_1");
	}
	
	/**
	 * 成品库流水账
	 */
	@Test
	public void OUT_4(){
		dataEchange.doExchange("OUT_4");
	}
	
	/**
	 * 生产日历
	 */
	@Test
	public void OUT_14(){
		dataEchange.doExchange("OUT_14");
	}
	
	/**
	 * 零件供应商信息
	 */
	@Test
	public void OUT_9(){
		dataEchange.doExchange("OUT_9");
	}
	
	/**
	 * 供应商信息
	 */
	@Test
	public void OUT_10(){
		dataEchange.doExchange("OUT_10");
	}
	
	/**
	 * 周期订单
	 */
	@Test
	public void OUT_11(){
		dataEchange.doExchange("OUT_11");
	}
	
	/**
	 * 入库反馈
	 */
	@Test
	public void OUT_8(){
		dataEchange.doExchange("OUT_8");
	}
	
	/**
	 * EFI要货令
	 */
	@Test
	public void OUT_7(){
		dataEchange.doExchange("OUT_7");
	}
	
	/**
	 * 发货通知信息
	 */
	@Test
	public void OUT_16(){
		dataEchange.doExchange("OUT_16");
	}
	
	/**
	 * GEVP发货通知
	 */
	@Test
	public void OUT_25(){
		dataEchange.doExchange("OUT_25");
	}
	
	
	/**
	 * 需拆分消耗点零件
	 */
	@Test
	public void OUT_20(){
		dataEchange.doExchange("OUT_20");
	}
	
	/**
	 * 预批量零件需求
	 */
	@Test
	public void OUT_17(){
		dataEchange.doExchange("OUT_17");
	}
	
	/**
	 * 资源文件
	 */
	@Test
	public void OUT_18(){
		dataEchange.doExchange("OUT_18");
	}
	
	/**
	 * 供货路线
	 */
	@Test
	public void OUT_19(){
		dataEchange.doExchange("OUT_19");
	}
	
	/**
	 * 循环对产线
	 */
	@Test
	public void OUT_24(){
		dataEchange.doExchange("OUT_24");
	}
	
	
	/**
	 * 仓库流水账
	 */
	@Test
	public void OUT_3(){
		dataEchange.doExchange("OUT_3");
	}
	
	/**
	 * 要货令输出接口
	 */
	@Test
	public void OUT_5(){
		dataEchange.doExchange("OUT_5");
	}
	
	/**
	 * 预告输出接口
	 */
	@Test
	public void OUT_6(){
		dataEchange.doExchange("OUT_6");
	}
	
}
