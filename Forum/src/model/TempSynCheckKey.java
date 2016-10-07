package model;

import org.mongodb.morphia.annotations.Entity;

@Entity("tempsyncheckkey")
public class TempSynCheckKey extends BaseModel
{
	public String key;
	
	public Long limit;//5分钟内有效

	public Long getLimit() {
		return limit;
	}

	public void setLimit(Long limit) {
		this.limit = limit;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
