package br.com.exame.runner;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(glue = { "br.com.exame.steps" }, 
                 features = {"src/test/resources/br/com/exame/feature" } 
                 ,tags = {"@geralbuscaruserinfo"})
public class RunnerUserInfo {

}
