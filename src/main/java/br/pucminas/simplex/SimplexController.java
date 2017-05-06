package br.pucminas.simplex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
public class SimplexController {

	@Autowired
	private SimplexService simplexService;

	@CrossOrigin
	@RequestMapping(value = "/simplex", method = RequestMethod.POST)
	public String executarSimplex(@RequestBody String exp) {
		Gson gson = new Gson();
		Expressao expressao = gson.fromJson(exp, Expressao.class);
		return simplexService.executarSimplex(expressao);
	}
}
