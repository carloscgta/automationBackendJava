package br.com.exame.steps;

import com.github.javafaker.Faker;
import com.google.gson.Gson;

import br.com.exame.core.PDFGenerator;
import br.com.exame.pojos.CreateUser;
import br.com.exame.servicos.Resposta;
import br.com.exame.servicos.Servicos;
import br.com.exame.utils.GeradorCPF;
import br.com.exame.utils.GeradorEmail;
import br.com.exame.utils.YamlHelper;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;


public class CadastroUsuarioSemFirstnameSteps {
	
	Gson gson = new Gson();
	PDFGenerator pdfgenerator = new PDFGenerator();
	YamlHelper yaml = new YamlHelper();
	CreateUser createUser = new CreateUser();
	GeradorEmail email = new GeradorEmail();
	GeradorCPF cpf = new GeradorCPF();
	Faker faker = new Faker();
	Servicos verbos = new Servicos();
	Scenario scen;
	Resposta resposta;
	
	
	@Before(value = "@cadastrousuariosemfirstname")
	public void before(Scenario cenario) throws Exception {
		pdfgenerator.iniciaPDF(cenario);
	}
	
	@Given("^que eu acesse o endpoint da aplicacao de \"([^\"]*)\" para o cadastro de usuario sem \"([^\"]*)\"  com \"([^\"]*)\"$")
	public void que_eu_acesse_o_endpoint_da_aplicacao_de_para_o_cadastro_de_usuario_sem_com(String endPoint, String firstname, String lastname) throws Throwable {
		createUser.setCpfCnpj(cpf.mostraResultado());
		createUser.setEmail(email.geraEmail());
		createUser.setFirstName(firstname);
		createUser.setLastName(lastname);
		createUser.setPassword("Yeshu@18");
		createUser.setPhone("+5511952178031");
		String json = gson.toJson(createUser);
		resposta = verbos.postEndPoint(yaml.getAtributo(endPoint).toString(), json);
		pdfgenerator.conteudoPDF("que eu acesse o endpoint da aplicacao de para o cadastro de usuario sem firstname:", resposta.logarEvidencia());
	}
	


	@Then("^a API me retorna o status code (\\d+) para o cadastro de primeiro nome em branco$")
	public void a_API_me_retorna_o_status_code_para_o_cadastro_de_usuario_em_branco(int statusCode) throws Throwable {
		resposta.getResposta().statusCode(statusCode);
		String texto = Integer.toString(resposta.getResponse().getStatusCode());
		pdfgenerator.conteudoPDF("a API me retorna o status code para o cadastro de usuario em branco:", "o status da resposta é: " + texto);
	}
	
	@After(value = "@cadastrousuariosemfirstname")
	public void finalizaPDF(Scenario cenario) throws Exception {
		scen = cenario;
		pdfgenerator.fechaPDF(scen);
	}

}
