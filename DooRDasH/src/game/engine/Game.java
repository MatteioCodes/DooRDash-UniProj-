package game.engine;

import game.engine.cards.Card;
import game.engine.dataloader.DataLoader;
import game.engine.monsters.Monster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    private final Board board;
    private final ArrayList<Monster> allMonsters;
    private final Monster player;
    private final Monster opponent;
    private Monster current;

    public Game(Role playerRole) throws IOException {
        ArrayList<Card> loadedCards = DataLoader.readCards();
        this.board = new Board(loadedCards);
        this.allMonsters = DataLoader.readMonsters();
        this.player = selectRandomMonsterByRole(playerRole);
        Role opponentRole = playerRole == Role.SCARER ? Role.LAUGHER : Role.SCARER;
        this.opponent = selectRandomMonsterByRole(opponentRole);
        this.current = this.player;
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
        ArrayList<Monster> matchingMonsters = new ArrayList<>();
        for (Monster monster : allMonsters) {
            if (monster.getRole() == role) {
                matchingMonsters.add(monster);
            }
        }
        if (matchingMonsters.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(matchingMonsters.size());
        return matchingMonsters.get(index);
    }
}
