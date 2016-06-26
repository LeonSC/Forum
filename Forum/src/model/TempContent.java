package model;

import org.mongodb.morphia.annotations.Entity;

/**
 * 用于保存临时的文档, 用于论坛, 发表长文
 * @author sasgsc
 */
@Entity("tempcontent")
public class TempContent extends BaseModel{

	private String userOuterKey="";
	
	private String title="";
	private String content="";
	
	private Long lastUpdate;

	public String getUserOuterKey() {
		return userOuterKey;
	}

	public void setUserOuterKey(String userOuterKey) {
		this.userOuterKey = userOuterKey;
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

	public Long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
