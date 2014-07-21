package com.sommerard.lillemap;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public final class Serialize {
	public static void encoder(Object objet, String nomFichier) {
		try (FileOutputStream fos = new FileOutputStream(nomFichier)) {
			XMLEncoder encodeur = new XMLEncoder(fos);
			try {
				encodeur.writeObject(objet);
				encodeur.flush();
			} finally {
				encodeur.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Object decoder(String nomFichier) {
		Object objet = null;
		try (FileInputStream fis = new FileInputStream(nomFichier)) {
			XMLDecoder decodeur = new XMLDecoder(fis);
			try {
				objet = decodeur.readObject();
			} finally {
				decodeur.close();
			}
		} catch (IOException e) {
			Serialize.encoder(null, nomFichier);
		}
		return objet;
	}
}
