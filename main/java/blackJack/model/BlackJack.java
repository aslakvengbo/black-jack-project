package blackJack.model;

public class BlackJack {

	private CardHand playerHand;
	private CardHand dealerHand;
	private boolean playerHasWon;
	private boolean playerHasLost;
	private boolean playerHasTied;

	public BlackJack() {
		this.playerHand = new CardHand();
		this.dealerHand = new CardHand();
		this.playerHasWon = false;
		this.playerHasLost = false;
		this.playerHasTied = false;

		for (int i = 0; i < 2; i++) {
			this.playerHand.dealCardFromDeckToHand(dealerHand.getCardDeck()); //Konstruktøren spesifiserer at det deles ut kort fra bunken til dealeren
		}
		this.dealerHand.dealCardFromDeckToHand(dealerHand.getCardDeck());
	}

	public boolean getPlayerHasWon() {
		return this.playerHasWon;
	}

	public boolean getPlayerHasLost() {
		return this.playerHasLost;
	}
	
	public boolean getPlayerHasTied() {
		return this.playerHasTied;
	}

	public void setPlayerHasWon() {
		this.playerHasWon = true;
	}

	public void setPlayerHasLost() {
		this.playerHasLost = true;
	}
	
	public void setPlayerHasTied() {
		this.playerHasTied = true;
	}
	
	public CardHand getDealerHand() {
		return dealerHand;
	}

	public void setDealerHand(CardHand dealerHand) {
		this.dealerHand = dealerHand;
	}

	public CardHand getPlayerHand() {
		return playerHand;
	}

	public void setPlayerHand(CardHand playerHand) {
		this.playerHand = playerHand;
	}

	public void playerHits() {
		isValidAction();
		this.playerHand.dealCardFromDeckToHand(dealerHand.getCardDeck());
		if (this.playerHand.isBust()) {
			this.playerHasLost = true;
		}
	}

	public void dealerHits() {
		isValidAction();
		if (dealerHand.getHandVal() < 17) {
			dealerHand.dealCardFromDeckToHand(dealerHand.getCardDeck());
		}
	}
	
	public void playerLosesDirectlyAfterStand() {
		isValidAction();
		if (getPlayerHand().getHandVal() < getDealerHand().getHandVal()) {
			setPlayerHasLost();
		}
	}
	
	private void isValidAction() {
		if (this.playerHasWon || this.playerHasLost || this.playerHasTied) {
			throw new IllegalStateException("Kan ikke hite eller stande når spillet er ferdig"); // Test
		}
	}

	private boolean isTied() { // Denne hjelpemetoden testes gjennom testResutltAfterStand()-testmetoden
		return dealerHand.getHandVal() == playerHand.getHandVal();
	}

	public void resultAfterStand() {
		isValidAction();
		if (isTied()) {
			setPlayerHasTied();
		} else if (dealerHand.getHandVal() > 21 || dealerHand.getHandVal() < playerHand.getHandVal()) {
			setPlayerHasWon();
		} else {
			setPlayerHasLost();
		}
	}

	@Override
	public String toString() {
		return this.playerHand.toString().substring(6) + "\n" + this.dealerHand.toString().substring(6) +
				"\n" + this.getPlayerHasWon() + "\n" + this.getPlayerHasLost() + "\n" + this.getPlayerHasTied();
	}
}