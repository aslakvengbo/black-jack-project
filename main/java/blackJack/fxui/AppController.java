package blackJack.fxui;

import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

import blackJack.model.BlackJack;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class AppController {

	private BlackJack game = new BlackJack();

	private GameManager saveHandler = new BlackJackManager();

	@FXML
	private Text fileNotFoundMessage, invalidHitStandMessage;

	@FXML
	private TextField playerHand, dealerHand, playerVal, dealerVal, filename;

	@FXML
	private TextArea output;

	@FXML
	private ImageView playerCard1, playerCard2, playerCard3, playerCard4, playerCard5, playerCard6, dealerCard1,
			dealerCard2, dealerCard3, dealerCard4, dealerCard5, dealerCard6, header;

	@FXML
	private void initialize() {
		onNewGame();
		headerTimer();
		fileNotFoundMessage.setVisible(false);
		playerHand.setVisible(false); // Setter playerHand og dealerHand usynlig fordi de var med før og kan tas med ved behov
		dealerHand.setVisible(false);
		playerHand.setEditable(false);
		dealerHand.setEditable(false);
		playerVal.setEditable(false);
		dealerVal.setEditable(false);
		output.setEditable(false);
	}
	
	@FXML
	private void headerTimer() { // INsiprasjon fra stack overflow
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				Platform.runLater(() -> header.setVisible(false));
			}
		}, 3000);
	}

	@FXML
	private void updateCards() {
		try {
			playerCard1.setImage(game.getPlayerHand().getCard(0).getImage());
		} catch (Exception e) {
			playerCard1.setImage(null);
		}
		try {
			playerCard2.setImage(game.getPlayerHand().getCard(1).getImage());
		} catch (Exception e) {
			playerCard2.setImage(null);
		}
		try {
			playerCard3.setImage(game.getPlayerHand().getCard(2).getImage());
		} catch (Exception e) {
			playerCard3.setImage(null);
		}
		try {
			playerCard4.setImage(game.getPlayerHand().getCard(3).getImage());
		} catch (Exception e) {
			playerCard4.setImage(null);
		}
		try {
			playerCard5.setImage(game.getPlayerHand().getCard(4).getImage());
		} catch (Exception e) {
			playerCard5.setImage(null);
		}
		try {
			playerCard6.setImage(game.getPlayerHand().getCard(5).getImage());
		} catch (Exception e) {
			playerCard6.setImage(null);
		}

		try {
			dealerCard1.setImage(game.getDealerHand().getCard(0).getImage());
		} catch (Exception e) {
			dealerCard1.setImage(null);
		}
		try {
			dealerCard2.setImage(game.getDealerHand().getCard(1).getImage());
		} catch (Exception e) {
			dealerCard2.setImage(new Image(getClass().getResource("cardBackSide.png").toString(), true));
		}
		try {
			dealerCard3.setImage(game.getDealerHand().getCard(2).getImage());
		} catch (Exception e) {
			dealerCard3.setImage(null);
		}
		try {
			dealerCard4.setImage(game.getDealerHand().getCard(3).getImage());
		} catch (Exception e) {
			dealerCard4.setImage(null);
		}
		try {
			dealerCard5.setImage(game.getDealerHand().getCard(4).getImage());
		} catch (Exception e) {
			dealerCard5.setImage(null);
		}
		try {
			dealerCard6.setImage(game.getDealerHand().getCard(5).getImage());
		} catch (Exception e) {
			dealerCard6.setImage(null);
		}

	}
	
	@FXML
	private void onHit() {
		// Surrounder hele koden i onHit() og onStand() med try-catch, slik at om det evt. hadde dukket opp en uforutsett Exception, så ville ikke spillet ha krasjet, men heller gitt meldingen "Invalid Action." i output-boksen
		try {
			
			try {
				game.playerHits();
			} catch (IllegalStateException e) {
				invalidHitStandMessage.setVisible(true);
				return;
			}
			drawGame();
			output.setText("Player hits.");
			output.setStyle("-fx-text-fill: blue; -fx-font-size: 20px");
	
			if (game.getPlayerHand().isBust()) {
				game.setPlayerHasLost();
				setOutputResult();
			}
			updateCards();
		
		} catch (Exception e) {
			output.setText("Action not valid.");
			output.setStyle("-fx-text-fill: black; -fx-font-size: 20px");
		}
	}

	@FXML
	private void onStand() {
		try {
		
			try {
				game.playerLosesDirectlyAfterStand();
				setOutputResult();
			} catch (IllegalStateException e) {
				invalidHitStandMessage.setVisible(true);
				return;
			}
			try {
				game.dealerHits();
				output.setText("Dealer reveals second card.");
				output.setStyle("-fx-text-fill: blue; -fx-font-size: 20px");
				drawGame();
			} catch (IllegalStateException e) {
				invalidHitStandMessage.setVisible(true);
				return;
			}
			while (game.getDealerHand().getHandVal() < 17) {
				try {
					game.dealerHits();
					output.setText("Dealer hits.");
					output.setStyle("-fx-text-fill: blue; -fx-font-size: 20px");
					drawGame();
				} catch (IllegalStateException e) {
					invalidHitStandMessage.setVisible(true);
					return;
				}
			}
			game.resultAfterStand();
			drawGame();
			setOutputResult();
		
		} catch (Exception e) {
			output.setText("Action not valid.");
			output.setStyle("-fx-text-fill: black; -fx-font-size: 20px");
		}
	}

	@FXML
	private void drawGame() {
		updateCards();
		invalidHitStandMessage.setVisible(false);
		playerHand.setText(game.getPlayerHand().toString());
		dealerHand.setText(game.getDealerHand().toString());
		playerVal.setText(Integer.toString(game.getPlayerHand().getHandVal()));
		dealerVal.setText(Integer.toString(game.getDealerHand().getHandVal()));
		output.setText("");
		output.setStyle("-fx-text-fill: black; -fx-font-size: 20px");
	}

	@FXML
	private void onNewGame() {
		try {
			game = new BlackJack();
			drawGame();
			output.setText("New game!");
			output.setStyle("-fx-text-fill: black; -fx-font-size: 20px");
			
			if (game.getPlayerHand().isBlackJack()) {
				game.setPlayerHasWon();
				output.setText("Player wins with Black Jack!");
				output.setStyle("-fx-text-fill: green; -fx-font-size: 17px");
			}
			
		} catch (Exception e) {
			output.setText("Action not valid.");
			output.setStyle("-fx-text-fill: black; -fx-font-size: 20px");
		}
	}
	
	@FXML
	private void setOutputResult() {
		if (game.getPlayerHasWon()) {
			output.setText("Player wins!");
			output.setStyle("-fx-text-fill: green; -fx-font-size: 20px");
		} else if (game.getPlayerHasLost()) {
			output.setText("Player loses!");
			output.setStyle("-fx-text-fill: red; -fx-font-size: 20px");
		} else if (game.getPlayerHasTied()) {
			output.setText("The game is tied!");
			output.setStyle("-fx-text-fill: black; -fx-font-size: 20px");
		}
	}

	private String getFilename() {
		String filename = this.filename.getText();
		if (filename.isEmpty()) {
			filename = "save_file";
		}
		return filename;
	}
	


	@FXML
	private void onSave() {
		try {
			saveHandler.save(getFilename(), game);
			fileNotFoundMessage.setVisible(false);
			invalidHitStandMessage.setVisible(false);
			
			if (game.getPlayerHasWon()) {
				output.setText("Saved won game.");
				output.setStyle("-fx-text-fill: green; -fx-font-size: 20px");
			} else if (game.getPlayerHasLost()) {
				output.setText("Saved lost game.");
				output.setStyle("-fx-text-fill: red; -fx-font-size: 20px");
			} else if (game.getPlayerHasTied())  {
				output.setText("Saved tied game.");
				output.setStyle("-fx-text-fill: black; -fx-font-size: 20px");
			} else {
				output.setText("Saved unfinished game.");
				output.setStyle("-fx-text-fill: black; -fx-font-size: 20px");
			} 

		} catch (FileNotFoundException e) {
			fileNotFoundMessage.setVisible(true);
		}
	}

	@FXML
	private void onLoad() {
		try {
			game = saveHandler.load(getFilename());
			fileNotFoundMessage.setVisible(false);
			drawGame();

			if (game.getPlayerHasWon()) {
				output.setText("Loaded won game.");
				output.setStyle("-fx-text-fill: green; -fx-font-size: 20px");
			} else if (game.getPlayerHasLost()) {
				output.setText("Loaded lost game.");
				output.setStyle("-fx-text-fill: red; -fx-font-size: 20px");
			} else if (game.getPlayerHasTied()) {
				output.setText("Loaded tied game.");
				output.setStyle("-fx-text-fill: black; -fx-font-size: 20px");
			} else {
				output.setText("Loaded unfinished game.");
				output.setStyle("-fx-text-fill: black; -fx-font-size: 20px");
			}
		} catch (FileNotFoundException e) {
			fileNotFoundMessage.setVisible(true);
		}
	}
}