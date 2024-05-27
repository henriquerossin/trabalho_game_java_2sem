package game.Model;

public class Score {

	private int pontos;

	public Score() {
		this.pontos = 0;
	}

	public int getPontos() {
		return pontos;
	}

	public void incrementaPontos(int valor) {
		this.pontos += valor;
	}
}
