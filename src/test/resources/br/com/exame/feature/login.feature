#encoding UTF-8
#Author: Bruno Carvalho

@gerallogin
Feature: Realizar login
                Eu como usuario
                gostaria de realizar login

@login                
Scenario Outline: Realizar login
        Given que eu acesse o endpoint da aplicacao "login" com "<username>"
        Then a API me retorna o status code 201

Examples:
|username                          |
|exametesteautomatizado@gmail.com  |              

        
@loginincorreto                
Scenario Outline: Realizar login incorreto
        Given que eu acesse o endpoint da aplicacao "login" com "<username>" incorreto
        Then a API me retorna o status code 401 do login incorreto    

Examples:
|username                                |
|exametesteautomatizadoo@gmail.com       |           

@logincampovazio
Scenario Outline: Realizar login com campo vazio
        Given que eu acesse o endpoint da aplicacao "login" com "<username>" em branco        
        Then a API me retorna o status code 400 do login em branco 

Examples:
|username                          |
|                                  |         
                      
                
                