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

public class PayWallValidarSteps {
	
	public PayWallValidarSteps() {
		
	}
	
	protected Integer idCustomer;
	protected String idUserCriado;
	protected String emailUsuarioCriado;
    protected String idTokenAuth;
	protected String idSubscription;
	protected Integer iddoCusto = 0;
	protected String cpfResposta;
	Gson gson = new Gson();
	Login login = new Login();
	Authenticacao autenticacao = new Authenticacao();
	GeradorTelefone telefone = new GeradorTelefone();
	CreateUser createUser = new CreateUser();
	GeradorEmail email = new GeradorEmail();
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


	@Before(value = "@apiPayWall")
	public void before(Scenario cenario) throws Exception {
		pdfgenerator.iniciaPDF(cenario);
	}

	@After(value = "@apiPayWall")
	public void finalizaPDF(Scenario cenario) throws Exception {

		scen = cenario;
		pdfgenerator.fechaPDF(scen);

	}
	
	@Given("^que realizo uma chamda no endpoint \"([^\"]*)\" e \"([^\"]*)\" com \"([^\"]*)\" e \"([^\"]*)\" e \"([^\"]*)\" e \"([^\"]*)\"$")
	public void que_realizo_uma_chamda_no_endpoint_e_com_e_e_e(String loginEndpoint, String payWallEndpoint, String additionaldetails, String notes, String phone, String phonetype) throws Throwable {
	    
		createUser.setCpfCnpj(cpf.mostraResultado());
		createUser.setEmail(email.geraEmail());
		createUser.setFirstName(firstname);
		createUser.setLastName(lastname);
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

		pdfgenerator.conteudoPDF("que_crio_um_usuario_para_gerar_um_idToken_fazendo_para_poder_criar_um_customer:", resposta.logarEvidencia());

		String urlEndpoint = yaml.getAtributo(payWallEndpoint).toString();
		String url = urlEndpoint + idData;
		resposta = verbos.getEndPointWithAuthorization2(url, idTokenAuth);
		pdfgenerator.conteudoPDF("que_realizo_uma_chamda_no_endpoint:", resposta.logarEvidencia());
	}

	@Then("^a API deve retornar na response o codigo (\\d+)$")
	public void a_API_deve_retornar_na_response_o_codigo(int statusCode) throws Exception {

		resposta.getResposta().statusCode(statusCode);
		String texto = Integer.toString(resposta.getResponse().getStatusCode());
		pdfgenerator.conteudoPDF("a_API_deve_retornar_na_response_o_codigo:", "o status da resposta é: " + texto);

	}


}
