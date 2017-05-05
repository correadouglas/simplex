package br.pucminas.simplex;

public class Celula {
	
	private double top, bottom;

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
