package dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Repository;

import model.ForumTitle;
import model.Page;
import startup.MongoDBConnector;

@Repository
public class ForumTitleDao {

	public ForumTitle save(ForumTitle ft)
	{
		if(ft==null)
		{
			ft=new ForumTitle();
			ft.setBM_DEL(-1);
			return ft;
		}
		
		if(ft.getName()==null||ft.getName().isEmpty())
		{
			ft=new ForumTitle();
			ft.setBM_DEL(-2);
			return ft;
		}
		
		ft.setBM_DEL(99);
		ft.setOrder(1);
		
		MongoDBConnector.datastore.save(ft);
		ObjectId id = ft.getId();
		return MongoDBConnector.datastore.get(ForumTitle.class,id);
	}
	
	/**
	 * 编辑
	 * @param ft
	 * @return
	 */
	public ForumTitle edit(ForumTitle ft)
	{
		if(ft==null)
		{
			ft=new ForumTitle();
			ft.setBM_DEL(-1);
			return ft;
		}
		
		if(ft.getBM_ID()==null||ft.getBM_ID().isEmpty())
		{
			ft=new ForumTitle();
			ft.setBM_DEL(-2);
			return ft;
		}
		
		Query<ForumTitle> updateQuery = MongoDBConnector.datastore.createQuery(ForumTitle.class).field("BM_ID").equal(ft.getBM_ID());
		
		UpdateOperations<ForumTitle> ops=MongoDBConnector.datastore.createUpdateOperations(ForumTitle.class);
		
		if(ft.getName()!=null&&!ft.getName().isEmpty())
		{
			ops.set("name", ft.getName());
		}
		
		if(ft.getOrder()!=0)
		{
			ops.set("order", ft.getOrder());
		}
		
		if(ft.getManager()!=null&&!ft.getManager().isEmpty())
		{
			ops.set("manager", ft.getManager());
		}
		
		MongoDBConnector.datastore.update(updateQuery, ops);
		
		return MongoDBConnector.datastore.createQuery(ForumTitle.class).field("BM_ID").equal(ft.getBM_ID()).get();
	}
	
	/**
	 * 查找顶级ROOT的论坛FT
	 * @param nowPage
	 * @param numInPage
	 * @return
	 */
	public Page<ForumTitle> find(int nowPage, int numInPage)
	{
		Query<ForumTitle> query=MongoDBConnector.datastore.createQuery(ForumTitle.class);
		
		Page<ForumTitle> page=new Page<ForumTitle>();
		
		page.setTotal(query.field("outerkey").equal("0").countAll());
		
		page.setNowPage(nowPage);
		page.setTotalInPage(numInPage);
		
		page.getPage();
		
		page.setList(query.field("outerkey").equal("0").order("-order").offset(page.getSkip()).limit(numInPage).asList());
		
		return page;
	}
	
	
	/**
	 * 使用ID查找
	 * @param id
	 * @return
	 */
	public ForumTitle findByID(String id)
	{
		if(id==null)
		{
			ForumTitle ft=new ForumTitle();
			ft.setBM_DEL(-1);
			return ft;
		}
		
		ObjectId keyid=new ObjectId(id);
		
		return MongoDBConnector.datastore.get(ForumTitle.class,keyid);
	}
	
	
	/**
	 * 使用BMID查找
	 * @param BMID
	 * @return
	 */
	public ForumTitle findByBMID(String BMID)
	{
		if(BMID==null)
		{
			ForumTitle ft=new ForumTitle();
			ft.setBM_DEL(-1);
			return ft;
		}
		
		return MongoDBConnector.datastore.createQuery(ForumTitle.class).field("BM_ID").equal(BMID).get();
	}
	
	/**
	 * build forum
	 * 这个方法用于在获取论坛的时候, 和缓存内的数据对比, 对照哪个是上线的或者没上线的
	 * 循环重建
	 * @param outerkey link to BMID
	 * @return
	 */
	public List<ForumTitle> findByOuterKey(String outerkey)
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
	 * 传入outerkey, 探测是不是有子节点
	 * 如果没有 返回false
	 * 有 返回true
	 * @param bmid
	 * @return
	 */
	public boolean checkExistByOuterKey(String bmid)
	{
		long check=MongoDBConnector.datastore.createQuery(ForumTitle.class).field("outerkey").equal(bmid).limit(1).countAll();
		if(check==0)
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * 使用BMID真实删除一个节点
	 * @param BMID
	 * @return
	 */
	public int realDelBMID(String BMID)
	{
		Query<ForumTitle> query=MongoDBConnector.datastore.createQuery(ForumTitle.class).field("BM_ID").equal(BMID);
		
		MongoDBConnector.datastore.delete(query);
		
		return 0;
	}
}
