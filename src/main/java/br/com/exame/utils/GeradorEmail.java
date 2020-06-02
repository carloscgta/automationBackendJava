package br.com.exame.utils;

import java.util.Locale;

import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

public class GeradorEmail {
	
	private String email;
	
	public String geraEmail() {
		FakeValuesService fakeValuesService = new FakeValuesService(new Locale("pt-BR"), new RandomService());

	    email = fakeValuesService.bothify("exametesteautomatizado+?########?@gmail.com");
			
	    return email;
	}

}
