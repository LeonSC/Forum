package model;

import org.mongodb.morphia.annotations.NotSaved;

/**
 * 只和ForumTitle联合使用
 * @author sasgsc
 *
 */
@NotSaved
public class Background {

	private String src;

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}
}
