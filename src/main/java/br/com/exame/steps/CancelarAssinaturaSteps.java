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

public class CancelarAssinaturaSteps {

	public CancelarAssinaturaSteps() {
		
	}

	Gson gson = new Gson();
	Login login = new Login();
	Authenticacao autenticacao = new Authenticacao();
	CreateUser createUser = new CreateUser();
	YamlHelper yaml = new YamlHelper();
	PDFGenerator pdfgenerator = new PDFGenerator();
	GeradorTelefone telefone = new GeradorTelefone();
	GeradorEmail email = new GeradorEmail();
	Servicos verbos = new Servicos();
	Address address = new Address();
	Customer customer = new Customer();
	GeradorCPF cpf = new GeradorCPF();
	Phones phones = new Phones();
	PaymentProfiles paymentprofile = new PaymentProfiles();
	List<Phones> phonesList = new ArrayList<Phones>();
	Faker faker = new Faker();
	Scenario scen;
	Resposta resposta;
	Subscription subscription = new Subscription();
	PaymentProfiles paymentprofiles = new PaymentProfiles();
	private String firstname = faker.name().firstName();
	private String lastname = faker.name().lastName();

	@Before(value = "@postCancelarSubscription")
	public void before(Scenario cenario) throws Exception {
		pdfgenerator.iniciaPDF(cenario);
	}
	
	@Given("^criar um user \"([^\"]*)\" gerar um idToken em \"([^\"]*)\" para ser possivel ter um \"([^\"]*)\" para chamar o endpoint \"([^\"]*)\" para depois validar o cancelamento \"([^\"]*)\" com os campos \"([^\"]*)\" e \"([^\"]*)\" e \"([^\"]*)\" e \"([^\"]*)\" e \"([^\"]*)\"$")
	public void criar_um_user_gerar_um_idToken_em_para_ser_possivel_ter_um_para_chamar_o_endpoint_para_depois_validar_o_cancelamento_com_os_campos_e_e_e_e(String endpointCriarUser, String endpointFazerLogin, String endpointCriarCustomer,
			String postCriarSubscription, String postCancelarSubscription, String phone, String additionaldetails, String cvv, String cardexp, String cardnum) throws Throwable {
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
		String cadastroUnicoCPF = resposta.salvarObjetosBody("idDocument");

		// Login
		login.setUsername(emailCriado);
		login.setPassword("Yeshu@18");
		String jsonLogin = gson.toJson(login);
		resposta = autenticacao.login(yaml.getAtributo(endpointFazerLogin).toString(), jsonLogin);

		// Salvar Token
		String tokenSalvo = resposta.salvarToken("idToken");

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
		customer.setNotes(additionaldetails);
		phones.setNumber("+5511952178031");
		phones.setPhoneType("mobile");
		phonesList.add(phones);
		customer.setAddress(address);
		customer.setRegistryCode(cadastroUnicoCPF);
		customer.setUserId(idLogin);
		customer.setPhones(phonesList);
		String jsonCustomer = gson.toJson(customer);

		resposta = verbos.postEndpointWithAuthorization(yaml.getAtributo(endpointCriarCustomer).toString(), tokenSalvo,
				jsonCustomer);
		String idData = resposta.salvarObjetosBody("data.id");
		Integer customerIdSalvoId = Integer.valueOf(idData);

		System.out.println(customerIdSalvoId);

		subscription.setPaymentMethodCode("credit_card");
		subscription.setCustomerId(customerIdSalvoId);
		paymentprofile.setCardCvv(cvv);
		paymentprofile.setCardExpiration(cardexp);
		paymentprofile.setCardNumber(cardnum);
		paymentprofile.setHolderName(firstname);
		paymentprofile.setPaymentCompanyCode("mastercard");
		paymentprofile.setRegistryCode(cadastroUnicoCPF);

		subscription.setPaymentProfile(paymentprofile);

		System.out.println(customerIdSalvoId + resposta.logarEvidencia());
		subscription.setPlanId(16861);
		String jsonSubscription = gson.toJson(subscription);

		resposta = verbos.postEndpointWithAuthorization(yaml.getAtributo(postCriarSubscription).toString(), tokenSalvo,jsonSubscription);

		String idDataAssinatura = resposta.salvarObjetosBody("data.id");

		resposta = verbos.postEndpointWithAuthorization(yaml.getAtributo(postCancelarSubscription).toString() + idDataAssinatura + "/cancel", tokenSalvo, "");

		pdfgenerator.conteudoPDF("gerar_uma_subscription_com_apontando_para_o_endpoint_para_depois_validar_o_cancelamento_em:",resposta.logarEvidencia());
	}

	@Given("^criar um user \"([^\"]*)\" gerar um idToken em \"([^\"]*)\" para ser possivel ter um \"([^\"]*)\" para chamar o endpoint \"([^\"]*)\" para depois validar o cancelamento \"([^\"]*)\"$")
	public void criar_um_user_gerar_um_idToken_em_para_ser_possivel_ter_um_para_chamar_o_endpoint_para_depois_validar_o_cancelamento(
			String endpointCriarUser, String endpointFazerLogin, String endpointCriarCustomer,
			String postCriarSubscription, String postCancelarSubscription) throws Throwable {
		

	}

	@Then("^o servico deve retornar codigo (\\d+) cancelando a Subscription$")
	public void o_servico_deve_retornar_codigo_cancelando_a_Subscription(int statusCode) throws Throwable {
		resposta.getResposta().statusCode(statusCode);
		String texto = Integer.toString(resposta.getResponse().getStatusCode());
		pdfgenerator.conteudoPDF("o_servico_deve_retornar_codigo_cancelando_a_Subscription:","o status da resposta é: " + texto);

	}

	@After(value = "@postCancelarSubscription")
	public void finalizaPDF(Scenario cenario) throws Exception {

		scen = cenario;
		pdfgenerator.fechaPDF(scen);

	}

}
