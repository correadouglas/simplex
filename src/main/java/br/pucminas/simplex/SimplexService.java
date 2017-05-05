package br.pucminas.simplex;

import org.springframework.stereotype.Service;

@Service
public class SimplexService {

	public String executarSimplex(Expressao exp) {
		return Main.executarSimplex(exp);
	}
}
