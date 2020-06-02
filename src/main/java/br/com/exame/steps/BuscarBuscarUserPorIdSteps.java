package br.com.exame.steps;

import org.junit.Assert;

import com.github.javafaker.Faker;
import com.google.gson.Gson;

import br.com.exame.core.PDFGenerator;
import br.com.exame.pojos.CreateUser;
import br.com.exame.pojos.Login;
import br.com.exame.servicos.Authenticacao;
import br.com.exame.servicos.Resposta;
import br.com.exame.servicos.Servicos;
import br.com.exame.utils.GeradorCPF;
import br.com.exame.utils.GeradorEmail;
import br.com.exame.utils.GeradorTelefone;
import br.com.exame.utils.YamlHelper;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class BuscarBuscarUserPorIdSteps {

	Gson gson = new Gson();
	PDFGenerator pdfgenerator = new PDFGenerator();
	Authenticacao autenticacao = new Authenticacao();
	CreateUser createUser = new CreateUser();
	Login login = new Login();
	YamlHelper yaml = new YamlHelper();
	GeradorEmail email = new GeradorEmail();
	GeradorTelefone telefone = new GeradorTelefone();
	GeradorCPF cpf = new GeradorCPF();
	Faker faker = new Faker();
	Servicos verbos = new Servicos();
	Resposta resposta;
	Scenario scen;
	Integer id;

	@Before(value = "@buscaruserinfoporid")
	public void before(Scenario cenario) throws Exception {
		pdfgenerator.iniciaPDF(cenario);
	}

	@Given("^realizo o \"([^\"]*)\" de usuario e \"([^\"]*)\" visualizando informacoes do \"([^\"]*)\" por Id com \"([^\"]*)\" e \"([^\"]*)\"$")
	public void realizo_o_de_usuario_e_visualizando_informacoes_do_por_Id_com_e(String endPointUserCadastro,
			String endPointLogin, String endPointUserInfo, String firstname, String lastname) throws Throwable {

		// Criar Usuario
		createUser.setCpfCnpj(cpf.mostraResultado());
		createUser.setEmail(email.geraEmail());
		createUser.setFirstName(firstname);
		createUser.setLastName(lastname);
		createUser.setPassword("Yeshu@18");
		createUser.setPhone(telefone.geraTelefone());
		String json = gson.toJson(createUser);
		resposta = verbos.postEndPoint(yaml.getAtributo(endPointUserCadastro).toString(), json);

		// Salvar Email e Id
		String email = resposta.salvarObjetosBody("email");
		String idResposta = resposta.salvarObjetosBody("id");
		id = Integer.valueOf(idResposta);

		// Login
		login.setUsername(email);
		login.setPassword("Yeshu@18");
		String jsonLogin = gson.toJson(login);
		resposta = autenticacao.login(yaml.getAtributo(endPointLogin).toString(), jsonLogin);

		// Salvar Token
		String idToken = resposta.salvarToken("idToken");

		resposta = verbos.getEndPointWithAuthorization2(yaml.getAtributo(endPointUserInfo).toString() + id, idToken);
		pdfgenerator.conteudoPDF("realizo o cadastro de usuario e login visualizando informacoes do userinfo por Id",
				resposta.logarEvidencia());
	}

	@Then("^a API me retorna (\\d+) para a buscar das UserInfos por Id$")
	public void a_API_me_retorna_para_a_buscar_das_UserInfos_por_Id(int statusCode) throws Throwable {
		String idRespostaUserInfo = resposta.salvarObjetosBody("id");
		Integer idUserInfo = Integer.valueOf(idRespostaUserInfo);

		Assert.assertEquals(id, idUserInfo);

		resposta.getResposta().statusCode(statusCode);

		String texto = Integer.toString(resposta.getResponse().getStatusCode());
		pdfgenerator.conteudoPDF("a API me retorna a API me retorna:", "o status da resposta é: " + texto);
	}

	@After(value = "@buscaruserinfoporid")
	public void finalizaPDF(Scenario cenario) throws Exception {
		scen = cenario;
		pdfgenerator.fechaPDF(scen);
	}

}
