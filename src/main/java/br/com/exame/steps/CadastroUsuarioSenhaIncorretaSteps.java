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

public class CadastroUsuarioSenhaIncorretaSteps {

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

	@Before(value = "@cadastrosenhaforapadrao")
	public void before(Scenario cenario) throws Exception {
		pdfgenerator.iniciaPDF(cenario);
	}

	@Given("^que eu acesse o endpoint da aplicacao de \"([^\"]*)\" para o cadastro de usuario com \"([^\"]*)\" fora do padrao com \"([^\"]*)\" e \"([^\"]*)\"$")
	public void que_eu_acesse_o_endpoint_da_aplicacao_de_para_o_cadastro_de_usuario_com_fora_do_padrao_com_e(
			String endPoint, String senha, String username, String lastname) throws Throwable {
		// Criação de Usuário
		createUser.setCpfCnpj(cpf.mostraResultado());
		createUser.setEmail(email.geraEmail());
		createUser.setFirstName(username);
		createUser.setLastName(lastname);
		createUser.setPassword(senha);
		createUser.setPhone("+5511952178031");
		String json = gson.toJson(createUser);
		resposta = verbos.postEndPoint(yaml.getAtributo(endPoint).toString(), json);

		pdfgenerator.conteudoPDF("que eu acesse o endpoint da aplicacao de para o cadastro de usuario com senha fora do padrao:", resposta.logarEvidencia());
	}
	

	@Then("^a API me retorna o status code (\\d+) para o cadastro fora do padrao correto$")
	public void a_API_me_retorna_o_status_code_para_o_cadastro_fora_do_padrao_correto(int statusCode) throws Throwable {
		// Criação e validação de resposta
		resposta.getResposta().statusCode(statusCode);
		String texto = Integer.toString(resposta.getResponse().getStatusCode());
		pdfgenerator.conteudoPDF("a API me retorna o status code para o cadastro fora do padrao correto:",
				"o status da resposta é: " + texto);
	}

	@After(value = "@cadastrosenhaforapadrao")
	public void finalizaPDF(Scenario cenario) throws Exception {

		scen = cenario;
		pdfgenerator.fechaPDF(scen);

	}

}
