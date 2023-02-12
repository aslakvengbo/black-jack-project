package blackJack;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import blackJack.model.Card;
import blackJack.model.CardHand;

public class CardHandTest {

	private CardHand cardHand;

	@BeforeEach
	public void setup() {
		cardHand = new CardHand();
	}

	@Test
	@DisplayName("Test: Konstruktør intialiserer tom mengde med kort")
	public void testConstuctor() {
		assertEquals(cardHand.getCardCount(), 0);
	}
	
	@Test
	@DisplayName("Test: addCard(Card) legger til et spesifisert kort i korthånda.")
	public void testAddCard() {
		Card card1 = new Card('S', 5);
		Card card2 = new Card('D', 9);
		Card card3 = new Card('C', 1);
		Card card4 = new Card('H', 13);
		cardHand.addCard(card1);
		cardHand.addCard(card2);
		cardHand.addCard(card3);
		cardHand.addCard(card4);
		assertEquals(cardHand.getCardCount(), 4);
		
		List<Card> cardHandAsList = new ArrayList<>();
		for (int i = 0; i < cardHand.getCardCount(); i++) {
			cardHandAsList.add(cardHand.getCard(i));
		}
		assertTrue(cardHandAsList.contains(card1));
		assertTrue(cardHandAsList.contains(card2));
		assertTrue(cardHandAsList.contains(card3));
		assertTrue(cardHandAsList.contains(card4));
		
	}

	@Test
	@DisplayName("Test: IllegalArgument kastes dersom indexen er ugyldig på getCard(int index)")
	public void testValidCardIndex() {
		assertThrows(IllegalArgumentException.class,
				() -> cardHand.getCard(-1),
				"IllegalArgument skal kastes når man setter indexen er ugyldig.");
		assertThrows(IllegalArgumentException.class,
				() -> cardHand.getCard(cardHand.getCardCount()),
				"IllegalArgument skal kastes når man setter indexen er ugyldig.");
	}

	@Test
	@DisplayName("Test: getHandVal() gir korrekt sum av verdi av kortene på hånda iht. BlackJack-regler")
	// Regler for kort-sum:
	// 1) Verdien til 2-10 er "vanlig".
	// 2) Verdien til knekt, dronning og konge er 10.
	// 3) Verdien til ess er enten 1 eller 11, avhengig av hva som gir høtest
	// kort-sum <= 21.
	public void testGetHandVal() {
		cardHand.addCard(new Card('S', 1));
		cardHand.addCard(new Card('H', 2));
		cardHand.addCard(new Card('D', 3));
		assertEquals(cardHand.getHandVal(), 11 + 2 + 3);

		cardHand = new CardHand();
		cardHand.addCard(new Card('S', 1));
		cardHand.addCard(new Card('H', 1));
		cardHand.addCard(new Card('D', 1));
		cardHand.addCard(new Card('C', 1));
		cardHand.addCard(new Card('D', 2));
		assertEquals(cardHand.getHandVal(), 2 + 11 + 1 + 1 + 1);

		cardHand = new CardHand();
		cardHand.addCard(new Card('S', 13));
		cardHand.addCard(new Card('H', 12));
		cardHand.addCard(new Card('D', 13));
		cardHand.addCard(new Card('C', 10));
		cardHand.addCard(new Card('D', 11));
		assertEquals(cardHand.getHandVal(), 10 * 5);

		cardHand = new CardHand();
		cardHand.addCard(new Card('S', 3));
		cardHand.addCard(new Card('H', 5));
		cardHand.addCard(new Card('D', 2));
		cardHand.addCard(new Card('C', 1));
		cardHand.addCard(new Card('D', 13));
		assertEquals(cardHand.getHandVal(), 3 + 5 + 2 + 10 + 1);

		cardHand = new CardHand();
		cardHand.addCard(new Card('S', 1));
		cardHand.addCard(new Card('H', 12));
		assertEquals(cardHand.getHandVal(), 21);
	}
	
	@Test
	@DisplayName("Test: dealCardFromDeckToHand(CardDeck cardDeck) gir kort med index 0 fra bunken til hånda. "
			+ "Dersom bunken er tom kastes IllegalArgument")
	void testDealCardFromDeckToHand() {
		cardHand.dealCardFromDeckToHand(cardHand.getCardDeck());
		cardHand.dealCardFromDeckToHand(cardHand.getCardDeck());
		cardHand.dealCardFromDeckToHand(cardHand.getCardDeck());
		assertEquals(cardHand.getCardCount(), 3);
		assertEquals(cardHand.getCardDeck().getCardCount(), 52-3);
		assertTrue(cardHand.getCardCount() + cardHand.getCardDeck().getCardCount() == 52);
		
		int cardCountAtStart = cardHand.getCardDeck().getCardCount(); // Gjør bunken tom
		for (int i = 0; i < cardCountAtStart; i++) {
			cardHand.dealCardFromDeckToHand(cardHand.getCardDeck());
		}
		
		assertEquals(cardHand.getCardDeck().getCardCount(), 0); // Sikkerhetsjekk på at bunken er tom
		assertThrows(
				IllegalArgumentException.class, 
				() -> cardHand.dealCardFromDeckToHand(cardHand.getCardDeck()), 
				"IllegalArgument skal kastes når man prøver å deale kort fra en tom bunke"
			);
		
	}
	
	@Test
	@DisplayName("Test: isBust returnerer true om kort-summen er over 21, og false ellers")
	public void testIsBust() {
		cardHand.addCard(new Card('S', 10));
		cardHand.addCard(new Card('H', 9));
		cardHand.addCard(new Card('D', 1));
		assertFalse(cardHand.isBust());
		
		cardHand.addCard(new Card('S', 2));
		assertTrue(cardHand.isBust());
	}
	
	@Test
	@DisplayName("Test: isBlackJack() returnerer true om en hånd har to kort som til sammen har 21 som verdi")
	public void testIsBlackJack() {
		
		CardHand blackJackHand;
		
		blackJackHand = new CardHand();
		blackJackHand.addCard(new Card('S', 1));
		blackJackHand.addCard(new Card('S', 13));
		assertTrue(blackJackHand.isBlackJack());
		
		blackJackHand = new CardHand();
		blackJackHand.addCard(new Card('C', 1));
		blackJackHand.addCard(new Card('D', 12));
		assertTrue(blackJackHand.isBlackJack());
	
	}
	
	@Test
	@DisplayName("Sjekk at toString fungerer som forventet")
	public void testToString() {
		cardHand.addCard(new Card('S', 10));
		cardHand.addCard(new Card('H', 9));
		cardHand.addCard(new Card('D', 1));
		assertEquals("Hand: S10, H9, D1", cardHand.toString());
		
		cardHand = new CardHand();
		cardHand.addCard(new Card('D', 1));
		cardHand.addCard(new Card('C', 2));
		cardHand.addCard(new Card('H', 3));
		assertEquals("Hand: D1, C2, H3", cardHand.toString());
	}

}
