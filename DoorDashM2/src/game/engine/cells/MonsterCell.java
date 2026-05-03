package game.engine.cells;

import game.engine.monsters.*;

public class MonsterCell extends Cell {
	private Monster cellMonster;

	public MonsterCell(String name, Monster cellMonster) {
		super(name);
		this.cellMonster = cellMonster;
	}

	public Monster getCellMonster() {
		return cellMonster;
	}


//ai{
public void onLand(Monster landingMonster, Monster opponentMonster) {
	super.onLand(landingMonster, opponentMonster);

	if (cellMonster.getRole() == landingMonster.getRole()) {
		landingMonster.executePowerupEffect(opponentMonster);
	} else if (landingMonster.getEnergy() > cellMonster.getEnergy()) {
		int difference = landingMonster.getEnergy() - cellMonster.getEnergy();

		cellMonster.alterEnergy(difference);
		landingMonster.alterEnergy(-difference);
	}
}
//}

}
