package startup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import model.ForumContent;
import model.ForumTitle;
import model.Startup;

public class ForumCache {
	private static ForumCache forumCache;
	private ForumCache(){}
	public static ForumCache getCache()
	{
		if(ForumCache.forumCache==null)
		{
			ForumCache.forumCache=new ForumCache();
			
			ForumCache.forumCache.reBuildForumTitleCache();
		}
		
		return ForumCache.forumCache;
	}
	
	/**
	 * 启动的时候创建唯一的论坛, 今后不再允许创建
	 * 查询BM_ID为root的forumTitle, 不存在则新建, 存在则无动作
	 * 只用在reBuildForumTitleCache 重建缓存中
	 * @return
	 */
	private int initUniqueForumTitle()
	{
		ForumTitle ft=MongoDBConnector.datastore.createQuery(ForumTitle.class).field("BM_ID").equal("root").get();
		
		if(ft!=null)//已经有根节点
		{
			return 1;
		}
		
		//创建一个根节点
		ft=new ForumTitle();
		
		ft.setBM_ID("root");
		ft.setOuterkey("0");
		ft.setName("My Forum");
		
		MongoDBConnector.datastore.save(ft);
		
		return 0;
	}
	
	
	///////////////////////////////////////
	public Map<String,ForumTitle> forumTitle=new HashMap<String,ForumTitle>();
	
	/**
	 * 重建缓存
	 * 只在启动的时候使用
	 * SynchroService->relordForumTitleTriger中重建缓存
	 * @return
	 */
	public int reBuildForumTitleCache()
	{
		if(MongoDBConnector.datastore==null)
		{
			return -1;
		}
		
		//检测是否有根节点
		this.initUniqueForumTitle();
		
		//检测配置文件
		Startup s=MongoDBConnector.datastore.createQuery(Startup.class).order("-BM_TIME").get();//读取配置, 如果有就读入内存, 没有就重建
		
		if(s==null)
		{
			return 1;
		}
		
		if(ForumCache.forumCache==null||ForumCache.forumCache.forumTitle==null)
		{
			return -1;
		}
		
		ForumCache.forumCache.forumTitle.clear();
		
		//rebuild
		for(String key:s.getForumTitle())
		{
			ForumTitle ft=MongoDBConnector.datastore.createQuery(ForumTitle.class).field("BM_ID").equal(key).get();
			
			if(ft==null)
			{
				continue;
			}
			
			ft.setSubForumTitle(this.findByOuterKey(ft.getBM_ID()));
			
			ForumCache.forumCache.forumTitle.put(key, ft);
		}
		
		return 0;
	}
	
	/**
	 * 依附重建缓存
	 * reBuildForumTitleCache
	 * @param outerkey
	 * @return
	 */
	private List<ForumTitle> findByOuterKey(String outerkey)
	{
		List<ForumTitle> tmp=MongoDBConnector.datastore.createQuery(ForumTitle.class).field("outerkey").equal(outerkey).order("-order").asList();
		
		for(int i=0;i<tmp.size();i++)
		{
			ForumTitle ft=tmp.get(i);
			
			ft.setSubForumTitle(this.findByOuterKey(ft.getBM_ID()));
		}
		
		return tmp;
	}
	
