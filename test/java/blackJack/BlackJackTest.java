package blackJack;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import blackJack.model.BlackJack;
import blackJack.model.Card;
import blackJack.model.CardHand;

public class BlackJackTest {
	
	BlackJack game;
	
	@BeforeEach
	public void setup() {
		game = new BlackJack();
	}
	
	@Test
	@DisplayName("Test: Konstuktøren initaliserer et BlackJack-objekt med riktig starttilstand")
	public void testConstructor() {
		assertFalse(game.getPlayerHasWon());
		assertFalse(game.getPlayerHasLost());
		assertEquals(game.getPlayerHand().getCardCount(), 2);
		assertEquals(game.getDealerHand().getCardCount(), 1);
	}
	
	@Test
	@DisplayName("Test: playerHits() gir spilleren et kort og spilleren buster så taper spiller, kaster IllegalState dersom spillet er ferdig")
	public void testPlayerHits() {
		game.setPlayerHand(new CardHand());
		game.playerHits();
		assertEquals(game.getPlayerHand().getCardCount(), 1);
		game.playerHits();
		assertEquals(game.getPlayerHand().getCardCount(), 2);
		
		
		game.setPlayerHasWon();
		assertThrows(IllegalStateException.class,
				() -> game.playerHits(),
				"IllegalState skal kastes når man kaller playerHits() på et vunnet spill");
		
		game = new BlackJack();
		game.setPlayerHasLost();
		assertThrows(IllegalStateException.class,
				() -> game.playerHits(),
				"IllegalState skal kastes når man kaller playerHits() på et tapt spill");
		
		game = new BlackJack();
		game.setPlayerHasTied();
		assertThrows(IllegalStateException.class,
				() -> game.playerHits(),
				"IllegalState skal kastes når man kaller playerHits() på et uavgjort spill");
	}
	
	@Test
	@DisplayName("Test: dealerHits() gir dealeren et kort så lenge kort-summen er mindre enn 17, og kaster IllegalState dersom metoden kalles på et ferdig spill")
	public void testDealerHits() {
		
		game.dealerHits();
		assertEquals(game.getDealerHand().getCardCount(), 2);
		
		//Husk: Kortsummen er alltid tilfeldig
		for (int i = 0; i < 50; i++) { // Tester derfor med if-else-statements som kjøres et vilkårlig antall ganger
			game = new BlackJack();
			int currentCardCount = game.getDealerHand().getCardCount();
			if (game.getDealerHand().getHandVal() < 17) {
				game.dealerHits();
				assertEquals(currentCardCount + 1, game.getDealerHand().getCardCount());
			} else {
				game.dealerHits();
				assertEquals(currentCardCount, game.getDealerHand().getCardCount());
			}
		}
		
		game = new BlackJack();
		game.setPlayerHasWon();
		assertThrows(IllegalStateException.class,
				() -> game.dealerHits(),
				"IllegalState skal kastes når man kaller dealerHits() på et vunnet spill");
		
		game = new BlackJack();
		game.setPlayerHasLost();
		assertThrows(IllegalStateException.class,
				() -> game.dealerHits(),
				"IllegalState skal kastes når man kaller dealerHits() på et tapt spill");
		
		game = new BlackJack();
		game.setPlayerHasTied();
		assertThrows(IllegalStateException.class,
				() -> game.dealerHits(),
				"IllegalState skal kastes når man kaller dealerHits() på et uavgjort (tied) spill");
	}
	
	@Test
	@DisplayName("Test: resultAfterStand() endrer tilstanden utfra om spilleren vinner, taper eller det blir uavgjort etter at spilleren har valgt 'stand' og dealeran ar hitet etter spillreglene")
	public void testResultAfterStand() { 
		// Antar at spilleren velger stand her
		game.dealerHits();
		// Antar at dealern må velge stand her (dealeren må det om kort-sum > 17)
		game.resultAfterStand();
		
		for (int i = 0; i < 50; i++) { // Tester ved et vilkårlig antall kjøringer af if-else-statements
			if (game.getPlayerHasTied()) {
				assertTrue(game.getPlayerHasTied());
			} else if (game.getDealerHand().isBust() || game.getDealerHand().getHandVal() < game.getPlayerHand().getHandVal()) {
				assertTrue(game.getPlayerHasWon());
			} else {
				assertTrue(game.getPlayerHasLost());
			}
		}
		
		game = new BlackJack();
		game.setPlayerHasWon();
		assertThrows(IllegalStateException.class,
				() -> game.resultAfterStand(),
				"IllegalState skal kastes når man kaller resultAfterStand() på et vunnet spill");
		
		game = new BlackJack();
		game.setPlayerHasLost();
		assertThrows(IllegalStateException.class,
				() -> game.resultAfterStand(),
				"IllegalState skal kastes når man kaller resultAfterStand() på et tapt spill");
		
		game = new BlackJack();
		game.setPlayerHasTied();
		assertThrows(IllegalStateException.class,
				() -> game.resultAfterStand(),
				"IllegalState skal kastes når man kaller resultAfterStand() på et uavgjort (tied) spill");
		
	}
	
	@Test
	@DisplayName("Test: playerLosesDirectlyAfterStand() setter playerHasLost dersom spilleren har mindre handSum enn dealeren, kaster IllegalState dersom metoden kalles etter spillet er ferdig")
	public void testPlayerLosesDirectlyAfterStand() {
		
		CardHand playerHand = new CardHand();
		playerHand.addCard(new Card('S', 5));
		playerHand.addCard(new Card('D', 3));
		game.setPlayerHand(playerHand);
		
		CardHand dealerHand = new CardHand();
		dealerHand.addCard(new Card('C', 13));
		game.setDealerHand(dealerHand);
		
		game.playerLosesDirectlyAfterStand();
		assertTrue(game.getPlayerHasLost());
		
		game = new BlackJack();
		game.setPlayerHasWon();
		assertThrows(IllegalStateException.class,
				() -> game.playerLosesDirectlyAfterStand(),
				"IllegalState skal kastes når man kaller playerLosesDirectlyAfterStand() på et vunnet spill");
		
		game = new BlackJack();
		game.setPlayerHasLost();
		assertThrows(IllegalStateException.class,
				() -> game.playerLosesDirectlyAfterStand(),
				"IllegalState skal kastes når man kaller playerLosesDirectlyAfterStand() på et tapt spill");
		
		game = new BlackJack();
		game.setPlayerHasTied();
		assertThrows(IllegalStateException.class,
				() -> game.playerLosesDirectlyAfterStand(),
				"IllegalState skal kastes når man kaller playerLosesDirectlyAfterStand() på et uavgjort (tied) spill");
	}
	
	@Test
	@DisplayName("Test: toString() fungerer som forventet")
	public void testToString() {
		CardHand playerHand = new CardHand();
		playerHand.addCard(new Card('C', 8));
		playerHand.addCard(new Card('H', 12));		
		playerHand.addCard(new Card('C', 3));
		
		CardHand DealerHand = new CardHand();
		DealerHand.addCard(new Card('H', 1));
		DealerHand.addCard(new Card('D', 8));
		
		game.setPlayerHand(playerHand);
		game.setDealerHand(DealerHand);
		game.setPlayerHasWon();
		
		assertEquals("C8, H12, C3\nH1, D8\ntrue\nfalse\nfalse", game.toString());
	}
	
}