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

public class CriarSuscriptionDebitoContaSteps {

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
	private String idTokenAuth = "";
	private String cpfResposta = "";
	protected Integer iddoCusto;


	@Before(value = "@postCriarSubscriptionDebitoConta")
	public void before(Scenario cenario) throws Exception {
		pdfgenerator.iniciaPDF(cenario);

	}


	@Given("^que a request com um usuario \"([^\"]*)\" e gero token em \"([^\"]*)\" para poder criar um \"([^\"]*)\" com os campos \"([^\"]*)\" e \"([^\"]*)\" e \"([^\"]*)\"$")
	public void que_a_request_com_um_usuario_e_gero_token_em_para_poder_criar_um_com_os_campos_e_e(String endPointCadastroUsuario, String endPointLogin, String endPointCustomer, String phone,
			String additionaldetails, String notes) throws Throwable {
		createUser.setCpfCnpj(cpf.mostraResultado());
		createUser.setEmail(email.geraEmail());
		createUser.setFirstName(firstname);
		createUser.setLastName(lastname);
		createUser.setPassword("Yeshu@18");
		createUser.setPhone(telefone.geraTelefone());
		String json = gson.toJson(createUser);
		resposta = verbos.postEndPoint(yaml.getAtributo(endPointCadastroUsuario).toString(), json);

		// Salvar Email, Senha, e Id
		String emailCriado = resposta.salvarObjetosBody("email");
		String idResposta = resposta.salvarObjetosBody("id");
		Integer idLogin = Integer.valueOf(idResposta);
		cpfResposta = resposta.salvarObjetosBody("idDocument");

		// Login
		login.setUsername(emailCriado);
		login.setPassword("Yeshu@18");
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

		pdfgenerator.conteudoPDF("que a request com um usuario e gero token em para poder criar um com os campos:",resposta.logarEvidencia());
	}
	
	@When("^criar uma subscription do debito com os dados necessarios para poder chamar o endpoint \"([^\"]*)\" com  \"([^\"]*)\" e \"([^\"]*)\" e \"([^\"]*)\" e \"([^\"]*)\"$")
	public void criar_uma_subscription_do_debito_com_os_dados_necessarios_para_poder_chamar_o_endpoint_com_e_e_e(String endPointpostCriarSubscription, String bankbranch, String bankaccount, String paymethodcode, String paycompcode) throws Throwable {
		subscription.setCustomerId(iddoCusto);
		subscription.setPaymentMethodCode(paymethodcode);
		paymentprofile.setHolderName(firstname);
		paymentprofile.setRegistryCode(cpfResposta);
		paymentprofile.setBankBranch(bankbranch);
		paymentprofile.setBankAccount(bankaccount);
		paymentprofile.setPaymentMethodCode(paymethodcode);
		paymentprofile.setCustomerId(iddoCusto);
		paymentprofile.setPaymentCompanyCode(paycompcode);
		subscription.setPaymentProfile(paymentprofile);
		subscription.setPlanId(16861);
		String jsonSubscription = gson.toJson(subscription);

		resposta = verbos.postEndpointWithAuthorization(yaml.getAtributo(endPointpostCriarSubscription).toString(), idTokenAuth,jsonSubscription);

		pdfgenerator.conteudoPDF("criar uma subscription do debito com os dados necessarios para poder chamar o endpoint:",resposta.logarEvidencia());
	   
	}

	@Then("^o serviço vai me retornar o codigo (\\d+)$")
	public void o_serviço_vai_me_retornar_o_codigo(int statusCode) throws Throwable {
		resposta.getResposta().statusCode(statusCode);
		String texto = Integer.toString(resposta.getResponse().getStatusCode());
		pdfgenerator.conteudoPDF("o serviço vai me retornar o codigo: ","o status da resposta é: " + texto);
	}

	@After(value = "@postCriarSubscriptionDebitoConta")
	public void finalizaPDF(Scenario cenario) throws Exception {

		scen = cenario;
		pdfgenerator.fechaPDF(scen);
	}

}
