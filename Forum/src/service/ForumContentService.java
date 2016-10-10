package service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.ForumContentDao;
import dao.ForumTempContentDao;
import model.Content;
import model.ForumContent;
import model.ForumTitle;
import model.Page;
import model.TempContent;
import model.User;
import startup.ForumCache;
import tools.Tools;

@Service
public class ForumContentService {

	@Autowired
	private ForumContentDao forumContentDao;
	@Autowired
	private ForumTempContentDao forumTempContentDao;
	
	
	/***********************普通用户使用下面方法*****************************************/
	/**
	 * 提交内容的时候进行预处理, 这里主要应对图片不能和内容提交不能同步的问题
	 * 预先生成内容的实体和BMID, 图片提交后全部绑定这个BMID, 不提交的BMID全部会做记录, 违规图片也可以精确的定位
	 * 预先生成图片上传的TOKEN
	 * @return
	 */
	public ForumContent getAPreSubmitForumContent(User u,String outerkey)
	{
		ForumContent fc=new ForumContent();
		
		fc.setStartuser(u);
		fc.setOuterkey(outerkey);
		
		return fc;
	}
	
	/**
	 * 保存个主题, 开新帖
	 * @param u
	 * @param outerkey
	 * @param title
	 * @param content
	 * @return
	 */
	public ForumContent saveATopic(ForumContent fc, String title,String content)
	{
		if(fc==null||title==null||content==null)
		{
			return null;
		}
		
		fc.setTitle(title);
		fc.setContent(content);
		
		fc.setLastReply(Tools.getServerTime());
		
		fc=this.forumContentDao.save(fc);
		
		if(fc.getBM_DEL()<0)
		{
			return null;
		}
		
		/**
		 * 保存后删除临时文件
		 */
		this.forumTempContentDao.delByUserOuterKey(fc.getStartuser().getBM_ID());
		
		/**
		 * 更新主题的回复时间和回复数量
		 */
		this.forumContentDao.editReplyTimeAndCountByBMID(fc.getOuterkey());
		
		return fc;
	}
	
	/**
	 * 保存个回复
	 * @param fc
	 * @param title
	 * @param content
	 * @return
	 */
	public ForumContent saveAReply(ForumContent fc, String title,String content)
	{
		fc.setLayer(1);
		return this.saveATopic(fc ,  title, content);
	}
	
	/**
	 * 临时存储
	 * @param userOuterKey 用户的BMID
	 * @param title
	 * @param content
	 * @return
	 */
	public int autoSaveAReply(String userOuterKey, String title,String content)
	{
		TempContent tc=new TempContent();
		
		tc.setUserOuterKey(userOuterKey);
		tc.setTitle(title);
		tc.setContent(content);
		
		this.forumTempContentDao.saveOrUpdate(tc);
		
		return 0;
	}
	
	/**
	 * 根据用户ID查找一个TempContent
	 * 用于存储临时写成的东西
	 * @param userOuterKey 用户的BMID
	 * @return
	 */
	public TempContent getTempContentByUserOuterKey(String userOuterKey)
	{
		return this.forumTempContentDao.findByUserOuterKey(userOuterKey);
	}
	
	/**
	 * 保存一个快速回复
	 * @param u
	 * @param outerkey
	 * @param title
	 * @param content
	 * @return
	 */
	public ForumContent saveAQuickReply(User u,String outerkey, String title,String content)
	{
		if(u==null||outerkey==null||content==null)
		{
			return null;
		}
		
		ForumContent fc=new ForumContent();
		
		
		fc.setOuterkey(outerkey);
		fc.setTitle("");
		fc.setContent(content);
		fc.setStartuser(u);
		fc.setLayer(1);
		
		fc=this.forumContentDao.save(fc);
		
		if(fc.getBM_DEL()<0)
		{
			return null;
		}
		
		/**
		 * 更新主题的回复时间和回复数量
		 */
		this.forumContentDao.editReplyTimeAndCountByBMID(fc.getOuterkey());
		
		return fc;
	}
	
	/**
	 * 查找一个帖子列表
	 * 用在进入帖子列表
	 * @param outerkey
	 * @param nowPage
	 * @return
	 */
	public Page<ForumContent> getForumContentList(String outerkey, Integer nowPage)
	{
		if(outerkey==null)
		{
			Page<ForumContent> page=new Page<ForumContent>();
			page.setList(new ArrayList<ForumContent>());
			return page;
		}
		
		if(nowPage==null)
		{
			nowPage=1;
		}
		
		
		return this.forumContentDao.find(outerkey, nowPage, 10, false);
	}
	
