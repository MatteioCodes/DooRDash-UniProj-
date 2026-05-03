package game.engine;

import java.io.IOException;
import java.util.*;

import game.engine.dataloader.DataLoader;
import game.engine.exceptions.*;
import game.engine.monsters.*;

import static game.engine.Board.*;

public class Game {
	private Board board;
	private ArrayList<Monster> allMonsters; 
	private Monster player;
	private Monster opponent;
	private Monster current;

	public Game(Role playerRole) throws IOException {
		this.board = new Board(DataLoader.readCards());

		this.allMonsters = DataLoader.readMonsters();

		this.player = selectRandomMonsterByRole(playerRole);

		Role opponentRole;
		if (playerRole == Role.SCARER) {
			opponentRole = Role.LAUGHER;
		} else {
			opponentRole = Role.SCARER;
		}

		this.opponent = selectRandomMonsterByRole(opponentRole);

		this.current = player;

		this.allMonsters.remove(player);
		this.allMonsters.remove(opponent);

		Board.setStationedMonsters(this.allMonsters);

		this.board.initializeBoard(DataLoader.readCells());
	}
	
	public Board getBoard() {
		return board;
	}
	
	public ArrayList<Monster> getAllMonsters() {
		return allMonsters; 
	}
	
	public Monster getPlayer() {
		return player;
	}
	
	public Monster getOpponent() {
		return opponent;
	}
	
	public Monster getCurrent() {
		return current;
	}
	
	public void setCurrent(Monster current) {
		this.current = current;
	}
	
	private Monster selectRandomMonsterByRole(Role role) {
		Collections.shuffle(allMonsters);
	    return allMonsters.stream()
	    		.filter(m -> m.getRole() == role)
	    		.findFirst()
	    		.orElse(null);
	}
	private Monster getCurrentOpponent(){
		Monster p = getPlayer();
		Monster o = getOpponent();
		Monster c = getCurrent();
		if(p.equals(c)){
			return o;
		}else {
			return p;
		}
	}
	private int rollDice(){
		int roll = (int)(Math.random() * 6) + 1;
		return roll;
	}
	public void usePowerup() throws OutOfEnergyException {
		if (getCurrent().getEnergy() < Constants.POWERUP_COST) {
			throw new OutOfEnergyException();
		}

		getCurrent().setEnergy(getCurrent().getEnergy() - Constants.POWERUP_COST);
		getCurrent().executePowerupEffect(getCurrentOpponent());
	}
	//ai{
	public void playTurn() throws InvalidMoveException {
		Monster current = getCurrent();

		if (current.isFrozen()) {
			current.setFrozen(false);
			switchTurn();
			return;
		}

		int roll = rollDice();
		board.moveMonster(current, roll, getCurrentOpponent());
		switchTurn();
	}
	//}

	private void switchTurn(){
		if(getCurrent() == getPlayer()){
			setCurrent(getOpponent());
		}else{
			setCurrent(getPlayer());
		}
	}
	private boolean checkWinCondition(Monster monster){
		int x = monster.getPosition();
		if(monster.getPosition() == Constants.WINNING_POSITION && monster.getEnergy() >= Constants.WINNING_ENERGY){
			return true;
		}else{
			return false;
		}
	}
	public Monster getWinner(){
		if(checkWinCondition(getPlayer())){
			return getPlayer();
		}else if(checkWinCondition(getOpponent())){
			return getOpponent();
		}else{
			return null;
		}
	}
}
