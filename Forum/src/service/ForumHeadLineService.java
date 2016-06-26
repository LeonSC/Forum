package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.ForumContentDao;
import dao.ForumHeadlineDao;
import model.ForumContent;
import model.ForumTitle;
import model.User;
import startup.ForumCache;



@Service
public class ForumHeadLineService {

	@Autowired
	private ForumHeadlineDao forumHeadlineDao;
	@Autowired
	private ForumContentDao forumContentDao;

	
	//查找置顶消息
	public List<ForumContent> getHeadLine(String bmid)
	{
		return this.forumHeadlineDao.find(bmid);
	}
	/**
	 * 把BMID的帖子置顶到这个BLOCK中
	 * 权限要求
	 * forumLv > 0
	 * ft中需要包含这个管理员
	 * @param bmid
	 * @return
	 */
	public ForumContent setToBlockHeadLine(User user, String bmid)
	{
		if(user.getAdmin()==null||user.getAdmin().getForumLv()<0)//验证是否是管理员
		{
			return null;
		}
		
		ForumContent fc=this.forumContentDao.findByBMID(bmid);
		
		ForumTitle ft=ForumCache.getCache().forumTitle.get(fc.getOuterkey());
		
		if(ft==null)
		{
			return null;
		}
		
		if(!ft.getManager().containsKey(user.getBM_ID()))
		{
			return null;
		}
		
		fc=this.forumHeadlineDao.setToHeadLine(bmid, 1);
		return fc;
	}
	
	/**
	 * 把BMID的帖子全局置顶
	 * 权限要求
	 * forumLv > 99
	 * @param bmid
	 * @return
	 */
	public ForumContent setToForumHeadLine(User user, String bmid)
	{
		if(user.getAdmin()==null||user.getAdmin().getForumLv()<99)//验证是否是管理员
		{
			return null;
		}
		
		ForumContent fc=this.forumHeadlineDao.setToHeadLine(bmid, 2);
		return fc;
	}
	
	/**
	 * 回置为普通帖子
	 * 权限要求
	 * forumLv > 0
	 * ft中需要包含这个管理员
	 * @param bmid
	 * @return
	 */
	public ForumContent setToNormal(User user, String bmid)
	{
		if(user.getAdmin()==null||user.getAdmin().getForumLv()<0)//验证是否是管理员
		{
			return null;
		}
		
		ForumContent fc=this.forumContentDao.findByBMID(bmid);
		
		ForumTitle ft=ForumCache.getCache().forumTitle.get(fc.getOuterkey());
		
		if(ft==null)
		{
			return null;
		}
		
		if(!ft.getManager().containsKey(user.getBM_ID()))
		{
			return null;
		}
		
		fc=this.forumHeadlineDao.setToHeadLine(bmid, 0);
		return fc;
	}
}
