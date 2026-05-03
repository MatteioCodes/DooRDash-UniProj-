package game.engine.monsters;

import game.engine.Board;
import game.engine.Constants;
import game.engine.Role;

public class Schemer extends Monster {
	
	public Schemer(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
	}

	//ai
	@Override
	public void executePowerupEffect(Monster opponentMonster) {
		int stolenTotal = 0;


		int stolen = stealEnergyFrom(opponentMonster);
		stolenTotal += stolen;


		for (Monster stationed : Board.getStationedMonsters()) {
			stolen = stealEnergyFrom(stationed);
			stolenTotal += stolen;
		}


		this.alterEnergy(stolenTotal);
	}

	private int stealEnergyFrom(Monster target) {
		int stolen = Math.min(Constants.SCHEMER_STEAL, target.getEnergy());
		target.alterEnergy(-stolen);
		return stolen;
	}


}
