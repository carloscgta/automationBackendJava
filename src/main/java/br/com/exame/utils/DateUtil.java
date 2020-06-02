package br.com.exame.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

	public String geraData() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss");
        String hoje = LocalDateTime.now().format(formatter);
        return hoje;
	}

}
