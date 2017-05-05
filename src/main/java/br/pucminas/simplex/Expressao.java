package br.pucminas.simplex;

public class Expressao {

	public static int MIN = 1;
	public static int MAX = 2;

	public static int IGUAL = 1;
	public static int MAIOR_QUE = 2;
	public static int MENOR_QUE = 3;

	private int objetivo;
	private double[] funcaoObjetivo;
	private double[][] restricoes;
	private double[] sinaisRestricoes;
	private double[] b;

	public Expressao(int objetivo, double[] funcaoObjetivo, double[][] restricoes, double[] sinaisRestricoes,
			double[] b) throws Exception {

		int nDecVariables = funcaoObjetivo.length;
		int nConstraints = restricoes.length;

		if (objetivo != MIN && objetivo != MAX) {
			throw new Exception("Objetivo Invalido.");
		}

		if (funcaoObjetivo.length == 0 || restricoes.length == 0 || sinaisRestricoes.length == 0 || b.length == 0) {
			throw new Exception("A funcao objetivo, as restricoes, e os sinais das restricoes nao podem ser vazias.");
		}

		if (nDecVariables != restricoes[0].length || nConstraints != sinaisRestricoes.length
				|| nConstraints != b.length) {
			throw new Exception("Numero de variveis de decisao e restricoes sao diferentes.");
		}

		for (int i = 0; i < sinaisRestricoes.length; i += 1) {
			if (sinaisRestricoes[i] != IGUAL && sinaisRestricoes[i] != MAIOR_QUE && sinaisRestricoes[i] != MENOR_QUE) {
				throw new Exception("Sinal Invalido da restricao.");
			}
		}

		this.objetivo = objetivo;
		this.funcaoObjetivo = funcaoObjetivo;
		this.sinaisRestricoes = sinaisRestricoes;
		this.restricoes = restricoes;
		this.b = b;
	}

	public boolean isObjetivoMax() {
		return this.objetivo == MAX;
	}

	public int getObjetivo() {
		return objetivo;
	}

	public double[] getFuncaoObjetivo() {
		return funcaoObjetivo;
	}

	public double[][] getConstraints() {
		return restricoes;
	}

	public double[] getConstraint(int l) {
		return restricoes[l];
	}

	public double getConstraint(int l, int j) {
		return restricoes[l][j];
	}

	public double[] getConstraintSigns() {
		return sinaisRestricoes;
	}

	public double getSinalRestricao(int l) {
		return sinaisRestricoes[l];
	}

	public double[] getB() {
		return b;
	}

	public double getB(int l) {
		return b[l];
	}

	public int contadorVariaveisBasicas() {
		return restricoes.length;
	}

	public int contadorVariveisNaoBasicas() {
		return restricoes[0].length;
	}
}
