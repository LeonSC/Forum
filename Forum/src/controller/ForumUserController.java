package controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import model.ForumContent;
import model.ForumTitle;
import model.User;
import service.ForumContentService;
import service.ForumHeadLineService;
import startup.Config;
import startup.ForumCache;

@Controller
public class ForumUserController {
	
	@Autowired
	private ForumContentService forumContentService;
	
	@Autowired
	private ForumHeadLineService forumHeadLineService;
	
	////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * index -> next step / block
	 * @param request
	 * @param BMID
	 * @return
	 */
	@RequestMapping("/block/{FOLLOWBMID}")
	public String block(HttpServletRequest request,@PathVariable String FOLLOWBMID,@RequestParam(value="page", required=false)Integer page)
	{
		ForumTitle ft=ForumCache.getCache().forumTitle.get(FOLLOWBMID);
		
		if(ft==null)
		{
			return "error/error";
		}
		
		request.setAttribute("ft", ft);
		
		//get headline
		request.setAttribute("headline", this.forumHeadLineService.getHeadLine(FOLLOWBMID));
		
		//get content
		request.setAttribute("page", this.forumContentService.getForumContentList(FOLLOWBMID, page));
		
		return "block";
	}
	
	
	/**
	 * index -> next step / block -> add a new topic
	 * @param request
	 * @param BMID
	 * @param TOPBMID
	 * @param FOLLOWBMID
	 * @return
	 */
	@RequestMapping("/newtopic/{FOLLOWBMID}")
	public String newTopic(HttpServletRequest request,@PathVariable String FOLLOWBMID)
	{
		User u=(User)request.getSession().getAttribute("mem");
		
		if(u==null)
		{
			request.getSession().setAttribute("recentView", request.getServletPath());
			return "redirect:"+Config.getConfig().getRootPath()+"/memlogin";
		}
		
		ForumTitle ft=ForumCache.getCache().forumTitle.get(FOLLOWBMID);
		
		if(ft==null)
		{
			return "error/error";
		}
		
		//进入页面需要
		request.setAttribute("ft", ft);
		
		//提交需要
		request.getSession().setAttribute("preSubmitFC", this.forumContentService.getAPreSubmitForumContent(u,FOLLOWBMID));
		
		//查找是否有临时文件
		request.getSession().setAttribute("tmpCon", this.forumContentService.getTempContentByUserOuterKey(u.getBM_ID()));
		return "newtopic";
	}
	
	/**
	 * index -> next step / block -> add a new topic -> submit and go back
	 * @param request
	 * @param BMID
	 * @param TOPBMID
	 * @param FOLLOWBMID
	 * @return
	 */
	@RequestMapping("/newtopicsubmit")
	public String newTopicSubmit(HttpServletRequest request,
			@RequestParam(value="title", required=false)String title,
			@RequestParam(value="content", required=false)String content)
	{
		ForumContent fc=(ForumContent)request.getSession().getAttribute("preSubmitFC");
		
		if(fc==null)
		{
			return "error/error";
		}
		
		this.forumContentService.saveATopic(fc, title, content);
		return "redirect:/block/"+fc.getOuterkey();
	}
	
	/**
	 * 发起编辑一个发帖
	 * @param request
	 * @param BMID
	 * @param CBMID
	 * @return
	 */
	@RequestMapping("/editnewtopic")
	public String editNewTopic(HttpServletRequest request,
			@RequestParam(value="page", required=false)String page,
			@RequestParam(value="key", required=false)String mainContentID,
			@RequestParam(value="cid", required=false)String CBMID)
	{
		User u=(User)request.getSession().getAttribute("mem");
		
		if(u==null)
		{
			request.getSession().setAttribute("recentView", request.getServletPath());
			return "redirect:"+Config.getConfig().getRootPath()+"/memlogin";
		}
		
		ForumContent mainFc=this.forumContentService.getForumContentByBMID(mainContentID);
		
		if(mainFc==null)
		{
			return "error/error";
		}
		
		ForumTitle ft=ForumCache.getCache().forumTitle.get(mainFc.getOuterkey());
		
		if(ft==null)
		{
			return "error/error";
		}
		
		request.setAttribute("page", page);
		request.setAttribute("mainContentID", mainContentID);
		
		request.setAttribute("ft", ft);
		
		ForumContent fc=this.forumContentService.getForumContentByBMID(CBMID);
		request.setAttribute("ForumContent", fc);
		
		return "editnewtopic";
	}
	
	/**
	 * 提交编辑后的帖子
	 * @param request
	 * @param BMID
	 * @param CBMID
	 * @return
	 */
	@RequestMapping("/editnewtopicsubmit")
	public String editNewTopicSubmit(HttpServletRequest request,
			@RequestParam(value="content", required=false)String content,
			@RequestParam(value="page", required=false)String page,
			@RequestParam(value="key", required=false)String mainContentID,
			@RequestParam(value="cid", required=false)String CBMID)
	{
		User u=(User)request.getSession().getAttribute("mem");
		
		if(u==null)
		{
			request.getSession().setAttribute("recentView", request.getServletPath());
			return "redirect:"+Config.getConfig().getRootPath()+"/memlogin";
		}
		
		this.forumContentService.editForumContentByBMID(u.getBM_ID(),CBMID, content);
		
		return new StringBuffer("redirect:/view?page=").append(page).append("&key=").append(mainContentID).toString();
	}
	
