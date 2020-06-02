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
import br.com.exame.utils.DateUtil;
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

public class CriarCustomerSubscriptionSteps {
	
	protected Integer idCustomer;
	protected String idUserCriado = null;
	protected String idTokenAuth = null;
	protected String emailUsuarioCriado = null;
	protected String idSubscription = null;
	protected Integer iddoCusto = 0;
	protected String cpfResposta = "";

	Gson gson = new Gson();
	Login login = new Login();
	Authenticacao autenticacao = new Authenticacao();
	GeradorEmail email = new GeradorEmail();
	GeradorTelefone telefone = new GeradorTelefone();
	DateUtil data = new DateUtil();
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
	
	@Before(value = "@postCriarSubscriptionCortesia")
	public void before(Scenario cenario) throws Exception {
		pdfgenerator.iniciaPDF(cenario);
	}
	
	@Given("^que a request com um usuario \"([^\"]*)\" e gero token em \"([^\"]*)\" para poder criar um \"([^\"]*)\" com os campos \"([^\"]*)\" e \"([^\"]*)\" e \"([^\"]*)\" com cortesia$")
	public void que_a_request_com_um_usuario_e_gero_token_em_para_poder_criar_um_com_os_campos_e_e_com_cortesia(String endPointUsuario, String endPointLogin, String endPointCustomer, String phone, String additionaldetails, String notes) throws Throwable {
		createUser.setCpfCnpj(cpf.mostraResultado());
		createUser.setEmail(email.geraEmail());
		createUser.setFirstName(firstname);
		createUser.setLastName(lastname);
		createUser.setPassword("Yeshu@18");
		createUser.setPhone(telefone.geraTelefone());
		String json = gson.toJson(createUser);
		resposta = verbos.postEndPoint(yaml.getAtributo(endPointUsuario).toString(), json);

		// Salvar Email, Senha, e Id
		String emailCriado = resposta.salvarObjetosBody("email");
		String idResposta = resposta.salvarObjetosBody("id");
		Integer idLogin = Integer.valueOf(idResposta);
		cpfResposta = resposta.salvarObjetosBody("idDocument");

		// Login
		login.setUsername("felipe.santos@brlink.com.br");
		login.setPassword("Exame@!12");
		String jsonLogin = gson.toJson(login);
		resposta = autenticacao.login(yaml.getAtributo(endPointLogin).toString(), jsonLogin);

		// Salvar Token
		idTokenAuth = resposta.salvarToken("idToken");

		// Customer Representation
		address.setAdditionalDetails(additionaldetails);
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
		phones.setNumber(phone);
		phones.setPhoneType("mobile");
		phonesList.add(phones);
		customer.setAddress(address);
		customer.setRegistryCode(cpfResposta);
		customer.setUserId(idLogin);
		customer.setPhones(phonesList);
		String jsonCustomer = gson.toJson(customer);

		resposta = verbos.postEndpointWithAuthorization(yaml.getAtributo(endPointCustomer).toString(), idTokenAuth,jsonCustomer);
		String idData = resposta.salvarObjetosBody("data.id");
		iddoCusto = Integer.valueOf(idData);

		pdfgenerator.conteudoPDF("que a request com um usuario e gero token em para poder criar um com os campos e e com cortesia:",resposta.logarEvidencia());
	}

	@When("^criar uma subscription do cortesia com os dados necessarios para poder chamar o endpoint \"([^\"]*)\" com  \"([^\"]*)\"$")
	public void criar_uma_subscription_do_cortesia_com_os_dados_necessarios_para_poder_chamar_o_endpoint_com(String endPointCriarSubscription, String paymethodcode) throws Throwable {
		subscription.setCustomerId(iddoCusto);
		subscription.setPaymentMethodCode(paymethodcode);
		subscription.setEndAt(data.geraData());
		subscription.setPlanId(16861);
		String jsonSubscription = gson.toJson(subscription);

		resposta = verbos.postEndpointWithAuthorization(yaml.getAtributo(endPointCriarSubscription).toString(), idTokenAuth,jsonSubscription);

		pdfgenerator.conteudoPDF("criar uma subscription do cortesia com os dados necessarios para poder chamar o endpoint subscription e paymethodcode:",resposta.logarEvidencia());
	}

	@Then("^o serviço vai me retornar o codigo (\\d+) criando o cortesia$")
	public void o_serviço_vai_me_retornar_o_codigo_criando_o_cortesia(int statusCode) throws Throwable {
		resposta.getResposta().statusCode(statusCode);
		String texto = Integer.toString(resposta.getResponse().getStatusCode());
		pdfgenerator.conteudoPDF("o servico vai me retornar o codigo criando o cortesia: ","o status da resposta é: " + texto);
	}
	
	@After(value = "@postCriarSubscriptionCortesia")
	public void finalizaPDF(Scenario cenario) throws Exception {

		scen = cenario;
		pdfgenerator.fechaPDF(scen);

	}

}
