package model;

import org.mongodb.morphia.annotations.Entity;

/**
 * 记录集群的论坛的IP和port,以及设置他们的别名
 * @author sasgsc
 */
@Entity("synchro")
public class Synchro extends BaseModel
{
	public String ipAddress="";
	public String alias="";
	
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
}
