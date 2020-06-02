package br.com.exame.pojos;

import com.google.gson.annotations.SerializedName;

public class Subscription {

	@SerializedName("customer_id")
	private Integer customerId;
	@SerializedName("plan_id")
	private Integer planId;
	@SerializedName("payment_method_code")
	private String paymentMethodCode;
	@SerializedName("payment_profile")
	private PaymentProfiles paymentProfile;
	@SerializedName("end_at")
	private String EndAt;
	
	public String getEndAt() {
		return EndAt;
	}

	public void setEndAt(String endAt) {
		EndAt = endAt;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getPlanId() {
		return planId;
	}

	public void setPlanId(Integer planId) {
		this.planId = planId;
	}


	
	public void setPaymentProfile(PaymentProfiles paymentProfile) {
		this.paymentProfile = paymentProfile;
	}


	public String getPaymentMethodCode() {
		return paymentMethodCode;
	}

	public void setPaymentMethodCode(String paymentMethodCode) {
		this.paymentMethodCode = paymentMethodCode;
	}


}
