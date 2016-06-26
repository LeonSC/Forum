package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.UserDao;
import model.User;
import startup.SuperAdminConfig;


@Service
public class AdminService {

	@Autowired
	private UserDao userDao;
	
	/**
	 * 加载ADMIN时候需要同时加载一个USER, 因为ADMIN是附着在USER上的一个表现
	 * ADMIN属性将会尽量的少
	 * ADMIN已经成为USER的一个内部类
	 * 首先判断是不是超级用户
	 * 登录全部在前段加密密码, 不再传递明文密码
	 * @param email
	 * @param pw
	 * @return
	 */
	public User checkAdmin(String email,String pw)
	{
		if(SuperAdminConfig.map.containsKey(email))
		{
			User user=SuperAdminConfig.map.get(email);
			
			if(user.getAdmin()!=null&&user.getAdmin().getPassword()!=null&&user.getAdmin().getPassword().equals(pw))
			{
				return user;
			}
			
			return null;
		}
		
		User admin=this.userDao.findByUserName(email);
		
		if(admin==null)
		{
			return null;
		}
		
		if(admin.getAdmin()==null||admin.getAdmin().getPassword()==null)
		{
			return null;
		}
		
		if(admin.getAdmin().getPassword().equals(pw))
		{			
			return admin;
		}
		return null;
	}
	
	/**
	 * 把一个user添加admin权限
	 * @param BMID
	 * @return
	 */
	public User setUserToAdmin(String BMID)
	{
		User u=this.userDao.findUserByBMID(BMID);
		
		if(u==null)
		{
			return null;
		}
		
		if(u.getAdmin()!=null)
		{
			return u;
		}
		
		return this.userDao.setUserToAdmin(u);
	}
}
