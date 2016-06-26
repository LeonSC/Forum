package model;

import java.util.List;

import org.mongodb.morphia.annotations.Entity;

@Entity("startup")
public class Startup extends BaseModel{

	private List<String> forumTitle;

	public List<String> getForumTitle() {
		return forumTitle;
	}

	public void setForumTitle(List<String> forumTitle) {
		this.forumTitle = forumTitle;
	}

	
}
