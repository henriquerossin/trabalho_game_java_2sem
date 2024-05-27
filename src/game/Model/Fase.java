package game.Model;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Fase extends JPanel implements ActionListener {

	private Image fundo;
	private Player player;
	private Timer timer;
	private List<Enemy> enemies;
	private List<Enemy2> enemies2;
	private List<Enemy3> enemies3;
	private List<Enemy4> enemies4;
	private boolean emJogo;
	private Score score;
	private Font fonteScore;

	private static final int NUMERO_MINIMO_INIMIGOS = 10;
	private static final int INTERVALO_NOVOS_INIMIGOS = 5000;
	private long ultimoTempoCriacaoInimigo = 0;

	public Fase() {
		setFocusable(true);
		setDoubleBuffered(true);

		ImageIcon referencia = new ImageIcon("res\\background.png");
		fundo = referencia.getImage();

		player = new Player();
		player.load();

		timer = new Timer(15, this);
		timer.start();

		addKeyListener(new TecladoAdapter());

		emJogo = true;
		score = new Score();
		fonteScore = new Font("Arial", Font.BOLD, 18);

		inicializaInimigos();
	}

	public void inicializaInimigos() {
		enemies = new ArrayList<>();
		enemies2 = new ArrayList<>();
		enemies3 = new ArrayList<>();
		enemies4 = new ArrayList<>();

		for (int i = 0; i < NUMERO_MINIMO_INIMIGOS / 4; i++) {
			// Gera coordenadas x e y dentro dos limites da tela para Enemy4
			int xEnemy4 = (int) (Math.random() * (getWidth() - 768) + 768);
			int yEnemy4 = (int) (Math.random() * (getHeight() - 30) + 30);
			enemies4.add(new Enemy4(xEnemy4, yEnemy4));

			// Gera coordenadas x e y dentro dos limites da tela para Enemy1
			int xEnemy = (int) (Math.random() * (getWidth() - 768) + 768);
			int yEnemy = (int) (Math.random() * (getHeight() - 30) + 30);
			enemies.add(new Enemy(xEnemy, yEnemy));

			// Gera coordenadas x e y dentro dos limites da tela para Enemy2
			int xEnemy2 = (int) (Math.random() * (getWidth() - 768) + 768);
			int yEnemy2 = (int) (Math.random() * (getHeight() - 30) + 30);
			enemies2.add(new Enemy2(xEnemy2, yEnemy2));

			// Gera coordenadas x e y dentro dos limites da tela para Enemy3
			int xEnemy3 = (int) (Math.random() * (getWidth() - 768) + 768);
			int yEnemy3 = (int) (Math.random() * (getHeight() - 30) + 30);
			enemies3.add(new Enemy3(xEnemy3, yEnemy3));
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D graficos = (Graphics2D) g;
		if (emJogo) {
			graficos.drawImage(fundo, 0, 0, null);
			graficos.drawImage(player.getImagem(), player.getX(), player.getY(), this);

			List<Tiro> tiros = player.getTiros();
			for (Tiro tiro : tiros) {
				tiro.load();
				graficos.drawImage(tiro.getImagem(), tiro.getX(), tiro.getY(), this);
			}

			for (Enemy enemy : enemies) {
				enemy.load();
				graficos.drawImage(enemy.getImagem(), enemy.getX(), enemy.getY(), this);
			}

			for (Enemy2 enemy2 : enemies2) {
				enemy2.load();
				graficos.drawImage(enemy2.getImagem(), enemy2.getX(), enemy2.getY(), this);
			}

			for (Enemy3 enemy3 : enemies3) {
				enemy3.load();
				graficos.drawImage(enemy3.getImagem(), enemy3.getX(), enemy3.getY(), this);
			}

			for (Enemy4 enemy4 : enemies4) {
				enemy4.update(); // Atualiza a posição do Enemy4
				enemy4.load();
				graficos.drawImage(enemy4.getImagem(), enemy4.getX(), enemy4.getY(), this);
			}

			graficos.setFont(fonteScore);
			graficos.drawString("Pontuação: " + score.getPontos(), 10, 20);
		} else {
			ImageIcon gameover = new ImageIcon("res\\gameover.png");
			graficos.drawImage(gameover.getImage(), 0, 0, null);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		player.update();

		List<Tiro> tiros = player.getTiros();
		Iterator<Tiro> iterator = tiros.iterator();
		while (iterator.hasNext()) {
			Tiro tiro = iterator.next();
			if (tiro.isVisivel()) {
				tiro.update();
			} else {
				iterator.remove();
			}
		}

		List<Enemy> enemiesCopy = new ArrayList<>(enemies);
		for (Enemy enemy : enemiesCopy) {
			if (enemy.isVisivel()) {
				enemy.update();
			} else {
				enemies.remove(enemy);
			}
		}

		List<Enemy2> enemies2Copy = new ArrayList<>(enemies2);
		for (Enemy2 enemy2 : enemies2Copy) {
			if (enemy2.isVisivel()) {
				enemy2.update();
			} else {
				enemies2.remove(enemy2);
			}
		}

		List<Enemy3> enemies3Copy = new ArrayList<>(enemies3);
		for (Enemy3 enemy3 : enemies3Copy) {
			if (enemy3.isVisivel()) {
				enemy3.update();
			} else {
				enemies3.remove(enemy3);
			}
		}

		List<Enemy4> enemies4Copy = new ArrayList<>(enemies4);
		for (Enemy4 enemy4 : enemies4Copy) {
			if (enemy4.isVisivel()) {
				enemy4.update();
			} else {
				enemies4.remove(enemy4);
			}
		}

		checarColisoes();
		adicionarNovosInimigos();
		repaint();

		if (!player.isVisivel()) {
			emJogo = false;
		}
	}

	public void checarColisoes() {
		Rectangle formaPlayer = player.getBounds();
		Rectangle formaEnemy;
		Rectangle formaEnemy2;
		Rectangle formaEnemy3;
		Rectangle formaEnemy4;

		Iterator<Enemy> iteratorEnemy = enemies.iterator();
		while (iteratorEnemy.hasNext()) {
			Enemy tempEnemy = iteratorEnemy.next();
			formaEnemy = tempEnemy.getBounds();
			if (formaPlayer.intersects(formaEnemy)) {
				player.setVisivel(false);
				tempEnemy.setVisivel(false);
			}

			List<Tiro> tiros = player.getTiros();
			for (Tiro tiro : tiros) {
				Rectangle formaTiro = tiro.getBounds();
				if (formaTiro.intersects(formaEnemy)) {
					tempEnemy.setVisivel(false);
					tiro.setVisivel(false);
					score.incrementaPontos(10);
				}
			}
		}

		Iterator<Enemy2> iteratorEnemy2 = enemies2.iterator();
		while (iteratorEnemy2.hasNext()) {
			Enemy2 tempEnemy2 = iteratorEnemy2.next();
			formaEnemy2 = tempEnemy2.getBounds();
			if (formaPlayer.intersects(formaEnemy2)) {
				player.setVisivel(false);
				tempEnemy2.setVisivel(false);
			}

			List<Tiro> tiros = player.getTiros();
			for (Tiro tiro : tiros) {
				Rectangle formaTiro = tiro.getBounds();
				if (formaTiro.intersects(formaEnemy2)) {
					tempEnemy2.setVisivel(false);
					tiro.setVisivel(false);
					score.incrementaPontos(10);
				}
			}
		}
		Iterator<Enemy3> iteratorEnemy3 = enemies3.iterator();
		while (iteratorEnemy3.hasNext()) {
			Enemy3 tempEnemy3 = iteratorEnemy3.next();
			formaEnemy3 = tempEnemy3.getBounds();
			if (formaPlayer.intersects(formaEnemy3)) {
				player.setVisivel(false);
				tempEnemy3.setVisivel(false);
			}

			List<Tiro> tiros = player.getTiros();
			for (Tiro tiro : tiros) {
				Rectangle formaTiro = tiro.getBounds();
				if (formaTiro.intersects(formaEnemy3)) {
					tempEnemy3.setVisivel(false);
					tiro.setVisivel(false);
					score.incrementaPontos(10);
				}
			}
		}
		Iterator<Enemy4> iteratorEnemy4 = enemies4.iterator();
	    while (iteratorEnemy4.hasNext()) {
	        Enemy4 tempEnemy4 = iteratorEnemy4.next();
	        formaEnemy4 = tempEnemy4.getBounds();
	        if (formaPlayer.intersects(formaEnemy4)) {
	            player.setVisivel(false);
	            tempEnemy4.setVisivel(false);
	        }

	        List<Tiro> tiros = player.getTiros();
	        for (Tiro tiro : tiros) {
	            Rectangle formaTiro = tiro.getBounds();
	            if (formaTiro.intersects(formaEnemy4)) {
	                tempEnemy4.setVisivel(false);
	                tiro.setVisivel(false);
	                score.incrementaPontos(10);
	            }
	        }
	    }
	}

	private void adicionarNovosInimigos() {
		long tempoAtual = System.currentTimeMillis();
		if (tempoAtual - ultimoTempoCriacaoInimigo > INTERVALO_NOVOS_INIMIGOS) {
			List<Enemy> novosInimigos = new ArrayList<>();
			List<Enemy2> novosInimigos2 = new ArrayList<>();
			List<Enemy3> novosInimigos3 = new ArrayList<>();
			List<Enemy4> novosInimigos4 = new ArrayList<>(); // Adicionando a lista novosInimigos4
			int quantidadeNovosInimigos = 30;

			for (int i = 0; i < quantidadeNovosInimigos; i++) {
				int x = (int) (Math.random() * 8000 + 768);
				int y = (int) (Math.random() * 650 + 30);
				novosInimigos.add(new Enemy(x, y));

				int xEnemy2 = (int) (Math.random() * 8000 + 768);
				int yEnemy2 = (int) (Math.random() * 650 + 30);
				novosInimigos2.add(new Enemy2(xEnemy2, yEnemy2));

				int xEnemy3 = (int) (Math.random() * 8000 + 768);
				int yEnemy3 = (int) (Math.random() * 650 + 30);
				novosInimigos3.add(new Enemy3(xEnemy3, yEnemy3));

				int xEnemy4 = (int) (Math.random() * 8000 + 768);
				int yEnemy4 = (int) (Math.random() * 650 + 30);
				novosInimigos4.add(new Enemy4(xEnemy4, yEnemy4)); // Adicionando novos inimigos4 à lista
			}

			enemies.addAll(novosInimigos);
			enemies2.addAll(novosInimigos2);
			enemies3.addAll(novosInimigos3);
			enemies4.addAll(novosInimigos4); // Adicionando novos inimigos4 à lista enemies4
			ultimoTempoCriacaoInimigo = tempoAtual;
		}
	}

	private class TecladoAdapter extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			player.keyPressed(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			player.keyReleased(e);
		}
	}
}
