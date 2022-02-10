package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import player.Player;
import player.instances.LasseBot;
import player.instances.Person;
import player.instances.SimpleBot;

public abstract class GameManager {

	/** The amount of coins every player gets at the start of the game. */
	public static final int START_COINS = 11;

	private static List<Player> players;

	private static List<Integer> CARDS;

	/**
	 * Executes the whole game and returns the winner.
	 */
	public static Player play(List<Player> playerList) {
		if (playerList.size() < 2)
			throw new IllegalArgumentException("There have to be atleast two players.");
		// INIT
		players = new ArrayList<>(playerList);
		Collections.shuffle(players);
		CARDS = generateCards(35);
		// GAME
		GameState g = new GameState(CARDS.remove(0));
		while (!CARDS.isEmpty()) {
			// For every player
			for (Player p : players) {
				// If they got coins they can take a card
				if (p.getCoins() > 1) {
					// If they take a card it, and the coins get added.
					if (p.takeCard(g)) {
						System.out.println(p + " takes " + g);
						if ((g = give(g, p)) == null)
							break;
					}
					// If they dont take the card, it gets passed on.
					else {
						System.out.println(p + " passes " + g);
						p.decrease();
						g.increase();
					}
				}
				// If they had no coins, they have to take the card.
				else {
					System.out.println(p + " has to take " + g);
					if ((g = give(g, p)) == null)
						break;
				}
			}

		}
		return countScores();
	}

	private static GameState give(GameState g, Player p) {
		p.give(g);
		if (!CARDS.isEmpty())
			return new GameState(CARDS.remove(0));
		return null;
	}

	/** Count all scores and display the Results. */
	private static Player countScores() {
		Map<Player, Integer> scores = new HashMap<>();
		for (Player p : players)
			scores.put(p, p.count());

		LinkedHashMap<Player, Integer> sortedMap = scores.entrySet().stream().sorted(Entry.comparingByValue())
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		System.out.println("\nScores " + "-".repeat(50));
		sortedMap.forEach((k, v) -> System.out.println(k + ": \t" + v + " \t" + k.getCoins() + " \t" + k.getCards()));
		return sortedMap.keySet().iterator().next();
	}

	/** Return a List of all PlayerNames */
	public static List<String> getPlayerNames() {
		List<String> names = new ArrayList<>();
		for (Player p : players)
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
		int next = players.indexOf(you) + 1;
		return players.get(next == players.size() ? 0 : next).name;
	}

	/** Identifies a {@link Player} by name. */
	private static Player getPlayerByName(String player) {
		return players.stream().filter(p -> p.name.equals(player)).findFirst().get();
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
		play(List.of(
				new LasseBot(1), 
				new Person("Lasse"),
				new SimpleBot("DennisBot", 0, 8.88375798924735, 6.397323652505392, 3.837189373582033, 5.482606473935411),
				new Person("Dennis"), 
				new Person("Leo")));
	}
}
