package com.sommerard.lillemap;
import java.awt.Color;
import java.awt.Graphics;

public abstract class Emplacement {

	public static final int DIMENSION = 15;

	protected int x;
	protected int y;

	protected Color couleur;

	public Emplacement() {
		this(0, 0, null);
	}

	public Emplacement(int x, int y, Color uneCouleur) {
		this.couleur = uneCouleur;
		this.x = x;
		this.y = y;
	}

	public abstract void affiche(Graphics g);

	public int donneDistance(Emplacement unEmplacement) {
		int deltaX = Math.abs(this.x - unEmplacement.getX());
		int deltaY = Math.abs(this.y - unEmplacement.getY());

		double distance = Math.pow(deltaX, 2) + Math.pow(deltaY, 2);
		distance = Math.sqrt(distance);

		distance = (distance * 50) / 15;

		return (int) (distance);
	}

	public int getX() {
		return this.x;
	}

	public void setX(int unX) {
		this.x = unX;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int unY) {
		this.y = unY;
	}

	public Color getCouleur() {
		return this.couleur;
	}

	public void setCouleur(Color uneCouleur) {
		this.couleur = uneCouleur;
	}
}
