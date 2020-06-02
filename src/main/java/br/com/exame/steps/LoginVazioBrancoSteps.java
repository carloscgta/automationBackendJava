package br.com.exame.steps;

import com.google.gson.Gson;

import br.com.exame.core.PDFGenerator;
import br.com.exame.pojos.Login;
import br.com.exame.servicos.Authenticacao;
import br.com.exame.servicos.Resposta;
import br.com.exame.servicos.Servicos;
import br.com.exame.utils.YamlHelper;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class LoginVazioBrancoSteps {
	
	Gson gson = new Gson();
	PDFGenerator pdfgenerator = new PDFGenerator();
	Authenticacao autenticacao = new Authenticacao();
	YamlHelper yaml = new YamlHelper();
	Login login = new Login();
	Servicos verbos = new Servicos();
	Resposta resposta;
    Scenario scen;
    
    @Before(value = "@logincampovazio")
	public void before(Scenario cenario) throws Exception {
		pdfgenerator.iniciaPDF(cenario);
	}
	
	@Given("^que eu acesse o endpoint da aplicacao \"([^\"]*)\" com \"([^\"]*)\" em branco$")
	public void que_eu_acesse_o_endpoint_da_aplicacao_com_em_branco(String endPoint, String username) throws Throwable {
		login.setUsername(username);
		login.setPassword("Yeshu@18");
		String json = gson.toJson(login);
		resposta = autenticacao.login(yaml.getAtributo(endPoint).toString(), json);
		pdfgenerator.conteudoPDF("que eu acesse o endpoint da aplicacao com usuario em branco:", resposta.logarEvidencia());
	}


	@Then("^a API me retorna o status code (\\d+) do login em branco$")
	public void a_API_me_retorna_o_status_code_do_login_em_branco(int statusCode)throws Throwable {
		resposta.getResposta().statusCode(statusCode);
		String texto = Integer.toString(resposta.getResponse().getStatusCode());
		pdfgenerator.conteudoPDF("Entao a API me retorna o status code do login em branco:", "o status da resposta é: " + texto);
	}
	
	@After(value = "@logincampovazio")
	public void finalizaPDF(Scenario cenario) throws Exception {

		scen = cenario;
		pdfgenerator.fechaPDF(scen);

	}

}
