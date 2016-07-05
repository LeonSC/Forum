package service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.ForumContentDao;
import dao.ForumTitleDao;
import dao.UserDao;
import model.ForumTitle;
import model.Page;
import model.User;
import startup.ForumCache;

@Service
public class ForumTitleService {

	@Autowired
	private ForumTitleDao forumTitleDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ForumContentDao forumContentDao;
	
	/**
	 * add a ForumTitle
	 * @param outerkey link to father key
	 * @param name 
	 * @param order
	 * @return
	 */
	public int saveForumTitle(String outerkey,String name, Integer order)
	{
		if(name==null)
		{
			return -2;
		}
		
		ForumTitle ft=new ForumTitle();
		
		if(outerkey!=null)
		{
			if(!outerkey.isEmpty())
			{
				ft.setOuterkey(outerkey);
			}
		}
		
		if(order!=null)
		{
			ft.setOrder(order);
		}
		
		ft.setName(name);
		
		return this.forumTitleDao.save(ft).getBM_DEL();
	}
	
	/**
	 * 编辑个ForumTitle
	 * @param BM_ID
	 * @param name
	 * @param order
	 * @return
	 */
	public int editForumTitle(String BM_ID,String name, Integer order)
	{
		if(BM_ID==null)
		{
			return -2;
		}
		
		ForumTitle ft=new ForumTitle();
		
		ft.setBM_ID(BM_ID);
		ft.setName(name);
		ft.setOrder(order);
		
		return this.forumTitleDao.edit(ft).getBM_DEL();
	}
	
	/**
	 * 编辑个ForumTitle
	 * @param BM_ID
	 * @param name
	 * @param order
	 * @return
	 */
	public int editForumTitleIcon(String BM_ID,String src)
	{
		if(BM_ID==null)
		{
			return -2;
		}
		
		ForumTitle ft=new ForumTitle();
		
		ft.setBM_ID(BM_ID);
		ft.setIcon(src);
		
		return this.forumTitleDao.editIcon(ft).getBM_DEL();
	}
	
	/**
	 * 查找十个ForumTitle
	 * 查找顶级ROOT的论坛FT
	 * @param nowPage
	 * @return
	 */
	public Page<ForumTitle> findTenForumTitle(Integer nowPage)
	{
		if(nowPage==null)
		{
			nowPage=1;
		}
		
		return this.forumTitleDao.find(nowPage, 10);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public ForumTitle findByID(String id)
	{
		return this.forumTitleDao.findByID(id);
	}
	
	/**
	 * 
	 * @param BMID
	 * @return
	 */
	public ForumTitle findByBMID(String BMID)
	{
		return this.forumTitleDao.findByBMID(BMID);
	}
	
	/**
	 * start to get forum
	 * 这个方法用于在获取论坛的时候, 和缓存内的数据对比, 对照哪个是上线的或者没上线的
	 * @param BMID
	 * @return
	 */
	public ForumTitle findFullForumTitleByBMID(String BMID)
	{
		ForumTitle reft= this.forumTitleDao.findByBMID(BMID);
		
		if(reft!=null)
		{
			reft.setSubForumTitle(this.forumTitleDao.findByOuterKey(reft.getBM_ID()));
		}
		
		return reft;
	}
	
	
	/**
	 * 为分论坛添加一个管理员
	 * @param key
	 * @param ukey
	 * @return
	 */
	public ForumTitle addManagerToBlock(String key,String ukey)
	{
		ForumTitle ft=this.findByBMID(key);
		
		Map<String, User> manager=new HashMap<String, User>();
		
		manager.putAll(ft.getManager());
		
		User u=this.userDao.findUserByBMID(ukey);
		
		if(u==null)
		{
			return ft;
		}
		
		manager.put(ukey, u);
		
		ft=new ForumTitle();
		
		ft.setBM_ID(key);
		ft.setManager(manager);
		
		return this.forumTitleDao.edit(ft);
	}
	
	/**
	 * 删除分论坛的管理员
	 * @param key
	 * @param ukey
	 * @return
	 */
	public ForumTitle delManagerInBlock(String key,String ukey)
	{
		ForumTitle ft=this.findByBMID(key);
		
		ft.getManager().remove(ukey);
		
		ForumTitle tmp=new ForumTitle();
		
		tmp.setBM_ID(key);
		tmp.setManager(ft.getManager());
		
		return this.forumTitleDao.edit(tmp);
	}
	
	
	/**
	 * 后台管理使用
	 * 真实删除一个节点
	 * 首先, 它不能再包含任何帖子, 必须是个已经被移动过的空板块
	 * 其次, 它不能包含其他板块, 必须是个空母块
	 * 再次, 必须根处于下线状态, 强制下线, 拦截器中会拦截处于离线状态论坛的提交
	 * 删除
	 * 重建, 对本论坛进行缓存重建
	 * @param key 某个节点的BMID
	 * @return rootKey
	 */
	public int realDelABlock(String key)
	{
		if(key==null)
		{
			return -3;
		}
		
		//必须是个已经被移动过的空板块
		if(this.forumContentDao.checkExistByOuterKey(key))
		{
			return -1;
		}
		
		//必须是个空母块
		if(this.forumTitleDao.checkExistByOuterKey(key))
		{
			return -2;
		}
		
		//强制下线
		String topkey=ForumCache.getCache().removeRootKey(key);
		
		//删除
		this.forumTitleDao.realDelBMID(key);
		
		//重新上线
		ForumTitle topFT=this.findFullForumTitleByBMID(topkey);
		
		//排除顶级KEY被删除的情况
		if(topFT!=null)
		{
			ForumCache.getCache().putRootKey(topkey, topFT);
		}
		
		return 0;
	}
}
