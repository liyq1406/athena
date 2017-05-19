package com.athena.print.core;

	import java.util.Comparator;
	import java.util.List;
	import java.util.ArrayList;
import java.util.Collections;

import com.athena.print.entity.pcenter.PrintQtaskmain;

	public class ComparatorPrintQtaskmain implements Comparator{

	public int compare(Object arg0, Object arg1) {
	PrintQtaskmain user0=(PrintQtaskmain)arg0;
	PrintQtaskmain user1=(PrintQtaskmain)arg1;
		int flag=user0.getCreatetime().compareTo(user1.getCreatetime());
		if(flag==0){
		return user0.getFinishedtime().compareTo(user1.getFinishedtime());
		}else{
		return flag;
		}
	}
}

