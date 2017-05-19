package com.athena.pc.module.exchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.athena.component.exchange.config.ExchangerConfig;
import com.athena.util.exception.ServiceException;
import com.toft.core2.ToftException;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class XlsExporter extends AbstractExporter<WritableSheet> implements Exporter {
	public XlsExporter(ExporterHeadInfo headInfo, Map<String, Object> jsonResult,Map<String, String> ecMap) {
		super(headInfo,jsonResult,ecMap);
	}

	public void execute() {
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(createOut(ec));
			WritableSheet sheet = workbook.createSheet(ec.get("sheetName"), Integer.parseInt(ec.get("sheetNum")));
			this.processJsonResult(sheet);
			workbook.write();
			try {
				workbook.close();
			} catch (WriteException e) {
				ToftException.ThrowToftException("999999", e);
			}
		} catch (IOException e) {
			ToftException.ThrowToftException("999999", e);
		}
	}

	public void executeOut() {
		WritableWorkbook wwb ;
		String path=createRealPath(ec.get("FilePath"),ec.get("FileName"));
		String sheetName = ec.get("sheetName");
		try{   
			  File resultFile = new File(path);
			  if(!resultFile.isFile()){//不存在则创建文件
			       wwb = Workbook.createWorkbook(resultFile); 
			  }else{//已存在则取出文件
			       Workbook rwb = Workbook.getWorkbook(resultFile);
			       wwb = Workbook.createWorkbook(resultFile, rwb);
			  }
			WritableSheet sheet = wwb.getSheet(sheetName);//取出工作表
			if(sheet == null){//如不存在，创建新的工作表
			      sheet = wwb.createSheet(sheetName, wwb.getNumberOfSheets());
			} 
			this.processJsonResult(sheet);
			wwb.write();
			try {
				wwb.close();
			} catch (WriteException e) {
				ToftException.ThrowToftException("999999", e);
			}
		}catch(Exception e){
			
		}
		
	}
	
	@Override
	void addHeadCell(WritableSheet container, int row, int col, String context) {
		addSheetCell(container,row,col,context);
	}

	@Override
	void addCell(WritableSheet container, int row, int col, String context) {
		addSheetCell(container,row,col,context);
	}
	
	/**
	 * 添加sheet单元格
	 * @return
	 */
	private void addSheetCell(WritableSheet sheet,int row,int col,String text){
		if(text==null||text.equals(""))return;
		//
		jxl.write.Label cell = new Label(col,row, text==null?"":text);
		try {
			sheet.addCell(cell);
		} catch (RowsExceededException e) {
			ToftException.ThrowToftException("999999", e);
		} catch (WriteException e) {
			ToftException.ThrowToftException("999999", e);
		}
	}

    /**
     * 创建输出流
     * @param ec 配置文件对象
     * @return
     */
    private File createOut(Map<String,String> ec ) {

        String filePath = ec.get("FilePath");
        String fileName = ec.get("FileName");
        String encoding = null;

        //默认为GBK
        if(encoding==null){
            encoding = "GBK";
        }
        //System.out.println("filePath:"+filePath+"---"+"fileName:"+fileName+"---"+"encoding:"+encoding);
        //1：如果没有用户输入的路径 就创建此路径
        File file = new File(filePath);
        if(!file.exists()){
            file.mkdirs();
        }

        //2:生成输出文件路径
        String outName = createRealPath(filePath,fileName);

        //3:创建输出流
        File writer = null;
        try {
            writer = new File(outName);	
        	
        } catch (Exception e) {

        }

        return writer;
    }
    
    /**
     * 生成输出文件路径
     * @param filePath
     * @param fileName
     * @return
     */
    private String createRealPath(String filePath, String fileName) {
        StringBuffer sb = new StringBuffer();
        sb.append(filePath);
        sb.append(File.separator);
        sb.append(fileName);
        return sb.toString();
    }
}
