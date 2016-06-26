package dao;

import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Repository;

import model.ForumContent;
import model.ForumTitle;
import startup.ForumCache;
import startup.MongoDBConnector;

/**
 * find content for headline
 * @author sasgsc
 */
@Repository
public class ForumHeadlineDao {

	public List<ForumContent> find(String outerkey)
	{
		List<ForumContent> cached= ForumCache.getCache().headline.get(outerkey);
		
		if(cached!=null)
		{
			return cached;
		}
		
		List<ForumTitle> lifeList=ForumCache.getCache().getAllEndForumTitleByBMID(outerkey);
		
		ArrayList<String> l=new ArrayList<String>();
		
		for(ForumTitle ft:lifeList)
		{
			l.add(ft.getBM_ID());
		}
		
		List<ForumContent> headlineForAllList=MongoDBConnector.datastore.createQuery(ForumContent.class).field("position").equal(2).field("outerkey").hasAnyOf(l).order("-BM_TIME").limit(ForumCache.headlineForAll).asList();
		List<ForumContent> headlineForThisList=MongoDBConnector.datastore.createQuery(ForumContent.class).field("position").equal(1).field("outerkey").equal(outerkey).order("-BM_TIME").limit(ForumCache.headlineForThis).asList();
		
		headlineForAllList.addAll(headlineForThisList);
		
		ForumCache.getCache().headline.put(outerkey, headlineForAllList);
		
		return headlineForAllList;
	}
	
	
	public ForumContent setToHeadLine(String BMID, Integer position)
	{
		Query<ForumContent> updateQuery = MongoDBConnector.datastore.createQuery(ForumContent.class).field("BM_ID").equal(BMID);
		
		UpdateOperations<ForumContent> ops=MongoDBConnector.datastore.createUpdateOperations(ForumContent.class);
		
		ops.set("position", position);
		
		MongoDBConnector.datastore.update(updateQuery, ops);
		
		ForumCache.getCache().headline.clear();
		
		return MongoDBConnector.datastore.createQuery(ForumContent.class).field("BM_ID").equal(BMID).get();
	}
}