	/**
	 * 保存forumCache到数据库
	 * 只用在removeRootKey, putRootKey和task内
	 * @return
	 */
	public int saveForumTitleCache()
	{
		if(ForumCache.forumCache==null||ForumCache.forumCache.forumTitle==null)
		{
			return -1;
		}
		
		Startup startup=MongoDBConnector.datastore.createQuery(Startup.class).order("-BM_TIME").get();
		
		if(startup==null)
		{
			startup=new Startup();
			
			startup.setForumTitle(new ArrayList<String>());
			
			for(String key:ForumCache.forumCache.forumTitle.keySet())
			{
				startup.getForumTitle().add(key);
			}
			
			MongoDBConnector.datastore.save(startup);
			
			return 0;
		}
		
		Query<Startup> updateQuery = MongoDBConnector.datastore.createQuery(Startup.class).field("BM_ID").equal(startup.getBM_ID());
		
		UpdateOperations<Startup> ops=MongoDBConnector.datastore.createUpdateOperations(Startup.class);
		
		startup.getForumTitle().clear();
		
		for(String key:ForumCache.forumCache.forumTitle.keySet())
		{
			if(ForumCache.forumCache.forumTitle.get(key)==null||!ForumCache.forumCache.forumTitle.get(key).getOuterkey().equals("0"))
			{
				continue;
			}
			
			startup.getForumTitle().add(key);
		}
		
		ops.set("forumTitle", startup.getForumTitle());
		
		MongoDBConnector.datastore.update(updateQuery, ops);
		
		return 0;
	}
	
	/**
	 * 从缓存里去掉一个键
	 * 只能对顶级类ROOT进行下线
	 * 传入子健, 顶级键下线
	 * 只有在论坛管理页面进行上下线后, 才会生效
	 * @param key
	 * @return 被删除的顶级KEY
	 */
	public String removeRootKey(String key)
	{
		String keytmp=this.goBackToRootForumTitle(key);
		
		ForumCache.getCache().forumTitle.remove(keytmp);
		
		ForumCache.getCache().saveForumTitleCache();
		
		return keytmp;
	}
	
	/**
	 * 加入一个键
	 * 只能对顶级类ROOT进行上线
	 * 只有在论坛管理页面进行上下线后, 才会生效
	 * @param key
	 * @param ft
	 * @return
	 */
	public int putRootKey(String key,ForumTitle ft)
	{
		if(ft==null||!ft.getOuterkey().equals("0"))
		{
			return -1;
		}
		
		ForumCache.getCache().forumTitle.put(key,ft);
		
		ForumCache.getCache().saveForumTitleCache();
		
		return 0;
	}
	
	
	/**
	 * 回溯到头
	 * @param BMID
	 * @return
	 */
	public String goBackToRootForumTitle(String BMID)
	{
		ForumTitle ft=ForumCache.getCache().forumTitle.get(BMID);
		
		if(ft.getOuterkey()==null||ft.getOuterkey().equals("0")||ft.getOuterkey().isEmpty())
		{
			return ft.getBM_ID();
		}
		
		return this.goBackToRootForumTitle(ft.getOuterkey());
	}
	
	/**
	 * 从根节点找起, 找到所有末端的叶子节点
	 * @param BMID
	 * @return
	 */
	public List<ForumTitle> getAllEndForumTitle(String BMID)
	{
		List<ForumTitle> ftlist=new ArrayList<ForumTitle>();
		
		List<ForumTitle> thelist=ForumCache.getCache().forumTitle.get(BMID).getSubForumTitle();
		
		if(thelist!=null)
		for(ForumTitle ft:thelist)
		{
			if(ft.getSubForumTitle()==null||ft.getSubForumTitle().isEmpty())
			{
				ftlist.add(ft);
			}
			else
			{
				ftlist.addAll(this.getAllEndForumTitle(ft.getBM_ID()));
			}
		}
		
		return ftlist;
	}
	
	/**
	 * 由一个BMID找到根节点, 再找到所有的终端叶子节点
	 * @param BMID
	 * @return
	 */
	public List<ForumTitle> getAllEndForumTitleByBMID(String BMID)
	{
		return this.getAllEndForumTitle(this.goBackToRootForumTitle(BMID));
	}
	
	
	//////////////////////////////////////////////
	public Map<String, ForumTitle> getForumTitle() {
		return forumTitle;
	}
	public void setForumTitle(Map<String, ForumTitle> forumTitle) {
		forumCache.forumTitle = forumTitle;
	}
	
	public final static int headlineForAll=2;
	
	public final static int headlineForThis=3;
	
	
	
	
	///////////////////////////////////////////////
	public Map<String,List<ForumContent>> headline=new HashMap<String,List<ForumContent>>();
}
