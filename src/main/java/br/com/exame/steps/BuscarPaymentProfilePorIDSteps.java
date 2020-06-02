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

public class BuscarPaymentProfilePorIDSteps {

	public BuscarPaymentProfilePorIDSteps() {

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
	Phones phones = new Phones();
	List<Phones> phonesList = new ArrayList<Phones>();
	Faker faker = new Faker();
	Scenario scen;
	Resposta resposta;
	Subscription subscription = new Subscription();
	PaymentProfiles paymentprofiles = new PaymentProfiles();

	protected Integer idCustomer;
	protected String idUserCriado = null;
	protected String emailUsuarioCriado = null;
	protected String idSubscription = null;
	protected Integer iddoCusto = 0;
	protected String cpfResposta = "";
	private String firstname = faker.name().firstName();
	private String lastname = faker.name().lastName();

	@Before(value = "@getPaymentProfilePorId")
	public void before(Scenario cenario) throws Exception {
		pdfgenerator.iniciaPDF(cenario);
	}

	@Given("^tendo o idToken obtido na chamada do endpoint \"([^\"]*)\" e tambem \"([^\"]*)\" e \"([^\"]*)\" com os campos \"([^\"]*)\" e \"([^\"]*)\" e \"([^\"]*)\" e \"([^\"]*)\" e \"([^\"]*)\" e \"([^\"]*)\" e \"([^\"]*)\" e \"([^\"]*)\" e \"([^\"]*)\"$")
	public void tendo_o_idToken_obtido_na_chamada_do_endpoint_e_tambem_e_com_os_campos_e_e_e_e_e_e_e_e(
			String endpointLogin, String endpointCadastro, String endpointCustomer, String phone, String additionaldts,
			String notes, String phonetype, String cvv, String cexp, String cardnumber, String paycompcode,
			String paymethod) throws Throwable {
		// Criar Usuario
		createUser.setCpfCnpj(cpf.mostraResultado());
		createUser.setEmail(email.geraEmail());
		createUser.setFirstName(firstname);
		createUser.setLastName(lastname);
		createUser.setPassword("Yeshua@18");
		createUser.setPhone(telefone.geraTelefone());
		String json = gson.toJson(createUser);
		resposta = verbos.postEndPoint(yaml.getAtributo(endpointCadastro).toString(), json);

		// Salvar Email, Senha, e Id
		String emailCriado = resposta.salvarObjetosBody("email");
		String idResposta = resposta.salvarObjetosBody("id");
		Integer idLogin = Integer.valueOf(idResposta);
		cpfResposta = resposta.salvarObjetosBody("idDocument");

		// Login
		login.setUsername(emailCriado);
		login.setPassword("Yeshua@18");
		String jsonLogin = gson.toJson(login);
		resposta = autenticacao.login(yaml.getAtributo(endpointLogin).toString(), jsonLogin);

		// Salvar Token
		String idTokenAuth = resposta.salvarToken("idToken");

		// Customer Representation
		address.setAdditionalDetails(additionaldts);
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
		phones.setPhoneType(phonetype);
		phonesList.add(phones);
		customer.setAddress(address);
		customer.setRegistryCode(cpfResposta);
		customer.setUserId(idLogin);
		customer.setPhones(phonesList);
		String jsonCustomer = gson.toJson(customer);

		resposta = verbos.postEndpointWithAuthorization(yaml.getAtributo("customer").toString(), idTokenAuth,jsonCustomer);
		String idData = resposta.salvarObjetosBody("data.id");
		iddoCusto = Integer.valueOf(idData);

		pdfgenerator.conteudoPDF("Dado que tenho o idToken obtido na chamada do endpoint", resposta.logarEvidencia());

		String urlEndpoint = yaml.getAtributo("postPaymentProfiles").toString();
		paymentprofiles.setCardCvv(cvv);
		paymentprofiles.setCardExpiration(cexp);
		paymentprofiles.setCardNumber(cardnumber);
		paymentprofiles.setCustomerId(iddoCusto);
		paymentprofiles.setHolderName(firstname);
		paymentprofiles.setPaymentCompanyCode(paycompcode);
		paymentprofiles.setPaymentMethodCode(paymethod);
		paymentprofiles.setRegistryCode(cpfResposta);

		String jsonPayment = gson.toJson(paymentprofiles).toString();
		resposta = verbos.postEndpointWithAuthorization(urlEndpoint, idTokenAuth, jsonPayment);
		
		String idDataPaymentProfile = resposta.salvarObjetosBody("data.id");
		String urlEndpointGetPaymentProfile = yaml.getAtributo("getPaymentProfilePorId").toString()+ idDataPaymentProfile;
		resposta = verbos.getEndPointWithAuthorization2(urlEndpointGetPaymentProfile, idTokenAuth);
		pdfgenerator.conteudoPDF("Quando chamar o endpoint:", resposta.logarEvidencia());
	}

	@Then("^API retornar todos os perfis de Pagamentos com codigo (\\d+)$")
	public void api_retornar_todos_os_perfis_de_Pagamentos_com_codigo(int statusCode) throws Throwable {
		resposta.getResposta().statusCode(statusCode);
		String texto = Integer.toString(resposta.getResponse().getStatusCode());
		pdfgenerator.conteudoPDF("api_retornar_todos_os_perfis_de_Pagamentos_com_status_code:","o status da resposta é: " + texto);

	}

	@After(value = "@getPaymentProfilePorId")
	public void finalizaPDF(Scenario cenario) throws Exception {

		scen = cenario;
		pdfgenerator.fechaPDF(scen);

	}

}
