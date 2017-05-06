package br.pucminas.simplex;

import org.springframework.stereotype.Service;

/**
 * 
 * Pontificia Universidade Catolica de Minas Gerais.
 * Otimizacao de Sistemas Computacionais.
 * 
 * Implementacao Simplex.
 *
 * Douglas Henrique Silva Correa.
 * Guilherme Silva Santos.
 * Mateus Felipe Martins Miranda.
 * 
 */
@Service
public class SimplexService {

	public String executarSimplex(Expressao exp) {
		return Main.executarSimplex(exp);
	}
}
