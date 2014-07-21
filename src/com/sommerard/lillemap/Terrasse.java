package com.sommerard.lillemap;
import java.awt.Color;
import java.awt.Graphics;

public class Terrasse extends Emplacement {

	public Terrasse() {
		this(0, 0);
	}

	public Terrasse(int x, int y) {
		super(x, y, Color.green);
	}

	public void affiche(Graphics g) {
		g.setColor(this.couleur);
		g.fillOval(this.x, this.y, Emplacement.DIMENSION, Emplacement.DIMENSION);

		g.setColor(Color.black);
		g.drawOval(this.x, this.y, Emplacement.DIMENSION, Emplacement.DIMENSION);
	}

}
