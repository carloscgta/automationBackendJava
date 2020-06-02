#encoding UTF-8
#Author: carlosteixeira@brq.com

@geralapipaywall
Feature: PayWall
Pesquisar um Customer(Cliente/Assinante) pelo ID do usuário com integração com o Payment-Service
  

  @apiPayWall
  Scenario Outline: Validar se o usuario é assinante
    Given que realizo uma chamda no endpoint "login" e "payWall" com "<additionaldetails>" e "<notes>" e "<phone>" e "<phonetype>" 
    Then a API deve retornar na response o codigo 404
 
   Examples:
   |additionaldetails    |notes                    |phone            |phonetype     | 
   |teste pay wall       |Teste api criar customer |+5511952178031   |mobile        |
   
    
 
