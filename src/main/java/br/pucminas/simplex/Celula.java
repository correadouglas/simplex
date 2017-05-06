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
public class Celula {
	
	private double top, bottom;
	
	/*
	 * construtor padrao da classe auxiliar.
	 */
	public Celula(){
		this.top = 0;	
		this.bottom = 0;
	}

	public Celula(double top){
		this.top = top;	
		this.bottom = 0;
	}

	public void setTop(double top) {
		this.top = top;
	}

	public void setBottom(double bottom) {
		this.bottom = bottom;
	}

	public double getTop() {
		return this.top;
	}

	public double getBottom() {
		return this.bottom;
	}

	public void cloneBottomToTop() {
		this.top = this.bottom;
	}

	public void cloneTopToBottom() {
		this.bottom = this.top;
	}
}
