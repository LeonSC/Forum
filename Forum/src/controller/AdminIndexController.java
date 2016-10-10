package controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import model.ForumTitle;
import service.ForumContentService;
import service.ForumTitleService;
import service.SynchroService;
import service.UserService;
import startup.ForumCache;

@Controller
@RequestMapping("/admin")
public class AdminIndexController {
			
	@Autowired
	private ForumTitleService forumTitleService;
	@Autowired
	private UserService userService;
	@Autowired
	private ForumContentService forumContentService;
	@Autowired
	private SynchroService synchroService;
	
	
	@RequestMapping("")
	public String root()
	{
		return "redirect:/admin/index";
	}
	
	@RequestMapping("/")
	public String home()
	{
		return "redirect:/admin/index";
	}
	
	
	@RequestMapping("/index")
	public String index()
	{
		return "admin/index";
	}
	
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	@RequestMapping("/forumedit")
	public String forumEdit(HttpServletRequest request)
	{
		request.setAttribute("forumtitle", this.forumTitleService.findFullForumTitleByBMID());
		
		return "admin/forumedit";
	}
	
	@RequestMapping("/forumadd-submit")
	public String forumAddSubmit(HttpServletRequest request,
			@RequestParam(value="name", required=false)String name,
			@RequestParam(value="order", required=false)String order,
			@RequestParam(value="outerkey", required=false)String outerkey,
			@RequestParam(value="topkey", required=false)String topkey)
	{
		int ordernum=0;
		try {
			ordernum=Integer.parseInt(order);
		} catch (NumberFormatException e) {
			
		}
		
		this.forumTitleService.saveForumTitle(outerkey,name,ordernum);
		
		return "redirect:forumedit";
	}
	
	/**
	 * structure edit
	 * @param name
	 * @param order
	 * @param BM_ID
	 * @return
	 */
	@RequestMapping("/forumedit-submit")
	public String forumEditSubmit(
			@RequestParam(value="name", required=false)String name,
			@RequestParam(value="order", required=false)String order,
			@RequestParam(value="BM_ID", required=false)String BM_ID)
	{
		int ordernum=0;
		try {
			ordernum=Integer.parseInt(order);
		} catch (NumberFormatException e) {
			
		}
		
		this.forumTitleService.editForumTitle(BM_ID,name,ordernum);
		
		return "redirect:forumedit";
	}
	
	/**
	 * turn a forum on or off
	 * @param request
	 * @param page
	 * @param key
	 * @return
	 */
	@RequestMapping("/forumonoffline")
	public String forumOnOffLine(HttpServletRequest request,
			@RequestParam(value="page", required=false)Integer page)
	{
		if(ForumCache.getCache().forumTitle.containsKey("root"))
		{
			ForumCache.getCache().forumTitle.clear();
			this.synchroService.setForumOff();
		}
		else
		{
			ForumCache.getCache().renewForumTitleCache();
			this.synchroService.setForumOn();
		}
		
		return "redirect:forumedit";
	}
	
	/*****************编辑背景图和ICON************************/
	/**
	 * 进入图标编辑页面
	 * @param request
	 * @param key
	 * @return
	 */
	@RequestMapping("/background")
	public String editBackgroundIcon(HttpServletRequest request,@RequestParam(value="key", required=false)String key)
	{
		ForumTitle ft=this.forumTitleService.findByBMID(key);
		
		if(ft==null)
		{
			return "error/error";
		}
		request.setAttribute("forumtitle", ft);
		
		return "admin/background";
	}
	
	/**
	 * 编辑ICON
	 * @param BM_ID
	 * @param imgSrc
	 * @return
	 */
	@RequestMapping("/background-icon-submit")
	public String submitBackgroundIcon(@RequestParam(value="BM_ID", required=false)String BM_ID, @RequestParam(value="imgSrc", required=false)String imgSrc)
	{
		this.forumTitleService.editForumTitleIcon(BM_ID, imgSrc);
		
		return "redirect:background?key="+BM_ID;
	}
	
	/**
	 * 编辑Background
	 * @param BM_ID
	 * @param imgSrc
	 * @return
	 */
	@RequestMapping("/background-background-submit")
	public String submitBackgroundBackground(@RequestParam(value="BM_ID", required=false)String BM_ID, @RequestParam(value="imgSrc", required=false)String imgSrc)
	{
		this.forumTitleService.editForumTitleBackground(BM_ID, imgSrc);
		
		return "redirect:background?key="+BM_ID;
	}
	
	
	/**********************************************************************/
	@RequestMapping("/editmanager")
	public String editManager(HttpServletRequest request,
			@RequestParam(value="page", required=false)Integer page,
			@RequestParam(value="key", required=false)String key,
			@RequestParam(value="sname", required=false)String sname)
	{
		ForumTitle ft=this.forumTitleService.findByBMID(key);
		
		if(ft==null)
		{
			return "redirect:forumlist?npage=1";
		}
		
		request.setAttribute("susers", this.userService.findTenUsersByAccountWithPage(sname, page));
		request.setAttribute("forumtitle", ft);
		request.setAttribute("nowkey", key);
		return "admin/editmanager";
	}
	
	
	@RequestMapping("/addmanagertoblock")
	public String addManagerToBlock(@RequestParam(value="key", required=false)String key,
			@RequestParam(value="ukey", required=false)String ukey)
	{
		this.forumTitleService.addManagerToBlock(key, ukey);
		return "redirect:editmanager?key="+key+"&page=1";
	}
	
