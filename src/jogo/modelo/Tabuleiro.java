package jogo.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import jogo.excecao.ExplosaoException;

public class Tabuleiro {

	private int linhas; // tamanho horizontal do tabuleiro
	private int colunas; // tamanho vertical do tabuleiro
	private int minas; // quantidade de minas
	
	// Lista de campos que ser�o abertos pelo jogador
	private final List<Campo> campos = new ArrayList<>();
	
    // Contrutor p�blico pois ser� criado um tabuleiro 
	// fora do Package Modelo	
	public Tabuleiro(int linhas, int colunas, int minas) {
		
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;
		
		// s�o execultados na primeira vez que o codigo inicializa
		gerarCampo();
		associarizinhos();
		sortearMinas();
	}
	
	/*
	 * esse metodo abre o campo quando o jogador faz sua jogada
	 * abrindo um campo e por consequ�ncia, uma �rea do tabuleiro
	 * Se caso o jogador perca a partida acionando uma mina, todas
	 * as minas do campo ser�o revelados por esse metodo atrav�s da 
	 * logica implementada na exece��o
	 */
	public void abrir(int linha, int coluna) {
		try {
			campos.parallelStream()
				.filter(c -> c.getLinhaX() == linha && c.getColunaY() == coluna)
				.findFirst()
				.ifPresent(c -> c.abir());;
				
				// essa exece��o deve ser propagada para outas classes. 
		} catch (ExplosaoException e) {
			campos.forEach(c -> c.setAberto(true));
			throw e;
		}
	}
	
	// esse metodo permite o jogador marcar o campo no qual
	// ele acha que est� minado.
	public void marcar(int linha, int coluna) {
		campos.parallelStream()
			.filter(c -> c.getLinhaX() == linha && c.getColunaY() == coluna)
			.findFirst()
			.ifPresent(c -> c.alternarMarcacao());;
				
	}

	// metodo que gera os campos basedo nas linhas e colunas
	private void gerarCampo() {
		for (int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				campos.add(new Campo(i, j));
			}
		}
	}
	
	/*
	 * O metodo ir� percorre duas vezes a lista e far� com que
	 * todos os campos sejam vizinhos e com isso determinar 
	 * a vizinhan�a no tabuleiro de acordo com a proximidade
	 */
	private void associarizinhos() {
		for (Campo c1 : campos) {
			for (Campo c2 : campos) {
				c1.adicionarVizinho(c2);
			}
		}
	}
	
	// metodo que ir� por as minas no tabuleiro qaundo o jogo for inicializado
	private void sortearMinas() {
		
		long minasArmadas = 0;
		Predicate<Campo> minado = c -> c.isMinado();
		
		// defini o la�o que ir� ficar execultando at� a quantidade
		// de minas armadas for menor que a quantiade de minas pr�-determinadas
		do {
	
			/*
			 * calculando um valor aleat�rio para determinado o
			 * valor de minas de modo que seja completado o la�o
			 * 'do while' e o tabuleiro seja preenchido com as minas
			 * de modo aleat�rio
			 */
			int aleatorio = (int) (Math.random() * campos.size());
			campos.get(aleatorio).minar();
			
			// Sabe-se a quantidade de minas armadas pela quantidade de
			// campos que tem o atributo 'minado' = true.
			minasArmadas = campos.stream().filter(minado).count();
			
		} while(minasArmadas < minas);
	}
	
	// Diz a vit�ria do jogador
	public boolean objetivoAlcancado() {
		/*
		 * Se todos os objetivos dos campos no metodo objetivoAlcancado()
		 * na classe Campo forem alcancados, 
		 * este metodo ir� confirma a vit�ria do jogador conferindo cada 
		 * objetivo alcan�ado
		 */
		return campos.stream().allMatch(c -> c.objetivoAlcancado());
	}
	
	// reinicia o jogo, ou seja, os campos
	public void reiniciar() {
		campos.stream().forEach(c -> c.reiniciar());
		sortearMinas();
	}
	
	// representa��o do tabuleiro no terminal
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		// indices do tabuleiro que ir�o auxiliar o jogador
		// na visualiza��o e jogabilidade
		sb.append("  ");
		for (int i = 0; i < colunas; i++) {
			sb.append(" ");
			sb.append(i); // indices da coluna
			sb.append(" ");
		}

		sb.append("\n");
		
		int k = 0; // indice que representa o valor do campo na impress�o
		for (int i = 0; i < linhas; i++) {
			sb.append(i); // indices das linhas
			sb.append(" ");
			for (int j = 0; j < colunas; j++) {
				sb.append(" ");
				sb.append(campos.get(k));
				sb.append(" ");
				k++;
			}
			sb.append("\n");
		}
		
		
		return sb.toString();
	}
}
