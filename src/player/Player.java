package player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import game.GameManager;
import game.GameState;

public abstract class Player {

	private final List<Integer> cards = new ArrayList<>();

	public final String name;

	private int coins = GameManager.START_COINS;

	/** Creates a new Player with a given name. */
	public Player() {
		this.name = getClass().getSimpleName();
	}

	/** Creates a new Player with a arbitrary name. */
	public Player(String name) {
		this.name = name;
	}

	/** Returns the cards that this player has. */
	public final List<Integer> getCards() {
		return new ArrayList<>(cards);
	}

	/** Returns the amount of coins that this player has. */
	public final int getCoins() {
		return coins;
	}

	/**
	 * Adds a card and the amount of coins on it to this player.
	 */
	public final void give(GameState g) {
		cards.add(g.getCard());
		coins += g.getCoins();
	}

	/** Removes one coin from the players stash. */
	public final void decrease() {
		if (coins == 0)
			throw new IllegalArgumentException("Coins are already zero.");
		coins--;
	}

	/**
	 * This method gets only called by the {@link GameManager} if the player has
	 * atleast one coin.
	 * 
	 * @param gameState displays the card, thats currently offered, as well as the
	 *                  coins on it.
	 * @return if true gets returned, this card gets added to the players cards and
	 *         the coin-count gets incremented. If false gets returned, one coin is
	 *         removed and this method gets called for the next player.
	 */
	public abstract boolean takeCard(GameState gameState);

	/**
	 * Counts the score for this player. The score for all cards that are increasing
	 * by one, the lowest one gets counted. After that the score gets subtracted by
	 * the amount of coins.
	 */
	public final int count() {
		Collections.sort(cards);
		int score = 0;
		int last = -1;
		for (int c : cards) {
			score += c != last + 1 ? c : 0;
			last = c;
		}
		return score - coins;
	}

	@Override
	public final String toString() {
		return name;
	}
}
