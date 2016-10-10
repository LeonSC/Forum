package model;

import java.util.Map;

import org.mongodb.morphia.annotations.Entity;

@Entity("startup")
public class Startup extends BaseModel{

	private Map<String,ForumTitle> forumTitle;

	public Map<String, ForumTitle> getForumTitle() {
		return forumTitle;
	}

	public void setForumTitle(Map<String, ForumTitle> forumTitle) {
		this.forumTitle = forumTitle;
	}

	
}
