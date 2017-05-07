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
public class SimplexTabela {
	
	SimplexExpressao e;
	private SimplexCelula[][] tabelaSimplex;
	private int[] variaveisBasicas;
	private int[] variaveisNaoBasicas;

	/**
	 * construtor padrao.
	 * 
	 */
	public SimplexTabela(SimplexExpressao e) {
		this.e = e;
		this.criarTabelaSimplex();
		this.preencherTabelaObjetivo();
		this.preencherRestricoes();
		this.setVariaveisNaoBasicas();
		this.setVariaveisBasicas();
	}

	/**
	 * cria a tabela do simplex.
	 */
	private void criarTabelaSimplex() {
		this.variaveisBasicas = new int[e.contadorVariaveisBasicas()];
		this.variaveisNaoBasicas = new int[e.contadorVariveisNaoBasicas()];
		int rows = variaveisBasicas.length + 1;
		int cols = variaveisNaoBasicas.length + 1;
		tabelaSimplex = new SimplexCelula[rows][cols];
	}
	
	/**
	 * preenche a tabela do simplex com os valores da funcao objetivo.
	 */
	private void preencherTabelaObjetivo() {
		tabelaSimplex[0][0] = new SimplexCelula();

		double[] funcaoObjetivo = e.getFuncaoObjetivo();
		int multiplicador;
		if (e.getObjetivo() == SimplexExpressao.MAX) {
			multiplicador = 1;
		} else {
			multiplicador = -1;
		}

		for (int i = 1; i <= funcaoObjetivo.length; i += 1) {
			tabelaSimplex[0][i] = new SimplexCelula(funcaoObjetivo[i - 1] * multiplicador);
		}
	}

	/**
	 * preenche as restricoes na tabela do simplex.
	 */
	private void preencherRestricoes() {
		for (int i = 1; i <= variaveisBasicas.length; i += 1) {
			int multiplicador;
			if (e.getSinalRestricao(i - 1) == SimplexExpressao.MAIOR_QUE) {
				multiplicador = -1;
			} else {
				multiplicador = 1;
			}
			tabelaSimplex[i][0] = new SimplexCelula(e.getB(i - 1) * multiplicador);
			for (int j = 1; j <= variaveisNaoBasicas.length; j += 1) {
				tabelaSimplex[i][j] = new SimplexCelula(e.getConstraint(i - 1, j - 1) * multiplicador);
			}
		}
	}
	
	/**
	 * seta as variaveis basicas.
	 */
	private void setVariaveisBasicas() {
		for (int i = 0; i < variaveisBasicas.length; i += 1) {
			variaveisBasicas[i] = i + variaveisNaoBasicas.length;
		}
	}
	
	/**
	 * seta as variaveis nao basicas.
	 */
	private void setVariaveisNaoBasicas() {
		for (int i = 0; i < variaveisNaoBasicas.length; i += 1) {
			variaveisNaoBasicas[i] = i;
		}
	}
	
