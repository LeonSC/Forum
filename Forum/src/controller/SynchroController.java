package controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import service.SynchroService;
import startup.ForumCache;

@Controller
@RequestMapping("/synchro")
public class SynchroController {

	@Autowired
	private SynchroService synchroService;
	
	@ResponseBody
	@RequestMapping("/check")
	public String checkConnection(HttpServletResponse response)
	{
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET");
		response.setHeader("Access-Control-Max-Age", "100");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		
		return "ok";
	}
	
	/**
	 * 本类中触发更新锁打开,然后分发到每个服务器的这个连接地址,触发论坛的重建
	 * @param key
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/reload/{key}")
	public String reloadForumTitleCache(@PathVariable String key)
	{
		if(this.synchroService.relordForumTrigerCheck(key)!=0)
		{
			return "no";
		}
		
		//仅仅更新缓存不保存至数据库
		ForumCache.getCache().readForumTitleFromCache();
		
		return "ok";
	}
	
	/**
	 * 论坛上下线
	 * @param key
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/forumonoffline/{key}")
	public String turnonOrOffForum(@PathVariable String key,@RequestParam(value="model", required=false)String model)
	{
		if(this.synchroService.relordForumTrigerCheck(key)!=0)
		{
			return "no";
		}
		
		if(model.equals("off")&&ForumCache.getCache().forumTitle.containsKey("root"))
		{
			//缓存清空
			ForumCache.getCache().forumTitle.clear();
		}
		else if(model.equals("on"))
		{
			//从数据库中读取结构
			ForumCache.getCache().readForumTitleFromCache();
		}
		
		return "ok";
	}
}
