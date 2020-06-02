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




public class CadastroUsuarioPhoneForaPadraoSteps {
	
	Gson gson = new Gson();
	PDFGenerator pdfgenerator = new PDFGenerator();
	YamlHelper yaml = new YamlHelper();
	CreateUser createUser = new CreateUser();
	GeradorCPF cpf = new GeradorCPF();
	GeradorEmail email = new GeradorEmail();
	Faker faker = new Faker();
	Servicos verbos = new Servicos();
	Scenario scen;
	Resposta resposta;
	
	@Before(value = "@cadastrophoneforapadrao ")
	public void before(Scenario cenario) throws Exception {
		pdfgenerator.iniciaPDF(cenario);
	}
	
	@Given("^que eu acesse o endpoint da aplicacao de \"([^\"]*)\" para o cadastro de usuario com \"([^\"]*)\" fora do padrao com \"([^\"]*)\" e com \"([^\"]*)\"$")
	public void que_eu_acesse_o_endpoint_da_aplicacao_de_para_o_cadastro_de_usuario_com_fora_do_padrao_com_e_com(String endPoint, String telefone, String firstname, String lastname) throws Throwable {
		// Criação de Usuário
		createUser.setCpfCnpj(cpf.mostraResultado());
		createUser.setEmail(email.geraEmail());
		createUser.setFirstName(firstname);
		createUser.setLastName(lastname);
		createUser.setPassword("Yeshu@18");
		createUser.setPhone(telefone);
		String json = gson.toJson(createUser);
		resposta = verbos.postEndPoint(yaml.getAtributo(endPoint).toString(), json);
		pdfgenerator.conteudoPDF("que eu acesse o endpoint da aplicacao de para o cadastro de usuario com telefone fora do padrao:", resposta.logarEvidencia());
	}
	

	@Then("^a API me retorna o status code (\\d+) para o cadastro com o telefone fora do padrao$")

	public void a_API_me_retorna_o_status_code_para_o_cadastro_com_o_telefone_fora_do_padrao(int statusCode) throws Throwable {
	    // Validação de Resposta
		resposta.getResposta().statusCode(statusCode);
		String texto = Integer.toString(resposta.getResponse().getStatusCode());
		pdfgenerator.conteudoPDF("a API me retorna o status code para o cadastro com o telefone fora do padrao:", "o status da resposta é: " + texto);
	
	}

	
	@After(value = "@cadastrophoneforapadrao")
	public void finalizaPDF(Scenario cenario) throws Exception {
		scen = cenario;
		pdfgenerator.fechaPDF(scen);
	}

}
