package com.athena.print.coon;

import java.util.Comparator;

import com.athena.print.entity.pcenter.PrintQtaskmain;

public class ComparatorPrintQtaskmain implements Comparator{

	public int compare(Object arg0, Object arg1) {
		PrintQtaskmain printQtaskmain=(PrintQtaskmain)arg0;
		PrintQtaskmain printQtaskmain1=(PrintQtaskmain)arg1;
			int flag=printQtaskmain.getCreatetime().compareTo(printQtaskmain1.getCreatetime());
//			if(flag==0){
//					return printQtaskmain.getFinishedtime().compareTo(printQtaskmain1.getFinishedtime());
//			}else{
					return flag;
//			}
	}
}

