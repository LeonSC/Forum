package service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import dao.ForumContentDao;
import dao.UserDao;
import model.Page;
import model.User;
import tools.Tools;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private ForumContentDao forumContentDao;
	
	/**
	 * 同时还要查找这个用户的管理员属性
	 * @param email
	 * @param pw
	 * @return
	 */
	public User checkUser(String email,String pw)
	{
		User u=this.userDao.findByUserName(email);
		if(u==null)
		{
			return null;
		}
		
		if(u.getPw()==null)
		{
			return null;
		}
		
		if(u.getPw().equals(pw))
		{			
			return u;
		}
		return null;
	}
	
	
	/**
	 * 首先查找这个用户是否存在, 如果存在, 查找这个用户输入的密码是否是正确的密码, 
	 * 如果是, 返回直接登录, 返回这个用户, 如果不是, 返回用户名已被占用
	 * 不存在, 正常注册.
	 * @param email 需要验证结尾
	 * @param pw
	 * @return
	 */
	public User registerUser(String email,String pw)
	{
		//验证邮件的正确性
		if(email==null||email.isEmpty())
		{
			return new User();
		}
		
		if(email.contains(" "))
		{
			return new User();
		}
		
		ArrayList<String> suffixsList=new ArrayList<String>();
		suffixsList.add("com");
		suffixsList.add("cn");
		ArrayList<String> addressList=new ArrayList<String>();
		addressList.add("gmail");
		addressList.add("hotmail");
		addressList.add("qq");
		addressList.add("163");
		addressList.add("yahoo");
		addressList.add("sina");
		addressList.add("126");
		addressList.add("outlook");
		
		String[] emailAt=email.split("@");
		
		if(emailAt.length!=2)
		{
			return new User();
		}
		
		String[] emailAtDot=emailAt[1].split("\\.");
		
		if(emailAtDot.length<2)
		{
			return new User();
		}
		if(!suffixsList.contains(emailAtDot[emailAtDot.length-1]))
		{
			return new User();
		}
		if(!addressList.contains(emailAtDot[emailAtDot.length-2]))
		{
			return new User();
		}
		//---验证邮件的正确性---
		
		User u=this.userDao.findByUserName(email);
		
		if(u==null)
		{
			User us=new User();
			
			us.setUsername(email);
			us.setNickname(email);
			us.setPw(pw);
			us.setLv(1);
			
			us=this.userDao.save(us);
			
			return us;
		}
		
		pw=Tools.digestSha1(pw);
		
		if(pw.equals(u.getPw()))
		{
			return u;
		}
		
		u.setBM_DEL(-1);//user account has been taken
		return u;
	}
	
	//登录API
	public User registerUser(String json)
	{
		JSONObject jo = JSONObject.parseObject(json);
		
		return this.registerUser(jo.getString("email"), jo.getString("pw"));
	}
	
	
	public Page<User> findTenUsersByAccountWithPage(String name,Integer nowPage)
	{
		if(name==null)
		{
			return null; 
		}
		
		if(nowPage==null)
		{
			nowPage=1;
		}
		
		if(nowPage<0)
		{
			nowPage=1;
		}
		
		return this.userDao.findUsersByAccountWithPage(name, nowPage, 10);
	}
	
	public User findUserByBMID(String bmid)
	{
		if(bmid==null)
		{
			return null;
		}
		return this.userDao.findUserByBMID(bmid);
	}
	
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
}
