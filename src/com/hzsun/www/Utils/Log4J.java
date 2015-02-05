package com.hzsun.www.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

//加载log4j文件
public class Log4J {
	private  static Logger log;
	
	 static{
		
		try {
			 InputStream is = new FileInputStream(System.getProperty("user.dir") + "/config/log4j.properties");
			 Properties p=new Properties();
			 p.load(is);
			 PropertyConfigurator.configure(p);
			 log=Logger.getLogger(Log4J.class);
			 is.close();
			 System.out.println("log4J初始成功！");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	 
	public static Logger  getLogger(){
		 return log;
	 }
}
