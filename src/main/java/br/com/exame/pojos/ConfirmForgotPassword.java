package br.com.exame.pojos;

public class ConfirmForgotPassword {
	

	private String code;
	private String newpw;
	private String username;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getNewpw() {
		return newpw;
	}
	public void setNewpw(String newpw) {
		this.newpw = newpw;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public String toString() {
		return "ConfirmForgotPassword [code=" + code + ", newpw=" + newpw + ", username=" + username + "]";
	}
	
	

}
