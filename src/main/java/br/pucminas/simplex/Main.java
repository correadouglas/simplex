package br.pucminas.simplex;

import com.google.gson.Gson;

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
public class Main {
	
	/**
	 * monta uma resposta para retorno na chamada.
	 * 
	 * @param retorno
	 * @return
	 */
	private static String montarResultado(double[] retorno) {
		String resultado = "";
		resultado += "{";
		resultado += "\"erro\": " + false + ", ";
		resultado += "\"z\": " + retorno[0] + ", ";
		resultado += "\"respostas\": {";
		for (int i = 1; i < retorno.length; i += 1) {
			resultado += "\"x" + i + "\": " + retorno[i];
			if (i != retorno.length - 1) {
				resultado += ", ";
			}
		}
		resultado += "}";
		resultado += "}";

		return resultado;
	}
	
	/**
	 * monta uma resposta para retorno na chamada quando ha erro.
	 * 
	 * @param erro
	 * @param codigo
	 * @return
	 */
	private static String montarResultadoErro(String erro, int codigo) {
		String resultado = "";
		resultado += "{";
		resultado += "\"erro\": " + true + ", ";
		resultado += "\"codigo\": " + codigo + ", ";
		resultado += "\"msg\": \"" + erro + "\"";
		resultado += "}";

		return resultado;
	}
	
	/**
	 * metodo que executa o algoritmo do simplex.
	 * 
	 * @param exp
	 * @return
	 */
	public static String executarSimplex(SimplexExpressao exp) {

		String retornoString = null;

		try {
			double[] retorno = Simplex.getInstancia().executar(exp);

			retornoString = montarResultado(retorno);

			return retornoString;

		} catch (Exception e) {
			retornoString = montarResultadoErro(e.getMessage(), Simplex.getInstancia().getStatus());
			return retornoString;
		}

	}
	
	/**
	 * classe main para testes.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			int objetivo = SimplexExpressao.MAX;
			double[] funcaoObjetivo = { 80, 60 };
			double[][] restricoes = { { 4, 6 }, { 4, 2 }, { 0, 1 } };
			double[] sinaisRestricoes = { SimplexExpressao.MAIOR_QUE, SimplexExpressao.MENOR_QUE, SimplexExpressao.MENOR_QUE };
			double[] b = { 24, 16, 3 };

			SimplexExpressao exp = new SimplexExpressao(objetivo, funcaoObjetivo, restricoes, sinaisRestricoes, b);
			Gson gson = new Gson();
			System.out.println("Json = " + gson.toJson(exp));
			executarSimplex(exp);

		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
