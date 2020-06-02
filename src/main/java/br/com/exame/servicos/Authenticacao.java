package br.com.exame.servicos;

import static io.restassured.RestAssured.given;
import io.restassured.response.Response;

public class Authenticacao {

	private Response resposta;

	public Authenticacao() {

	}

	public Resposta login(String endPoint, Object mensagem) {

		resposta = given().contentType("application/json").body(mensagem).when().log().all().post(endPoint);
		return new Resposta(resposta);

	}

}
