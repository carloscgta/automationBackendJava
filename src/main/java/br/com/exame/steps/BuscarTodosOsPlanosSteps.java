package br.com.exame.steps;

import com.github.javafaker.Faker;
import com.google.gson.Gson;

import br.com.exame.core.PDFGenerator;
import br.com.exame.pojos.Address;
import br.com.exame.pojos.CreateUser;
import br.com.exame.pojos.Customer;
import br.com.exame.pojos.Login;
import br.com.exame.pojos.PaymentProfiles;
import br.com.exame.pojos.Subscription;
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

public class BuscarTodosOsPlanosSteps {

	public BuscarTodosOsPlanosSteps() {
		// TODO Auto-generated constructor stub
	}

	Gson gson = new Gson();
	Login login = new Login();
	Authenticacao autenticacao = new Authenticacao();
	GeradorEmail email = new GeradorEmail();
	CreateUser createUser = new CreateUser();
	YamlHelper yaml = new YamlHelper();
	PDFGenerator pdfgenerator = new PDFGenerator();
	GeradorTelefone telefone = new GeradorTelefone();
	Servicos verbos = new Servicos();
	Address address = new Address();
	Customer customer = new Customer();
	GeradorCPF cpf = new GeradorCPF();
	Faker faker = new Faker();
	Scenario scen;
	Resposta resposta;
	Subscription subscription = new Subscription();
	PaymentProfiles paymentprofiles = new PaymentProfiles();

	protected Integer customerId;
	protected String idUserCriado;
	protected String emailUsuarioCriado;
	protected String cpfUsuarioCriado;
	protected String idSubscription;
	private String firstname = faker.name().firstName();
	private String lastname = faker.name().lastName();

	@Before(value = "@buscarTodosPlanos")
	public void before(Scenario cenario) throws Exception {
		pdfgenerator.iniciaPDF(cenario);

	}

	@Given("^realizo request no \"([^\"]*)\"  faco \"([^\"]*)\" no servico de autenticacao e chamo o \"([^\"]*)\" com \"([^\"]*)\"$")
	public void realizo_request_no_faco_no_servico_de_autenticacao_e_chamo_o_com(String endpointCriarUser,
			String endpointFazerLogin, String getPlans, String phone) throws Throwable {
		// Criar Usuário
		createUser.setCpfCnpj(cpf.mostraResultado());
		createUser.setEmail(email.geraEmail());
		createUser.setFirstName(firstname);
		createUser.setLastName(lastname);
		createUser.setPassword("Yeshu@18");
		createUser.setPhone(telefone.geraTelefone());
		String json = gson.toJson(createUser);
		resposta = verbos.postEndPoint(yaml.getAtributo(endpointCriarUser).toString(), json);

		// Salvar Email, Senha, e Id
		String emailCriado = resposta.salvarObjetosBody("email");

		// Login
		login.setUsername(emailCriado);
		login.setPassword("Yeshu@18");
		String jsonLogin = gson.toJson(login);
		resposta = autenticacao.login(yaml.getAtributo(endpointFazerLogin).toString(), jsonLogin);

		// Salvar Token
		String idTokenAuthUserLogado = resposta.salvarToken("idToken");

		resposta = verbos.getEndPointWithAuthorization2(yaml.getAtributo(getPlans).toString(), idTokenAuthUserLogado);

		pdfgenerator.conteudoPDF("realizo_request_no_faco_no_servico_de_autenticacao_e_chamo_o:",
				resposta.logarEvidencia());
	}

	@Then("^recebo o codigo (\\d+)$")
	public void recebo_o_codigo(int statusCode) throws Throwable {
		resposta.getResposta().statusCode(statusCode);
		String texto = Integer.toString(resposta.getResponse().getStatusCode());
		pdfgenerator.conteudoPDF("recebo_o_codigo:", "o status da resposta é: " + texto);

	}

	@After(value = "@buscarTodosPlanos")
	public void finalizaPDF(Scenario cenario) throws Exception {

		scen = cenario;
		pdfgenerator.fechaPDF(scen);
	}

}