	/** pega os indices dos membros livres que sao negativos.
	 * 
	 */
	public int getIndiceMembrosLivresNegativos() {
		for (int i = 1; i < tabelaSimplex.length; i += 1) {
			if (tabelaSimplex[i][0].getTop() < 0) { 
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * pega o indice da funcao positiva.
	 */
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

	/**
	 * pega a coluna com elemento negativo na linha passada.
	 * 
	 * @param linha
	 * @return
	 */
	public int getColunaComElementoNegativo(int linha) {
		for (int i = 1; i < tabelaSimplex[linha].length; i += 1) {
			if (tabelaSimplex[linha][i].getTop() < 0) { 
				return i;
			}
		}
		return -1;
	}

	/**
	 * multiplica os elementos da linha e coluna selecionada pelo inverso 
	 * multiplica as celulas abaixo e refaz a tabela, e troca as variaveis  
	 * nao basicas com as basicas.
	 * 
	 * @param linhaPermitida
	 * @param colunaPermitida
	 */
	public void mudarAlgoritmo(int linhaPermitida, int colunaPermitida) {
		double elementoPermitido = tabelaSimplex[linhaPermitida][colunaPermitida].getTop();

		double inverso = Math.pow(elementoPermitido, -1);
		tabelaSimplex[linhaPermitida][colunaPermitida].setBottom(inverso);

		multiplicarLinhaPermitidaPeloInverso(linhaPermitida, colunaPermitida, inverso);
		
		multiplicarColunaPermitidaPeloInversoNegativo(linhaPermitida, colunaPermitida, inverso);

		multiplicarCelulasAbaixo(linhaPermitida, colunaPermitida);

		refazerTabela(linhaPermitida, colunaPermitida);

		trocarVariaveisNaoBasicasComBasicas(linhaPermitida, colunaPermitida);

	}

	/**
	 * multiplica as linhas pelo inverso.
	 * 
	 * @param linhaPermitida
	 * @param colunaPermitida
	 * @param inverso
	 */
	private void multiplicarLinhaPermitidaPeloInverso(int linhaPermitida, int colunaPermitida, double inverso) {
		for (int j = 0; j < tabelaSimplex[0].length; j += 1) {
			if (j != colunaPermitida) { 
				tabelaSimplex[linhaPermitida][j].setBottom((tabelaSimplex[linhaPermitida][j].getTop()) * inverso);
			}
		}
	}

	/**
	 * multiplica coluna permitida pelo inverso negativo.
	 * 
	 * @param linhaPermitida
	 * @param colunaPermitida
	 * @param inverso
	 */
	private void multiplicarColunaPermitidaPeloInversoNegativo(int linhaPermitida, int colunaPermitida, double inverso) {
		for (int i = 0; i < tabelaSimplex.length; i += 1) {
			if (i != linhaPermitida) { 
				tabelaSimplex[i][colunaPermitida].setBottom((tabelaSimplex[i][colunaPermitida].getTop()) * -inverso);
			}
		}
	}

	/**
	 * multiplica as celulas abaixo.
	 * 
	 * @param linhaPermitida
	 * @param colunaPermitida
	 */
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

	/**
	 * refaz a tabela do simplex.
	 * 
	 * @param linhaPermitida
	 * @param colunaPermitida
	 */
	private void refazerTabela(int linhaPermitida, int colunaPermitida) {
		SimplexCelula[][] tabela = new SimplexCelula[tabelaSimplex.length][tabelaSimplex[0].length];

		copiaCelulas(tabela, linhaPermitida, colunaPermitida);
		somaCelulas(tabela, linhaPermitida, colunaPermitida);

		tabelaSimplex = tabela;
	}

	/**
	 * copia as celulas.
	 * 
	 * @param tabela
	 * @param linhaPermitida
	 * @param colunaPermitida
	 */
	private void copiaCelulas(SimplexCelula[][] tabela, int linhaPermitida, int colunaPermitida) {
		for (int i = 0; i < tabela.length; i += 1) {
			tabela[i][colunaPermitida] = new SimplexCelula(tabelaSimplex[i][colunaPermitida].getBottom());
		}

		for (int j = 0; j < tabela[0].length; j += 1) {
			tabela[linhaPermitida][j] = new SimplexCelula(tabelaSimplex[linhaPermitida][j].getBottom());
		}
	}

	/**
	 * soma as celulas.
	 * 
	 * @param tabela
	 * @param linhaPermitida
	 * @param colunaPermitida
	 */
	private void somaCelulas(SimplexCelula[][] tabela, int linhaPermitida, int colunaPermitida) {

		for (int i = 0; i < tabela.length; i += 1) {
			if (i != linhaPermitida) {
				for (int j = 0; j < tabela[i].length; j += 1) {
					if (j != colunaPermitida) {
						double value = tabelaSimplex[i][j].getTop() + tabelaSimplex[i][j].getBottom();
						tabela[i][j] = new SimplexCelula(value);
					}
				}
			}
		}
	}

	/**
	 * metodo que faz a troca das variaveis nao basicas com as basicas.
	 * 
	 * @param linhaPermitida
	 * @param colunaPermitida
	 */
	private void trocarVariaveisNaoBasicasComBasicas(int linhaPermitida, int colunaPermitida) {
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

	/**
	 * pega os valores das variaveis.
	 * 
	 * @return
	 */
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

	public SimplexCelula[][] getTabelaSimplex() {
		return tabelaSimplex;
	}

	public int[] getVariaveisBasicas() {
		return variaveisBasicas;
	}

	public int[] getVariaveisNaoBasicas() {
		return variaveisNaoBasicas;
	}
}
