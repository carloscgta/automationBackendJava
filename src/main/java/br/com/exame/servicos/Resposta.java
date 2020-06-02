package br.com.exame.servicos;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class Resposta {

	private Response resposta;

	public Resposta(Response resposta) {
		this.resposta = resposta;
	}

	public Resposta() {

	}
	
	public String logarEvidencia() {
		return "\nRESPONSE:\n".concat(getResponse().prettyPrint().toString());
	}
	
	public ValidatableResponse getResposta() {
		return resposta.then().log().all();
	}

	public Response getResponse() {
		return resposta;
	}
	
	public String salvarToken(String caminho) {
		return resposta.jsonPath().get(caminho).toString();	
	}

	public Integer receberStatusCode(Integer statusCode) {
		return resposta.statusCode();

	}
	
	public String salvarObjetosBody(String caminho) {
		return resposta.jsonPath().get(caminho).toString();
	}

}
