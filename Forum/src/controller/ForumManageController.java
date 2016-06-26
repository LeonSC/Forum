package controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import model.ForumContent;
import model.ForumTitle;
import model.User;
import service.ForumContentService;
import service.ForumHeadLineService;
import startup.ForumCache;

@Controller
@RequestMapping("/forum/{BMID}/manage")
public class ForumManageController {

	@Autowired
	private ForumHeadLineService forumHeadLineService;
	@Autowired
	private ForumContentService forumContentService;
	
	/**
	 * 在本版置顶
	 * @param request
	 * @param topicid
	 * @return
	 */
	@RequestMapping("/settopictoblocktop")
	public String setTopicToBlockTop(HttpServletRequest request,@PathVariable String BMID,@RequestParam(value="topicid", required=false)String topicid)
	{
		User u=(User)request.getSession().getAttribute("mem");
		
		ForumContent fc=this.forumHeadLineService.setToBlockHeadLine(u,topicid);
		
		if(fc==null)
		{
			return "error/error";
		}
		
		return new StringBuffer("redirect:/forum/").append(BMID).append("/block/").append(fc.getOuterkey()).append("?page=1").toString();//跳转回BLOCK中
	}
	
	/**
	 * 在全区置顶
	 * @param request
	 * @param topicid
	 * @return
	 */
	@RequestMapping("/settopictoforumtop")
	public String setTopicToForumTop(HttpServletRequest request,@PathVariable String BMID,@RequestParam(value="topicid", required=false)String topicid)
	{
		User u=(User)request.getSession().getAttribute("mem");
		
		ForumContent fc=this.forumHeadLineService.setToForumHeadLine(u,topicid);
		
		if(fc==null)
		{
			return "error/error";
		}
		
		return new StringBuffer("redirect:/forum/").append(BMID).append("/block/").append(fc.getOuterkey()).append("?page=1").toString();//跳转回BLOCK中
	}
	
	/**
	 * 取消置顶
	 * @param request
	 * @param topicid
	 * @return
	 */
	@RequestMapping("/settopictonormal")
	public String setTopicToNormal(HttpServletRequest request,@PathVariable String BMID,@RequestParam(value="topicid", required=false)String topicid)
	{
		User u=(User)request.getSession().getAttribute("mem");
		
		ForumContent fc=this.forumHeadLineService.setToNormal(u,topicid);
		
		if(fc==null)
		{
			return "error/error";
		}
		
		return new StringBuffer("redirect:/forum/").append(BMID).append("/block/").append(fc.getOuterkey()).append("?page=1").toString();//跳转回BLOCK中
	}
	
	/**
	 * 进入回收站
	 * @return
	 */
	@RequestMapping("/recycle")
	public String recycle(HttpServletRequest request,@PathVariable String BMID,@RequestParam(value="page", required=false)Integer page)
	{
		User u=(User)request.getSession().getAttribute("mem");
		
		request.setAttribute("forumtitle", ForumCache.getCache().forumTitle.get(BMID));
		
		request.setAttribute("page", this.forumContentService.getRecycleForumContentList(BMID,u.getBM_ID(), page));
		
		return "forum/recycle";
	}
	
	
	/**
	 * 回收
	 * @return
	 */
	@RequestMapping("/settopictorecycle")
	public String setTopicToRecycle(HttpServletRequest request,@PathVariable String BMID,@RequestParam(value="topicid", required=false)String topicid)
	{
		User u=(User)request.getSession().getAttribute("mem");
		
		request.setAttribute("forumtitle", ForumCache.getCache().forumTitle.get(BMID));
		
		this.forumContentService.setForumContentToREC(topicid, u.getBM_ID());
		
		return new StringBuffer("redirect:/forum/").append(BMID).append("/manage/recycle?page=1").toString();
	}
	
	
	/**
	 * 反回收
	 * @return
	 */
	@RequestMapping("/setbacktopicfromrecycle")
	public String setBackTopicFromRecycle(HttpServletRequest request,@PathVariable String BMID,@RequestParam(value="key", required=false)String topicid)
	{
		User u=(User)request.getSession().getAttribute("mem");
		
		request.setAttribute("forumtitle", ForumCache.getCache().forumTitle.get(BMID));
		
		this.forumContentService.setForumContentToBack(topicid, u.getBM_ID());
		
		return new StringBuffer("redirect:/forum/").append(BMID).append("/manage/recycle?page=1").toString();
	}
	
	/**
	 * 查看被放入回收站的帖子
	 * @param request
	 * @param BMID
	 * @param topickey
	 * @param page
	 * @return
	 */
	@RequestMapping("/delview")
	public String viewDelTopic(HttpServletRequest request,@PathVariable String BMID,@RequestParam(value="key", required=false) String topickey,@RequestParam(value="page", required=false)Integer page)
	{
		ForumContent fc=this.forumContentService.getForumDelContentByBMID(topickey);
		
		if(fc==null)
		{
			return "error/error";
		}
		
		ForumTitle ft=ForumCache.getCache().forumTitle.get(fc.getOuterkey());
		
		if(ft==null)
		{
			return "error/error";
		}
		
		request.setAttribute("forumtitle", ForumCache.getCache().forumTitle.get(BMID));
		request.setAttribute("ft", ft);
		request.setAttribute("fc", fc);
		request.setAttribute("page", this.forumContentService.getForumContentList(topickey, page));
		
		return "forum/delview";
	}
	
	/**
	 * 真实删除这个帖子以及所属的帖子
	 * @param request
	 * @param BMID
	 * @param topicid
	 * @return
	 */
	@RequestMapping("/del")
	public String realDel(HttpServletRequest request,@PathVariable String BMID,@RequestParam(value="topicid", required=false)String topicid)
	{
		User u=(User)request.getSession().getAttribute("mem");
		
		ForumContent fc=this.forumContentService.realDel(topicid,u.getBM_ID());
		
		if(fc==null)
		{
			return "error/error";
		}
		
		return new StringBuffer("redirect:/forum/").append(BMID).append("/block/").append(fc.getOuterkey()).append("?page=1").toString();//跳转回BLOCK中
	}
}
