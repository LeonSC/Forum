package dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Repository;

import model.Content;
import model.ForumContent;
import model.Page;
import model.User;
import startup.MongoDBConnector;
import tools.Tools;

@Repository
public class ForumContentDao {

	/**
	 * 保存
	 * @param fc
	 * @return
	 */
	public ForumContent save(ForumContent fc)
	{
		if(fc==null)
		{
			fc=new ForumContent();
			fc.setBM_DEL(-1);
			return fc;
		}
		
		if(fc.getOuterkey()==null||fc.getOuterkey().isEmpty())
		{
			fc=new ForumContent();
			fc.setBM_DEL(-2);
			return fc;
		}
		
		if(fc.getContent()==null||fc.getContent().isEmpty())
		{
			fc=new ForumContent();
			fc.setBM_DEL(-3);
			return fc;
		}
		
		if(fc.getStartuser()==null||fc.getStartuser().getBM_ID()==null)
		{
			fc=new ForumContent();
			fc.setBM_DEL(-4);
			return fc;
		}
		
		fc.setLastReply(Tools.getServerTime());
		
		MongoDBConnector.datastore.save(fc);
		ObjectId id =fc.getId();
		return MongoDBConnector.datastore.get(ForumContent.class,id);
	}
	/////////////////////////del////////////////////////////////////
	/**
	 * 真实删除
	 * 用于删除自己写的帖子
	 * 只能自己删自己的
	 * @param bmid
	 * @return
	 */
	public int realDelByBMID(String userBMID,String bmid)
	{
		Query<ForumContent> query=MongoDBConnector.datastore.createQuery(ForumContent.class).field("startuser.BM_ID").equal(userBMID).field("BM_ID").equal(bmid);
		
		MongoDBConnector.datastore.delete(query);
		
		return 0;
	}
	
	//////////////////////edit/////////////////////////////////////
	/**
	 * 伪删除
	 * @param BMID
	 * @param del
	 * @return
	 */
	public ForumContent editDel(String BMID,int del)
	{
		Query<ForumContent> updateQuery = MongoDBConnector.datastore.createQuery(ForumContent.class).field("BM_ID").equal(BMID);
		
		UpdateOperations<ForumContent> ops=MongoDBConnector.datastore.createUpdateOperations(ForumContent.class);
		
		ops.set("BM_DEL", del);
		
		MongoDBConnector.datastore.update(updateQuery, ops);
		
		return MongoDBConnector.datastore.createQuery(ForumContent.class).field("BM_ID").equal(BMID).get();
	}
	
	/**
	 * 批量伪删除
	 * @param BM_ID_LIST
	 * @param del
	 * @return
	 */
	public int editDelList(List<String> BM_ID_LIST,int del)
	{
		Query<ForumContent> updateQuery = MongoDBConnector.datastore.createQuery(ForumContent.class).field("BM_ID").hasAnyOf(BM_ID_LIST);
		
		UpdateOperations<ForumContent> ops=MongoDBConnector.datastore.createUpdateOperations(ForumContent.class);
		
		ops.set("BM_DEL", del);
		
		MongoDBConnector.datastore.update(updateQuery, ops);
		
		return 0;
	}
	
	/**
	 * 修改内嵌文档
	 * 修改用户, 只改十天内的帖子
	 * @return
	 */
	public int editStartUser(String startUserBMID)
	{
		Long fromTime=Tools.getServerTime()-10*24*60*60*1000;
		
		//查找用户
		User u=MongoDBConnector.datastore.createQuery(User.class).field("BM_ID").equal(startUserBMID).get();
		
		if(u==null||u.getBM_ID()==null)
		{
			return -1;
		}
		
		Query<ForumContent> updateQuery = MongoDBConnector.datastore.createQuery(ForumContent.class).field("startuser.BM_ID").equal(startUserBMID).field("BM_TIME").greaterThan(fromTime);
		
		UpdateOperations<ForumContent> ops=MongoDBConnector.datastore.createUpdateOperations(ForumContent.class);
		
		ops.set("startuser", u);
		
		MongoDBConnector.datastore.update(updateQuery, ops);
		
		return 0;
	}
	
	/**
	 * 使用outerkey批量修改删除标识位
	 * 主要使用于批量丢入垃圾桶
	 * @param outerkey
	 * @param del
	 * @return
	 */
	public int editDelByOuterKey(String outerkey,int del)
	{
		Query<ForumContent> updateQuery = MongoDBConnector.datastore.createQuery(ForumContent.class).field("outerkey").equal(outerkey);
		
		UpdateOperations<ForumContent> ops=MongoDBConnector.datastore.createUpdateOperations(ForumContent.class);
		
		ops.set("BM_DEL", del);
		
		MongoDBConnector.datastore.update(updateQuery, ops);
		
		return 0;
	}
	
