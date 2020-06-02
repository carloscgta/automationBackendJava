package br.com.exame.pojos;

public class Logout {
	
	private String accessToken;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	@Override
	public String toString() {
		return "Logout [accessToken=" + accessToken + "]";
	}

}
