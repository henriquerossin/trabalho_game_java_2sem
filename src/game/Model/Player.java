package game.Model;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public class Player {

	private int x, y;
	private int dx, dy;
	private Image imagem;
	private int altura, largura;
	private List<Tiro> tiros;
	private boolean isVisivel;

	public Player() {
		this.x = 100;
		this.y = 100;
		isVisivel = true;

		tiros = new ArrayList<Tiro>();
	}

	public void load() {
		ImageIcon referencia = new ImageIcon("res\\caminhao.gif");
		imagem = referencia.getImage();
		Player caminhao = new Player();
		altura = imagem.getHeight(null);
		largura = imagem.getWidth(null);
	}

	public void update() {
		x += dx;
		y += dy;
	}

	public void tiroSimples() {
		this.tiros.add(new Tiro(x + largura, y + (altura / 2)));
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, largura, altura);
	}

	public void keyPressed(KeyEvent tecla) {
		int codigo = tecla.getKeyCode();

		if (codigo == KeyEvent.VK_A) {
			tiroSimples();
		}

		if (codigo == KeyEvent.VK_UP) {
			dy = -6;
		}

		if (codigo == KeyEvent.VK_DOWN) {
			dy = 6;
		}

		if (codigo == KeyEvent.VK_LEFT) {
			dx = -6;
		}

		if (codigo == KeyEvent.VK_RIGHT) {
			dx = 6;
		}
	}

	public void keyReleased(KeyEvent tecla) {
		int codigo = tecla.getKeyCode();

		if (codigo == KeyEvent.VK_UP) {
			dy = 0;
		}

		if (codigo == KeyEvent.VK_DOWN) {
			dy = 0;
		}

		if (codigo == KeyEvent.VK_LEFT) {
			dx = 0;
		}

		if (codigo == KeyEvent.VK_RIGHT) {
			dx = 0;
		}
	}

	public boolean isVisivel() {
		return isVisivel;
	}

	public void setVisivel(boolean isVisivel) {
		this.isVisivel = isVisivel;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getDx() {
		return dx;
	}

	public void setDx(int dx) {
		this.dx = dx;
	}

	public int getDy() {
		return dy;
	}

	public void setDy(int dy) {
		this.dy = dy;
	}

	public Image getImagem() {
		return imagem;
	}

	public void setImagem(Image imagem) {
		this.imagem = imagem;
	}

	public int getAltura() {
		return altura;
	}

	public void setAltura(int altura) {
		this.altura = altura;
	}

	public int getLargura() {
		return largura;
	}

	public void setLargura(int largura) {
		this.largura = largura;
	}

	public List<Tiro> getTiros() {
		return tiros;
	}

}
