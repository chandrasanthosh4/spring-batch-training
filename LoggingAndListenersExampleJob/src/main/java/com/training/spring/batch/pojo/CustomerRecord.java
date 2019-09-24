package com.training.spring.batch.pojo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CustomerRecord {

	private Long customerId;

	@NotNull(message = "'CustomerName' cannot be NULL")
	private String customerName;

	@NotNull(message = "'CustomerAddress' cannot be NULL")
	private String customerAddress;

	@NotNull(message = "'CustomerEmail' cannot be NULL")
	private String customerEmail;

	@NotNull(message = "'CustomerMobile' cannot be NULL")
	@Size(min = 10, max = 10, message = "Length of 'CustomerMobile' field must be 10 ")
	private String customerMobile;

	@NotNull(message = "'CustomerActiveInd' cannot be NULL")
	private Boolean customerActiveInd;

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

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCustomerMobile() {
		return customerMobile;
	}

	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
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
	
	@Override
	public String toString() {
		return "CustomerRecord [customerId=" + customerId + ", customerName=" + customerName + ", customerAddress="
				+ customerAddress + ", customerEmail=" + customerEmail + ", customerMobile=" + customerMobile
				+ ", customerActiveInd=" + customerActiveInd + ", customerVersion=" + customerVersion + "]";
	}

}
