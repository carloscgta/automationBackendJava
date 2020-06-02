package br.com.exame.steps;

import java.util.ArrayList;
import java.util.List;

import com.github.javafaker.Faker;
import com.google.gson.Gson;

import br.com.exame.core.PDFGenerator;
import br.com.exame.pojos.Address;
import br.com.exame.pojos.CreateUser;
import br.com.exame.pojos.Customer;
import br.com.exame.pojos.Login;
import br.com.exame.pojos.PaymentProfiles;
import br.com.exame.pojos.Phones;
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
import cucumber.api.java.en.When;

public class CriarPaymentProfileSteps {

	public CriarPaymentProfileSteps() {
	
	}

	protected static Integer idCustomer;
	protected String idUserCriado = null;
	protected String emailUsuarioCriado = null;
	protected static String idTokenAuth = "";
	protected String idSubscription = null;
	protected Integer iddoCusto = 0;
	protected String cpfResposta = "";

	Gson gson = new Gson();
	Login login = new Login();
	GeradorEmail email = new GeradorEmail();
	Authenticacao autenticacao = new Authenticacao();
	CreateUser createUser = new CreateUser();
	GeradorTelefone telefone = new GeradorTelefone();
	YamlHelper yaml = new YamlHelper();
	PDFGenerator pdfgenerator = new PDFGenerator();
	Servicos verbos = new Servicos();
	Address address = new Address();
	PaymentProfiles paymentprofile = new PaymentProfiles();
	Customer customer = new Customer();
	GeradorCPF cpf = new GeradorCPF();
	Phones phones = new Phones();
	List<Phones> phonesList = new ArrayList<Phones>();
	Faker faker = new Faker();
	Scenario scen;
	Resposta resposta;
	Subscription subscription = new Subscription();
	PaymentProfiles paymentprofiles = new PaymentProfiles();

	@Before(value = "@postPaymentProfiles")
	public void before(Scenario cenario) throws Exception {
		pdfgenerator.iniciaPDF(cenario);
	}

	@Given("^considerando que obtenho o idToken chamando o endpoint \"([^\"]*)\"$")
	public void considerando_que_obtenho_o_idToken_chamando_o_endpoint(String loginDEV) throws Throwable {
		// Write code here that turns the phrase above into concrete actions

		// Write code here that turns the phrase above into concrete actions
		createUser.setCpfCnpj(cpf.mostraResultado());
		createUser.setEmail(email.geraEmail());
		createUser.setFirstName("Carlos");
		createUser.setLastName("Almeida");
		createUser.setPassword("Yeshu@18");
		createUser.setPhone(telefone.geraTelefone());
		String json = gson.toJson(createUser);
		resposta = verbos.postEndPoint(yaml.getAtributo("cadastro").toString(), json);

		// Salvar Email, Senha, e Id
		String emailCriado = resposta.salvarObjetosBody("email");
		String idResposta = resposta.salvarObjetosBody("id");
		Integer idLogin = Integer.valueOf(idResposta);
		cpfResposta = resposta.salvarObjetosBody("idDocument");

		// Login
		login.setUsername(emailCriado);
		login.setPassword("Yeshu@18");
		String jsonLogin = gson.toJson(login);
		resposta = autenticacao.login(yaml.getAtributo("login").toString(), jsonLogin);

		// Salvar Token
		idTokenAuth = resposta.salvarToken("idToken");

		// Customer Representation
		address.setAdditionalDetails("dfg");
		address.setCity("São paulo");
		address.setCountry("BR");
		address.setNeighborhood("Pacaembu");
		address.setNumber("4");
		address.setState("SP");
		address.setStreet("Av. Arnal");
		address.setZipcode("034567000");
		customer.setEmail(emailCriado);
		customer.setName("Carlos Almeida");
		customer.setNotes("Teste api criar customer");
		phones.setNumber("5571930405412");
		phones.setPhoneType("mobile");
		phonesList.add(phones);
		customer.setAddress(address);
		customer.setRegistryCode(cpfResposta);
		customer.setUserId(idLogin);
		customer.setPhones(phonesList);
		String jsonCustomer = gson.toJson(customer);

		resposta = verbos.postEndpointWithAuthorization(yaml.getAtributo("customer").toString(), idTokenAuth,
				jsonCustomer);
		String idData = resposta.salvarObjetosBody("data.id");
		iddoCusto = Integer.valueOf(idData);
		pdfgenerator.conteudoPDF("que_crio_um_usuario_para_gerar_um_idToken_fazendo_para_poder_criar_um_customer:",
				resposta.logarEvidencia());

	}

	@When("^usar as seguintes info na chamada (\\d+) e (\\d+) e (\\d+)/(\\d+) e (\\d+)  e (\\d+) e Carlos Almeida e mastercard e credit_card  e \"([^\"]*)\"  e chamar \"([^\"]*)\" para criar um PaymentProfile$")
	public void usar_as_seguintes_info_na_chamada_e_e_e_e_e_Carlos_Almeida_e_mastercard_e_credit_card_e_e_chamar_para_criar_um_PaymentProfile(
			int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, String arg7, String arg8) throws Throwable {

		String urlEndpoint = yaml.getAtributo("postPaymentProfiles").toString();

		paymentprofiles.setCardCvv("344");
		paymentprofiles.setCardExpiration("10/2024");
		paymentprofiles.setCardNumber("5555555555555557");
		paymentprofiles.setHolderName("Carlos T Gabrilu");
		paymentprofiles.setPaymentCompanyCode("mastercard");
		paymentprofiles.setPaymentMethodCode("credit_card");
		paymentprofiles.setRegistryCode(cpfResposta);
		paymentprofiles.setCustomerId(iddoCusto);

		String json = gson.toJson(paymentprofiles).toString();
		resposta = verbos.postEndpointWithAuthorization(urlEndpoint, idTokenAuth, json);
		pdfgenerator.conteudoPDF("usar_as_seguintes_info_definidas_logo_apos_chamar_para_criar_um_PaymentProfile:",
				resposta.logarEvidencia());

	}

	@Then("^API retornar (\\d+) created exibindo os dados do Profile criado$")
	public void api_retornar_created_exibindo_os_dados_do_Profile_criado(int statusCode) throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		resposta.getResposta().statusCode(statusCode);
		String texto = Integer.toString(resposta.getResponse().getStatusCode());
		pdfgenerator.conteudoPDF("Entao a API me retorna o status code:", "o status da resposta é: " + texto);

	}

	@After(value = "@postPaymentProfiles")
	public void finalizaPDF(Scenario cenario) throws Exception {

		scen = cenario;
		pdfgenerator.fechaPDF(scen);

	}

}
