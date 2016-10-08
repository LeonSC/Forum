package dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Repository;

import model.Page;
import model.Synchro;
import model.TempSynCheckKey;
import startup.MongoDBConnector;
import tools.Tools;

@Repository
public class SynchroDao {

	/**
	 * 录入一个实体, 带有重复效验
	 * ipAddress不可重复
	 * @param syn
	 * @return
	 */
	public Synchro save(Synchro syn)
	{
		if(syn==null)
		{
			syn=new Synchro();
			syn.setBM_DEL(-1);
			return syn;
		}
		
		if(syn.getIpAddress()==null)
		{
			syn=new Synchro();
			syn.setBM_DEL(-2);
			return syn;
		}
		
		//检测重复IP
		long l=MongoDBConnector.datastore.createQuery(Synchro.class).field("ipAddress").equal(syn.getIpAddress()).countAll();
		
		if(l>0)
		{
			syn=new Synchro();
			syn.setBM_DEL(-3);
			return syn;
		}
		
		MongoDBConnector.datastore.save(syn);
		
		ObjectId id = syn.getId();
		
		return MongoDBConnector.datastore.get(Synchro.class,id);
	}
	
	/**
	 * 分页查询集群的信息
	 * @param nowPage
	 * @param numInPage
	 * @return
	 */
	public Page<Synchro> find(int nowPage, int numInPage)
	{
		Query<Synchro> query=MongoDBConnector.datastore.createQuery(Synchro.class);
		
		Page<Synchro> page=new Page<Synchro>();
		
		page.setTotal(query.countAll());
		
		page.setNowPage(nowPage);
		page.setTotalInPage(numInPage);
		
		page.getPage();
		
		page.setList(query.order("-BM_TIME").offset(page.getSkip()).limit(numInPage).asList());
		
		return page;
	}
	
	/**
	 * 查询全部的节点,仅用于更新
	 * @return
	 */
	public List<Synchro> find()
	{
		return MongoDBConnector.datastore.createQuery(Synchro.class).order("-BM_TIME").asList();
	}
	
	
	/**
	 * 查询一个同步KEY,如果没有就新增一个
	 * @return
	 */
	public String saveOrUpdateTempSynCheckKey()
	{		
		Query<TempSynCheckKey> updateQuery = MongoDBConnector.datastore.createQuery(TempSynCheckKey.class);
		
		UpdateOperations<TempSynCheckKey> ops=MongoDBConnector.datastore.createUpdateOperations(TempSynCheckKey.class);
		
		String key=Tools.getID("synkey");
		
		ops.set("key", key);
		ops.set("limit", Tools.getServerTime());
		
		MongoDBConnector.datastore.update(updateQuery, ops,true);
		
		return key;
	}
	
	/**
	 * 查找唯一的一个KEY
	 * @return
	 */
	public TempSynCheckKey findTempSynCheckKey()
	{
		return MongoDBConnector.datastore.createQuery(TempSynCheckKey.class).get();
	}
}
