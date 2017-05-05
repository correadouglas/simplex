package br.pucminas.simplex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimplexController {

	@Autowired
	private SimplexService simplexService;

	@RequestMapping(value = "/simplex", method = RequestMethod.POST)
	public String executarSimplex(@RequestBody Expressao exp) {
		return simplexService.executarSimplex(exp);
	}
}
