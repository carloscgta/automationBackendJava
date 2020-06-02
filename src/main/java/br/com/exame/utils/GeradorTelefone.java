package br.com.exame.utils;

import java.util.Locale;

import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

public class GeradorTelefone {
	
	private String geraTelefone;
	
	public String geraTelefone() {
		FakeValuesService fakeValuesService = new FakeValuesService(new Locale("pt-BR"), new RandomService());

		geraTelefone= fakeValuesService.bothify("+55119########");
			
	    return geraTelefone;
		
	}

}
