package blackJack.model;

import java.util.Arrays;
import java.util.Collection;

import javafx.scene.image.Image;

public class Card {

	private char suit;
	private int face;

	public Card(char suit, int face) {
		validSuit(suit);
		validFace(face);
		this.suit = suit;
		this.face = face;
	}

	public char getSuit() {
		return suit;
	}

	public int getFace() {
		return face;
	}
	
	public Image getImage() {
		try {
			String filename = this.toString() + ".png";
			Image image = new Image(getClass().getResource(filename).toString());
			return image;
		} catch (Exception e) {
			throw e;
		} 
	}

	private static void validSuit(char suit) {
		Collection<Character> suits = Arrays.asList('S', 'H', 'D', 'C');
		if (!suits.contains(suit)) {
			throw new IllegalArgumentException("Kort-typen (suit) m� v�re �n av bokstavene S, H, D eller C.");
		}
	}

	private static void validFace(int face) {
		if (face < 1 || face > 13) {
			throw new IllegalArgumentException("Kort-tallet (face) m� v�re et heltall fra og med 1 til og med 13.");
		}
	}

	@Override
	public String toString() {
		return suit + String.valueOf(face);
	}
}