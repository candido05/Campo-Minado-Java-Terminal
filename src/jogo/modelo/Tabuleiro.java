package jogo.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import jogo.excecao.ExplosaoException;

public class Tabuleiro {

	private int linhas; // tamanho horizontal do tabuleiro
	private int colunas; // tamanho vertical do tabuleiro
	private int minas; // quantidade de minas
	
	// Lista de campos que serão abertos pelo jogador
	private final List<Campo> campos = new ArrayList<>();
	
    // Contrutor público pois será criado um tabuleiro 
	// fora do Package Modelo	
	public Tabuleiro(int linhas, int colunas, int minas) {
		
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;
		
		// são execultados na primeira vez que o codigo inicializa
		gerarCampo();
		associarizinhos();
		sortearMinas();
	}
	
	/*
	 * esse metodo abre o campo quando o jogador faz sua jogada
	 * abrindo um campo e por consequência, uma área do tabuleiro
	 * Se caso o jogador perca a partida acionando uma mina, todas
	 * as minas do campo serão revelados por esse metodo através da 
	 * logica implementada na execeção
	 */
	public void abrir(int linha, int coluna) {
		try {
			campos.parallelStream()
				.filter(c -> c.getLinhaX() == linha && c.getColunaY() == coluna)
				.findFirst()
				.ifPresent(c -> c.abir());;
				
				// essa execeção deve ser propagada para outas classes. 
		} catch (ExplosaoException e) {
			campos.forEach(c -> c.setAberto(true));
			throw e;
		}
	}
	
	// esse metodo permite o jogador marcar o campo no qual
	// ele acha que está minado.
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
	 * O metodo irá percorre duas vezes a lista e fará com que
	 * todos os campos sejam vizinhos e com isso determinar 
	 * a vizinhança no tabuleiro de acordo com a proximidade
	 */
	private void associarizinhos() {
		for (Campo c1 : campos) {
			for (Campo c2 : campos) {
				c1.adicionarVizinho(c2);
			}
		}
	}
	
	// metodo que irá por as minas no tabuleiro qaundo o jogo for inicializado
	private void sortearMinas() {
		
		long minasArmadas = 0;
		Predicate<Campo> minado = c -> c.isMinado();
		
		// defini o laço que irá ficar execultando até a quantidade
		// de minas armadas for menor que a quantiade de minas pré-determinadas
		do {
	
			/*
			 * calculando um valor aleatório para determinado o
			 * valor de minas de modo que seja completado o laço
			 * 'do while' e o tabuleiro seja preenchido com as minas
			 * de modo aleatório
			 */
			int aleatorio = (int) (Math.random() * campos.size());
			campos.get(aleatorio).minar();
			
			// Sabe-se a quantidade de minas armadas pela quantidade de
			// campos que tem o atributo 'minado' = true.
			minasArmadas = campos.stream().filter(minado).count();
			
		} while(minasArmadas < minas);
	}
	
	// Diz a vitória do jogador
	public boolean objetivoAlcancado() {
		/*
		 * Se todos os objetivos dos campos no metodo objetivoAlcancado()
		 * na classe Campo forem alcancados, 
		 * este metodo irá confirma a vitória do jogador conferindo cada 
		 * objetivo alcançado
		 */
		return campos.stream().allMatch(c -> c.objetivoAlcancado());
	}
	
	// reinicia o jogo, ou seja, os campos
	public void reiniciar() {
		campos.stream().forEach(c -> c.reiniciar());
		sortearMinas();
	}
	
	// representação do tabuleiro no terminal
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		// indices do tabuleiro que irão auxiliar o jogador
		// na visualização e jogabilidade
		sb.append("  ");
		for (int i = 0; i < colunas; i++) {
			sb.append(" ");
			sb.append(i); // indices da coluna
			sb.append(" ");
		}

		sb.append("\n");
		
		int k = 0; // indice que representa o valor do campo na impressão
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
