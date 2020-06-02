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
import br.com.exame.pojos.Phones;
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

public class CadastroBuscarCustomerSteps {

	Gson gson = new Gson();
	Login login = new Login();
	PDFGenerator pdfgenerator = new PDFGenerator();
	Authenticacao autenticacao = new Authenticacao();
	GeradorEmail email = new GeradorEmail();
	GeradorTelefone telefone = new GeradorTelefone();
	YamlHelper yaml = new YamlHelper();
	CreateUser createUser = new CreateUser();
	Address address = new Address();
	Customer customer = new Customer();
	Phones phones = new Phones();
	List<Phones> phonesList = new ArrayList<Phones>();
	GeradorCPF cpf = new GeradorCPF();
	Faker faker = new Faker();
	Servicos verbos = new Servicos();
	Scenario scen;
	Resposta resposta;

	private String firstname = faker.name().firstName();
	private String lastname = faker.name().lastName();

	@Before(value = "@buscarcustomer")
	public void before(Scenario cenario) throws Exception {
		pdfgenerator.iniciaPDF(cenario);
	}

	@Given("^que eu acesse o endpoint da aplicacao \"([^\"]*)\" e \"([^\"]*)\" e \"([^\"]*)\" e \"([^\"]*)\" para buscar o cliente  com \"([^\"]*)\" e \"([^\"]*)\"$")
	public void que_eu_acesse_o_endpoint_da_aplicacao_e_e_e_para_buscar_o_cliente_com_e(String endPointUsuario,
			String endPointLogin, String endPointCustomer, String endPointCustomerGet, String notes,
			String additionaldetails) throws Throwable {
		// Criar Usuario
		createUser.setCpfCnpj(cpf.mostraResultado());
		createUser.setEmail(email.geraEmail());
		createUser.setFirstName(firstname);
		createUser.setLastName(lastname);
		createUser.setPassword("Yeshu@18");
		createUser.setPhone(telefone.geraTelefone());
		String json = gson.toJson(createUser);
		resposta = verbos.postEndPoint(yaml.getAtributo(endPointUsuario).toString(), json);

		// Salvar Email, Senha, e Id
		String email = resposta.salvarObjetosBody("email");
		String idResposta = resposta.salvarObjetosBody("id");
		Integer idLogin = Integer.valueOf(idResposta);
		String cpfResposta = resposta.salvarObjetosBody("idDocument");

		// Login
		login.setUsername(email);
		login.setPassword("Yeshu@18");
		String jsonLogin = gson.toJson(login);
		resposta = autenticacao.login(yaml.getAtributo(endPointLogin).toString(), jsonLogin);

		// Salvar Token
		String idToken = resposta.salvarToken("idToken");

		// Customer Representation
		address.setAdditionalDetails(additionaldetails);
		address.setCity("São paulo");
		address.setCountry("BR");
		address.setNeighborhood("Pacaembu");
		address.setNumber("4");
		address.setState("SP");
		address.setStreet("Av. Arnal");
		address.setZipcode("034567000");
		customer.setEmail(email);
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

		resposta = verbos.postEndpointWithAuthorization(yaml.getAtributo(endPointCustomer).toString(), idToken,
				jsonCustomer);

		String idData = resposta.salvarObjetosBody("data.id");

		resposta = verbos.getEndPointWithAuthorization2(yaml.getAtributo(endPointCustomerGet).toString() + idData,
				idToken);

		pdfgenerator.conteudoPDF("que eu acesse o endpoint da aplicacao para buscar o cliente:",
				resposta.logarEvidencia());
	}

	@Given("^que eu acesse o endpoint da aplicacao \"([^\"]*)\" e \"([^\"]*)\" e \"([^\"]*)\" e \"([^\"]*)\" para buscar o cliente$")
	public void que_eu_acesse_o_endpoint_da_aplicacao_e_e_e_para_buscar_o_cliente(String endPointUsuario,
			String endPointLogin, String endPointCustomer, String endPointCustomerGet) throws Throwable {

	}

	@Then("^a API me retorna o status code (\\d+) para a busca do cliente$")
	public void a_API_me_retorna_o_status_code_para_a_busca_do_cliente(int statusCode) throws Throwable {
		resposta.getResposta().statusCode(statusCode);
		String texto = Integer.toString(resposta.getResponse().getStatusCode());
		pdfgenerator.conteudoPDF("a API me retorna o status code para a busca do cliente:",
				"o status da resposta é: " + texto);
	}

	@After(value = "@buscarcustomer")
	public void finalizaPDF(Scenario cenario) throws Exception {
		scen = cenario;
		pdfgenerator.fechaPDF(scen);
	}

}
