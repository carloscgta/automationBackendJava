#encoding UTF-8
#Author: Bruno Carvalho

@geralcriaruser
Feature: Criar Usuario
                Eu como usuario
                gostaria de cadastrar minhas informacoes

@cadastro
Scenario Outline: Criar usuario
	       Given que eu acesse o endpoint da aplicacao "cadastro" para o cadastro de usuario "<firstname>" com "<lastname>"
	       Then a API me retorna o status code 201 para o cadastro

Examples:
|firstname |lastname|
|Carlos 	 |Almeida |     


@cadastroinvalidocpfcnpj
Scenario Outline: Criar usuario com cpf e cnpj invalido
       Given que eu acesse o endpoint da aplicacao "cadastro" para o cadastro de usuario com "<cnpjinvalido>" e "<firstname>" e "<lastname>"
       Then a API me retorna o status code 400 para o cadastro invalido  

Examples:
|cnpjinvalido      |firstname|lastname    |
|773426210480      |Bruno    |Carvalho    |  
       

@cadastrosenhaforapadrao 
Scenario Outline: Criar usuario com senha fora do padrao correto
       Given que eu acesse o endpoint da aplicacao de "cadastro" para o cadastro de usuario com "<senha>" fora do padrao com "<username>" e "<lastname>"
       Then a API me retorna o status code 400 para o cadastro fora do padrao correto  
       
Examples:
|senha        |username   |lastname   |
|12345678     |Bruno      | Carvalho  |
              
@cadastrophoneforapadrao     
Scenario Outline: Criar usuario com telefone fora do padrao correto
       Given que eu acesse o endpoint da aplicacao de "cadastro" para o cadastro de usuario com "<telefone>" fora do padrao com "<firstname>" e com "<lastname>"
       Then a API me retorna o status code 400 para o cadastro com o telefone fora do padrao 

Examples:
|telefone           |firstname    |lastname    |
|11952178031        |Bruno        | Carvalho   |    
       
@cadastrousuariosemcpf
Scenario Outline: Criar usuario sem CPF ou com campo em branco
       Given que eu acesse o endpoint da aplicacao de "cadastro" para o cadastro de usuario sem "<cpf>" com "<firstname>" e "<lastname>"
       Then a API me retorna o status code 400 para o cadastro de usuario sem CPF

Examples:
| cpf    |firstname |lastname   |  
|        | Carlos   |Almeida    |
       
       

@cadastrousuariosemfirstname
Scenario Outline: Criar usuario sem o campo firstName ou campo em branco
       Given que eu acesse o endpoint da aplicacao de "cadastro" para o cadastro de usuario sem "<firstname>"  com "<lastname>"   
       Then a API me retorna o status code 400 para o cadastro de primeiro nome em branco   

Examples:
|firstname|lastname       |
|         |Almeida        |

@cadastrousuariosemlastname
Scenario Outline: Criar usuario sem o campo lastName ou campo em branco
      Given que eu acesse o endpoint da aplicacao de "cadastro" para o cadastro de usuario sem "<firstname>" com "<lastname>"
      Then a API me retorna o status code 400 para o cadastro de sobrenome em branco

Examples:
|firstname|lastname       |
|Urik     |               |      
      

@cadastrousuariosemtelefone
Scenario Outline: Criar usuario sem o campo telefone ou campo em branco
       Given que eu acesse o endpoint da aplicacao de "cadastro" para o cadastro de usuario sem "<telefone>" com "<firstname>" e com "<lastname>"
       Then a API me retorna o status code 400 para o cadastro de telefone em branco   
       
Examples:
|telefone|firstname      |lastname        |
|        | Bruno         | Carvalho       |       
    
@cadastrousuariotelefoneinvalido
Scenario Outline: Criar usuario com telefone invalido
       Given  que eu acesse o endpoint da aplicacao de "cadastro" para o cadastro de "<telefoneinvalido>" com "<firstname>" e "<lastname>"
       Then a API me retorna o status code 400 para o cadastro de telefone invalido  
       
Examples:
|telefone              |firstname      |lastname        |
|952178031             | Bruno         | Carvalho       |                         
    
   
   
     
         
       
