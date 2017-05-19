package exchange;




import org.junit.Test;
import com.athena.component.exchange.DataExchange;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;


public class INPUT_DataExchangeTest extends AbstractCompomentTests{
	
	@Inject
	private DataExchange dataEchange;
	
	
	
	/**
	 * 零件参考系接口测试
	 */
	@Test
	public void in_component(){
		dataEchange.doExchange("IN_1");
	}
	
	
    /**
     * 供应商参考系接口测试
     */
	@Test
	public void in_supplier(){
		dataEchange.doExchange("IN_3");
	}
	
	
	/**
     * 零件供应商参考系接口测试
     */
	@Test
	public void in_component_supplier(){
		dataEchange.doExchange("IN_4");
	}
	
	
	/**
     * 零件周期毛需求接口测试
     */
	@Test
	public void in_gundmxq(){
		dataEchange.doExchange("IN_2");
	}
	
	
	/**
     * KD件在途、物理点信息接口测试
     */
	@Test
	public void in_kdwld(){
		dataEchange.doExchange("IN_14");
	}
	
	
	/**
     * 零件MAF库存接口测试
     */
	@Test
	public void in_maf(){
		dataEchange.doExchange("IN_12");
	}
	
	
	/**
     * 九天排产计划(商业化的时间)
     */
	@Test
	public void in_clddxx(){
		dataEchange.doExchange("IN_10");
	}
	
	
	/**
     * 九天排产计划(JT的顺序)
     */
	@Test
	public void in_jtpcjh(){
		dataEchange.doExchange("IN_9");
	}
	
	
	
	/**
     * KDPY订单信息接口测试
     */
	@Test
	public void in_ddxx(){
		dataEchange.doExchange("IN_16");
	}
	
	
	/**
     * EDI发货通知接口测试
     */
	@Test
	public void IN_EDI_FHTZ(){
		dataEchange.doExchange("IN_11");
	}
	
	
	/**
     * EFI发货通知接口测试
     */
	@Test
	public void IN_EFI_FHTZ(){
		dataEchange.doExchange("IN_13");
	}
	
	
	/**
     * GEVP要货令接口测试
     */
	@Test
	public void IN_GEVP_YAOHL(){
		dataEchange.doExchange("IN_15");
	}
	
	
	/**
     * 消耗点参考系接口测试
     */
	@Test
	public void in_xhdckx(){
		dataEchange.doExchange("IN_5");
	}
	
	/**
     * 消耗点零件参考系接口测试
     */
	@Test
	public void in_xhdljckx(){
		dataEchange.doExchange("IN_6");
	}
	
	
	/**
     * 单量份车拆分结果接口测试
     */
	@Test
	public void in_coddc(){
		dataEchange.doExchange("IN_7");
	}
	
	/**
     * CLDV-CODDC对应关系接口测试
     */
	@Test
	public void in_cldv_coddc(){
		dataEchange.doExchange("IN_8");
	}
	
	/**
     * 商业OF接口接口测试
     */
	@Test
	public void in_syof(){
		dataEchange.doExchange("IN_18");
	}
	
	/**
     * 外部订单预告接口测试
     */
	@Test
	public void in_wbddyg(){
		dataEchange.doExchange("IN_20");
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}