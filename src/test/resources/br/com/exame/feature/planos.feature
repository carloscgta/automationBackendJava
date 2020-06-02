#encoding UTF-8
#Author: carlos Almeida

@geralbuscarplanos
Feature: Planos da Exame
  

  @buscarTodosPlanos
  Scenario Outline: Buscar os planos da Exame
    Given realizo request no "cadastro"  faco "login" no servico de autenticacao e chamo o "getPlans" com "<telefone>"
    Then recebo o codigo 200
  
  Examples:
  |telefone               |
  |+5511952178031         |    
 
     