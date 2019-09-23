package com.training.spring.batch.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "CUSTOMER")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CUSTOMER_ID", insertable = false, updatable = false)
	private Long customerId;

	@Column(name = "CUSTOMER_NAME", length = 50, nullable = false)
	private String customerName;

	@Column(name = "CUSTOMER_ADDRESS", nullable = false, length = 100)
	private String customerAddress;

	@Column(name = "CUSTOMER_EMAIL", length = 50)
	private String customerEmail;
	
	@Column(name = "CUSTOMER_MOBILE")
	private String customerMobile;

	@Column(name = "CUSTOMER_ACTIVE_IND")
	private Boolean customerActiveInd;

	@Version
	@Column(name = "CUSTOMER_VERSION")
	private Long customerVersion;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getCustomerMobile() {
		return customerMobile;
	}

	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public Boolean getCustomerActiveInd() {
		return customerActiveInd;
	}

	public void setCustomerActiveInd(Boolean customerActiveInd) {
		this.customerActiveInd = customerActiveInd;
	}

	public Long getCustomerVersion() {
		return customerVersion;
	}

	public void setCustomerVersion(Long customerVersion) {
		this.customerVersion = customerVersion;
	}

}
