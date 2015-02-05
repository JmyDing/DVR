package com.hzsun.www.Message;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class XmlFactory {

	
	
	public static  ParseXml parse(String xml) throws DocumentException{
		String  type=GetXmlType(xml) ;
		ParseXml  parsexml;
		if(type.equalsIgnoreCase("MediaBye")){
			parsexml=new MediaByeXml();
			parsexml.parse(xml);
			return parsexml;
		}else if(type.equalsIgnoreCase("MediaPlay")){
			parsexml=new MediaPlayXml();
			parsexml.parse(xml);
			return parsexml;
		}
		return null;
	}
	
	

	
	private   static String GetXmlType(String xml) throws DocumentException{
		Document doc = DocumentHelper.parseText(xml); 
         Element rootElt = doc.getRootElement(); 
         String Type=rootElt.elementText("Type");
         return Type;
	}
}
