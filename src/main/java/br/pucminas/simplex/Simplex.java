package br.pucminas.simplex;

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
public class Simplex {

	public static int OTIMO = 0;
	public static int ILIMITADO = 1;
	public static int IMPOSSIVEL = 2;

	private static Simplex instancia;

	private SimplexTabela tabela;

	private int status;

	
	/**
	 * metodo que retorna o simplex instanciado.
	 * 
	 * @return
	 */
	public static Simplex getInstancia() {
		if (instancia == null) {
			instancia = new Simplex();
		}

		return instancia;
	}
	
	/**
	 * metodo que faz a chamada dos passos do simplex.
	 * 
	 * @param e
	 * @return
	 * @throws Exception
	 */
	public double[] executar(SimplexExpressao e) throws Exception {
		tabela = new SimplexTabela(e);
		primeiroPasso();
		segundoPasso();

		this.status = OTIMO;
		return tabela.getValoresVariaveis();
	}
	
	/**
	 * executa o primeiro passo do algoritmo.
	 * 
	 */
	private void primeiroPasso() throws Exception {
		int indiceMembrosLivresNegativos;
		while ((indiceMembrosLivresNegativos = tabela.getIndiceMembrosLivresNegativos()) != -1) {
			int linhaPermitida;
			int colunaPermitida = tabela.getColunaComElementoNegativo(indiceMembrosLivresNegativos);

			if (colunaPermitida == -1) {
				status = IMPOSSIVEL;
				throw new Exception("Nao existe solucao.");
			}

			linhaPermitida = tabela.getLinhaPermitida(colunaPermitida);

			tabela.mudarAlgoritmo(linhaPermitida, colunaPermitida);
		}
	}

	/**
	 * executa o segundo passo do algoritmo.
	 * 
	 */
	private void segundoPasso() throws Exception {
		int colunaPermitida;
		while ((colunaPermitida = tabela.getIndiceFXPositivo()) != -1) {
			if (colunaPermitida == -2) {
				status = ILIMITADO;
				throw new Exception("Solucao Ilimitada.");
			}
			int linhaPermitida;

			linhaPermitida = tabela.getLinhaPermitida(colunaPermitida);
			tabela.mudarAlgoritmo(linhaPermitida, colunaPermitida);
		}
	}

	public int getStatus() {
		return status;
	}
}