	/**
	 * 删除一个分论坛管理员
	 * @param key
	 * @param ukey
	 * @return
	 */
	@RequestMapping("/delmanagerinblock")
	public String delManagerInBlock(@RequestParam(value="key", required=false)String key,@RequestParam(value="ukey", required=false)String ukey)
	{
		this.forumTitleService.delManagerInBlock(key, ukey);
		return "redirect:editmanager?key="+key+"&page=1";
	}
	
	
	///////////////////////////////////////////////////////////////////////////
	/**
	 * 合并板块
	 * @return
	 */
	@RequestMapping("/merge")
	public String mergeBlockIn(HttpServletRequest request,
			@RequestParam(value="root", required=false)String root,
			@RequestParam(value="page", required=false)Integer page,
			@RequestParam(value="from", required=false)String from)
	{
		request.setAttribute("root",root);
		
		request.setAttribute("from", ForumCache.getCache().forumTitle.get(from));
		
		request.setAttribute("to", ForumCache.getCache().getAllEndForumTitleByBMID(from));
		
		//get content
		request.setAttribute("page", this.forumContentService.getForumContentList(from, page));
		
		return "admin/merge";
	}
	
	/**
	 * 一次性全部合并
	 * @param request
	 * @param page
	 * @param from
	 * @param toBMID
	 * @return
	 */
	@RequestMapping("/all-merge-submit")
	public String allMergeSubmit(HttpServletRequest request,
			@RequestParam(value="root", required=false)String root,
			@RequestParam(value="page", required=false)Integer page,
			@RequestParam(value="from", required=false)String from,
			@RequestParam(value="toBMID", required=false)String toBMID)
	{
		this.forumContentService.mergeAllContentToTarget(from, toBMID);
		
		return "redirect:merge?from="+from+"&page=1&root="+root;
	}
	
	/**
	 * 部分合并
	 * @param request
	 * @param page
	 * @param from
	 * @return
	 */
	@RequestMapping("/merge-submit")
	public String mergeSubmit(HttpServletRequest request,
			@RequestParam(value="root", required=false)String root,
			@RequestParam(value="page", required=false)Integer page,
			@RequestParam(value="from", required=false)String from,
			@RequestParam(value="toBMID", required=false)String toBMID,
			@RequestParam(value="titlechecked", required=false)String[] titlechecked)
	{
		this.forumContentService.mergeContentToTarget(from, toBMID, titlechecked);
		
		return "redirect:merge?from="+from+"&page=1&root="+root;
	}
	
	/**
	 * 全部丢入回收站
	 * @param request
	 * @param page
	 * @param from
	 * @param titlechecked
	 * @return
	 */
	@RequestMapping("/all-recycle-submit")
	public String allRecycleSubmit(HttpServletRequest request,
			@RequestParam(value="root", required=false)String root,
			@RequestParam(value="page", required=false)Integer page,
			@RequestParam(value="from", required=false)String from)
	{
		this.forumContentService.setForumContentToRECByOuterKeyWithoutAuth(from);
		
		return "redirect:merge?from="+from+"&page=1&root="+root;
	}
	
	/**
	 * 部分放入回收站
	 * @param request
	 * @param page
	 * @param from
	 * @param titlechecked
	 * @return
	 */
	@RequestMapping("/recycle-submit")
	public String recycleSubmit(HttpServletRequest request,
			@RequestParam(value="root", required=false)String root,
			@RequestParam(value="page", required=false)Integer page,
			@RequestParam(value="from", required=false)String from,
			@RequestParam(value="titlechecked", required=false)String[] titlechecked)
	{
		this.forumContentService.setForumContentToRECWithoutAuth(titlechecked);
		
		return "redirect:merge?from="+from+"&page=1&root="+root;
	}
	
	/**
	 * 删除一个节点
	 * @param from
	 * @param root
	 * @return
	 */
	@RequestMapping("/block-del-submit")
	public String delBlock(@RequestParam(value="from", required=false)String from,@RequestParam(value="root", required=false)String root)
	{
		this.forumTitleService.realDelABlock(from);
		
		return "redirect:forumedit?key="+root;
	}
	
	/**
	 * 删除一个节点
	 * @param from
	 * @param root
	 * @return
	 */
	@RequestMapping("/top-del-submit")
	public String delTopKey(@RequestParam(value="key", required=false)String key)
	{
		this.forumTitleService.realDelABlock(key);
		
		return "redirect:forumlist";
	}

	//////////////////////////////////////同步设置相关/////////////////////////////////////////////
	/**
	 * 同步首页
	 * @param request
	 * @return
	 */
	@RequestMapping("/synchro")
	public String synIndex(HttpServletRequest request,@RequestParam(value="page", required=false)Integer page)
	{
		request.setAttribute("page", this.synchroService.findLimit(page));
		
		return "admin/syn";
	}
	
	/**
	 * 提交同步列表
	 * @param ip
	 * @return
	 */
	@RequestMapping("/synchro-submit")
	public String synSubmit(@RequestParam(value="ip", required=false)String ip,@RequestParam(value="alias", required=false)String alias)
	{
		this.synchroService.save(ip, alias);
		
		return "redirect:synchro";
	}
	
	
	/**
	 * 点击事件后,触发更新指令,把更新分发到各个节点去
	 * @return
	 */
	@RequestMapping("/synchro-now")
	public String synNow()
	{
		this.synchroService.renewSynKeyAndRelord();
		
		return "redirect:synchro";
	}
	
	/**
	 * 节点管理,删除
	 * @return
	 */
	@RequestMapping("/synchro-del")
	public String synNodeDel(@RequestParam(value="bmid", required=false)String BMID)
	{
		this.synchroService.deleteANode(BMID);
		return "redirect:synchro";
	}
}
