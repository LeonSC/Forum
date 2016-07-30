package startup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;


import tools.Tools;

public class Config {

	private static Config config;
	
	private Config(){}
	
	public static Config getConfig()
	{
		if(Config.config==null)
		{
			Config.config=new Config();
			Config.config.propertyReader();
		}
		
		return Config.config;
	}
	
	private final static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	public static String updateTime=sdf.format(new Date(Tools.getServerTime()));

	///////////////常用路径参数////////////////////////
	public static String rootPath="";
	
	//远程上传图片
	public static String mainIP="";
	public static String uploadCallbackPath="";
	
	public static String bucketName="";
	public static String domainBucket="";
	
	//远程图片服务器的用户和口令
	public static String imageLogin="";
	public static String imagePw="";
	
	/**
	 * 数据库设置
	 */
	public static String dbUser;
	public static String dbBase;
	public static String dbPassword;
	public static String dbIP;
	public static Integer dbPort;
	//
	
	public String getUpdateTime() {
		return updateTime;
	}
	public String getRootPath() {
		return rootPath;
	}
	public String getDomainBucket()
	{
		return domainBucket;
	}
	
	/**
	 * 读取配置文件
	 */
	public void propertyReader()
	{
		Properties prop = this.getProperties();
		
		Config.rootPath = prop.getProperty("rootPath");

		Config.mainIP = prop.getProperty("mainIP");
		Config.uploadCallbackPath = prop.getProperty("uploadCallbackPath")+";"+Config.rootPath+"/img/uploadcallback;"+Config.mainIP+"/img/uploadcallback";
		
		Config.bucketName=prop.getProperty("bucketName");
		Config.domainBucket=prop.getProperty("domainBucket");
		
		Config.imageLogin = prop.getProperty("imageLogin");
		Config.imagePw = prop.getProperty("imagePw");
		
		Config.dbUser = prop.getProperty("dbUser");
		Config.dbBase = prop.getProperty("dbBase");
		Config.dbPassword = prop.getProperty("dbPassword");
		Config.dbIP = prop.getProperty("dbIP");
		Config.dbPort = 27017;
		
		if(prop.getProperty("dbPort")!=null)
		{
			try {
				Config.dbPort=Integer.parseInt(prop.getProperty("dbPort"));
			} catch (NumberFormatException e) {}
		}
	}
	
	/**
	 * 读取
	 * @return
	 */
	private Properties getProperties()
	{
		Properties prop = new Properties();
		InputStream in = null;
		
		File[] roots = File.listRoots();
		
		String root="";
		
		if(roots.length>0)
		{
			root=roots[0].toString();
		}
		
		String[] pathList={root+"forum.property",root+"home"+File.separator+"forum.property",root+"work"+File.separator+"cms.property"};
		
		try {
			for(String base:pathList)
			{
				File file = new File(base);
				if(file.exists()&&!file.isDirectory())
				{
					in = new FileInputStream(file);
					prop.load(in);
					in.close();
					return prop;
				}
			}
		} catch (FileNotFoundException e) {
			return prop;
		} catch (IOException e) {
			return prop;
		} finally{
			try {if(in!=null){in.close();}} catch (IOException e) {}
		}
		
		return prop;
	}
}
