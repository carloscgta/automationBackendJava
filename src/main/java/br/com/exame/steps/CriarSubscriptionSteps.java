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

public class CriarSubscriptionSteps {

	public CriarSubscriptionSteps() {

	}

	protected static Integer idCustomer;
	protected String idTokenAuth = null;
	protected String idUserCriado = null;
	protected String emailUsuarioCriado = null;
	protected String idSubscription = null;
	protected Integer iddoCusto = 0;
	protected String cpfResposta = "";

	Gson gson = new Gson();
	Login login = new Login();
	Authenticacao autenticacao = new Authenticacao();
	GeradorEmail email = new GeradorEmail();
	GeradorTelefone telefone = new GeradorTelefone();
	CreateUser createUser = new CreateUser();
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
	private String firstname = faker.name().firstName();
	private String lastname = faker.name().lastName();

	@Before(value = "@postCriarSubscription")
	public void before(Scenario cenario) throws Exception {
		pdfgenerator.iniciaPDF(cenario);
	}
	
	
		@Given("^que crio um usuario \"([^\"]*)\" para gerar um idToken fazendo \"([^\"]*)\" para poder criar um \"([^\"]*)\" quando preencher os dados da request no endpoint \"([^\"]*)\"  com \"([^\"]*)\" e \"([^\"]*)\" e \"([^\"]*)\" e \"([^\"]*)\" e \"([^\"]*)\"e  \"([^\"]*)\" para criar assinatura$")
	public void que_crio_um_usuario_para_gerar_um_idToken_fazendo_para_poder_criar_um_quando_preencher_os_dados_da_request_no_endpoint_com_e_e_e_e_e_para_criar_assinatura(String endpointCriarUser, String endpointFazerLogin, String endpointCriarCustomer,String postCriarSubscription, String additional, String notes, String  cvv, String companycode, String cardexp, String cardnumber) throws Throwable {
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
		String idResposta = resposta.salvarObjetosBody("id");
		Integer idLogin = Integer.valueOf(idResposta);
		cpfResposta = resposta.salvarObjetosBody("idDocument");

		// Login
		login.setUsername(emailCriado);
		login.setPassword("Yeshu@18");
		String jsonLogin = gson.toJson(login);
		resposta =  autenticacao.login(yaml.getAtributo(endpointFazerLogin).toString(), jsonLogin);

		// Salvar Token
		String idTokenAuth = resposta.salvarToken("idToken");

		// Customer Representation
		address.setAdditionalDetails(additional);
		address.setCity("São paulo");
		address.setCountry("BR");
		address.setNeighborhood("Pacaembu");
		address.setNumber("4");
		address.setState("SP");
		address.setStreet("Av. Arnal");
		address.setZipcode("034567000");
		customer.setEmail(emailCriado);
		customer.setName(firstname);
		customer.setNotes(notes);
		phones.setNumber("5511952178031");
		phones.setPhoneType("mobile");
		phonesList.add(phones);
		customer.setAddress(address);
		customer.setRegistryCode(cpfResposta);
		customer.setUserId(idLogin);
		customer.setPhones(phonesList);
		String jsonCustomer = gson.toJson(customer);

		resposta = verbos.postEndpointWithAuthorization(yaml.getAtributo(endpointCriarCustomer).toString(), idTokenAuth,jsonCustomer);

		// Salvar o id do Customer
		String idData = resposta.salvarObjetosBody("data.id");
		iddoCusto = Integer.valueOf(idData);

		subscription.setPaymentMethodCode("credit_card");
		subscription.setCustomerId(iddoCusto);
		paymentprofile.setCardCvv(cvv);
		paymentprofile.setCardExpiration(cardexp);
		paymentprofile.setCardNumber(cardnumber);
		paymentprofile.setHolderName(firstname);
		paymentprofile.setPaymentCompanyCode(companycode);
		paymentprofile.setRegistryCode(cpfResposta);
		subscription.setPaymentProfile(paymentprofile);
		subscription.setPlanId(16861);
		String jsonSubscription = gson.toJson(subscription);

		resposta = verbos.postEndpointWithAuthorization(yaml.getAtributo(postCriarSubscription).toString(), idTokenAuth,jsonSubscription);
	
	}

	@Then("^a api retorno codigo (\\d+) apos criar a subscription$")
	public void a_api_retorno_codigo_apos_criar_a_subscription(int statusCode) throws Throwable {
		resposta.getResposta().statusCode(statusCode);
		String texto = Integer.toString(resposta.getResponse().getStatusCode());
		pdfgenerator.conteudoPDF("a API me retorna o status para a alteracao do cliente:","o status da resposta é: " + texto);
	}

	@After(value = "@postCriarSubscription")
	public void finalizaPDF(Scenario cenario) throws Exception {

		scen = cenario;
		pdfgenerator.fechaPDF(scen);

	}

}
