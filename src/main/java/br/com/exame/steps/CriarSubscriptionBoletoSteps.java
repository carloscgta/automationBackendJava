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

public class CriarSubscriptionBoletoSteps {

	public CriarSubscriptionBoletoSteps() {
		// TODO Auto-generated constructor stub
	}

	protected Integer idCustomer;
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

	@Before(value = "@postCriarSubscriptionBoleto")
	public void before(Scenario cenario) throws Exception {
		pdfgenerator.iniciaPDF(cenario);
	}

	@Given("^que request um usuario \"([^\"]*)\" e gero token em \"([^\"]*)\" apos isso crio um \"([^\"]*)\" e chamo \"([^\"]*)\"  usando os campos \"([^\"]*)\"$")
	public void que_request_um_usuario_e_gero_token_em_apos_isso_crio_um_e_chamo_usando_os_campos(String endPointUsuario,
			String endPointLogin, String endPointCadastro, String postCriarSubscription, String paycompcode) throws Throwable {

		// Criar Usuario
		createUser.setCpfCnpj(cpf.mostraResultado());
		String emailCriado = email.geraEmail();
		createUser.setEmail(emailCriado);
		createUser.setFirstName(firstname);
		createUser.setLastName(lastname);
		createUser.setPassword("Yeshu@18");
		createUser.setPhone(telefone.geraTelefone());
		String json = gson.toJson(createUser);
		resposta = verbos.postEndPoint(yaml.getAtributo(endPointUsuario).toString(), json);

		// Salvar Email, Senha, e Id
		String email = resposta.salvarObjetosBody("email");
		String idResposta = resposta.salvarObjetosBody("id");
		Integer id = Integer.valueOf(idResposta);
		String cpfResposta = resposta.salvarObjetosBody("idDocument");

		// Login
		// Para criar assinante cortesia, deve usar o login do admin

		login.setUsername("felipe.santos@brlink.com.br");
		login.setPassword("Exame@!12");
		String jsonLogin = gson.toJson(login);
		resposta = autenticacao.login(yaml.getAtributo(endPointLogin).toString(), jsonLogin);

		// Salvar Token
		String idToken = resposta.salvarToken("idToken");

		// Customer Representation
		address.setAdditionalDetails("Teste");
		address.setCity("São paulo");
		address.setCountry("BR");
		address.setNeighborhood("Pacaembu");
		address.setNumber("4");
		address.setState("SP");
		address.setStreet("Av. Arnal");
		address.setZipcode("034567000");
		customer.setEmail(emailCriado);
		customer.setName(firstname);
		customer.setNotes("Criar Customer para Criar metodo de pagamento boleto");
		phones.setNumber("5511952178031");
		phones.setPhoneType("mobile");
		phonesList.add(phones);
		customer.setAddress(address);
		customer.setRegistryCode(cpfResposta);
		customer.setUserId(id);
		customer.setPhones(phonesList);
		String jsonCustomer = gson.toJson(customer);

		resposta = verbos.postEndpointWithAuthorization(yaml.getAtributo(endPointCadastro).toString(), idToken,
				jsonCustomer);

		// Salvar o id do Customer
		String idData = resposta.salvarObjetosBody("data.id");
		iddoCusto = Integer.valueOf(idData);

		subscription.setPaymentMethodCode(paycompcode);
		subscription.setCustomerId(iddoCusto);
		subscription.setPlanId(16862);

		String jsonSubscription = gson.toJson(subscription);

		resposta = verbos.postEndpointWithAuthorization(yaml.getAtributo(postCriarSubscription).toString(), idToken,jsonSubscription);

		pdfgenerator.conteudoPDF("que_request_um_usuario_e_gero_token_em_apos_isso_crio_um_e_chamo_usando_os_campos: ",resposta.logarEvidencia());

	}

	@Then("^a request retorna na response o codigo (\\d+)$")
	public void a_request_retorna_na_response_o_codigo(int statusCode) throws Throwable {

		resposta.getResposta().statusCode(statusCode);
		String texto = Integer.toString(resposta.getResponse().getStatusCode());
		pdfgenerator.conteudoPDF("a request retorna na response o codigo:", "o status da resposta é: " + texto);

	}

	@After(value = "@postCriarSubscriptionBoleto")
	public void finalizaPDF(Scenario cenario) throws Exception {

		scen = cenario;
		pdfgenerator.fechaPDF(scen);

	}

}