	/**
	 * 根据FC BMID修改内容
	 * @param fc
	 * @return
	 */
	public int editContentByBMID(ForumContent fc)
	{
		if(fc==null||fc.getBM_ID()==null)
		{
			return -1;
		}
		
		Query<ForumContent> updateQuery = MongoDBConnector.datastore.createQuery(ForumContent.class).field("BM_ID").equal(fc.getBM_ID());
		
		UpdateOperations<ForumContent> ops=MongoDBConnector.datastore.createUpdateOperations(ForumContent.class);
		
		ops.set("content", fc.getContent());
		ops.set("contentList", fc.getContentList()==null?new ArrayList<Content>():fc.getContentList());
		ops.set("lastReply", Tools.getServerTime());
		
		MongoDBConnector.datastore.update(updateQuery, ops,true);
		
		return 0;
	}
	
	/**
	 * 在某个主题被回复的时候使用
	 * 对这个主题进行回复重新count操作
	 * 更新最后回复时间
	 * @param bmid
	 * @return
	 */
	public int editReplyTimeAndCountByBMID(String bmid)
	{
		Query<ForumContent> updateQuery = MongoDBConnector.datastore.createQuery(ForumContent.class).field("BM_ID").equal(bmid);
		
		UpdateOperations<ForumContent> ops=MongoDBConnector.datastore.createUpdateOperations(ForumContent.class);
		
		ops.set("replyCount", this.findCountAnyByOuterKey(bmid));
		ops.set("lastReply", Tools.getServerTime());
		
		MongoDBConnector.datastore.update(updateQuery, ops,true);
		
		return 0;
	}
	
	/***********************************管理员更新***********************************************/
	/**
	 * 修改全体outerkey的值, merge中用到
	 * @param outerkey
	 * @param changeto
	 * @return
	 */
	public int editOuterKey(String outerkey,String changeto)
	{
		Query<ForumContent> updateQuery = MongoDBConnector.datastore.createQuery(ForumContent.class).field("outerkey").equal(outerkey);
		
		UpdateOperations<ForumContent> ops=MongoDBConnector.datastore.createUpdateOperations(ForumContent.class);
		
		ops.set("outerkey", changeto);
		
		MongoDBConnector.datastore.update(updateQuery, ops);
		
		return 0;
	}
	
	/**
	 * 修改选中的outerkey的值, merge中用到
	 * @param outerkey
	 * @param changeto
	 * @param BM_ID_LIST
	 * @return
	 */
	public int editOuterKey(String outerkey,String changeto,List<String> BM_ID_LIST)
	{
		Query<ForumContent> updateQuery = MongoDBConnector.datastore.createQuery(ForumContent.class).field("outerkey").equal(outerkey).field("BM_ID").hasAnyOf(BM_ID_LIST);
		
		UpdateOperations<ForumContent> ops=MongoDBConnector.datastore.createUpdateOperations(ForumContent.class);
		
		ops.set("outerkey", changeto);
		
		MongoDBConnector.datastore.update(updateQuery, ops);
		
		return 0;
	}
	
	
	///////////////////////del////////////////////////////////////////
	
	
	///////////////////////find///////////////////////////////////////
	/**
	 * 条目列表view列表
	 * 查找删除标识位BM_DEL为0, 正常, 的数据
	 * @param outerkey
	 * @param nowPage
	 * @param numInPage
	 * @return
	 */
	public Page<ForumContent> find(String outerkey, int nowPage, int numInPage)
	{
		Query<ForumContent> query=MongoDBConnector.datastore.createQuery(ForumContent.class).field("BM_DEL").equal(0).field("outerkey").equal(outerkey);
		
		Page<ForumContent> page=new Page<ForumContent>();
		
		page.setTotal(query.countAll());
		
		page.setNowPage(nowPage);
		page.setTotalInPage(numInPage);
		
		page.getPage();
		
		page.setList(query.field("BM_DEL").equal(0).order("-BM_TIME").offset(page.getSkip()).limit(numInPage).asList());
		
		return page;
	}
	
	/**
	 * 条目列表view列表
	 * 查找删除标识位BM_DEL为0, 正常, 的数据
	 * 用在查找主题时候的顺序列表
	 * @param outerkey
	 * @param nowPage
	 * @param numInPage
	 * @return
	 */
	public Page<ForumContent> findOrderByBMTIMEAsc(String outerkey, int nowPage, int numInPage)
	{
		Query<ForumContent> query=MongoDBConnector.datastore.createQuery(ForumContent.class).field("BM_DEL").equal(0).field("outerkey").equal(outerkey);
		
		Page<ForumContent> page=new Page<ForumContent>();
		
		page.setTotal(query.countAll());
		
		page.setNowPage(nowPage);
		page.setTotalInPage(numInPage);
		
		page.getPage();
		
		page.setList(query.field("BM_DEL").equal(0).order("BM_TIME").offset(page.getSkip()).limit(numInPage).asList());
		
		return page;
	}
	
