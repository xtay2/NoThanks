package game;

public final class GameState {

	private final int card;

	private int coins = 0;

	public GameState(int cardValue) {
		if (cardValue < 1)
			throw new IllegalArgumentException("The card value has to be positive.");
		this.card = cardValue;
	}

	/** Increases the value of this by one. */
	public final void increase() {
		coins++;
	}

	public final int getCard() {
		return card;
	}

	public final int getCoins() {
		return coins;
	}

	@Override
	public String toString() {
		return "Card: " + card + " with " + coins + " coins.";
	}
}