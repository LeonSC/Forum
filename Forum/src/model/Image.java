package model;

import org.mongodb.morphia.annotations.Entity;

@Entity("image")
public class Image extends BaseModel{

	private String name="";
	private String hash="";
	private Integer w=0;
	private Integer h=0;
	private String usedin="";
	private String outerkey="";
	private String userid="";
	
	
	public String getUsedin() {
		return usedin;
	}
	public void setUsedin(String usedin) {
		this.usedin = usedin;
	}
	public String getOuterkey() {
		return outerkey;
	}
	public void setOuterkey(String outerkey) {
		this.outerkey = outerkey;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public Integer getW() {
		return w;
	}
	public void setW(Integer w) {
		this.w = w;
	}
	public Integer getH() {
		return h;
	}
	public void setH(Integer h) {
		this.h = h;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
}
