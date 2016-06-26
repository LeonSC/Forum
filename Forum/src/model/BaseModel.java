package model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

import tools.Tools;

public class BaseModel {

	@Id
	private ObjectId id;

	/**
	 * assistant key, sometime, we need change key manually to keep the structure being right, this is the key u can change. outer-key should announce in each structure.
	 */
	@Indexed
	protected String BM_ID=Tools.getID();//key u can change.
	protected Integer BM_DEL=0;//0 normal, 1, deleted, -x, error code for each class, 99, pending
	protected Long BM_TIME=Tools.getServerTime();
	
	public void setBM_ID(String bM_ID) {
		BM_ID = bM_ID;
	}

	public void setBM_DEL(Integer bM_DEL) {
		BM_DEL = bM_DEL;
	}

	public void setBM_TIME(Long bM_TIME) {
		BM_TIME = bM_TIME;
	}

	public Long getBM_TIME() {
		return BM_TIME;
	}

	public String getBM_ID() {
		return BM_ID;
	}

	public Integer getBM_DEL() {
		return BM_DEL;
	}
	
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
}
