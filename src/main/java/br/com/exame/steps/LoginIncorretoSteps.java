package br.com.exame.steps;

import com.google.gson.Gson;

import br.com.exame.core.PDFGenerator;
import br.com.exame.pojos.Login;
import br.com.exame.servicos.Authenticacao;
import br.com.exame.servicos.Resposta;
import br.com.exame.utils.YamlHelper;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class LoginIncorretoSteps {

	Gson gson = new Gson();
	PDFGenerator pdfgenerator = new PDFGenerator();
	YamlHelper yaml = new YamlHelper();
	Authenticacao autenticacao = new Authenticacao();
	Login login = new Login();
	Resposta resposta;
	Scenario scen;

	@Before(value = "@loginincorreto")
	public void before(Scenario cenario) throws Exception {
		pdfgenerator.iniciaPDF(cenario);
	}

	@Given("^que eu acesse o endpoint da aplicacao \"([^\"]*)\" com usuario incorreto$")
	public void que_eu_acesse_o_endpoint_da_aplicacao_com_usuario_incorreto(String endPoint) throws Throwable {

	}

	@Given("^que eu acesse o endpoint da aplicacao \"([^\"]*)\" com \"([^\"]*)\" incorreto$")
	public void que_eu_acesse_o_endpoint_da_aplicacao_com_incorreto(String endPoint, String username) throws Throwable {
		login.setUsername(username);
		login.setPassword("Yeshu@18");
		String json = gson.toJson(login);
		resposta = autenticacao.login(yaml.getAtributo(endPoint).toString(), json);
		pdfgenerator.conteudoPDF("que eu acesse o endpoint da aplicacao com usuario incorreto",
				resposta.logarEvidencia());
	}

	@Then("^a API me retorna o status code (\\d+) do login incorreto$")
	public void a_API_me_retorna_o_status_code_do_login_incorreto(int statusCode) throws Throwable {
		resposta.getResposta().statusCode(statusCode);
		String texto = Integer.toString(resposta.getResponse().getStatusCode());
		pdfgenerator.conteudoPDF("a API me retorna o status code  do login incorreto:",
				"o status da resposta é: " + texto);
	}

	@After(value = "@loginincorreto")
	public void finalizaPDF(Scenario cenario) throws Exception {

		scen = cenario;
		pdfgenerator.fechaPDF(scen);

	}
}
