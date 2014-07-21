package com.sommerard.lillemap;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Plan extends JPanel implements MouseListener, MouseMotionListener,
		KeyListener, WindowListener {

	private static final long serialVersionUID = 3815809150533909143L;
	private List<Emplacement> lesEmplacements;
	private JFrame fenetre;
	private Image image;
	private int bordureHorizontale;
	private int bordureVerticale;
	private String plan;

	public Plan() {
		this.lesEmplacements = new ArrayList<Emplacement>();
		this.plan = "lille";

		fenetre = new JFrame("Administration CL - Plan");
		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetre.setLocationRelativeTo(null);
		fenetre.setContentPane(this);
		fenetre.setResizable(false);
		fenetre.pack();

		this.bordureHorizontale = fenetre.getSize().width
				- this.getSize().width;
		this.bordureVerticale = fenetre.getSize().height
				- this.getSize().height;

		this.chargePlan(this.plan);

		fenetre.setSize(image.getWidth(null) + this.bordureHorizontale,
				image.getHeight(null) + this.bordureVerticale);

		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		fenetre.addWindowListener(this);

		fenetre.setVisible(true);
	}

	private void ajouteEmplacement(Emplacement unEmplacement) {
		if (this.lesEmplacements.isEmpty()) {
			this.lesEmplacements.add(unEmplacement);
			this.repaint();
		} else {
			for (Emplacement emplacement : this.lesEmplacements) {
				if (emplacement.donneDistance(unEmplacement) < 50) {
					JOptionPane.showMessageDialog(null,
							"Impossible d'ajouter l'emplacement � cet endroit",
							"Erreur", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			this.lesEmplacements.add(unEmplacement);
			this.repaint();
		}
	}

	private void supprimeEmplacement(Emplacement unEmplacement) {
		if (this.lesEmplacements.contains(unEmplacement)) {
			this.lesEmplacements.remove(unEmplacement);
			this.repaint();
		}
	}

	public void paint(Graphics g) {
		g.drawImage(this.image, 0, 0, null);
		for (Emplacement unEmplacement : this.lesEmplacements)
			unEmplacement.affiche(g);
	}

	private void chargePlan(String nomPlan) {
		String cheminPlan;
		this.plan = nomPlan;
		if (this.plan.equals("lille"))
			cheminPlan = "data/maps/plan/" + this.plan + ".png";
		else {
			String cheminSauvegardePlan = "data/save/" + this.plan + ".xml";
			cheminPlan = "data/maps/plan/" + this.plan + ".png";
			@SuppressWarnings("unchecked")
			List<Emplacement> tmpArrayList = (ArrayList<Emplacement>) Serialize
					.decoder(cheminSauvegardePlan);
			if (tmpArrayList != null)
				this.lesEmplacements = tmpArrayList;
		}

		try {
			this.image = ImageIO.read(new File(cheminPlan));
		} catch (IOException e) {
			e.printStackTrace();
		}
		fenetre.setSize(this.image.getWidth(null) + this.bordureHorizontale,
				this.image.getHeight(null) + this.bordureVerticale);

		this.repaint();
	}

	private void sauvegardePlan() {
		if (!this.plan.equals("lille")) {
			String cheminSauvegardePlan = "data/save/" + this.plan + ".xml";
			Serialize.encoder(this.lesEmplacements, cheminSauvegardePlan);
			this.lesEmplacements.clear();
		}
	}

	private Emplacement surEmplacement(int x, int y) {
		int index = 0;
		int xMin, xMax;
		int yMin, yMax;
		while (index < this.lesEmplacements.size()) {
			xMin = this.lesEmplacements.get(index).getX();
			xMax = xMin + Emplacement.DIMENSION;
			yMin = this.lesEmplacements.get(index).getY();
			yMax = yMin + Emplacement.DIMENSION;

			if (x > xMin && x < xMax && y > yMin && y < yMax)
				return this.lesEmplacements.get(index);
			index++;
		}
		return null;
	}

	private void selection(String nomSelection) {
		fenetre.setCursor(Cursor.getDefaultCursor());
		fenetre.addKeyListener(this);
		this.removeMouseMotionListener(this);
		this.chargePlan(nomSelection);
	}

	private void activeSelection(String nomSelection) {
		String cheminSelection = "data/maps/selection/" + nomSelection + ".png";
		try {
			this.getGraphics().drawImage(
					ImageIO.read(new File(cheminSelection)), 0, 0, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		fenetre.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	public void mouseClicked(MouseEvent event) {
		int x = event.getX();
		int y = event.getY();

		if (event.getButton() == MouseEvent.BUTTON1
				&& this.plan.equals("lille")) {
			if (x > 355 && x < 475 && y > 110 && y < 130)
				this.selection("vieuxLille");
			else if (x > 425 && x < 560 && y > 325 && y < 345)
				this.selection("lilleCentre");
			else if (x > 455 && x < 540 && y > 525 && y < 540)
				this.selection("moulin");
			else if (x > 250 && x < 355 && y > 455 && y < 470)
				this.selection("wazemme");
			else if (x > 180 && x < 270 && y > 305 && y < 320)
				this.selection("vauban");
		} else if (event.getButton() == MouseEvent.BUTTON1) {
			String[] typeE = { "Terrasse", "Etalage" };
			String choix = (String) JOptionPane.showInputDialog(null,
					"Veuillez indiquer le type d'emplacement !",
					"Type d'emplacement", JOptionPane.QUESTION_MESSAGE, null,
					typeE, typeE[0]);
			if (choix != null) {
				if (choix.equals("Etalage"))
					ajouteEmplacement(new Etalage(x
							- (Emplacement.DIMENSION / 2), y
							- (Emplacement.DIMENSION / 2)));
				else if (choix.equals("Terrasse"))
					ajouteEmplacement(new Terrasse(x
							- (Emplacement.DIMENSION / 2), y
							- (Emplacement.DIMENSION / 2)));
			}
		} else if (event.getButton() == MouseEvent.BUTTON3) {
			Emplacement unEmplacement = this.surEmplacement(x, y);
			if (unEmplacement != null) {
				int option = JOptionPane.showConfirmDialog(null,
						"Voulez-vous supprimer l'emplacement ?",
						"Suppression de l'emplacement",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (option == JOptionPane.OK_OPTION)
					this.supprimeEmplacement(unEmplacement);
			}
		}
	}

	public void mouseMoved(MouseEvent event) {
		int x = event.getX();
		int y = event.getY();

		if (x > 355 && x < 475 && y > 110 && y < 130)
			this.activeSelection("selectionVieuxLille");
		else if (x > 425 && x < 560 && y > 325 && y < 345)
			this.activeSelection("selectionLilleCentre");
		else if (x > 455 && x < 540 && y > 525 && y < 540)
			this.activeSelection("selectionMoulin");
		else if (x > 250 && x < 355 && y > 455 && y < 470)
			this.activeSelection("selectionWazemme");
		else if (x > 180 && x < 270 && y > 305 && y < 320)
			this.activeSelection("selectionVauban");
		else {
			fenetre.setCursor(Cursor.getDefaultCursor());
			this.repaint();
		}
	}

	public void keyPressed(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.sauvegardePlan();
			this.chargePlan("lille");
			fenetre.removeKeyListener(this);
			this.addMouseMotionListener(this);
		}
	}

	public void windowClosing(WindowEvent event) {
		this.sauvegardePlan();
	}

	@Override
	public void windowActivated(WindowEvent arg0) {

	}

	@Override
	public void windowClosed(WindowEvent arg0) {

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {

	}

	@Override
	public void windowIconified(WindowEvent arg0) {

	}

	@Override
	public void windowOpened(WindowEvent arg0) {

	}

	@Override
	public void keyReleased(KeyEvent arg0) {

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	@Override
	public void mouseDragged(MouseEvent arg0) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent arg0) {

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}

}
