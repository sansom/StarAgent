package com.core.jadlwork.model.mbypt;


/**         
 * @ClassName：ServiceDependecy   
 * @Description：   
 * @author ：zhangqing   
 * @date ：2015-9-21 下午1:26:35      
 * @version      
 */
public class ServiceDependency {
private static final long serialVersionUID = 1L;
	
	public static final String db_tablename = "T_SVR_SERVICEDEPENDENCY";
	public static final String db_tablepkfields = "id"; 
	
	private String id;
	private String serviceId;
	private String serviceName;
	private String preServiceId;
	private String preServiceName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getPreServiceId() {
		return preServiceId;
	}
	public void setPreServiceId(String preServiceId) {
		this.preServiceId = preServiceId;
	}
	public String getPreServiceName() {
		return preServiceName;
	}
	public void setPreServiceName(String preServiceName) {
		this.preServiceName = preServiceName;
	}
	

}
