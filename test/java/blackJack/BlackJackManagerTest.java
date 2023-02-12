package blackJack;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import blackJack.fxui.BlackJackManager;
import blackJack.fxui.GameManager;
import blackJack.model.BlackJack;
import blackJack.model.Card;
import blackJack.model.CardHand;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class BlackJackManagerTest {
	
	private BlackJack game;
	private GameManager blackJackManager = new BlackJackManager();
	
	@BeforeEach
	public void setup() {
		game = new BlackJack();
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
	}
	
	@Test
	public void testLoad() {
		BlackJack savedNewGame;
		try {
//			savedNewGame = blackJackManager.load("test-save");
			savedNewGame = blackJackManager.load("src/test/resources/test-save");
		} catch (FileNotFoundException e) {
			fail("Could not load saved file");
			return;
		}
		assertEquals(game.toString(), savedNewGame.toString());
			
	}
	
	@Test
	public void testLoadNonExistingFile() {
		assertThrows(
				FileNotFoundException.class, 
				() -> game = blackJackManager.load("foo"), 
				"FileNotFoundException should be thrown when file does not exist!"
			);
	}
	
	@Test
	public void testLoadInvalidFile() { 
		assertThrows(
			FileNotFoundException.class, 
			() -> game = blackJackManager.load("src/test/resources/invalid-save"), 
			"An exception should be thrown if loaded file is invalid!"
		);
	}
	
	@Test
	public void testSave() {
		try {
			blackJackManager.save("src/test/resources/test-save-new", game);
		} catch (FileNotFoundException e) {
			fail("Could not save file");
		}
		
		byte[] testFile = null, newFile = null;
		
		try {
			testFile = Files.readAllBytes(Path.of("src/test/resources/test-save" + ".txt"));
		} catch (IOException e) {
			fail("Could not save file");
		}
		
		try {
			newFile = Files.readAllBytes(Path.of("src/test/resources/test-save" + ".txt"));
		} catch (IOException e) {
			fail("Could not save file");
		}
		assertNotNull(testFile);
		assertNotNull(newFile);
		assertTrue(Arrays.equals(testFile, newFile));
	}
	
	@AfterAll
	static void teardown() {
		File newTestSaveFile = new File("src/test/resources/test-save-new" + ".txt");
		newTestSaveFile.delete();
	}
}
