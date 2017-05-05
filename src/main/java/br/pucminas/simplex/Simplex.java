package br.pucminas.simplex;


public class Simplex {

	public static int OTIMO = 0;
	public static int ILIMITADO = 1;
	public static int IMPOSSIVEL = 2;

	private static Simplex instancia;

	private TabelaSimplex tabela;

	private int status;

	private Simplex() {
	}

	public static Simplex getInstancia() {
		if (instancia == null) {
			instancia = new Simplex();
		}

		return instancia;
	}

	public double[] executar(Expressao e) throws Exception {
		tabela = new TabelaSimplex(e);
		primeiroPasso();
		segundoPasso();

		this.status = OTIMO;
		return tabela.getValoresVariaveis();
	}

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

			tabela.trocarAlgoritmo(linhaPermitida, colunaPermitida);
		}
	}

	private void segundoPasso() throws Exception {
		int colunaPermitida;
		while ((colunaPermitida = tabela.getIndiceFXPositivo()) != -1) {
			if (colunaPermitida == -2) {
				status = ILIMITADO;
				throw new Exception("Solucao Ilimitada.");
			}
			int linhaPermitida;

			linhaPermitida = tabela.getLinhaPermitida(colunaPermitida);
			tabela.trocarAlgoritmo(linhaPermitida, colunaPermitida);
		}
	}

	public int getStatus() {
		return status;
	}
}
