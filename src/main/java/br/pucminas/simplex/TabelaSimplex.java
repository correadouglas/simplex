package br.pucminas.simplex;

public class TabelaSimplex {
	
	Expressao e;
	private Celula[][] tabelaSimplex;
	private int[] variaveisBasicas;
	private int[] variaveisNaoBasicas;

	public TabelaSimplex(Expressao e) {
		this.e = e;
		this.criarTabelaSimplex();
		this.preencherTabelaObjetivo();
		this.preencherRestricoes();
		this.setVariaveisNaoBasicas();
		this.setVariaveisBasicas();
	}

	private void criarTabelaSimplex() {
		this.variaveisBasicas = new int[e.contadorVariaveisBasicas()];
		this.variaveisNaoBasicas = new int[e.contadorVariveisNaoBasicas()];
		int rows = variaveisBasicas.length + 1;
		int cols = variaveisNaoBasicas.length + 1;
		tabelaSimplex = new Celula[rows][cols];
	}

	private void preencherTabelaObjetivo() {
		tabelaSimplex[0][0] = new Celula();

		double[] funcaoObjetivo = e.getFuncaoObjetivo();
		int multiplicador;
		if (e.getObjetivo() == Expressao.MAX) {
			multiplicador = 1;
		} else {
			multiplicador = -1;
		}

		for (int i = 1; i <= funcaoObjetivo.length; i += 1) {
			tabelaSimplex[0][i] = new Celula(funcaoObjetivo[i - 1] * multiplicador);
		}
	}

	private void preencherRestricoes() {
		for (int i = 1; i <= variaveisBasicas.length; i += 1) {
			int multiplicador;
			if (e.getSinalRestricao(i - 1) == Expressao.MAIOR_QUE) {
				multiplicador = -1;
			} else {
				multiplicador = 1;
			}
			tabelaSimplex[i][0] = new Celula(e.getB(i - 1) * multiplicador);
			for (int j = 1; j <= variaveisNaoBasicas.length; j += 1) {
				tabelaSimplex[i][j] = new Celula(e.getConstraint(i - 1, j - 1) * multiplicador);
			}
		}
	}

	private void setVariaveisBasicas() {
		for (int i = 0; i < variaveisBasicas.length; i += 1) {
			variaveisBasicas[i] = i + variaveisNaoBasicas.length;
		}
	}

	private void setVariaveisNaoBasicas() {
		for (int i = 0; i < variaveisNaoBasicas.length; i += 1) {
			variaveisNaoBasicas[i] = i;
		}
	}

	public int getIndiceMembrosLivresNegativos() {
		for (int i = 1; i < tabelaSimplex.length; i += 1) {
			if (tabelaSimplex[i][0].getTop() < 0) { 
				return i;
			}
		}
		return -1;
	}

	public int getIndiceFXPositivo() {
		for (int i = 1; i < tabelaSimplex[0].length; i += 1) {
			if (tabelaSimplex[0][i].getTop() == 0) {
				return -2;
			} else if (tabelaSimplex[0][i].getTop() > 0) { 
				return i;
			}
		}
		return -1;
	}

	public int getColunaComElementoNegativo(int line) {
		for (int i = 1; i < tabelaSimplex[line].length; i += 1) {
			if (tabelaSimplex[line][i].getTop() < 0) { 
				return i;
			}
		}
		return -1;
	}

	public void trocarAlgoritmo(int linhaPermitida, int colunaPermitida) {
		double elementoPermitido = tabelaSimplex[linhaPermitida][colunaPermitida].getTop();

		double inverso = Math.pow(elementoPermitido, -1);
		tabelaSimplex[linhaPermitida][colunaPermitida].setBottom(inverso);

		multiplicarLinhaPermitidaPeloInverso(linhaPermitida, colunaPermitida, inverso);
		multiplicarColunaPermitidaPeloInversoNegativo(linhaPermitida, colunaPermitida, inverso);

		multiplicarCelulasAbaixo(linhaPermitida, colunaPermitida);

		refazerTabela(linhaPermitida, colunaPermitida);

		trocarVariveisNaoBasicasComBasicas(linhaPermitida, colunaPermitida);

	}

	private void multiplicarLinhaPermitidaPeloInverso(int linhaPermitida, int colunaPermitida, double inverso) {
		for (int j = 0; j < tabelaSimplex[0].length; j += 1) {
			if (j != colunaPermitida) { 
				tabelaSimplex[linhaPermitida][j].setBottom((tabelaSimplex[linhaPermitida][j].getTop()) * inverso);
			}
		}
	}

