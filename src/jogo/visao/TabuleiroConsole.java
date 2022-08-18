package jogo.visao;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

import jogo.excecao.ExplosaoException;
import jogo.excecao.SairException;
import jogo.modelo.Tabuleiro;

public class TabuleiroConsole {

	/*
	 *  Essa classe será usada na Aplicação, pois uma
	 *  vez chamando esta classe, o jogo inicializa 
	 *  na classe main
	 */
	
	private Tabuleiro tabuleiro;
	
	// Scanener para pedir as informações do jogado no terminal
	private Scanner entrada = new Scanner(System.in);

	public TabuleiroConsole(Tabuleiro tabuleiro) {
		this.tabuleiro = tabuleiro;
		
		execultarJogo();
	}
	
	// Fica testando a permanência do jogador no jogo
	private void execultarJogo() {
		
		// dentro do try o jogo é reiniciado a parti da vontade do jogador
		// respondendo uma pergunta se quer ou não continuar
		try {
			
			boolean continuar = true;
			
			while(continuar) {
				
				cicloDoJogo();
				
				System.out.println("Outra partida? [ S / n ]");
				String resposta = entrada.nextLine();
				
				if ("n".equalsIgnoreCase(resposta)) {
					continuar = false;
				} else {
					tabuleiro.reiniciar();
				}
			}
			
		} catch (SairException e) {
			System.out.println("Tchauuuu!! :)");
		} finally {
			entrada.close();
		}
		
	}

	// Nesse metodo terá o looping que ficará pergunta do ao jogador
	// qual a linha, qual a coluna, se ele quer marcar e/ou desmarcar 
	private void cicloDoJogo() {
		
		try {
			
			// Esse laço while se basea se o objetivo do jogo 
			// foi ou não alcançado
			while(!tabuleiro.objetivoAlcancado()) {
				System.out.println(tabuleiro.toString());
				
				String digitado = capturarValorDigitado("Digite [ X, Y ] ou [SAIR]: ");
				
				/*
				 * Gerando uma stream a parti de um array e gerando essa stream,
				 * pode-se um map para tranformar cada um dos elementos que são
				 * do tipo String e transfoma-los em Integer
				 */
				
				Iterator<Integer> xy = Arrays.stream(digitado.split(","))
						.map(e -> Integer.parseInt(e.trim())).iterator(); // pega a string,
																   // quebra a virgula e fica
																   // com os valores numericos 

				digitado = capturarValorDigitado("1 -- ABRIR // 2 -- (DES)MARCAR: ");
				
				// Lógica dos comandos dados pelo jogador
				if("1".equals(digitado)) {
					// abre os campos
					tabuleiro.abrir(xy.next(), xy.next());
				} else if ("2".equals(digitado)) {
					// marca ou desmarca um campo
					tabuleiro.marcar(xy.next(), xy.next());
				}
				
			}
			
			System.out.println("Você ganhou!!!! :) GRANDE MITADA SUPER BASED CHAD");
		} catch (ExplosaoException e) {
			// mostra o tabuleiro com todos as bombas reveladas quando o 
			// jogador perde a partida
			System.out.println(tabuleiro);
			System.out.println("Você perdeu, mais sorte na próxima partida\n");
		}
	}
	
	// Este metodo vai receber um texto que será exibido para o usuario
	private String capturarValorDigitado(String texto) {
		System.out.print(texto);
		String digitado = entrada.nextLine();
		
		// lança uma execeção caso o jogador digite "sair" 
		// para que possa sair do jogo
		if ("sair".equalsIgnoreCase(digitado)) {
			throw new SairException();
		}
		
		return digitado;
	}
}