	/**
	 * 查找一个帖子列表
	 * 和主题列表使用
	 * 正序
	 * @param outerkey
	 * @param nowPage
	 * @return
	 */
	public Page<ForumContent> getForumContentListAsc(String outerkey, Integer nowPage)
	{
		if(outerkey==null)
		{
			Page<ForumContent> page=new Page<ForumContent>();
			page.setList(new ArrayList<ForumContent>());
			return page;
		}
		
		if(nowPage==null)
		{
			nowPage=1;
		}
		
		
		return this.forumContentDao.findOrderByBMTIMEAsc(outerkey, nowPage, 10);
	}
	
	/**
	 * 使用BMID查找帖子
	 * @param bmid
	 * @return
	 */
	public ForumContent getForumContentByBMID(String bmid)
	{
		return this.forumContentDao.findByBMID(bmid);
	}
	
	/**
	 * 修改内容
	 * 超过十分钟则添加
	 * @param bmid
	 * @param content
	 * @return
	 */
	public int editForumContentByBMID(String userID, String bmid,String content)
	{
		//查找
		ForumContent fc=this.forumContentDao.findByBMID(bmid);
		
		if(fc==null)
		{
			return -1;
		}
		
		if(fc.getStartuser()==null)
		{
			return -2;
		}
		
		if(!userID.equals(fc.getStartuser().getBM_ID()))
		{
			return -3;
		}
		
		Long minusTime=Tools.getServerTime()-fc.getLastReply()-10*60*60*1000L;
		
		//超过十分钟以前的帖子了, 不能修改
		if(minusTime>0)
		{
			if(fc.getContentList()==null)
			{
				fc.setContentList(new ArrayList<Content>());
			}
			
			Content c=new Content();
			c.setChangeTime(Tools.getServerTime());
			c.setContent(fc.getContent());
			
			fc.getContentList().add(c);
		}
		
		fc.setBM_ID(bmid);
		fc.setContent(content);
		
		return this.forumContentDao.editContentByBMID(fc);
	}
	
	/**
	 * 真实删除
	 * 用于删除自己写的帖子
	 * 只能自己删自己的
	 * @param userBMID
	 * @param bmid
	 * @return
	 */
	public int delSelfContent(String userBMID,String bmid)
	{
		return this.forumContentDao.realDelByBMID(userBMID, bmid);
	}
	
	/************************论坛管理员使用下列方法*******************************/
	/**
	 * 回收
	 * 查找是否有这个板块的权限
	 * 有可操作
	 * @param bmid
	 * @return
	 */
	public ForumContent setForumContentToREC(String bmid, String userkey)
	{
		ForumContent fc=this.forumContentDao.findByBMID(bmid);
		
		if(fc==null)
		{
			return null;
		}
		
		ForumTitle ft=this.gobackForCheckingAuth(fc.getOuterkey(), userkey);
		
		if(ft!=null)
		{
			return this.forumContentDao.editDel(bmid, 1);
		}
		
		return null;
	}
	
	/**
	 * 从回收站里把帖子拉回来
	 * @param bmid
	 * @param userkey
	 * @return
	 */
	public ForumContent setForumContentToBack(String bmid, String userkey)
	{
		ForumContent fc=this.forumContentDao.findDelByBMID(bmid);
		
		if(fc==null)
		{
			return null;
		}
		
		ForumTitle ft=this.gobackForCheckingAuth(fc.getOuterkey(), userkey);
		
		if(ft!=null)
		{
			return this.forumContentDao.editDel(bmid, 0);
		}
		
		return null;
	}
	
	/**
	 * 逆向找寻权限
	 * setForumContentToREC
	 * setForumContentToBack 
	 * @param bmid
	 * @param userkey
	 * @return
	 */
	private ForumTitle gobackForCheckingAuth(String bmid, String userkey)
	{
		ForumTitle ft=ForumCache.getCache().forumTitle.get(bmid);
		
		if(userkey==null)
		{
			return null;
		}
		
		if(ft==null)
		{
			return null;
		}
		
		if(ft.getManager().keySet().contains(userkey))
		{
			return ft;
		}
		
		if(ft.getOuterkey()==null||ft.getOuterkey().isEmpty()||ft.getOuterkey().equals("0"))
		{
			return null;
		}
		
		return this.gobackForCheckingAuth(ft.getOuterkey(),userkey);
	}
	
	/**
	 * 通过userkey查找此人是哪个版区内的管理员, 或者是超级管理员
	 * 根据权限查找可供操作的条目
	 * @param bmid 某个论坛的BMID, 隔离不同论坛的结果
	 * @param userkey 用户BMID, 查询他有那些可管理的部分
	 * @param nowPage
	 * @return
	 */
	public Page<ForumContent> getRecycleForumContentList(String bmid, String userkey, Integer nowPage)
	{		
		ForumTitle ft=ForumCache.getCache().forumTitle.get(bmid);
		
		ArrayList<String> authList=this.checkRootManager(ft, userkey);
		
		if(nowPage==null)
		{
			nowPage=1;
		}
		
		return this.forumContentDao.findDelList(authList, nowPage, 10, true);
	}
	
