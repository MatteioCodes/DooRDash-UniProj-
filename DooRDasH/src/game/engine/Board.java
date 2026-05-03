package game.engine;

import game.engine.cards.Card;
import game.engine.cells.*;
import game.engine.monsters.Monster;

import java.util.ArrayList;

public class Board {
    private final Cell[][] boardCells;
    private static ArrayList<Monster> stationedMonsters;
    private static ArrayList<Card> originalCards;
    public static ArrayList<Card> cards;

    public Board(ArrayList<Card> readCards) {
        this.boardCells = new Cell[Constants.BOARD_ROWS][Constants.BOARD_COLS];
        stationedMonsters = new ArrayList<>();
        cards = new ArrayList<>();
        originalCards = readCards;
    }

    public Cell[][] getBoardCells() {
        return boardCells;
    }

    public static ArrayList<Monster> getStationedMonsters() {
        return stationedMonsters;
    }

    public static void setStationedMonsters(ArrayList<Monster> monsters) {
        stationedMonsters = monsters;
    }

    public static ArrayList<Card> getOriginalCards() {
        return originalCards;
    }

    public static ArrayList<Card> getCards() {
        return cards;
    }

    public static void setCards(ArrayList<Card> newCards) {
        cards = newCards;
    }
}