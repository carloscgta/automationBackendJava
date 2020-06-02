#encoding UTF-8
#Author: carlosteixeira@brq.com

@geralsubscriptions
Feature: Subscriptions
  		   Como usuário quero fazer uma assinatura (subscriptions)

  @getSubscriptionsID
  Scenario Outline: Buscar uma Subscription por ID
    Given faco uma chamada em "cadastro" apos chamo o "login" para poder criar um "customer" com os campos "<phone>" e "<additionaldetails>" e "<notes>"
    And  criar uma subscription com os dados necessarios para poder chamar o endpoint "postCriarSubscription"  request no "getSubscriptionsID" com  "<cvv>" e "<cardexp>" e "<cardnumber>"
    Then o serviço retorna a subscription exibindo o codigo 200 
   
   Examples:
   |phone              |additionaldetails        |notes                    | cvv    |cardexp     |cardnumber        |
   | +5511952178031    |teste buscar customer    |teste api criar customer | 455    | 10/2026    |5555555555555557  |
     

  @postCriarSubscription
  Scenario Outline: Criar uma Subscription
    	Given que crio um usuario "cadastro" para gerar um idToken fazendo "login" para poder criar um "customer" quando preencher os dados da request no endpoint "postCriarSubscription"  com "<additional>" e "<notes>" e "<cvv>" e "<companycode>" e "<cardexp>"e  "<cardnumber>" para criar assinatura
    	Then a api retorno codigo 201 apos criar a subscription
    
    Examples:
     |additional             |notes                     | cvv    |companycode    |cardexp    |cardnumber        |
     |teste de additional    |Teste api criar customer  | 234    |mastercard     |10/2028    |5555555555555557  |

  @postCancelarSubscription  
  Scenario Outline: Cancelar uma Subscription
    	Given criar um user "cadastro" gerar um idToken em "login" para ser possivel ter um "customer" para chamar o endpoint "postCriarSubscription" para depois validar o cancelamento "postCancelarSubscription" com os campos "<phone>" e "<additionaldetails>" e "<cvv>" e "<cardexp>" e "<cardnum>"
    	Then o servico deve retornar codigo 200 cancelando a Subscription
    
    Examples:
    | phone            |additionaldetails         |cvv      |cardexp      |cardnum            |
    | +5511952178031   |Teste api criar customer  |234      |10/2025      |5555555555555557   |
    	
	@postCriarSubscriptionBoleto
	Scenario Outline: Criar pagamento Boleto
		Given que request um usuario "cadastro" e gero token em "login" apos isso crio um "customer" e chamo "postCriarSubscription"  usando os campos "<paycompcode>"
		Then a request retorna na response o codigo 201
		
		Examples:
    | phone            |additionaldetails         |paycompcode      |
    | +5511952178031   |Teste api criar customer  |bank_slip        |
    
  
  @postCriarSubscriptionDebitoConta
  Scenario Outline: Criar pagamento Debito em Conta
    Given que a request com um usuario "cadastro" e gero token em "login" para poder criar um "customer" com os campos "<phone>" e "<additionaldetails>" e "<notes>"
		When criar uma subscription do debito com os dados necessarios para poder chamar o endpoint "postCriarSubscription" com  "<bankbranch>" e "<bankaccount>" e "<paymethodcode>" e "<paycompcode>"
		Then o serviço vai me retornar o codigo 201
	
   Examples:
   |phone              |additionaldetails        |notes                    | bankbranch    |bankaccount     |paymethodcode    |paycompcode         |
   | +5511952178031    |teste buscar customer    |teste api criar customer | 8150          | 11111-1        |bank_debit       | bradesco           |
   
  
  @postCriarSubscriptionCortesia
  Scenario Outline: Criar pagamento cortesia
     Given que a request com um usuario "cadastro" e gero token em "login" para poder criar um "customer" com os campos "<phone>" e "<additionaldetails>" e "<notes>" com cortesia
		 When criar uma subscription do cortesia com os dados necessarios para poder chamar o endpoint "postCriarSubscription" com  "<paymethodcode>" 
		 Then o serviço vai me retornar o codigo 201 criando o cortesia
	
	Examples:
	 |phone              |additionaldetails        |notes                    |paymethodcode |
   | +5511952178031    |teste buscar customer    |teste api criar customer |courtesy      |
   
   
 
		