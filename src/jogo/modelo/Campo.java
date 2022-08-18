package jogo.modelo;

import java.util.ArrayList;
import java.util.List;

import jogo.excecao.ExplosaoException;

//Classe que define os campos do tabuleiro
public class Campo {

	// Define o campo está com uma mina ou não 
	private boolean minado = false;
	
	/* 
	 * Define se o campo estará aberto ou fechado
	 * a parti das jogadas do jogador
	 * 
	 * Inicia como sendo 'false', pois o campo só
	 * será aberto com uma jogada, até isso acontecer,
	 * o campo fica fechado.
	*/ 
	private boolean aberto = false;
	
	// Define a marcação feita pelo jogado ao 
	// sinalizar se um campo está ou não minado;
	private boolean marcado = false;
	
	// Duas variaveis que irão ser as coordenadas
	// do tabuleiro.
	private final int linhaX;
	private final int colunaY;
	
	// Define a área que é expandida ao redor do campo no 
	// qual o jogador clicou quando fez sua jogada.
	private List<Campo> vizinhos = new ArrayList<Campo>();
	
	//Metodo construtor 
	Campo( int linhaX, int colunaY){
		this.linhaX = linhaX;
		this.colunaY = colunaY;
	}
	
	/* Defini a logica para um campo ser vizinho ou não
	 * 
	 *  Por exemplo, define que o campo 0x0 seja vizinho do
	 *  campo 0x1 e impede que seja vizinho do campo 10x5;
	 */
	boolean adicionarVizinho(Campo vizinho) {
		
		boolean linhaDiferente = linhaX != vizinho.linhaX;
		boolean colunaDiferente = colunaY != vizinho.colunaY;
		boolean diagonal = linhaDiferente && colunaDiferente;
		
		// Linha ou Coluna, diferença numero = 1
		// Diagonal, diferença numerica = 2;
		int deltaLinha = Math.abs(linhaX - vizinho.linhaX); 
		int deltaColuna = Math.abs(colunaY - vizinho.colunaY); 
		int deltaGeral = deltaColuna + deltaLinha;
		
		/*
		 *  Verifica, define e adciona o campo como vizinho 
		 *  lateral (mesma linhaX) e/ou vertical (mesma colunaY).
		 *  
		 *	Também Verifica, define e adciona o campo como vizinho
		 *  diagonal (1 coluna e linha a menos ou a mais)
		 */	
		if (deltaGeral == 1 && !diagonal) {
			vizinhos.add(vizinho);
			return true;
		} else if (deltaGeral == 2 && diagonal) {
			vizinhos.add(vizinho);
			return true;
		} else {
			return false;
		}
	}
	
	// Marcaçao de um campo pelo jogador
	void alternarMarcacao() {
		if(!aberto) {
			// alternancia da marcaçao
			marcado = !marcado;
		}
	}
	
	// impede que o jogador abra um campo enquanto
	// a marcação que o mesmo jogador pôs estiver valendo
	boolean abir() {
		if (!aberto && !marcado) {
			aberto = true;
			
			// Se o jogador entrar nessa exeção o jogo está
			// perdido pois acionou uma mina.
			if (minado) {
				throw new ExplosaoException();
			}
			
			// Se o jogador entrar nessa exeção uma area
			// segura será aberta
			if (vizinhacaSegura()) {
				vizinhos.forEach(v -> v.abir());
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 *  garante a área de espansão do campo quando o jogador
	 *  faz sua jogada garantido que durante a abertura da área
	 *  não seja acionada uma mina, apenas se caso o jogador 
	 *  click em um campo com uma mina.
	 */
	boolean vizinhacaSegura() {
		 /*
		  * Se o rsultado for false, significa que ao menos
		  * um dos vizinhos está minado
		  * Se caso o reultado for true, significa que a 
		  * vizinhança está seguro, pois nenhum campo pode
		  * dar match no outro
		  */
		 return vizinhos.stream().noneMatch(v -> v.minado);
	}
	
	//metodo que mina um campo
	void minar() {
		minado = true;
	}
	
	public boolean isMinado() {
		return minado;
	}
	
	public boolean isMarcado() {
		return marcado;
	}
	
	void setAberto(boolean aberto) {
		this.aberto = aberto;
	}

	public boolean isAberto() {
		return aberto;
	}
	
	public boolean isFechado() {
		return !isAberto();
	}

	public int getLinhaX() {
		return linhaX;
	}

	public int getColunaY() {
		return colunaY;
	}
	
	// nesse metodo é determinado a vitoria do jogado
	// caso ele consiga marca todos os campos com minas
	boolean objetivoAlcancado() {
		// Atigindo um dos objetivos, é conquitado um dos
		// objetivos do jogo para UM DOS CAMPOS
		boolean desvendado = !minado && aberto;
		boolean protegido = minado && marcado;
		
		return desvendado || protegido;
	}
	
	// metodo que mostrara os numeros nas vizinhancas do campos
	// qunado o jogador abre
	long minasNaVizinhanca() {
		// Filter é usado para filtrar apenas os vizinhos
		// que têm minas.
		return vizinhos.stream().filter(v -> v.minado).count();
	}
	
	/*
	 *  reinicia o jogo
	 *  --> A logica para a colocação aleatorias das minas e 
	 *  da restauração do campo está na classe Tabuleiro <--
	 */
	void reiniciar() {
		aberto = false;
		minado = false;
		marcado = false;
	}
	
	public String toString() {
		if (marcado) {
			return "X"; // marcação
		} else if (aberto && minado) {
			return "*"; //bomba
		} else if (aberto && minasNaVizinhanca() > 0) {
			return Long.toString(minasNaVizinhanca()); // numero de minas
		} else if (aberto) {
			return " "; // área de campo aberto 
		} else {
			return "?"; // campo fechado que não foi mechido ainda
		}
	}
}
