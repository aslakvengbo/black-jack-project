package blackJack;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import blackJack.model.Card;
import blackJack.model.CardDeck;

public class CardDeckTest {

	private CardDeck cardDeck;

	@BeforeEach
	public void setup() {
		cardDeck = new CardDeck();
	}

	@Test
	@DisplayName("Test: Konstukt�ren initialiserer et CardDeck-objekt med 52 unike kort som er tilfeldig stokket")
	public void testConstructor() {
		assertEquals(cardDeck.getCardCount(), 52);

		List<Card> cardDeckAsList = new ArrayList<>();
		for (int i = 0; i < cardDeck.getCardCount(); i++) {
			cardDeckAsList.add(cardDeck.getCard(i));
		}

		assertTrue(cardDeckAsList.stream() // Om alle kortene er unike og Card er definert til � v�re 52 distinkte typer (4 suits * 13 faces) s� m� alle kortene n�dvendigvis v�re til stede i bunken)
				.distinct()
				.collect(Collectors.toList())
				.size() == cardDeck.getCardCount());
		
		// Her tester jeg at en ny kortstokk blir stokket i konsturkt�ren ved � sammenligne alle kortene og sjekke at det i en ny bunke er mindre enn 52 kort med lik index.
		int total = 0;
		CardDeck cardDeck2 = new CardDeck();
		for (int i = 0; i < cardDeck.getCardCount(); i++) {
			if (cardDeck.getCard(i).toString().equals(cardDeck2.getCard(i).toString())) {
				total++;
			}
		}
		assertTrue(total < 52); // 1 av 52! = 8.06 * 10^62 ganger vil denne feile, men antar at sannsynligheten er s� lav at det er hensiktsmessig � teste koden p� denne m�ten
	}
	
	@Test
	@DisplayName("Test: removeCard(index) fjerner et kort etter gitt index")
	public void testRemoveCard() {
		cardDeck.removeCard(0);
		cardDeck.removeCard(0);
		assertEquals(cardDeck.getCardCount(), 50);
		
		Card cardInDeck = cardDeck.getCard(25);
		cardDeck.removeCard(25);
		
		List<Card> cardDeckAsList = new ArrayList<>();
		for (int i = 0; i < cardDeck.getCardCount(); i++) {
			cardDeckAsList.add(cardDeck.getCard(i));
		}
		assertFalse(cardDeckAsList.stream().
				anyMatch(Card -> Card == cardInDeck));
	}
	
	@Test
	@DisplayName("Test: removeSpesificCard() fjerner angitt kort fra bunken, kaster IllegalArgument dersom kortet ikke er i bunken ")
	public void testRemoveSpesificCard() {
		cardDeck.removeSpesificCard(new Card('S', 1));
		assertEquals(cardDeck.getCardCount(), 51);
		
		assertThrows(IllegalArgumentException.class, // At det kastes IllegalArgument her medf�rer at vi er sikre p� at S1 ogs� ble fjernet i f�rste del av testen
				() -> cardDeck.removeSpesificCard(new Card('S', 1)),
				"IllegalArgument skal kastes dersom det spesifiserte kortet ikke er i h�nda.");
	}

	@Test
	@DisplayName("Test: IllegalArgument kastes dersom indexen er ugyldig p� getCard(int index) og removeCard(int index)")
	public void testValidCardIndex() {
		assertThrows(IllegalArgumentException.class,
				() -> cardDeck.getCard(-1),
				"IllegalArgument skal kastes n�r man setter indexen er ugyldig.");
		assertThrows(IllegalArgumentException.class,
				() -> cardDeck.getCard(cardDeck.getCardCount()),
				"IllegalArgument skal kastes n�r man setter indexen er ugyldig.");
		assertThrows(IllegalArgumentException.class,
				() -> cardDeck.removeCard(-1),
				"IllegalArgument skal kastes n�r man setter indexen er ugyldig.");
		assertThrows(IllegalArgumentException.class,
				() -> cardDeck.removeCard(cardDeck.getCardCount()),
				"IllegalArgument skal kastes n�r man setter indexen er ugyldig.");
	}

}
