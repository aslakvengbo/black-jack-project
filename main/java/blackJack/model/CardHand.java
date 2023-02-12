package blackJack.model;

import java.util.ArrayList;

public class CardHand {

	private ArrayList<Card> cardHand;
	private CardDeck cardDeck = new CardDeck();

	public CardHand() {
		cardHand = new ArrayList<Card>();
	}

	public CardDeck getCardDeck() {
		return cardDeck;
	}

	public void addCard(Card card) { // Ikke test, innebygd metode
		this.cardHand.add(card);
	}

	public int getCardCount() { // Ikke test, innebygd metode
		return cardHand.size();
	}

	public Card getCard(int i) {
		if (i < 0 || i >= getCardCount()) {
			throw new IllegalArgumentException(
					String.format("%s is an illegal card index, when the size of the hand is %s", i, getCardCount()));
		}
		return cardHand.get(i); 
	}

	public int getHandVal() {
		return this.getHandSumExcludingAce() + this.getAceSum();
	}

	private int getHandSumExcludingAce() {
		int totalValue = 0;
		for (Card card : cardHand) {
			int cardValue = card.getFace();
			if (cardValue > 1) {
				if (cardValue > 10) {
					cardValue = 10;
				}
				totalValue += cardValue;
			}
		}
		return totalValue;
	}

	private int getAceSum() {
		int aceCount = 0;
		for (Card card : cardHand) {
			if (card.getFace() == 1) {
				aceCount++;
			}
		}
		int aceCount11 = aceCount;
		int aceCount1 = 0;
		while (aceCount11 != 0) {
			if ((11 * aceCount11 + aceCount1 + this.getHandSumExcludingAce()) < 22) {
				return 11 * aceCount11 + aceCount1;
			} else {
				aceCount11--;
				aceCount1++;
			}
		}
		return aceCount;
	}

	public void dealCardFromDeckToHand(CardDeck cardDeck) { // Dealer kort fra gitt bunke (i spillet vil dette være dealeren sin bunke).
		if (cardDeck.getCardCount() == 0) {
			throw new IllegalArgumentException("Can't deal a card from an empty card deck.");
		}
		this.addCard(cardDeck.getCard(0));
		cardDeck.removeCard(0);
	}

	public boolean isBust() { // Returnerer true dersom bust dvs. handVal overskrider 21
		return this.getHandVal() > 21;
	}
	
	public boolean isBlackJack() {
		return this.getCardCount() == 2 && this.getHandVal() == 21;
	}

	@Override
	public String toString() {
		return "Hand: " + cardHand.toString().substring(1, cardHand.toString().length() - 1);
	}
}