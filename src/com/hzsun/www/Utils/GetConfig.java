package com.hzsun.www.Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GetConfig {
	
	private static  GetConfig  conf=null;
	
	private Integer  Serviceport;
	private Integer  SIPPort;
	private String  SIPAddress;
	private Integer MIN_PORT;
	private Integer MAX_PORT;
	private  String MMSERVICE;
	private Integer CLIENTPORT;
	private Properties p=new Properties();
	
	private GetConfig() throws IOException{
		InputStream is = new FileInputStream(System.getProperty("user.dir") + "/config/config.properties");
		p.load(is);
		Serviceport=Utils.formatInteger(p.getProperty("Serviceport"));
		SIPPort=Utils.formatInteger(p.getProperty("SIPPort"));
		SIPAddress=Utils.formatString(p.getProperty("SIPAddress"));
		MIN_PORT=Utils.formatInteger(p.getProperty("MIN_PORT"));
		MAX_PORT=Utils.formatInteger(p.getProperty("MAX_PORT"));
		MMSERVICE=Utils.formatString(p.getProperty("MMSERVICE"));
		CLIENTPORT=Utils.formatInteger(p.getProperty("CLIENTPORT"));
		is.close();
	}
	
	public static GetConfig getInstance(){
		if(conf==null){
			try {
				conf=new GetConfig();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return conf;
	}

	public Integer getServiceport() {
		return Serviceport;
	}

	
	public Integer getSIPPort() {
		return SIPPort;
	}

	
	public String getSIPAddress() {
		return SIPAddress;
	}

	public Integer getMIN_PORT() {
		return MIN_PORT;
	}

	public Integer getMAX_PORT() {
		return MAX_PORT;
	}

	public String getMMSERVICE() {
		return MMSERVICE;
	}

	public Integer getCLIENTPORT() {
		return CLIENTPORT;
	}

	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	

}
