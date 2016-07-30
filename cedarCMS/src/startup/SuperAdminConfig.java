package startup;

import java.util.HashMap;
import java.util.Map;

import model.Admin;
import model.User;

public class SuperAdminConfig {

	public final static Map<String, User> map= new HashMap<String, User>();

	/**
	 * 定义几个超级用户
	 * @return
	 */
	public static Map<String, User> getMap() {
		
		if(!SuperAdminConfig.map.isEmpty())
		{
			return SuperAdminConfig.map;
		}
		
		User user=new User();
		
		Admin ad=new Admin();
		
		ad.setPassword("10E6C182032B3DBADBD2A49C438D9D35A8B567B4");
		ad.setLv(999);
		ad.setForumLv(999);
		user.setAdmin(ad);
		user.setNickname("admin");
		
		
		SuperAdminConfig.map.put("sasgsc@gmail.com", user);
		
		return SuperAdminConfig.map;
	}
}
