#encoding UTF-8
#Author: Bruno Carvalho

@geralbuscaruserinfo
Feature: Buscar informações de Usuario
         Eu como usuario gostaria
         de buscar informacoes de Usuario
         
@buscaruserinfo
Scenario Outline: Buscar User Info
          Given realizo o "cadastro" de usuario e "login" visualizando informacoes do "userinfo" com "<firstname>" e "<lastname>"
          Then a API me retorna 200 para a buscar das UserInfos

Examples:
|firstname      |lastname         |         
|TestFirst      |TestLast         |                
         
@buscaruserinfoporid
Scenario Outline: Buscar User Info Por Id
          Given realizo o "cadastro" de usuario e "login" visualizando informacoes do "getuserinfoid" por Id com "<firstname>" e "<lastname>"
          Then  a API me retorna 200 para a buscar das UserInfos por Id

Examples:
|firstname      |lastname         |         
|TestFirst      |TestLast         |  

          
          
         
