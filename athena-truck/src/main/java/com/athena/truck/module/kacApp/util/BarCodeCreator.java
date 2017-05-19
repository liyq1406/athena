package com.athena.truck.module.kacApp.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.StringTokenizer;

public class BarCodeCreator
{
	private static final long serialVersionUID = 1L;
	public BarCode barcode;

    public BarCodeCreator()
    {
    }

    public BarCode getChart(Map<String, Object> map)
    {
        if(barcode == null)
            barcode = new BarCode();
        try
        {
            setParameter("barType", map.get("barType").toString());
            if(map.get("width").toString() != null && map.get("height").toString()!=null)
            {
                setParameter("width", map.get("width").toString());
                setParameter("height", map.get("height").toString());
                setParameter("autoSize", "n");
            }
            setParameter("code", map.get("code").toString());
           /* setParameter("st", map.get("st").toString());
            setParameter("textFont", map.get("textFont").toString());
            setParameter("fontColor", map.get("fontColor").toString());
            setParameter("barColor", map.get("barColor").toString());
            setParameter("backColor", map.get("backColor").toString());
            setParameter("rotate", map.get("rotate").toString());
            setParameter("barHeightCM", map.get("barHeightCM").toString());
            setParameter("x", map.get("x").toString());
            setParameter("n", map.get("n").toString());
            setParameter("leftMarginCM", map.get("leftMarginCM").toString());
            setParameter("topMarginCM", map.get("topMarginCM").toString());*/
            setParameter("checkCharacter", map.get("checkCharacter").toString());
            setParameter("checkCharacterInText", map.get("checkCharacterInText").toString());
            /*setParameter("Code128Set", map.get("Code128Set").toString());
            setParameter("UPCESytem", map.get("UPCESytem").toString());*/
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            barcode.code = "Parameter Error";
        }
        return barcode;
    }

    public void setParameter(String s, String s1)
    {
        if(s1 != null)
            if(s.equals("code"))
                barcode.code = s1;
            else
            if(s.equals("width"))
                barcode.width = (new Integer(s1)).intValue();
            else
            if(s.equals("height"))
                barcode.height = (new Integer(s1)).intValue();
            else
            if(s.equals("autoSize"))
                barcode.autoSize = s1.equalsIgnoreCase("y");
            else
            if(s.equals("st"))
                barcode.showText = s1.equalsIgnoreCase("n");
            else
            if(s.equals("textFont"))
                barcode.textFont = convertFont(s1);
            else
            if(s.equals("fontColor"))
                barcode.fontColor = convertColor(s1);
            else
            if(s.equals("barColor"))
                barcode.barColor = convertColor(s1);
            else
            if(s.equals("backColor"))
                barcode.backColor = convertColor(s1);
            else
            if(s.equals("rotate"))
                barcode.rotate = (new Integer(s1)).intValue();
            else
            if(s.equals("barHeightCM"))
                barcode.barHeightCM = (new Double(s1)).doubleValue();
            else
            if(s.equals("x"))
                barcode.X = (new Double(s1)).doubleValue();
            else
            if(s.equals("n"))
                barcode.N = (new Double(s1)).doubleValue();
            else
            if(s.equals("leftMarginCM"))
                barcode.leftMarginCM = (new Double(s1)).doubleValue();
            else
            if(s.equals("topMarginCM"))
                barcode.topMarginCM = (new Double(s1)).doubleValue();
            else
            if(s.equals("checkCharacter"))
                barcode.checkCharacter = s1.equalsIgnoreCase("y");
            else
            if(s.equals("checkCharacterInText"))
                barcode.checkCharacterInText = s1.equalsIgnoreCase("y");
            else
            if(s.equals("Code128Set"))
                barcode.Code128Set = s1.charAt(0);
            else
            if(s.equals("UPCESytem"))
                barcode.UPCESytem = s1.charAt(0);
            else
            if(s.equals("barType"))
                if(s1.equalsIgnoreCase("CODE39"))
                    barcode.barType = 0;
                else
                if(s1.equalsIgnoreCase("CODE39EXT"))
                    barcode.barType = 1;
                else
                if(s1.equalsIgnoreCase("INTERLEAVED25"))
                    barcode.barType = 2;
                else
                if(s1.equalsIgnoreCase("CODE11"))
                    barcode.barType = 3;
                else
                if(s1.equalsIgnoreCase("CODABAR"))
                    barcode.barType = 4;
                else
                if(s1.equalsIgnoreCase("MSI"))
                    barcode.barType = 5;
                else
                if(s1.equalsIgnoreCase("UPCA"))
                    barcode.barType = 6;
                else
                if(s1.equalsIgnoreCase("IND25"))
                    barcode.barType = 7;
                else
                if(s1.equalsIgnoreCase("MAT25"))
                    barcode.barType = 8;
                else
                if(s1.equalsIgnoreCase("CODE93"))
                    barcode.barType = 9;
                else
                if(s1.equalsIgnoreCase("EAN13"))
                    barcode.barType = 10;
                else
                if(s1.equalsIgnoreCase("EAN8"))
                    barcode.barType = 11;
                else
                if(s1.equalsIgnoreCase("UPCE"))
                    barcode.barType = 12;
                else
                if(s1.equalsIgnoreCase("CODE128"))
                    barcode.barType = 13;
                else
                if(s1.equalsIgnoreCase("CODE93EXT"))
                    barcode.barType = 14;
                else
                if(s1.equalsIgnoreCase("POSTNET"))
                    barcode.barType = 15;
                else
                if(s1.equalsIgnoreCase("PLANET"))
                    barcode.barType = 16;
                else
                if(s1.equalsIgnoreCase("UCC128"))
                    barcode.barType = 17;
    }

