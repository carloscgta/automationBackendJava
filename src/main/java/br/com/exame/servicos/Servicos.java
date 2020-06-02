package br.com.exame.servicos;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;

public class Servicos implements ServicosImpl{
	
	private Response resposta;


	public Servicos() {
		
		
	}

	@Override
	public Resposta getEndPoint(String endPoint) {
		resposta = given().contentType("application/json").when().log().all().post(endPoint);
		return new Resposta(resposta);
	}
	

	@Override
	public Resposta postEndPoint(String endPoint, Object mensagem) {
		resposta = given().header("g-recaptcha-response","xxxxxxxxxxx").contentType("application/json").body(mensagem).when().log().all().post(endPoint);
		return new Resposta(resposta);
	}
	
	//Created by Carlos Almeida 20/03
	public Resposta getEndPointWithAuthorization2(String endPoint, String token) {
		resposta =	given().header("Authorization",token).when().log().all().get(endPoint);
		return new Resposta(resposta);
	}
	
	public Resposta postEndpointWithAuthorization(String endPoint, String token, Object mensagem) {
		resposta = given().header("Authorization",token)
				  .contentType("application/json")
				  .body(mensagem).when().log()
				.  all().post(endPoint);
		return new Resposta(resposta);
		
	}
	
	public Resposta putEndpointWithAuthorization(String endPoint, String token, Object mensagem) {
		resposta = given().header("Authorization",token)
				  .contentType("application/json")
				  .body(mensagem).when().log()
				.  all().put(endPoint);
		return new Resposta(resposta);
	}
	

	@Override
	public Resposta putEndPoint(String endPoint, Object mensagem) {
		resposta = given().contentType("application/json").body(mensagem).when().log().all().post(endPoint);
		return new Resposta(resposta);
	}

	@Override
	public Resposta deleteEndpoint(String endPoint) {
		resposta = given().contentType("application/json").when().log().all().delete(endPoint);
		return new Resposta(resposta);
	}



}

