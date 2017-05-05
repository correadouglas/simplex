package br.pucminas.simplex;

public class Main {

	private static int nVariaveisIndice = 0;
	private static int nRestricoesIndice = 1;
	private static int objetivoIndice = 2;
	private static int funcaoObjetivoIndice = 3;

	private static void mostrarResultado(double[] retorno) {
		System.out.print("{");
		System.out.print("\"erro\": " + false + ", ");
		System.out.print("\"z\": " + retorno[0] + ", ");
		System.out.print("\"respostas\": {");
		for (int i = 1; i < retorno.length; i += 1) {
			System.out.print("\"x" + i + "\": " + retorno[i]);
			if (i != retorno.length - 1) {
				System.out.print(", ");
			}
		}
		System.out.print("}");
		System.out.print("}");
	}

	private static void mostrarErro(String erro, int codigo) {
		System.out.print("{");
		System.out.print("\"erro\": " + true + ", ");
		System.out.print("\"codigo\": " + codigo + ", ");
		System.out.print("\"msg\": \"" + erro + "\"");
		System.out.print("}");
	}

	private static Expressao formatarExpressao(String[] parametro) throws Exception {
		int nVariaveis = Integer.parseInt(parametro[nVariaveisIndice]);
		int nRestricoes = Integer.parseInt(parametro[nRestricoesIndice]);
		int objetivo = Integer.parseInt(parametro[objetivoIndice]);

		double[] funcaoObjetivo = new double[nVariaveis];
		double[][] restricoes = new double[nRestricoes][nVariaveis];
		double[] sinaisRestricoes = new double[nRestricoes];
		double[] b = new double[nRestricoes];

		int indiceParametro = funcaoObjetivoIndice;
		for (int i = 0; i < nVariaveis; i += 1) {
			funcaoObjetivo[i] = Double.parseDouble(parametro[indiceParametro]);
			indiceParametro += 1;
		}

		for (int i = 0; i < nRestricoes; i += 1) {
			for (int j = 0; j < nVariaveis; j += 1) {
				restricoes[i][j] = Double.parseDouble(parametro[indiceParametro]);
				indiceParametro += 1;
			}
		}

		for (int i = 0; i < nRestricoes; i += 1) {
			sinaisRestricoes[i] = Double.parseDouble(parametro[indiceParametro]);
			indiceParametro += 1;
		}

		for (int i = 0; i < nRestricoes; i += 1) {
			b[i] = Double.parseDouble(parametro[indiceParametro]);
			indiceParametro += 1;
		}

		return new Expressao(objetivo, funcaoObjetivo, restricoes, sinaisRestricoes, b);
	}

	public static String executarSimplex(Expressao exp) {

		try {
			double[] retorno = Simplex.getInstancia().executar(exp);

			mostrarResultado(retorno);

		} catch (Exception e) {
			mostrarErro(e.getMessage(), Simplex.getInstancia().getStatus());
		}

		return null;
	}

	public static void main(String[] args) {
		try {
			int objetivo = Expressao.MAX;
			double[] funcaoObjetivo = { 80, 60 };
			double[][] restricoes = { { 4, 6 }, { 4, 2 }, { 0, 1 } };
			double[] sinaisRestricoes = { Expressao.MAIOR_QUE, Expressao.MENOR_QUE, Expressao.MENOR_QUE };
			double[] b = { 24, 16, 3 };

			Expressao exp = new Expressao(objetivo, funcaoObjetivo, restricoes, sinaisRestricoes, b);

			executarSimplex(exp);

		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
