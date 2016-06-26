package dao;

import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Repository;

import model.Admin;
import model.Page;
import model.User;
import startup.MongoDBConnector;
import tools.Tools;

@Repository
public class UserDao {

	/**
	 * 保存一个用户
	 * @param user
	 * @return
	 */
	public User save(User user)
	{
		if(user==null)
		{
			user=new User();
			user.setBM_DEL(-1);
			return user;
		}
		
		if(user.getUsername()==null||user.getUsername().isEmpty())
		{
			user=new User();
			user.setBM_DEL(-2);
			return user;
		}
		
		//////////检测username是不是重复/////////////
		long l=MongoDBConnector.datastore.createQuery(User.class).field("username").equal(user.getUsername()).countAll();
		if(l!=0L)
		{
			user=new User();
			user.setBM_DEL(-4);
			return user;
		}
		
		
		if(user.getPw()==null||user.getPw().isEmpty())
		{
			user=new User();
			user.setBM_DEL(-3);
			return user;
		}
		
		if(user.getNickname()==null)
		{
			user.setNickname("");
		}
		
		user.setPw(Tools.digestSha1(user.getPw()));
		
		MongoDBConnector.datastore.save(user);
		
		ObjectId id = user.getId();
		
		return MongoDBConnector.datastore.get(User.class,id);
	}
	
	
	/***********************************update****************************************************/
	public User setUserToAdmin(User user)
	{
		if(user==null)
		{
			user=new User();
			user.setBM_DEL(-1);
			return user;
		}
		
		if(user.getBM_ID()==null)
		{
			user=new User();
			user.setBM_DEL(-2);
			return user;
		}
		
		User u=MongoDBConnector.datastore.createQuery(User.class).field("BM_ID").equal(user.getBM_ID()).get();
		
		if(u==null)
		{
			user=new User();
			user.setBM_DEL(-3);
			return user;
		}
		if(user.getAdmin()!=null)
		{
			return user;
		}
		
		Admin admin=new Admin();
		admin.setPassword(u.getPw());
		u.setAdmin(admin);
		
		MongoDBConnector.datastore.updateFirst(MongoDBConnector.datastore.createQuery(User.class).field("BM_ID").equal(u.getBM_ID()), u,false);
		
		return u;
	}
	
	
	public User editUser(User user)
	{
		if(user==null)
		{
			user=new User();
			user.setBM_DEL(-1);
			return user;
		}
		
		if(user.getBM_ID()==null)
		{
			user=new User();
			user.setBM_DEL(-2);
			return user;
		}
		
		User u=MongoDBConnector.datastore.createQuery(User.class).field("BM_ID").equal(user.getBM_ID()).get();
		
		if(u==null)
		{
			user=new User();
			user.setBM_DEL(-3);
			return user;
		}
		if(user.getHeaderIcon()!=null&&!user.getHeaderIcon().isEmpty())
		{
			u.setHeaderIcon(user.getHeaderIcon());
		}
		if(user.getGender()!=null)
		{
			u.setGender(user.getGender());
		}
		if(user.getNickname()!=null&&!user.getNickname().isEmpty())
		{
			u.setNickname(user.getNickname());
		}
		
		MongoDBConnector.datastore.updateFirst(MongoDBConnector.datastore.createQuery(User.class).field("BM_ID").equal(u.getBM_ID()), u,false);
		
		return MongoDBConnector.datastore.createQuery(User.class).field("BM_ID").equal(u.getBM_ID()).get();
	}
	
	/***********************************find****************************************************/
	//验证用户
	public User findByUserName(String email)
	{
		if(email==null)
		{
			User u=new User();
			u.setBM_DEL(-1);
			return u;
		}
		
		return MongoDBConnector.datastore.createQuery(User.class).field("username").equal(email).get();
	}
	
	/**
	 * 根据账户去查找
	 * @param name
	 * @param nowPage
	 * @param numInPage
	 * @return
	 */
	public Page<User> findUsersByAccountWithPage(String name, int nowPage, int numInPage)
	{
		Query<User> query=MongoDBConnector.datastore.createQuery(User.class);
		
		Page<User> page=new Page<User>();
		
		page.setTotal(query.countAll());
		
		
		page.setNowPage(nowPage);
		page.setTotalInPage(numInPage);
		
		page.getPage();
		
		page.setList(query.field("username").containsIgnoreCase(name).offset(page.getSkip()).limit(numInPage).asList());
		
		return page;
	}
	
	/**
	 * 
	 * @param bmid
	 * @return
	 */
	public User findUserByBMID(String bmid)
	{
		return MongoDBConnector.datastore.createQuery(User.class).field("BM_ID").equal(bmid).get();
	}
}
