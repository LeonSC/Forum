package model;

import java.util.List;

import org.mongodb.morphia.annotations.Entity;

@Entity("forumcontent")
public class ForumContent extends BaseModel {

	
	private String outerkey;//link to forumTitle BM_ID
	
	private String title;
	private String content;
	
	private List<Content> contentList;
	
	private User startuser;
	
	private Integer position=0;//0 for normal, 1 for headline, 2, for all headline
	
	private Integer editLv=0;//lv, default 0.

	
	private Long replyCount=0L;
	
	private Long lastReply=0L;
	
	private Integer layer=0;//this is for topic or reply
	
	public Long getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(Long replyCount) {
		this.replyCount = replyCount;
	}

	public Long getLastReply() {
		return lastReply;
	}

	public void setLastReply(Long lastReply) {
		this.lastReply = lastReply;
	}

	public String getOuterkey() {
		return outerkey;
	}

	public void setOuterkey(String outerkey) {
		this.outerkey = outerkey;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getStartuser() {
		return startuser;
	}

	public void setStartuser(User startuser) {
		this.startuser = startuser;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public Integer getEditLv() {
		return editLv;
	}

	public void setEditLv(Integer editLv) {
		this.editLv = editLv;
	}
	

	public List<Content> getContentList() {
		return contentList;
	}

	public void setContentList(List<Content> contentList) {
		this.contentList = contentList;
	}

	public Integer getLayer() {
		return layer;
	}

	public void setLayer(Integer layer) {
		this.layer = layer;
	}
}
