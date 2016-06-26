package model;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class Content {

	private String content="";
	private Long changeTime=0L;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getChangeTime() {
		return changeTime;
	}
	public void setChangeTime(Long changeTime) {
		this.changeTime = changeTime;
	}
	
}
