package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.ForumContentDao;
import dao.UserDao;
import model.ForumContent;
import model.Page;
import model.User;

/**
 * 用于个人信息相关
 * @author sasgsc
 */
@Service
public class PersonalService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private ForumContentDao forumContentDao;
	
	/**
	 * 编辑用户信息
	 * @param BMID
	 * @param headerIcon
	 * @param nickname
	 * @param gender
	 * @return
	 */
	public User editUserByBMID(String BMID,String headerIcon,String nickname,Integer gender)
	{
		User u=new User();
		
		u.setBM_ID(BMID);
		u.setHeaderIcon(headerIcon);
		u.setNickname(nickname);
		u.setGender(gender);
		
		u=this.userDao.editUser(u);
		
		//用户信息修改后, 需要对相应的内置文档进行修改
		this.forumContentDao.editStartUser(BMID);
		
		return u;
	}
	
	/**
	 * 查找用户发布的主题
	 * @param bmid
	 * @return
	 */
	public Page<ForumContent> findUserTopic(String bmid,Integer nowPage)
	{
		if(nowPage==null)
		{
			nowPage=1;
		}
		
		return this.forumContentDao.findByUser(bmid, 0, nowPage, 20, false);
	}
	
	/**
	 * 查找用户发布的回答
	 * @param bmid
	 * @return
	 */
	public Page<ForumContent> findUserAnswer(String bmid,Integer nowPage)
	{
		if(nowPage==null)
		{
			nowPage=1;
		}
		
		return this.forumContentDao.findByUser(bmid, 1, nowPage, 20, false);
	}
}
