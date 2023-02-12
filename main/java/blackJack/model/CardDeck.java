package blackJack.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CardDeck {

	private List<Card> cardDeck = new ArrayList<Card>();

	public CardDeck() {
		List<Character> suits = Arrays.asList('S', 'H', 'D', 'C');
		for (int i = 0; i < suits.size(); i++) {
			for (int j = 1; j < 14; j++) {
				Card card = new Card(suits.get(i), j);
				cardDeck.add(card);
			}
		}
		Collections.shuffle(cardDeck);
	}

	public int getCardCount() { // Ikke test, innebygd metode
		return cardDeck.size(); 
	}
	
	public Card getCard(int i) {
		validCardDeckIndex(i); 
		return this.cardDeck.get(i); 
	}

	public void removeCard(int i) {
		validCardDeckIndex(i); 
		this.cardDeck.remove(i); 
	}
	
	public void removeSpesificCard(Card card) {
		if (! cardDeck.stream()
				.anyMatch(o -> o.getSuit() == card.getSuit() && o.getFace() == card.getFace())) {
			throw new IllegalArgumentException("Bunken inneholder ikke det spesifiserte kortet.");
			}
		for (int i = 0; i < this.getCardCount(); i++) {
			if (this.getCard(i).getSuit() == card.getSuit() 
					&& this.getCard(i).getFace() == card.getFace()) {
				this.removeCard(i);
				return;
			}
		}
	}

	private void validCardDeckIndex(int n) {
		if (n < 0 || n >= this.getCardCount()) {
			throw new IllegalArgumentException("Invalid card deck index.");
		}
	}
}