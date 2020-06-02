package br.com.exame.pojos;

import com.google.gson.annotations.SerializedName;

public class PaymentProfiles {

	@SerializedName("bank_account")
	private String bankAccount;
	@SerializedName("bank_branch")
	private String bankBranch;
	@SerializedName("card_cvv")
	private String cardCvv;
	@SerializedName("card_expiration")
	private String cardExpiration;
	@SerializedName("card_number")
	private String cardNumber;
	@SerializedName("customer_id")
	private int customerId;
	@SerializedName("gateway_token")
	private String gatewayToken;
	@SerializedName("holder_name")
	private String holderName;
	@SerializedName("payment_company_code")
	private String paymentCompanyCode;
	@SerializedName("payment_method_code")
	private String paymentMethodCode;
	@SerializedName("registry_code")
	private String registryCode;
	@SerializedName("end_at")
	private String endAt;

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	
	public String getBankBranch() {
		return bankBranch;
	}

	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}

	public String getCardCvv() {
		return cardCvv;
	}

	public void setCardCvv(String cardCvv) {
		this.cardCvv = cardCvv;
	}

	public String getCardExpiration() {
		return cardExpiration;
	}

	public void setCardExpiration(String cardExpiration) {
		this.cardExpiration = cardExpiration;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getGatewayToken() {
		return gatewayToken;
	}

	public void setGatewayToken(String gatewayToken) {
		this.gatewayToken = gatewayToken;
	}

	public String getHolderName() {
		return holderName;
	}

	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}

	public String getPaymentCompanyCode() {
		return paymentCompanyCode;
	}

	public void setPaymentCompanyCode(String paymentCompanyCode) {
		this.paymentCompanyCode = paymentCompanyCode;
	}

	public String getPaymentMethodCode() {
		return paymentMethodCode;
	}

	public void setPaymentMethodCode(String paymentMethodCode) {
		this.paymentMethodCode = paymentMethodCode;
	}

	public String getRegistryCode() {
		return registryCode;
	}

	public void setRegistryCode(String registryCode) {
		this.registryCode = registryCode;
	}

	public String getEndAt() {
		return endAt;
	}

	public void setEndAt(String endAt) {
		this.endAt = endAt;
	}

}