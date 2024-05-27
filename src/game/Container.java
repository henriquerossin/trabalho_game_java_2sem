package game;

import javax.swing.JFrame;

import game.Model.Fase;

public class Container extends JFrame {

	public Container() {
		add(new Fase());
		setTitle("Game APS");
		setSize(1030, 1030);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		this.setResizable(false);
		setVisible(true);
	}

	public static void main(String[] args) {

		new Container();
	}
}
