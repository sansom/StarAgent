package com.core.jadlwork.model.mbypt;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**         
 * @ClassName：Service   
 * @Description：   
 * @author ：zhangqing   
 * @date ：2015-9-6 上午10:51:13      
 * @version      
 */
public class Service implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public static final String db_tablename = "t_svr_service";
	public static final String db_tablepkfields = "serviceId"; 
	
	private String serviceId;
	private String serviceName;
	private String serviceType;
	private String serviceTypeName;
	private float price;
	private String priceUnit;
	private String url;
	private String serviceDesc;
	private String status;
	private String serviceSource;
	private String input;
	private String output;
	private String sort;
	private List<ServiceDependency> dependencyList = new ArrayList<ServiceDependency>();
	
	public List<ServiceDependency> getDependencyList() {
		return dependencyList;
	}
	
	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public void setDependencyList(List<ServiceDependency> dependencyList) {
		this.dependencyList = dependencyList;
	}
	public String getPriceUnit() {
		return priceUnit;
	}
	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getServiceSource() {
		return serviceSource;
	}
	public void setServiceSource(String serviceSource) {
		this.serviceSource = serviceSource;
	}
	public String getServiceDesc() {
		return serviceDesc;
	}
	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
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
	public String getServiceTypeName() {
		return serviceTypeName;
	}
	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
	
}
