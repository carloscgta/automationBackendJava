#encoding UTF-8
#Author: Bruno Carvalho

@geralcriarcustomer
Feature: Criar um Cliente
                Eu como usuario
                gostaria de criar um cliente
               

@customercadastro
Scenario Outline: Criar customer
       Given que eu acesse o endpoint da aplicacao "cadastro" e "login" e para cadastro de "customer"  com "<notes>" e "<additionaldetails>"
       Then a API me retorna o status code 201 para o cadastro de cliente  

Examples:
|notes                   |additionaldetails                      |
|Teste api criar customer|informacoes adicionais                 |
       
@buscarcustomer
Scenario Outline: Buscar algum customer especifico              
       Given que eu acesse o endpoint da aplicacao "cadastro" e "login" e "customer" e "customerGet" para buscar o cliente  com "<notes>" e "<additionaldetails>"                   
       Then a API me retorna o status code 200 para a busca do cliente

Examples:
|notes                   |additionaldetails                      |
|Teste api criar customer|informacoes adicionais                 |              

@alterarcustomer   
Scenario Outline: Alterar algum customer especifico  
       Given que eu acesse o endpoint da aplicacao "cadastro" e "login" e "customer" e "customerPut" para alterar o cliente  com "<notes>" e "<additionaldetails>" com "<alteracaonome>"
       Then a API me retorna o status 200 para a alteracao do cliente  

Examples:
|notes                   |additionaldetails                      |alteracaonome                           |
|Teste api criar customer|informacoes adicionais                 |Teste alteracao customer                |

       
		
	    
 	    
        

         
         