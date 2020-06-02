#encoding UTF-8
#Email: carlosteixeira@brq.com
#Author: Carlos ALmeida

@geralpaymentprofile
Feature: Perfil Pagamento
  Eu quero criar um perfil para ser usado nas assinaturas da revista exame

  @postPaymentProfiles
  Scenario Outline: Realizar a Criacao de um Perfil de Pagamento
    Given considerando que obtenho o idToken chamando o endpoint "login"
    When usar as seguintes info na chamada <iD> e <cardCvv> e <cardExpiration> e <cardNumber>  e <customerId> e <holderName> e <paymentCompanyCode> e <paymentMethodCode>  e "registryCode"  e chamar "postPaymentProfiles" para criar um PaymentProfile
    Then API retornar 201 created exibindo os dados do Profile criado 
    

    Examples: 
       |iD						  |cardCvv										|cardExpiration		|cardNumber				|customerId			|holderName				|paymentCompanyCode				|paymentMethodCode				|registryCode				|
       | 34533					|459											 	|10/2027					|5555555555555557	|265165					|Carlos Almeida		|mastercard								|credit_card							|92053085010				|
      
      
  @getPaymentProfilePorId
  Scenario Outline: Buscar Perfil de Pagamento por ID
    Given tendo o idToken obtido na chamada do endpoint "login" e tambem "cadastro" e "customer" com os campos "<phone>" e "<additionaldts>" e "<notes>" e "<phonetype>" e "<cvv>" e "<cexp>" e "<cardnumber>" e "<paycode>" e "<paymethod>"
    Then API retornar todos os perfis de Pagamentos com codigo 200
    
  Examples:
  
    |phone            | additionaldts                 | notes                   | phonetype   | cvv       | cexp     |cardnumber                    | paycode         |paymethod    |
    |+5511952178031   |  teste de perfil de pagamento |Teste api criar customer |  mobile     | 344       |10/2028   |5555555555555557              | mastercard      |credit_card  |
    
    
