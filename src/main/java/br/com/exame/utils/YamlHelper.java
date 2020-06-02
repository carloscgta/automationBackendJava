package br.com.exame.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class YamlHelper {
	
	public Object getAtributo(String... param) throws Exception {
		File file = new File("/home/runner/work/exame-automacao-testes-backend/exame-automacao-testes-backend/src/main/resources/Properties.yaml");
		if(file.exists()) {
			file = new File("/home/runner/work/exame-automacao-testes-backend/exame-automacao-testes-backend/src/main/resources/Properties.yaml");
		}
		else{
			file = new File("src/main/resources/Properties.yaml");
		}
		InputStream input = new FileInputStream(file);
		Map<?, ?> mapAux = (Map<?, ?>) new Yaml().load(input);
		if (mapAux == null) {
			throw new Exception(String.format("A massa de dados nao foi localizada no arquivo %s", file.getName()));
		}
		int cont;
		for (cont = 0; cont < (param.length - 1); cont++) {
			mapAux = (Map<?, ?>) mapAux.get(param[cont]);
		}
		return mapAux.get(param[cont]);
	}


}


