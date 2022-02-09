package player.instances;

import game.GameState;
import player.Player;

public final class Bot extends Player {

	public Bot() {
		super("Your name.");
	}

	@Override
	public boolean takeCard(GameState gameState) {
		return false;
	}

}
