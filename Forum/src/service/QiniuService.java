package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.ImageDao;
import model.Image;
import startup.Config;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

@Service
public class QiniuService {

	@Autowired
	private ImageDao imageDao;
	
	private String token="thisistoken";
	
	private Auth auth = Auth.create(Config.imageLogin, Config.imagePw);
	
	/**
	 * 生成上传TOKEN
	 * @param usedin
	 * @param outerkey
	 * @return
	 */
	public String getUpToken(String usedin,String outerkey,String userid)
	{
		StringBuffer sb=new StringBuffer("key=$(key)&hash=$(etag)&w=$(imageInfo.width)&h=$(imageInfo.height)&token=")
				.append(this.token)
				.append("&usedin=").append(usedin)
				.append("&outerkey=").append(outerkey)
				.append("&userid=").append(userid);
		
		String token=this.auth.uploadToken(Config.bucketName, null, 3600, new StringMap()
	         .put("callbackUrl", Config.uploadCallbackPath)
	         .putNotEmpty("callbackHost", Config.mainIP)
	         .put("callbackBody", sb.toString())
	         //.put("callbackBodyType", "application/json")
	         .put("fsizeLimit", 1024*1024*1024*3)
	         .put("mimeLimit","image/*"));
	    
	    StringBuffer re=new StringBuffer("{\"uptoken\":\"").append(token).append("\"}");
	    
	    return re.toString();
	}
	
	/**
	 * 用户上传头像的策略
	 * @param userid
	 * @return
	 */
	public String getHeaderIconUpToken(String userid)
	{
		StringBuffer sb=new StringBuffer("key=$(key)&hash=$(etag)&w=$(imageInfo.width)&h=$(imageInfo.height)&token=")
				.append(this.token)
				.append("&userid=").append(userid);
		
		String token=this.auth.uploadToken(Config.bucketName, null, 3600, new StringMap()
	         .put("callbackUrl", Config.uploadCallbackPath)
	         .putNotEmpty("callbackHost", Config.mainIP)
	         .put("callbackBody", sb.toString())
	         .put("persistentOps", "imageView2/1/w/192/h/192/q/10/format/png")
	         .put("persistentNotifyUrl", "http://115.28.12.209/img/notify")
	         .put("fsizeLimit", 1024*1024*1024*3)
	         .put("mimeLimit","image/*"));
	    
	    StringBuffer re=new StringBuffer("{\"uptoken\":\"").append(token).append("\"}");
	    
	    return re.toString();
	}
	
	/**
	 * 
	 * @param token
	 * @param name 图片名称
	 * @param hash
	 * @param w
	 * @param h
	 * @param usedin 具体是哪个模块用了这个图
	 * @param outerkey 具体是哪个BMID用了这张图
	 * @return -1 token出错
	 */
	public int setUploadImgToDB(String token, String name, String hash, Integer w, Integer h,String usedin, String outerkey, String userid)
	{
		/**
		if(!this.equals(token))
		{
			return -1;
		}
		**/
		
		Image img=new Image();
		
		img.setName(name);
		img.setHash(hash);
		img.setW(w);
		img.setH(h);
		img.setUsedin(usedin);
		img.setOuterkey(outerkey);
		img.setUserid(userid);
		
		this.imageDao.save(img);
		
		return 0;
	}
}
