package blackJack.fxui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import blackJack.model.BlackJack;
import blackJack.model.Card;
import blackJack.model.CardHand;

public class BlackJackManager implements GameManager {

	@Override
	public void save(String filename, BlackJack game) throws FileNotFoundException {
		// Lagres i C:\Users\navn\git\tdt4100-prosjekt-brukernavn\project
		try (PrintWriter writer = new PrintWriter(getFilePath(filename))) {
			writer.println(game);
//			System.out.println("Game saved with filename " + filename);
		}
	}

	@Override
	public BlackJack load(String filename) throws FileNotFoundException {
		// Lastest fra C:\Users\navn\git\tdt4100-prosjekt-brukernavn\project
		try (Scanner scanner = new Scanner(new File(getFilePath(filename)))) {
			BlackJack game = new BlackJack();
			try {
				String linePlayerHand = scanner.nextLine().replaceAll("\\s+", "");
				String[] listPlayerHand = linePlayerHand.split(",");
				CardHand playerHand = new CardHand();
				List <Card> cardsToRemoveFromDeck = new ArrayList<>();
				for (String cardString : listPlayerHand) {
					Card card = new Card(cardString.charAt(0), Integer.parseInt(cardString.substring(1)));
					playerHand.addCard(card);
					cardsToRemoveFromDeck.add(card);
				}
				game.setPlayerHand(playerHand);

				String lineDealerHand = scanner.nextLine().replaceAll("\\s+", "");
				String[] listDealerHand = lineDealerHand.split(",");
				CardHand dealerHand = new CardHand();
				for (String cardString : listDealerHand) {
					Card card = new Card(cardString.charAt(0), Integer.parseInt(cardString.substring(1)));
					dealerHand.addCard(card);
					cardsToRemoveFromDeck.add(card);
				}
				game.setDealerHand(dealerHand);
				
				for (Card card : cardsToRemoveFromDeck) { //Fjerner kort fra bunken slik at det evt ikke blir to av et kort. Vi bruker alltid dealern sin bunke.
					game.getDealerHand().getCardDeck().removeSpesificCard(card);
				}

				if (scanner.nextBoolean()) {
					game.setPlayerHasWon();
				} else if (scanner.nextBoolean()) {
					game.setPlayerHasLost();
				} else if (scanner.nextBoolean()) {
					game.setPlayerHasTied();
				}
			} catch (Exception e) {
				throw new FileNotFoundException("Can't load invalid file");
			}
//			System.out.println("Game loaded with filename " + filename);
			return game;
		}
	}

	private static String getFilePath(String filename) {
		return filename + ".txt";
	}
}
