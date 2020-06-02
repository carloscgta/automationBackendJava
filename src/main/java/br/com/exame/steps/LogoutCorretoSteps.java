package br.com.exame.steps;

import com.google.gson.Gson;

import br.com.exame.core.PDFGenerator;
import br.com.exame.pojos.Login;
import br.com.exame.pojos.Logout;
import br.com.exame.servicos.Authenticacao;
import br.com.exame.servicos.Resposta;
import br.com.exame.servicos.Servicos;
import br.com.exame.utils.YamlHelper;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class LogoutCorretoSteps {
	
	Gson gson = new Gson();
	PDFGenerator pdfgenerator = new PDFGenerator();
	Authenticacao autenticacao = new Authenticacao();
	YamlHelper yaml = new YamlHelper();
	Logout logout = new Logout();
	Login login = new Login();
	Servicos verbos = new Servicos();
	Resposta resposta;
	Scenario scen;
	
	@Before(value = "@logout")
	public void before(Scenario cenario) throws Exception {
		pdfgenerator.iniciaPDF(cenario);
	}
	
	@Given("^que eu acessse a aplicacao de \"([^\"]*)\" com o \"([^\"]*)\"$")
	public void que_eu_acessse_a_aplicacao_de_com_o(String endPoint, String username) throws Throwable {
		login.setUsername(username);
		login.setPassword("Yeshu@18");
		String json = gson.toJson(login);
		resposta = autenticacao.login(yaml.getAtributo(endPoint).toString(), json);
		pdfgenerator.conteudoPDF("que eu acesse o endpoint da aplicacao:", resposta.logarEvidencia());
	}

	@When("^eu realizo o \"([^\"]*)\"$")
	public void eu_realizo_o(String endPointLogout) throws Throwable {
		String AccessToken = resposta.salvarToken("accessToken");
		String IdToken = resposta.salvarToken("idToken");
		logout.setAccessToken(AccessToken);
		String json = gson.toJson(logout);
		resposta = verbos.postEndpointWithAuthorization(yaml.getAtributo(endPointLogout).toString(), IdToken, json);
		pdfgenerator.conteudoPDF("eu realizo o:", resposta.logarEvidencia());
	}

	@Then("^a API me retorna (\\d+) que foi deslogado$")
	public void a_API_me_retorna_que_foi_deslogado(int statusCode) throws Throwable {
		resposta.getResposta().statusCode(statusCode);
		String texto = Integer.toString(resposta.getResponse().getStatusCode());
		pdfgenerator.conteudoPDF("a API me retorna que foi deslogado:", "o status da resposta é: " + texto);
	}
	
	@After(value = "@logout")
	public void finalizaPDF(Scenario cenario) throws Exception {

		scen = cenario;
		pdfgenerator.fechaPDF(scen);

	}

}
