package game.engine.cards;

import game.engine.monsters.Monster;

public class ShieldCard extends Card {
	
	public ShieldCard(String name, String description, int rarity) {
		super(name, description, rarity, true); 
	}

	//ShieldCard Grants the player a shield that blocks the next negative energy effect.
	//Removes any existing shield on the opponent.

	@Override
	public void performAction(Monster player, Monster opponent) {
		player.setShielded(true);
		if(opponent.isShielded()) {
			opponent.setShielded(false);
		}
	}
}
