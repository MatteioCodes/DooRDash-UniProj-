package game.engine.cards;

import game.engine.monsters.Monster;

public class SwapperCard extends Card {

	public SwapperCard(String name, String description, int rarity) {
		super(name, description, rarity, true);
	}

//SwapperCard If the player is behind the opponent in position,
//the two monsters swap their positions.
	public void performAction(Monster player, Monster opponent){
if(player.getPosition() < opponent.getPosition()){
	int temp = opponent.getPosition();
	int temp2= player.getPosition();
	player.setPosition(temp);
	opponent.setPosition(temp2);
}


}
}