    public void doGet(Map<String, Object> map)
        throws IOException
    {
        try
        {
        	
            BarCode barcode1 = getChart(map);
            barcode1.setSize(barcode1.width, barcode1.height);
            if(barcode1.autoSize)
            {
                BufferedImage bufferedimage = new BufferedImage(barcode1.getSize().width, barcode1.getSize().height, 13);
                java.awt.Graphics2D graphics2d = bufferedimage.createGraphics();
                barcode1.paint(graphics2d);
                barcode1.invalidate();
                graphics2d.dispose();
            }
            BufferedImage bufferedimage1 = new BufferedImage(barcode1.getSize().width, barcode1.getSize().height, 1);
            java.awt.Graphics2D graphics2d1 = bufferedimage1.createGraphics();
            barcode1.paint(graphics2d1);
            /*JPEGImageEncoder jpegimageencoder = JPEGCodec.createJPEGEncoder(servletoutputstream);
            JPEGEncodeParam jpegencodeparam = jpegimageencoder.getDefaultJPEGEncodeParam(bufferedimage1);
            jpegencodeparam.setQuality(1.0F, true);
            jpegimageencoder.setJPEGEncodeParam(jpegencodeparam);
            jpegimageencoder.encode(bufferedimage1, jpegencodeparam);*/
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private Font convertFont(String s)
    {
        StringTokenizer stringtokenizer = new StringTokenizer(s, "|");
        String s1 = stringtokenizer.nextToken();
        String s2 = stringtokenizer.nextToken();
        String s3 = stringtokenizer.nextToken();
        byte byte0 = -1;
        if(s2.trim().toUpperCase().equals("PLAIN"))
            byte0 = 0;
        else
        if(s2.trim().toUpperCase().equals("BOLD"))
            byte0 = 1;
        else
        if(s2.trim().toUpperCase().equals("ITALIC"))
            byte0 = 2;
        return new Font(s1, byte0, (new Integer(s3)).intValue());
    }

    private Color convertColor(String s)
    {
        Color color = null;
        if(s.trim().toUpperCase().equals("RED"))
            color = Color.red;
        else
        if(s.trim().toUpperCase().equals("BLACK"))
            color = Color.black;
        else
        if(s.trim().toUpperCase().equals("BLUE"))
            color = Color.blue;
        else
        if(s.trim().toUpperCase().equals("CYAN"))
            color = Color.cyan;
        else
        if(s.trim().toUpperCase().equals("DARKGRAY"))
            color = Color.darkGray;
        else
        if(s.trim().toUpperCase().equals("GRAY"))
            color = Color.gray;
        else
        if(s.trim().toUpperCase().equals("GREEN"))
            color = Color.green;
        else
        if(s.trim().toUpperCase().equals("LIGHTGRAY"))
            color = Color.lightGray;
        else
        if(s.trim().toUpperCase().equals("MAGENTA"))
            color = Color.magenta;
        else
        if(s.trim().toUpperCase().equals("ORANGE"))
            color = Color.orange;
        else
        if(s.trim().toUpperCase().equals("PINK"))
            color = Color.pink;
        else
        if(s.trim().toUpperCase().equals("WHITE"))
            color = Color.white;
        else
        if(s.trim().toUpperCase().equals("YELLOW"))
            color = Color.yellow;
        return color;
    }
}