	/**
	 * 依附方法checkRootManager
	 * 当父级目录有权限的时候, 使用本方法遍历全部的FT BMID
	 * @param ft
	 * @return
	 */
	private ArrayList<String> getRootManager(ForumTitle ft)
	{
		ArrayList<String> re=new ArrayList<String>();
		
		if(ft.getSubForumTitle()==null||ft.getSubForumTitle().isEmpty())
		{
			re.add(ft.getBM_ID());
			return re;
		}
		
		for(ForumTitle each:ft.getSubForumTitle())
		{
			re.addAll(this.getRootManager(each));
		}
		
		return re;
	}
	
	/**
	 * 主入口
	 * 检查自己是否有权限, 如果没有则检测下一级目录, 如果有权限则调用getRootManager
	 * getRootManager中如果已经是结束目录, 那么把BMID放入LIST返回
	 * @param ft
	 * @param userkey
	 * @return
	 */
	private ArrayList<String> checkRootManager(ForumTitle ft,String userkey)
	{
		ArrayList<String> re=new ArrayList<String>();
		
		if(ft.getManager().keySet().contains(userkey))
		{
			re.addAll(this.getRootManager(ft));
			return re;
		}
		
		if(ft.getSubForumTitle()!=null&&!ft.getSubForumTitle().isEmpty())
		{
			for(ForumTitle each:ft.getSubForumTitle())
			{
				re.addAll(this.checkRootManager(each, userkey));
			}
		}
		
		return re;
	}
	
	/**
	 * 使用BMID查找回收站的帖子
	 * @param bmid
	 * @return
	 */
	public ForumContent getForumDelContentByBMID(String bmid)
	{
		return this.forumContentDao.findByBMID(bmid,1);
	}
	
	
	/**
	 * 真实删除
	 * @param bmid 要删除的BMID
	 * @param userkey
	 * @return
	 */
	public ForumContent realDel(String bmid, String userkey)
	{
		ForumContent fc=this.forumContentDao.findAnyByBMID(bmid);
		
		if(fc==null)
		{
			return fc;
		}
		
		ForumTitle ft=this.gobackForCheckingAuth(fc.getOuterkey(), userkey);
		
		if(ft==null)
		{
			return fc;
		}
		
		this.forumContentDao.realDelByOuterKey(bmid);
		this.forumContentDao.realDelByBMID(bmid);
		
		return fc;
	}
	
	/***********************************后台方法*************************************************/
	///////////////合并/删除/////////////////
	/**
	 * 合并
	 * @param from
	 * @param to
	 * @param fcBMIDs
	 * @return
	 */
	public int mergeContentToTarget(String from,String to, String[] fcBMIDs)
	{
		if(from==null||from.isEmpty())
		{
			return -2;
		}
		
		if(to==null||to.isEmpty())
		{
			return -3;
		}
		
		if(fcBMIDs==null||fcBMIDs.length==0)
		{
			return -1;
		}
		else
		{
			ArrayList<String> BM_ID_LIST=new ArrayList<String>();
			
			for(String BMID:fcBMIDs)
			{
				BM_ID_LIST.add(BMID);
			}
			
			this.forumContentDao.editOuterKey(from, to, BM_ID_LIST);
		}
		
		return 0;
	}
	
	/**
	 * 一次性全部合并
	 * @param from
	 * @param to
	 * @return
	 */
	public int mergeAllContentToTarget(String from,String to)
	{
		if(from==null||from.isEmpty())
		{
			return -2;
		}
		
		if(to==null||to.isEmpty())
		{
			return -3;
		}
		
		this.forumContentDao.editOuterKey(from, to);
		return 0;
	}
	
	
	/******************************后台方法*************************************/
	/**
	 * 批量垃圾桶
	 * @param bmid
	 * @return
	 */
	public int setForumContentToRECWithoutAuth(String[] bmid)
	{
		if(bmid==null||bmid.length==0)
		{
			return -1;
		}
		
		ArrayList<String> BM_ID_LIST=new ArrayList<String>();
		
		for(String BMID:bmid)
		{
			BM_ID_LIST.add(BMID);
		}
		
		return this.forumContentDao.editDelList(BM_ID_LIST,1);		
	}
	
	/**
	 * 使用outerkey直接全部垃圾桶
	 * @param outerkey
	 * @return
	 */
	public int setForumContentToRECByOuterKeyWithoutAuth(String outerkey)
	{
		return this.forumContentDao.editDelByOuterKey(outerkey, 1);
	}
}
