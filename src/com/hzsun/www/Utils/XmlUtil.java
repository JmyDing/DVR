package com.hzsun.www.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class XmlUtil {
	
	@SuppressWarnings("unchecked")
	public static String  createXml(Map elements){
		Document document = DocumentHelper.createDocument();
		Element ZYHY=document.addElement("IPCP");
		Set<String>  key=elements.keySet();
		Iterator<String>  it=key.iterator();
		while(it.hasNext()){
			String  element=it.next();
			Element code=ZYHY.addElement(element);
			code.setText((String)elements.get(element));
		}
		
		return document.asXML();
	}
	
	
	@SuppressWarnings("unchecked")
	public static String illegalRequestXml(  ){
		
		Document document = DocumentHelper.createDocument();
		Element ZYHY=document.addElement("ZYHY");
		Element code=ZYHY.addElement("Code");
		code.setText("0");
		Element Msg=ZYHY.addElement("Msg");
		Msg.setText("签名错误");
		return document.asXML();
	}
	
	@SuppressWarnings("unchecked")
	private static Element secondXml(Element ZYHY, Map elements ){
		
//		 = DocumentHelper.createDocument();
		
		
//		Element ZYHY=document.addElement("ZYHY");
		Set<String>  key=elements.keySet();
		Iterator<String>  it=key.iterator();
		while(it.hasNext()){
			String  element=it.next();
			Element code=ZYHY.addElement(element);
			Object ob=elements.get(element);
			if(ob instanceof Map){
				Map mob=(Map) ob;
//				System.out.println(ob instanceof Map);
//				System.out.println(ob);
				secondXml(code,mob);
			}else{
				code.setText((String)elements.get(element));
			}
			
		}
		
		
		return ZYHY;
	}
	
	@SuppressWarnings("unchecked")
	public static String recursionXml(Map elements){
		Document document = DocumentHelper.createDocument();
		Element ZYHY=document.addElement("ZYHY");
		secondXml(ZYHY,elements);
		
		return document.asXML();
		
	}
	
	
	@SuppressWarnings("unchecked")
	public static String  praseXml(Map elements){
		Document document = DocumentHelper.createDocument();
		Element ZYHY=document.addElement("IPCP");
		Set<String>  key=elements.keySet();
		Iterator<String>  it=key.iterator();
		while(it.hasNext()){
			String  element=it.next();
			if(element.equalsIgnoreCase("Table")){
				ArrayList<Map>  ls=(ArrayList) elements.get(element);
				
				for(Map e:ls){
					Element TABLE=ZYHY.addElement(element);
					Set<String>  keys=e.keySet();
					Iterator<String>  its=keys.iterator();
					while(its.hasNext()){
						String  en=its.next();
						Element code=TABLE.addElement(en);
						code.setText((String)e.get(en));
					}
				}
				
				
			}else{
			Element code=ZYHY.addElement(element);
			code.setText((String)elements.get(element));
			}
		}
		
		return document.asXML();
		
		
		
	}

}
