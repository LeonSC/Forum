package model;

import org.mongodb.morphia.annotations.NotSaved;

/**
 * Admin is up on the user. Admin is a role mark it has this right. User is the bean.
 * @author sasgsc
 */
@NotSaved
public class Admin {

	private Integer lv=0;
	
	private Integer forumLv=0;//0-99 , block master, 100-199 forum master
	
	private String password;

	public Integer getLv() {
		return lv;
	}

	public void setLv(Integer lv) {
		this.lv = lv;
	}

	public Integer getForumLv() {
		return forumLv;
	}

	public void setForumLv(Integer forumLv) {
		this.forumLv = forumLv;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
