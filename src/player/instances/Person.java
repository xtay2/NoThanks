package player.instances;

import java.util.Scanner;

import game.GameState;
import player.Player;

public class Person extends Player {

	public Person(String name) {
		super(name);
	}

	@Override
	public boolean takeCard(GameState gameState) {
		System.out.println("\n\nIts your turn " + name + "!");
		System.out.println("Cards: " + getCards());
		System.out.println("Coins: " + getCoins());
		System.out.println("\nDo you want to take the card " + gameState.getCard() + " with " + gameState.getCoins() + "? \n[YES/NO]");
		boolean res = false;
		while (true) {
			Scanner s = new Scanner(System.in);
			String line = s.nextLine();
			if ("YES".equalsIgnoreCase(line)) {
				res = true;
				break;
			}
			if ("NO".equalsIgnoreCase(line)) {
				res = false;
				break;
			}
			s.close();
			System.err.println("Wrong input. Please write YES or NO.");
		}
		System.out.println("\n\n");
		return res;
	}

}
