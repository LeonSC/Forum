package controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import model.ForumContent;
import model.User;
import service.QiniuService;

@Controller
@RequestMapping("/img")
public class ImageController {

	@Autowired
	private QiniuService qiniuService;
	
	//////////////////////接受图片回调//////////////////////////////////////
	@ResponseBody
	@RequestMapping("/uploadcallback")
	public String acceptImageUploadCallBack(
		@RequestParam(value="token", required=false)String token,
		@RequestParam(value="key", required=false)String key,
		@RequestParam(value="hash", required=false)String hash,
		@RequestParam(value="w", required=false)Integer w,
		@RequestParam(value="h", required=false)Integer h,
		@RequestParam(value="usedin", required=false)String usedin,
		@RequestParam(value="outerkey", required=false)String outerkey,
		@RequestParam(value="userid", required=false)String userid)
	{
		this.qiniuService.setUploadImgToDB(token, key, hash, w, h, usedin, outerkey,userid);
		
		return "{\"success\":true}";
	}
	
	
	//////////////////////获取图片上传所需要的TOKEN//////////////////////////////////////
	@ResponseBody
	@RequestMapping("/token")
	public String getUploadToken(HttpServletRequest request)
	{
		Object tmp=request.getSession().getAttribute("mem");
		if(tmp==null)
		{
			return "{\"uptoken\":\"\"}";
		}
		
		User u=(User)tmp;
		
		ForumContent fc=(ForumContent)request.getSession().getAttribute("preSubmitFC");
		return this.qiniuService.getUpToken(fc.getClass().getSimpleName(), fc.getBM_ID(),u.getBM_ID());
	}
	
	@ResponseBody
	@RequestMapping("/adminToken")
	public String getAdminUploadToken(HttpServletRequest request)
	{
		Object tmp=request.getSession().getAttribute("admin");
		if(tmp==null)
		{
			return "{\"uptoken\":\"\"}";
		}
		User adm=(User)tmp;
		
		return this.qiniuService.getUpToken("", "",adm.getBM_ID());
	}
	
	
	@ResponseBody
	@RequestMapping("/headerIconToken")
	public String getHeaderIconUploadToken(HttpServletRequest request)
	{
		Object tmp=request.getSession().getAttribute("mem");
		if(tmp==null)
		{
			return "{\"uptoken\":\"\"}";
		}
		
		User u=(User)tmp;
		
		return this.qiniuService.getHeaderIconUpToken(u.getBM_ID());
	}
}