	/**
	 * 详情列表block列表
	 * 查找删除标识位BM_DEL为0, 正常, 的数据
	 * @param outerkey
	 * @param nowPage
	 * @param numInPage
	 * @param order true asc
	 * 
	 * @return
	 */
	public Page<ForumContent> find(String outerkey, int nowPage, int numInPage, boolean order)
	{
		Query<ForumContent> query=MongoDBConnector.datastore.createQuery(ForumContent.class).field("BM_DEL").equal(0).field("outerkey").equal(outerkey);
		
		Page<ForumContent> page=new Page<ForumContent>();
		
		page.setTotal(query.countAll());
		
		page.setNowPage(nowPage);
		page.setTotalInPage(numInPage);
		
		page.getPage();
		
		if(order)
		{
			page.setList(query.field("BM_DEL").equal(0).order("lastReply").offset(page.getSkip()).limit(numInPage).asList());
		}
		else
		{
			page.setList(query.field("BM_DEL").equal(0).order("-lastReply").offset(page.getSkip()).limit(numInPage).asList());
		}
		
		return page;
	}
	
	/**
	 * 查找一个带正常标识位的条目
	 * 1, 被删除, 0, 正常
	 * @param bmid
	 * @return
	 */
	public ForumContent findByBMID(String bmid)
	{
		return MongoDBConnector.datastore.createQuery(ForumContent.class).field("BM_DEL").equal(0).field("BM_ID").equal(bmid).get();
	}
	
	/**
	 * 查找一个标识位的条目
	 * 1, 被删除, 0, 正常
	 * @param bmid
	 * @return
	 */
	public ForumContent findByBMID(String bmid, int del)
	{
		return MongoDBConnector.datastore.createQuery(ForumContent.class).field("BM_DEL").equal(del).field("BM_ID").equal(bmid).get();
	}
	
	/**
	 * 查找一个任意标识位的条目
	 * bmid主键查找
	 * @param bmid
	 * @return
	 */
	public ForumContent findAnyByBMID(String bmid)
	{
		return MongoDBConnector.datastore.createQuery(ForumContent.class).field("BM_ID").equal(bmid).get();
	}
	
	/**
	 * 传入outerkey, 探测是不是有子项目
	 * 如果没有 返回false
	 * 有 返回true
	 * @param bmid
	 * @return
	 */
	public boolean checkExistByOuterKey(String bmid)
	{
		long check=MongoDBConnector.datastore.createQuery(ForumContent.class).field("outerkey").equal(bmid).limit(1).countAll();
		
		if(check==0)
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * 根据outerKey查找有多少条子条目
	 * @param outerKey
	 * @return
	 */
	public Long findCountAnyByOuterKey(String outerKey)
	{
		return MongoDBConnector.datastore.createQuery(ForumContent.class).field("outerkey").equal(outerKey).countAll();
	}
	
	
	/************************论坛管理员使用下列方法查找*******************************/
	/**
	 * 查找一个删除标识位的条目
	 * @param bmid
	 * @return
	 */
	public ForumContent findDelByBMID(String bmid)
	{
		return MongoDBConnector.datastore.createQuery(ForumContent.class).field("BM_DEL").equal(1).field("BM_ID").equal(bmid).get();
	}
	/**
	 * 查找被删除的帖子列表
	 * authList 拥有可操作权限的外键
	 * @param outerkey
	 * @param nowPage
	 * @param numInPage
	 * @param order
	 * @return
	 */
	public Page<ForumContent> findDelList(List<String> authList, int nowPage, int numInPage, boolean order)
	{
		if(authList==null)
		{
			authList=new ArrayList<String>();
		}
		
		Query<ForumContent> query=MongoDBConnector.datastore.createQuery(ForumContent.class).field("BM_DEL").equal(1).field("outerkey").hasAnyOf(authList);
		
		Page<ForumContent> page=new Page<ForumContent>();
		
		page.setTotal(query.countAll());
		
		page.setNowPage(nowPage);
		page.setTotalInPage(numInPage);
		
		page.getPage();
		
		if(order)
		{
			page.setList(query.order("BM_TIME").offset(page.getSkip()).limit(numInPage).asList());
		}
		else
		{
			page.setList(query.order("-BM_TIME").offset(page.getSkip()).limit(numInPage).asList());
		}
		
		return page;
	}
	
	/**
	 * 真实删除
	 * @param bmid
	 * @return
	 */
	public int realDelByBMID(String bmid)
	{
		Query<ForumContent> query=MongoDBConnector.datastore.createQuery(ForumContent.class).field("BM_ID").equal(bmid);
		
		MongoDBConnector.datastore.delete(query);
		
		return 0;
	}
	
	/**
	 * 真实删除
	 * @param bmid
	 * @return
	 */
	public int realDelByOuterKey(String bmid)
	{
		Query<ForumContent> query=MongoDBConnector.datastore.createQuery(ForumContent.class).field("outerkey").equal(bmid);
		
		MongoDBConnector.datastore.delete(query);
		
		return 0;
	}
}
