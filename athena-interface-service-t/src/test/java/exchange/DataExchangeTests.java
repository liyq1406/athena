package exchange;





import java.util.Date;

import org.junit.Test;

import com.athena.component.exchange.DataExchange;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;
		
public class DataExchangeTests  extends AbstractCompomentTests{
	
		@Inject
		private DataExchange dataEchange;
		
		
		@Test
		public void doExchange()
		{		
           Date date=new Date();
           long begintime=date.getTime();
           
		   dataEchange.doExchange("1010");
		   
		   
		   Date b_date=new Date();
		   long endtime=b_date.getTime();
		   long time=endtime-begintime;
		   System.out.println(time/1000+"秒");
		}
}






