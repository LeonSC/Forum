package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.NotSaved;

import startup.ForumCache;

@Entity("forumtitle")
public class ForumTitle extends BaseModel{

	private String outerkey;//link to BM_ID
	
	private String name;
	private String icon;
	private int order=0;
	private String intro;
	private int lv;//0为通用
	private int visible;//0为通用
	
	@NotSaved
	private List<ForumTitle> subForumTitle;
	
	private Map<String, User> manager=new HashMap<String, User>();//BMID, USER MODEL
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public int getLv() {
		return lv;
	}
	public void setLv(int lv) {
		this.lv = lv;
	}
	public int getVisible() {
		return visible;
	}
	public void setVisible(int visible) {
		this.visible = visible;
	}
	public List<ForumTitle> getSubForumTitle() {
		return subForumTitle;
	}
	//build the forum title
	public void setSubForumTitle(List<ForumTitle> subForumTitle) {
		
		//loop to HashMap		
		for(int i=0;i<subForumTitle.size();i++)
		{
			ForumTitle tmp=subForumTitle.get(i);
			
			ForumCache.getCache().forumTitle.put(tmp.getBM_ID(), tmp);
		}
		
		this.subForumTitle = subForumTitle;
	}
	public String getOuterkey() {
		return outerkey;
	}
	public void setOuterkey(String outerkey) {
		this.outerkey = outerkey;
	}
	public Map<String, User> getManager() {
		return manager;
	}
	public void setManager(Map<String, User> manager) {
		this.manager = manager;
	}
}
