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

public class BuscarSubscriptionPorIDSteps {

	public BuscarSubscriptionPorIDSteps() {

	}

	Gson gson = new Gson();
	Login login = new Login();
	Authenticacao autenticacao = new Authenticacao();
	GeradorEmail email = new GeradorEmail();
	CreateUser createUser = new CreateUser();
	YamlHelper yaml = new YamlHelper();
	PDFGenerator pdfgenerator = new PDFGenerator();
	Servicos verbos = new Servicos();
	Address address = new Address();
	PaymentProfiles paymentprofile = new PaymentProfiles();
	Customer customer = new Customer();
	GeradorCPF cpf = new GeradorCPF();
	GeradorTelefone telefone = new GeradorTelefone();
	Phones phones = new Phones();
	List<Phones> phonesList = new ArrayList<Phones>();
	Faker faker = new Faker();
	Scenario scen;
	Resposta resposta;
	Subscription subscription = new Subscription();
	PaymentProfiles paymentprofiles = new PaymentProfiles();
	private String firstname = faker.name().firstName();
	private String lastname = faker.name().lastName();
	protected static Integer idCustomer;
	protected String idUserCriado = null;
	protected String emailUsuarioCriado = null;
	protected static String idTokenAuth = "";
	protected String idSubscription = null;
	protected Integer iddoCusto = 0;
	protected String cpfResposta = "";

	@Before(value = "@getSubscriptionsID")
	public void before(Scenario cenario) throws Exception {
		pdfgenerator.iniciaPDF(cenario);

	}

	@Given("^faco uma chamada em \"([^\"]*)\" apos chamo o \"([^\"]*)\" para poder criar um \"([^\"]*)\" com os campos \"([^\"]*)\" e \"([^\"]*)\" e \"([^\"]*)\"$")
	public void faco_uma_chamada_em_apos_chamo_o_para_poder_criar_um_com_os_campos_e_e(String endpointCadastroUsuario,
			String endpointFazerLogin, String endpointCriarCustomer, String phone, String additionaldetails,
			String notes) throws Throwable {
		createUser.setCpfCnpj(cpf.mostraResultado());
		createUser.setEmail(email.geraEmail());
		createUser.setFirstName(firstname);
		createUser.setLastName(lastname);
		createUser.setPassword("Yeshu@18");
		createUser.setPhone(telefone.geraTelefone());
		String json = gson.toJson(createUser);
		resposta = verbos.postEndPoint(yaml.getAtributo(endpointCadastroUsuario).toString(), json);

		// Salvar Email, Senha, e Id
		String emailCriado = resposta.salvarObjetosBody("email");
		String idResposta = resposta.salvarObjetosBody("id");
		Integer idLogin = Integer.valueOf(idResposta);
		cpfResposta = resposta.salvarObjetosBody("idDocument");

		// Login
		login.setUsername(emailCriado);
		login.setPassword("Yeshu@18");
		String jsonLogin = gson.toJson(login);
		resposta = autenticacao.login(yaml.getAtributo(endpointFazerLogin).toString(), jsonLogin);

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

		resposta = verbos.postEndpointWithAuthorization(yaml.getAtributo(endpointCriarCustomer).toString(), idTokenAuth,jsonCustomer);
		String idData = resposta.salvarObjetosBody("data.id");
		iddoCusto = Integer.valueOf(idData);

		pdfgenerator.conteudoPDF("que_crio_um_usuario_para_gerar_um_idToken_fazendo_para_poder_criar_um_customer:",resposta.logarEvidencia());
	}

	@When("^criar uma subscription com os dados necessarios para poder chamar o endpoint \"([^\"]*)\"  request no \"([^\"]*)\" com  \"([^\"]*)\" e \"([^\"]*)\" e \"([^\"]*)\"$")
	public void criar_uma_subscription_com_os_dados_necessarios_para_poder_chamar_o_endpoint_request_no_com_e_e(
			String postCriarSubscription, String getSubscriptionsID, String cvv, String cardexp, String cardnumber)
			throws Throwable {

		subscription.setPaymentMethodCode("credit_card");
		subscription.setCustomerId(iddoCusto);
		paymentprofile.setCardCvv(cvv);
		paymentprofile.setCardExpiration(cardexp);
		paymentprofile.setCardNumber(cardnumber);
		paymentprofile.setHolderName(firstname);
		paymentprofile.setPaymentCompanyCode("mastercard");
		paymentprofile.setRegistryCode(cpfResposta);
		subscription.setPaymentProfile(paymentprofile);
		subscription.setPlanId(16861);
		String jsonSubscription = gson.toJson(subscription);

		resposta = verbos.postEndpointWithAuthorization(yaml.getAtributo(postCriarSubscription).toString(), idTokenAuth,jsonSubscription);

		String idData = resposta.salvarObjetosBody("data.id");

		resposta = verbos.getEndPointWithAuthorization2(yaml.getAtributo(getSubscriptionsID).toString() + idData,idTokenAuth);

		pdfgenerator.conteudoPDF("criar_uma_subscription_com_os_dados_para_criar_um_PaymentProfile_chamando_o_endpoint_request_no:",resposta.logarEvidencia());
	}

	@Then("^o serviço retorna a subscription exibindo o codigo (\\d+)$")
	public void o_serviço_retorna_a_subscription_exibindo_o_codigo(int statusCode) throws Exception {
		resposta.getResposta().statusCode(statusCode);
		String texto = Integer.toString(resposta.getResponse().getStatusCode());
		pdfgenerator.conteudoPDF("o serviço retorna a subscription exibindo o codigo:","o status da resposta é: " + texto);

	}

	@After(value = "@getSubscriptionsID")
	public void finalizaPDF(Scenario cenario) throws Exception {

		scen = cenario;
		pdfgenerator.fechaPDF(scen);
	}

}
