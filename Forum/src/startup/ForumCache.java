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
	
	/**
	 * 启动的时候,如果库里有数据,则装载已有数据,如果没有数据则读取初始化数据并把初始化数据放入库中
	 * 论坛结构调整和是否在线无关
	 */
	///////////////////////////////////////
	public Map<String,ForumTitle> forumTitle=new HashMap<String,ForumTitle>();
	
	/**
	 * 重建缓存
	 * 只在启动的时候使用
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
		Startup s=MongoDBConnector.datastore.createQuery(Startup.class).order("-BM_TIME").get();
		
		//读取配置, 如果有就读入内存
		if(s!=null)
		{
			this.forumTitle=s.getForumTitle();
			return 1;
		}
		
		//没有重建,从root根读起,root的父ID为0
		if(ForumCache.forumCache==null||ForumCache.forumCache.forumTitle==null)
		{
			return -1;
		}
		
		this.forumTitle.clear();
		
		//rebuild
		ForumTitle ft=MongoDBConnector.datastore.createQuery(ForumTitle.class).field("BM_ID").equal("root").get();
		
		if(ft==null)
		{
			return -2;
		}
			
		ft.setSubForumTitle(this.findByOuterKey(ft.getBM_ID()));
		
		this.forumTitle.put(ft.getBM_ID(), ft);
		
		//把组建好的数据放入到Startup中,保存到数据库里去
		s=new Startup();
		
		s.setForumTitle(this.forumTitle);
		
		MongoDBConnector.datastore.save(s);
		//保存完成
		
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
		
		//如果没有查询到任何数据则返回一个空LIST
		if(tmp==null)
		{
			return new ArrayList<ForumTitle>();
		}
		
		for(ForumTitle ft:tmp)
		{
			//放入缓存中以供调用
			this.forumTitle.put(ft.getBM_ID(), ft);
			
			ft.setSubForumTitle(this.findByOuterKey(ft.getBM_ID()));
		}
		
		return tmp;
	}
	
	/**
	 * 保存forumCache到数据库
	 * 在启动的时候已经加入缓存,所以如果为空则说明有错误
	 * 用在论坛从下线状态切换到上线状态
	 * @return
	 */
	public int renewForumTitleCache()
	{
		if(ForumCache.forumCache==null||ForumCache.forumCache.forumTitle==null)
		{
			return -1;
		}
		
		Startup startup=MongoDBConnector.datastore.createQuery(Startup.class).order("-BM_TIME").get();
		
		if(startup==null)
		{
			return -2;
		}
		
		//缓存重建
		this.forumTitle.clear();
		
		ForumTitle ft=MongoDBConnector.datastore.createQuery(ForumTitle.class).field("BM_ID").equal("root").get();
		
		if(ft==null)
		{
			return -2;
		}
			
		ft.setSubForumTitle(this.findByOuterKey(ft.getBM_ID()));
		
		this.forumTitle.put(ft.getBM_ID(), ft);
		//缓存重建结束
		
		
		//更新到数据库里
		Query<Startup> updateQuery = MongoDBConnector.datastore.createQuery(Startup.class).field("BM_ID").equal(startup.getBM_ID());
		
		UpdateOperations<Startup> ops=MongoDBConnector.datastore.createUpdateOperations(Startup.class);
		
		//把缓存中的结构存入到数据库中
		ops.set("forumTitle", this.forumTitle);
		
		MongoDBConnector.datastore.update(updateQuery, ops);
		
		return 0;
	}
	
	/**
	 * 用于集群调用的时候,不需要把结构重新写入到数据库中
	 * 只是从数据库内把结构读出来
	 * @return
	 */
	public int readForumTitleFromCache()
	{
		ForumTitle ft=MongoDBConnector.datastore.createQuery(ForumTitle.class).field("BM_ID").equal("root").get();
		
		if(ft==null)
		{
			return -2;
		}
		
		ft.setSubForumTitle(this.findByOuterKey(ft.getBM_ID()));
		
		this.forumTitle.put(ft.getBM_ID(), ft);
		
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
	 * 从这个节点找起, 找到所有末端的叶子节点
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
