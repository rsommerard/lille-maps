package com.sommerard.lillemap;
import java.awt.Color;
import java.awt.Graphics;

public class Etalage extends Emplacement {

	public Etalage() {
		this(0, 0);
	}

	public Etalage(int x, int y) {
		super(x, y, Color.red);
	}

	public void affiche(Graphics g) {
		g.setColor(this.couleur);
		g.fillRect(this.x, this.y, Emplacement.DIMENSION, Emplacement.DIMENSION);

		g.setColor(Color.black);
		g.drawRect(this.x, this.y, Emplacement.DIMENSION, Emplacement.DIMENSION);
	}

}
