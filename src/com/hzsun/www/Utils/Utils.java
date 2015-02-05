package com.hzsun.www.Utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {
	
	private static final  String  MD5="ok15we1@oid8x5afd@";
	
	public static  String getMD5(){
		return  MD5;
	}
	
	public static  String formatString (Object ob){
		if(ob!=null){
			return ob.toString().trim();
		}else {
			return "";
		}
		
	}
	
	public static String formatNull(Object ob){
		if(ob!=null || "null".equalsIgnoreCase(ob.toString())){
			return ob.toString().trim();
		}else {
			return "";
		}
	}
	
	public static Integer formatInteger (Object ob){
		if(ob!=null){
			return Integer.parseInt(ob.toString().trim());
		}else {
			return 0;
		}
	}
	
	public static Long formatLong (Object ob){
		if(ob!=null){
			return Long.parseLong(ob.toString().trim());
		}else {
			return 0L;
		}
	}
	
	public static Integer formatNagetive (Object ob){
		if(ob!=null){
			return Integer.parseInt(ob.toString().trim());
		}else {
			return -1;
		}
	}
	
	public static Double formatDouble (Object ob){
		if(ob!=null){
			return Double.parseDouble((ob.toString().trim()));
		}else {
			return 0.00;
		}
	}
	
	public static Integer  getTotalPage(Integer total,Integer rows){
		Integer  pages=1;
		if(total.equals(0)){
			pages=1;
		}else{
		
		if(total%rows==0){
			pages=total/rows;
		}else{
			pages=total/rows+1;
		}
		
		}
		return pages;
		
	}
	
	
	public static Integer  getWebpage(String page,Integer totalpages){
		Integer pg=1;
		if(page==null){
			pg=1;
			return pg;
		}else {
			pg=Integer.parseInt(page);
		
			if(pg<1){
				pg=1;
			}else if (pg>totalpages){
				pg=totalpages;
			}
		}
		return pg;
	}
	
	public static String regString(String text, String reg) {
		Pattern  pattern=Pattern.compile(reg);
		Matcher  match=pattern.matcher(text);
		String  str="<p style=\"text-align: center\"> ";
		while(match.find()){
			String  tem=match.group(0);
			String[] s=tem.split("\"");
			tem="http://www.wellsale.cn"+s[1];
			str+="<img  width=\"100%\"  src=\""+tem+"\"  />";
		}
		
		str+="<p>";
		
		return str;
	}
	
	public  static String  getCurrentTime(){
		SimpleDateFormat  sdf=new SimpleDateFormat("yyyyMMdd");
		String   str=sdf.format(new Date());
		Calendar  currentDate=Calendar.getInstance();
		Calendar tommorowDate = new GregorianCalendar(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE), 0, 0, 0);
		  str+=Utils.formatString((Calendar.getInstance().getTimeInMillis()-tommorowDate.getTimeInMillis())/1000);
		
		return str;
	}
	
	public static  Double  formatNumberDouble(Double ob){
		BigDecimal   bd=new BigDecimal(ob);
		return bd.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	
	
//	public static Map<String , Object > getParamter(HttpServletRequest request,
//			String savepath,String pathurl  ) throws Exception{
//		Map<String , Object >  map=new HashMap<String , Object >();
//		//�?��目录
//		File  uploadDir=new File(savepath);
//		if(!uploadDir.isDirectory()){
//			uploadDir.mkdirs();
//		}
//		
//
//		//创建根目�?
//		savepath += "uploadFile/";
//		pathurl+="uploadFile/";
//		File saveDirFile = new File(savepath);
//		if (!saveDirFile.exists()) {
//			saveDirFile.mkdirs();
//		}
//		//根据文件时间创建目录
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//		String ymd = sdf.format(new Date());
//		savepath += ymd + "/";
//		pathurl+=ymd + "/";
//		File dirFile = new File(savepath);
//		if (!dirFile.exists()) {
//			dirFile.mkdirs();
//		}
//		
//		FileItemFactory factory = new DiskFileItemFactory();
//		ServletFileUpload upload = new ServletFileUpload(factory);
//		upload.setHeaderEncoding("UTF-8");
//		List items=null;
//		try {
//			items=upload.parseRequest(request);
//			Iterator  it=items.iterator();
//			while(it.hasNext()){
//				
//				
//				FileItem item = (FileItem) it.next();
//				if(!item.isFormField()){
//					String  name=item.getName();
//					
//					if(name!=null &&  !("".equals(name)) ){
//						//更改文件�?
//					String fileExt = name.substring(name.lastIndexOf(".") + 1).toLowerCase();
//					SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
//					String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
//					pathurl+=newFileName;
//					File   upfile=new File(savepath,newFileName);
//					item.write(upfile);
//					map.put(item.getFieldName(), pathurl);
//					}else {
//						map.put(item.getFieldName(), "");
//					}
//				}else {
//					map.put(item.getFieldName(), item.getString("utf-8"));
//				}
//			}
//		} catch (FileUploadException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return map;
//	}
	
	public static boolean  isEmpty(Object ob){
		
		return ob==null || "".equals(ob) ;
	}
	

	public static  String  getUUid(){
		UUID uuid = UUID.randomUUID();
        String uid=uuid.toString().replace("-","");
        return uid;
	}

	public static void  main (String[] args){
		System.out.print(Utils.formatNumberDouble(3.493893459843));
	}
	
	public static String parseInvite(String parsemsg ){
		String inip="",inport="";
		if(parsemsg.contains("c=IN IP4")){
			Integer  s=parsemsg.indexOf("c=IN IP4")+8;
			inip=parsemsg.substring(s+1,parsemsg.indexOf("\r\n",s));
			System.out.println(inip); 
		}
		if(parsemsg.contains("m=video")){
			Integer  s=parsemsg.indexOf("m=video")+7;
			inport=parsemsg.substring(s+1,parsemsg.indexOf(" ",s+1));
			System.out.println(inport);
		}
		return inip+":"+inport;
	}
	
	
}
