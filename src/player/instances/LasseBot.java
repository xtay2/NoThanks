package player.instances;

import java.util.ArrayList;

import java.util.List;

import game.GameManager;
import game.GameState;
import player.Player;

public final class LasseBot extends Player {

	private static List<String> others;
	private static List<Integer> CardsLeftInDeck = new ArrayList<>();

	public LasseBot(int i) {
		super("LasseBot" + i);
		addALLCards();

	}

	private void addALLCards() {
		for (int i = 1; i <= 35; i++)
			CardsLeftInDeck.add(i);
	}

	private void addAllPlayersInOrder() {
		others = GameManager.getPlayerNames();
		String a;
		while (!name.equals(others.get(0))) {
			a = others.get(0);
			others.remove(0);
			others.add(a);
		}
	}

	@Override
	public boolean takeCard(GameState gameState) {
//        System.out.println(GameManager.getCardsOf(me));
		int card = gameState.getCard();
		int coins = gameState.getCoins();

		if (others == null)
			addAllPlayersInOrder();

		removePlayedCards();

		if (card <= coins) {
//            System.out.println("        i took last card");
			return true;
		}
		if (GameManager.getCardsOf(name).contains(card - 1) || (GameManager.getCardsOf(name).contains(card + 1) && coins >= 1)) {
//            System.out.println("        was good for my cards");
			return true;
		}
		if (oneOfOthersCantNotTake()) {
//            System.out.println("        one of the aothers will get the card");
			return false;
		}

		if ((CardsLeftInDeck.contains(card - 1) || CardsLeftInDeck.contains(card + 1)) && GameManager.getCoinsOf(name) < coins
				&& CardsLeftInDeck.size() - 1 <= coins) {
//            System.out.println("        weil ja");
			return true;
		}
		if (probFor(card - 1) + probFor(card + 1) + probFor(card - 2) + probFor(card + 2) > 50) {
//            System.out.println("        hohe warscheinlichketi für nachbarn");
			return true;
		}
		if (nextCanTake(card) && coins < 11 * others.size() / 2) {
//            System.out.println("        neheme weg");
			return true;
		}

		return false;
	}

	private boolean nextCanTake(int card) {
		return GameManager.getCardsOf(others.get(1)).contains(card - 1) || GameManager.getCardsOf(others.get(1)).contains(card + 1);
	}

	private boolean oneOfOthersCantNotTake() {
		for (int i = 0; i < others.size(); i++) {
			if (GameManager.getCoinsOf(others.get(i)) == 0) {
				return true;
			}
		}
		return false;
	}

	private void removePlayedCards() {
		for (String p : GameManager.getPlayerNames()) {
			List<Integer> cards = GameManager.getCardsOf(p);
			CardsLeftInDeck.removeAll(cards);
		}
	}

	private double probFor(double card) {

		return (1 / CardsLeftInDeck.size()) * 100;

	}
}