	private void multiplicarColunaPermitidaPeloInversoNegativo(int linhaPermitida, int colunaPermitida, double inverso) {
		for (int i = 0; i < tabelaSimplex.length; i += 1) {
			if (i != linhaPermitida) { 
				tabelaSimplex[i][colunaPermitida].setBottom((tabelaSimplex[i][colunaPermitida].getTop()) * -inverso);
			}
		}
	}

	private void multiplicarCelulasAbaixo(int linhaPermitida, int colunaPermitida) {
		for (int i = 0; i < tabelaSimplex.length; i += 1) {
			if (i != linhaPermitida) {
				for (int j = 0; j < tabelaSimplex[i].length; j += 1) {
					if (j != colunaPermitida) {
						double value = tabelaSimplex[linhaPermitida][j].getTop()
								* tabelaSimplex[i][colunaPermitida].getBottom();
						tabelaSimplex[i][j].setBottom(value);
					}
				}
			}
		}
	}

	private void refazerTabela(int linhaPermitida, int colunaPermitida) {
		Celula[][] tabela = new Celula[tabelaSimplex.length][tabelaSimplex[0].length];

		copiaCelulas(tabela, linhaPermitida, colunaPermitida);
		somaCelulas(tabela, linhaPermitida, colunaPermitida);

		tabelaSimplex = tabela;
	}

	private void copiaCelulas(Celula[][] tabela, int linhaPermitida, int colunaPermitida) {
		for (int i = 0; i < tabela.length; i += 1) {
			tabela[i][colunaPermitida] = new Celula(tabelaSimplex[i][colunaPermitida].getBottom());
		}

		for (int j = 0; j < tabela[0].length; j += 1) {
			tabela[linhaPermitida][j] = new Celula(tabelaSimplex[linhaPermitida][j].getBottom());
		}
	}

	private void somaCelulas(Celula[][] tabela, int linhaPermitida, int colunaPermitida) {

		for (int i = 0; i < tabela.length; i += 1) {
			if (i != linhaPermitida) {
				for (int j = 0; j < tabela[i].length; j += 1) {
					if (j != colunaPermitida) {
						double value = tabelaSimplex[i][j].getTop() + tabelaSimplex[i][j].getBottom();
						tabela[i][j] = new Celula(value);
					}
				}
			}
		}
	}

	private void trocarVariveisNaoBasicasComBasicas(int linhaPermitida, int colunaPermitida) {
		int index = -1;
		int variavelNaoBasica = variaveisNaoBasicas[colunaPermitida + index];
		variaveisNaoBasicas[colunaPermitida + index] = variaveisBasicas[linhaPermitida + index];
		variaveisBasicas[linhaPermitida + index] = variavelNaoBasica;
	}

	public int getLinhaPermitida(int colunaPermitida) {
		int linhaPermitida = -1;
		double quociente = Double.MAX_VALUE;

		for (int i = 1; i < tabelaSimplex.length; i += 1) {
			double membroLivre = tabelaSimplex[i][0].getTop();
			double elementoPerimitido = tabelaSimplex[i][colunaPermitida].getTop();

			if ((membroLivre < 0 && elementoPerimitido < 0) || (membroLivre > 0 && elementoPerimitido > 0)) {
				double x = membroLivre / elementoPerimitido;
				if (x < quociente) {
					quociente = x;
					linhaPermitida = i;
				}
			}
		}

		return linhaPermitida;
	}

	public double[] getValoresVariaveis() {
		double[] valores = new double[variaveisBasicas.length + variaveisNaoBasicas.length + 1];

		valores[0] = tabelaSimplex[0][0].getTop();
		for (int i = 0; i < variaveisBasicas.length; i += 1) {
			int index = variaveisBasicas[i];
			valores[index + 1] = tabelaSimplex[i + 1][0].getTop();
		}

		for (int j = 0; j < variaveisNaoBasicas.length; j += 1) {
			int index = variaveisNaoBasicas[j];
			valores[index + 1] = tabelaSimplex[0][j + 1].getTop();
		}

		return valores;
	}

	public Celula[][] getSimplexTable() {
		return tabelaSimplex;
	}

	public int[] getBasicVariables() {
		return variaveisBasicas;
	}

	public int[] getNonBasicVariables() {
		return variaveisNaoBasicas;
	}
}
