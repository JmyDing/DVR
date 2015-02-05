package MediaTest;

import org.junit.Test;

import com.hzsun.www.Utils.GetConfig;

public class mytest {

	@Test
	public void  testread(){
		GetConfig  config=GetConfig.getInstance();
		System.out.println(config.getSIPAddress());
	}
	
	@Test
	public void  testBooelean(){
		String a=null;
		if(1==1 || a.equals("")){
			System.out.println(1);
		}
		String key="lcx-dfsfsdfs-2107";
		System.out.println(key.indexOf("l"));
		String time=key.substring(key.lastIndexOf("-")+1);
		System.out.println(time);
	}
}
