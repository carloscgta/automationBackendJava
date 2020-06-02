#encoding UTF-8
#Author: Bruno Carvalho

@gerallogout
Feature: Realizar logout
                Eu como usuario
                gostaria de realizar logout

@logout
Scenario Outline: Realizar logout
        Given que eu acessse a aplicacao de "login" com o "<username>"
        When eu realizo o "logout"
        Then a API me retorna 200 que foi deslogado    

Examples:
|username                               |
|exametesteautomatizado@gmail.com       |    
        

