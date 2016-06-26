package tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

public class Tools {

	public static Long getServerTime()
	{
		return System.currentTimeMillis();
	}
	
	
	/**
	 * 
	 */
	private static Integer count = 0;
	public static String getID()
	{
		StringBuffer sb=new StringBuffer();
		
		sb.append(Tools.getServerTime()).append(Tools.count);
		
		Tools.count++;
		if(Tools.count>3500000)
		{
			Tools.count=0;
		}
		
		return sb.toString();
	}
	
	//
	public static String getID(String sq)
	{
		if(sq==null)
		{
			sq="A";
		}
		
		StringBuffer sb=new StringBuffer();
		
		sb.append(sq).append(Tools.getServerTime()).append(Tools.count);
		
		Tools.count++;
		if(Tools.count>3500000)
		{
			Tools.count=0;
		}
		
		return sb.toString();
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for(int i=0;i<100;i++)
		{
			System.out.println(Tools.getID());
			
		}
	}
	
	
	private static SimpleDateFormat sdf_yyyy_MM_dd= new SimpleDateFormat("yyyy-MM-dd");
	public static String longTransDateyyyyMMdd(Long l)
	{
		return Tools.sdf_yyyy_MM_dd.format(l);
	}
	
	private static SimpleDateFormat sdf_yyyy_MM_dd_HH_mm= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static String longTransDateyyyyMMddHHmm(Long l)
	{
		return Tools.sdf_yyyy_MM_dd_HH_mm.format(l);
	}
	
	public static String digestSha1(String sou)
	{
		if(sou==null)
		{
			return null;
		}
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(sou.getBytes());
			return Tools.bytes2Hex(md.digest());
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	
	private static String bytes2Hex(byte[] src)
	{         
		char[] res = new char[src.length*2];    
		final char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};    
		for(int i=0,j=0; i<src.length; i++){    
			res[j++] = hexDigits[src[i] >>>4 & 0x0f];    
			res[j++] = hexDigits[src[i] & 0x0f];    
		}    
            
		return new String(res);    
    }
	
	public static String replaceHTML(String str)
	{
		return str.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<.*?>", "");
	}
}
