package jogo;

import jogo.modelo.Tabuleiro;
import jogo.visao.TabuleiroConsole;

public class Aplicacao {

	public static void main(String[] args) {
		
		Tabuleiro tabuleiro = new Tabuleiro(8, 8, 7);
		
		new TabuleiroConsole(tabuleiro);
	}
}
