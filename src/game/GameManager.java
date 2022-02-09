package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import player.Player;
import player.instances.Bot;

public abstract class GameManager {

	/** The amount of coins every player gets at the start of the game. */
	public static final int START_COINS = 11;

	private static final List<Player> PLAYERS = List.of(new Bot(), new Bot());

	private static final List<Integer> CARDS = generateCards(35);

	/**
	 * Executes the whole game and announces the winner.
	 */
	private static void play() {
		GameState g = new GameState(CARDS.remove(0));
		while (!CARDS.isEmpty()) {
			// For every player
			for (Player p : PLAYERS) {
				// If they got coins they can take a card
				if (p.getCoins() > 1) {
					// If they take a card it, and the coins get added.
					if (p.takeCard(g)) {
						System.out.println(p + " takes " + g);
						p.give(g);
						if (!CARDS.isEmpty())
							g = new GameState(CARDS.remove(0));
						else
							break;
					}
					// If they dont take the card, it gets passed on.
					else {
						System.out.println(p + " passes " + g);
						g.increase();
						p.decrease();
					}
				}
				// If they had no coins, they have to take the card.
				else {
					System.out.println(p + " has to take " + g);
					p.give(g);
					if (!CARDS.isEmpty())
						g = new GameState(CARDS.remove(0));
					else
						break;
				}
			}

		}
		countScores();
	}

	/** Count all scores and display the Results. */
	private static void countScores() {
		Map<Player, Integer> scores = new HashMap<>();
		for (Player p : PLAYERS)
			scores.put(p, p.count());

		Map<Player, Integer> sortedMap = scores.entrySet().stream().sorted(Entry.comparingByValue(Comparator.reverseOrder()))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		System.out.println("\nScores " + "-".repeat(50));
		sortedMap.forEach((k, v) -> System.out.println(k + ": " + v));
	}

	/** Return a List of all PlayerNames */
	public static List<String> getPlayerNames() {
		List<String> names = new ArrayList<>();
		for (Player p : PLAYERS)
			names.add(p.name);
		return names;
	}

	/** Returns the amount of coins of a player. */
	public static int getCoinsOf(String player) {
		return getPlayerByName(player).getCoins();
	}

	/** Returns the cards of a player. */
	public static List<Integer> getCardsOf(String player) {
		return getPlayerByName(player).getCards();
	}

	/** Pass this and youll receive the next players name. */
	public static String getNextPlayer(Player you) {
		int next = PLAYERS.indexOf(you) + 1;
		return PLAYERS.get(next == PLAYERS.size() ? 0 : next).name;
	}

	/** Identifies a {@link Player} by name. */
	private static Player getPlayerByName(String player) {
		return PLAYERS.stream().filter(p -> p.name.equals(player)).findFirst().get();
	}

	/**
	 * Returns a deck of shuffled cards between 1 and the given parameter
	 * {@code cards}.
	 */
	private static List<Integer> generateCards(int cards) {
		if (cards < 1)
			throw new IllegalArgumentException("There has to be atleast one card.");
		List<Integer> c = new ArrayList<>();
		for (int i = 1; i <= cards; i++)
			c.add(i);
		Collections.shuffle(c);
		return c;
	}

	public static void main(String[] args) {
		play();
	}
}