	/**
	 * 删除自己写的帖子
	 * @param request
	 * @param BMID
	 * @param mainContentID
	 * @param CBMID
	 * @return
	 */
	@RequestMapping("/delselfcontent")
	public String delSelfContent(HttpServletRequest request,
			@RequestParam(value="block", required=false)String blockID,
			@RequestParam(value="key", required=false)String mainContentID,
			@RequestParam(value="cid", required=false)String CBMID)
	{
		User u=(User)request.getSession().getAttribute("mem");
		
		if(u==null)
		{
			request.getSession().setAttribute("recentView", request.getServletPath());
			return "redirect:"+Config.getConfig().getRootPath()+"/memlogin";
		}
		
		this.forumContentService.delSelfContent(u.getBM_ID(), CBMID);
		
		if(blockID!=null)
		{
			return new StringBuffer("redirect:/block/").append(blockID).toString();
		}
		
		return new StringBuffer("redirect:/view?key=").append(mainContentID).toString();
	}
	
	////////////////////////////////////////////////////////////////////////////////
	/**
	 * index -> next step / block -> view a topic
	 * @param request
	 * @param topickey
	 * @param page
	 * @return
	 */
	@RequestMapping("/view")
	public String viewTopic(HttpServletRequest request,@RequestParam(value="key", required=false) String topickey,@RequestParam(value="page", required=false)Integer page)
	{
		ForumContent fc=this.forumContentService.getForumContentByBMID(topickey);
		
		if(fc==null)
		{
			return "error/error";
		}
		
		ForumTitle ft=ForumCache.getCache().forumTitle.get(fc.getOuterkey());
		
		if(ft==null)
		{
			return "error/error";
		}
		
		request.setAttribute("ft", ft);
		request.setAttribute("fc", fc);
		request.setAttribute("page", this.forumContentService.getForumContentListAsc(topickey, page));
		
		return "view";
	}
	
	/**
	 * index -> next step / block -> view a topic -> add a new reply (not quick reply)
	 * @param request
	 * @param topickey
	 * @return
	 */
	@RequestMapping("/reply/{topickey}")
	public String newReply(HttpServletRequest request,@PathVariable String topickey)
	{
		User u=(User)request.getSession().getAttribute("mem");
		
		if(u==null)
		{
			request.getSession().setAttribute("recentView", request.getServletPath());
			return "redirect:"+Config.getConfig().getRootPath()+"/memlogin";
		}
		
		ForumContent fc=this.forumContentService.getForumContentByBMID(topickey);
		
		if(fc==null)
		{
			return "error/error";
		}
		
		ForumTitle ft=ForumCache.getCache().forumTitle.get(fc.getOuterkey());
		
		if(ft==null)
		{
			return "error/error";
		}
		
		//进入页面需要
		request.setAttribute("ft", ft);
		request.setAttribute("fc", fc);
		
		//提交需要
		request.getSession().setAttribute("preSubmitFC", this.forumContentService.getAPreSubmitForumContent(u,fc.getBM_ID()));
		
		return "reply";
	}
	
	/**
	 * index -> next step / block -> view a topic -> add a new reply (not quick reply) -> submit
	 * @param request
	 * @param title
	 * @param content
	 * @return
	 */
	@RequestMapping("/replysubmit")
	public String newReplySubmit(HttpServletRequest request,
			@RequestParam(value="title", required=false)String title,
			@RequestParam(value="content", required=false)String content)
	{		
		ForumContent fc=(ForumContent)request.getSession().getAttribute("preSubmitFC");
		
		if(fc==null)
		{
			return "error/error";
		}
		this.forumContentService.saveAReply(fc, title, content);
		return "redirect:/view?page=1&key="+fc.getOuterkey();
	}
	
	@RequestMapping("/quickreplysubmit")
	public String newQuickReplySubmit(HttpServletRequest request,
			@RequestParam(value="title", required=false)String title,
			@RequestParam(value="content", required=false)String content,
			@RequestParam(value="key", required=false)String outerkey)
	{
		User u=(User)request.getSession().getAttribute("mem");
		
		if(u==null)
		{
			request.getSession().setAttribute("recentView", "/view?page=1&key="+outerkey);
			return "redirect:"+Config.getConfig().getRootPath()+"/memlogin";
		}
		
		this.forumContentService.saveAQuickReply(u, outerkey, title, content);
		
		return new StringBuffer("redirect:/view?page=1&key=").append(outerkey).toString();
	}
	
	/**
	 * 用于长文的自动保存
	 * @param request
	 * @param title
	 * @param content
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/replyautosave")
	public String autoSave(HttpServletRequest request,@RequestParam(value="title", required=false)String title,@RequestParam(value="content", required=false)String content)
	{
		User u=(User)request.getSession().getAttribute("mem");
		
		if(u==null)
		{
			return "-1";
		}
		
		this.forumContentService.autoSaveAReply(u.getBM_ID(), title, content);
		
		return "";
	}
}
