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

public class CadastroCustomerCorretoSteps {

	Gson gson = new Gson();
	Login login = new Login();
	Authenticacao autenticacao = new Authenticacao();
	CreateUser createUser = new CreateUser();
	YamlHelper yaml = new YamlHelper();
	PDFGenerator pdfgenerator = new PDFGenerator();
	GeradorTelefone telefone = new GeradorTelefone();
	Servicos verbos = new Servicos();
	Address address = new Address();
	Customer customer = new Customer();
	GeradorEmail email = new GeradorEmail();
	GeradorCPF cpf = new GeradorCPF();
	Phones phones = new Phones();
	List<Phones> phonesList = new ArrayList<Phones>();
	Faker faker = new Faker();
	Scenario scen;
	Resposta resposta;
	private String firstname = faker.name().firstName();
	private String lastname = faker.name().lastName();

	@Before(value = "@customercadastro")
	public void before(Scenario cenario) throws Exception {
		pdfgenerator.iniciaPDF(cenario);
	}

	@Given("^que eu acesse o endpoint da aplicacao \"([^\"]*)\" e \"([^\"]*)\" e para cadastro de \"([^\"]*)\"  com \"([^\"]*)\" e \"([^\"]*)\"$")
	public void que_eu_acesse_o_endpoint_da_aplicacao_e_e_para_cadastro_de_com_e(String endPointUsuario,
			String endPointLogin, String endPointCadastro, String notes, String additionaldetails) throws Throwable {
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
		Integer id = Integer.valueOf(idResposta);
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
		customer.setUserId(id);
		customer.setPhones(phonesList);
		String jsonCustomer = gson.toJson(customer);

		resposta = verbos.postEndpointWithAuthorization(yaml.getAtributo(endPointCadastro).toString(), idToken,
				jsonCustomer);

		pdfgenerator.conteudoPDF("que eu acesse o endpoint da aplicacao login e cadastro para o cadastro de cliente:",
				resposta.logarEvidencia());
	}

	@Given("^que eu acesse o endpoint da aplicacao \"([^\"]*)\" e \"([^\"]*)\" e \"([^\"]*)\" para o cadastro de cliente$")
	public void que_eu_acesse_o_endpoint_da_aplicacao_e_e_para_o_cadastro_de_cliente(String endPointUsuario,
			String endPointLogin, String endPointCadastro) throws Throwable {

	}

	@Then("^a API me retorna o status code (\\d+) para o cadastro de cliente$")
	public void a_API_me_retorna_o_status_code_para_o_cadastro_de_cliente(int statusCode) throws Throwable {
		resposta.getResposta().statusCode(statusCode);
		String texto = Integer.toString(resposta.getResponse().getStatusCode());
		pdfgenerator.conteudoPDF("a API me retorna o status code para o cadastro de cliente:",
				"o status da resposta é: " + texto);
	}

	@After(value = "@customercadastro")
	public void finalizaPDF(Scenario cenario) throws Exception {
		scen = cenario;
		pdfgenerator.fechaPDF(scen);
	}

}
