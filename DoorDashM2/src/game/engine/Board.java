package game.engine;


import java.util.*;
import game.engine.exceptions.*;
import game.engine.cards.*;
import game.engine.cells.*;
import game.engine.monsters.*;

import static game.engine.Constants.*;

public class Board {
	private Cell[][] boardCells;
	private static ArrayList<Monster> stationedMonsters;
	private static ArrayList<Card> originalCards;
	public static ArrayList<Card> cards;

	public Board(ArrayList<Card> readCards) {
		this.boardCells = new Cell[Constants.BOARD_ROWS][Constants.BOARD_COLS];
		stationedMonsters = new ArrayList<Monster>();
		originalCards = readCards;
		cards = new ArrayList<Card>();
		setCardsByRarity();
		reloadCards();
	}

	public Cell[][] getBoardCells() {
		return boardCells;
	}

	public static ArrayList<Monster> getStationedMonsters() {
		return stationedMonsters;
	}

	public static void setStationedMonsters(ArrayList<Monster> stationedMonsters) {
		Board.stationedMonsters = stationedMonsters;
	}

	public static ArrayList<Card> getOriginalCards() {
		return originalCards;
	}

	public static ArrayList<Card> getCards() {
		return cards;
	}

	public static void setCards(ArrayList<Card> cards) {
		Board.cards = cards;
	}

	//ai{
	private int[] indexToRowCol(int index) {
		int row = index / Constants.BOARD_COLS;
		int col = index % Constants.BOARD_COLS;

		if (row % 2 != 0) {
			col = Constants.BOARD_COLS - 1 - col;
		}

		return new int[]{row, col};
	}
	//}
	private Cell getCell(int index){
		int[] get = indexToRowCol(index);
		int row = get[0];
		int col= get[1];
		return boardCells[row][col];
	}
	private void setCell(int index, Cell cell){
		int[] get = indexToRowCol(index);
		int row = get[0];
		int col= get[1];
		boardCells[row][col] = cell;
	}
	//ai{
	public void initializeBoard(ArrayList<Cell> specialCells){
		 //A method that populates the board grid using the special cells loaded from the CSV
		//Even-indexed positions become normal rest cells and odd-indexed positions become DoorCells. Card cells, conveyor belts, contamination socks, and monster cells are placed at their designated indices as defined in Constants in order
		//Stationed monsters are assigned positions and placed on the board.
		//new MonsterCell(monster.getName(), monster)
		// Monster monster = stationedMonsters.get(i);
		//First 50 cells in specialCells are DoorCells
		for (int i = 0; i < Constants.BOARD_SIZE; i++) {
			if (i % 2 == 0) {
				setCell(i, new Cell("Normal Cell"));
			} else {
				setCell(i, specialCells.get(i / 2));
			}
		}

		for (int i = 0; i < Constants.CARD_CELL_INDICES.length; i++) {
			setCell(Constants.CARD_CELL_INDICES[i], new CardCell("Card Cell"));
		}

		int conveyorIndex = 0;
		int sockIndex = 0;

		for (int i = 50; i < specialCells.size(); i++) {
			Cell cell = specialCells.get(i);

			if (cell instanceof ConveyorBelt) {
				setCell(Constants.CONVEYOR_CELL_INDICES[conveyorIndex], cell);
				conveyorIndex++;
			} else if (cell instanceof ContaminationSock) {
				setCell(Constants.SOCK_CELL_INDICES[sockIndex], cell);
				sockIndex++;
			}
		}

		if (stationedMonsters != null) {
			for (int i = 0; i < Constants.MONSTER_CELL_INDICES.length && i < stationedMonsters.size(); i++) {
				int index = Constants.MONSTER_CELL_INDICES[i];
				Monster monster = stationedMonsters.get(i);

				monster.setPosition(index);
				setCell(index, new MonsterCell(monster.getName(), monster));
			}
		}

	}
//}
private void setCardsByRarity(){
	//	card.getRarity();
	//originalcards,Board.getOriginalCards()
	ArrayList<Card> names = new ArrayList<Card>();

	for (Card x: originalCards){
		for(int y = x.getRarity(); y > 0; y--){
			names.add(x);
		}
	}
	originalCards = names;
}

	public static void reloadCards(){
		//A method that resets the active card deck to a freshly shuffled copy of the original expanded card list.
		cards = new ArrayList<Card>(originalCards); //reset
		Collections.shuffle(cards);
	}

	public static Card drawCard() {
		//that removes and returns the top card (first index) from
		//the shuffled deck. If the deck is empty, it is reloaded before drawing.
		if (cards.isEmpty()) {
			reloadCards();
		}
		Card x = cards.get(0);
		cards.remove(0);
		return x;
	}
//ai{
	public void moveMonster(Monster currentMonster, int roll, Monster opponentMonster)
			throws InvalidMoveException {
		int csave = currentMonster.getPosition();
		boolean wasConfused = currentMonster.isConfused();

		currentMonster.move(roll);

		getCell(currentMonster.getPosition()).onLand(currentMonster, opponentMonster);

		if (currentMonster.getPosition() == opponentMonster.getPosition()) {
			currentMonster.setPosition(csave);
			updateMonsterPositions(currentMonster, opponentMonster);
			throw new InvalidMoveException();
		}

		if (wasConfused) {
			currentMonster.decrementConfusion();
			opponentMonster.decrementConfusion();
		}

		updateMonsterPositions(currentMonster, opponentMonster);
	}

	private void updateMonsterPositions(Monster player, Monster opponent) {
		for (int row = 0; row < Constants.BOARD_ROWS; row++) {
			for (int col = 0; col < Constants.BOARD_COLS; col++) {
				boardCells[row][col].setMonster(null);
			}
		}

		getCell(player.getPosition()).setMonster(player);
		getCell(opponent.getPosition()).setMonster(opponent);
	}
	//}
}
