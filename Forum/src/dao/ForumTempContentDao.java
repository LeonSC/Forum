package dao;

import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Repository;

import model.TempContent;
import startup.MongoDBConnector;
import tools.Tools;

@Repository
public class ForumTempContentDao {

	/**
	 * 保存临时文件
	 * @param tc
	 * @return
	 */
	public int saveOrUpdate(TempContent tc)
	{
		if(tc==null)
		{
			return -1;
		}
		
		if(tc.getUserOuterKey()==null)
		{
			return -2;
		}
		
		tc.setLastUpdate(Tools.getServerTime());
		
		Query<TempContent> updateQuery = MongoDBConnector.datastore.createQuery(TempContent.class).field("userOuterKey").equal(tc.getUserOuterKey());
		
		UpdateOperations<TempContent> ops=MongoDBConnector.datastore.createUpdateOperations(TempContent.class);
		
		ops.set("title", tc.getTitle());
		ops.set("content", tc.getContent());
		
		MongoDBConnector.datastore.update(updateQuery, ops,true);
		
		return 0;
	}
	
	/**
	 * 根据用户ID查找一个TempContent
	 * 用于存储临时写成的东西
	 * @param userouterkey 用户的BMID
	 * @return
	 */
	public TempContent findByUserOuterKey(String userouterkey)
	{
		if(userouterkey==null)
		{
			return null;
		}
		
		return MongoDBConnector.datastore.createQuery(TempContent.class).field("userOuterKey").equal(userouterkey).get();
	}
	
	/**
	 * 删除临时文件
	 * 用在这个主题/留言被提交
	 * @param userouterkey
	 * @return
	 */
	public int delByUserOuterKey(String userouterkey)
	{
		if(userouterkey==null)
		{
			return -1;
		}
		
		Query<TempContent> query=MongoDBConnector.datastore.createQuery(TempContent.class).field("userOuterKey").equal(userouterkey);
		
		MongoDBConnector.datastore.delete(query);
		return 0;
	}
}
