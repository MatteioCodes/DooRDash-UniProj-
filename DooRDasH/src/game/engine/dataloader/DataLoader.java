package game.engine.dataloader;

import game.engine.Role;
import game.engine.cards.*;
import game.engine.cells.*;
import game.engine.exceptions.InvalidCSVFormat;
import game.engine.monsters.*;

import java.io.*;
import java.util.ArrayList;

public class DataLoader {
    public static final String CARDS_FILE_NAME = "cards.csv";
    public static final String CELLS_FILE_NAME = "cells.csv";
    private static final String MONSTERS_FILE_NAME = "monsters.csv";

    public static ArrayList<Card> readCards() throws IOException {
        ArrayList<Card> cards = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(CARDS_FILE_NAME));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] parts = line.split(",");
            if (parts.length < 4) {
                throw new InvalidCSVFormat(line);
            }
            String cardType = parts[0].trim();
            String name = parts[1].trim();
            String description = parts[2].trim();
            int rarity = Integer.parseInt(parts[3].trim());
            Card card = null;
            switch (cardType) {
                case "SWAPPER":
                    card = new SwapperCard(name, description, rarity);
                    break;
                case "SHIELD":
                    card = new ShieldCard(name, description, rarity);
                    break;
                case "ENERGYSTEAL":
                    if (parts.length < 5) {
                        throw new InvalidCSVFormat(line);
                    }
                    int energy = Integer.parseInt(parts[4].trim());
                    card = new EnergyStealCard(name, description, rarity, energy);
                    break;
                case "STARTOVER":
                    if (parts.length < 5) {
                        throw new InvalidCSVFormat(line);
                    }
                    boolean lucky = Boolean.parseBoolean(parts[4].trim());
                    card = new StartOverCard(name, description, rarity, lucky);
                    break;
                case "CONFUSION":
                    if (parts.length < 5) {
                        throw new InvalidCSVFormat(line);
                    }
                    int duration = Integer.parseInt(parts[4].trim());
                    card = new ConfusionCard(name, description, rarity, duration);
                    break;
                default:
                    throw new InvalidCSVFormat(line);
            }
            if (card != null) {
                cards.add(card);
            }
        }
        reader.close();
        return cards;
    }

    public static ArrayList<Cell> readCells() throws IOException {
        ArrayList<Cell> cells = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(CELLS_FILE_NAME));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] parts = line.split(",");
            if (parts.length < 2) {
                throw new InvalidCSVFormat(line);
            }
            String name = parts[0].trim();
            Cell cell = null;
            if (parts.length == 3) {
                String roleStr = parts[1].trim();
                int energy = Integer.parseInt(parts[2].trim());
                Role role = Role.valueOf(roleStr);
                cell = new DoorCell(name, role, energy);
            } else if (parts.length == 2) {
                int effect = Integer.parseInt(parts[1].trim());
                if (effect > 0) {
                    cell = new ConveyorBelt(name, effect);
                } else {
                    cell = new ContaminationSock(name, effect);
                }
            } else {
                throw new InvalidCSVFormat(line);
            }
            if (cell != null) {
                cells.add(cell);
            }
        }
        reader.close();
        return cells;
    }

    public static ArrayList<Monster> readMonsters() throws IOException {
        ArrayList<Monster> monsters = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(MONSTERS_FILE_NAME));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] parts = line.split(",");
            if (parts.length < 5) {
                throw new InvalidCSVFormat(line);
            }
            String monsterType = parts[0].trim();
            String name = parts[1].trim();
            String description = parts[2].trim();
            Role role = Role.valueOf(parts[3].trim());
            int energy = Integer.parseInt(parts[4].trim());
            Monster monster = null;
            switch (monsterType) {
                case "DASHER":
                    monster = new Dasher(name, description, role, energy);
                    break;
                case "DYNAMO":
                    monster = new Dynamo(name, description, role, energy);
                    break;
                case "MULTITASKER":
                    monster = new MultiTasker(name, description, role, energy);
                    break;
                case "SCHEMER":
                    monster = new Schemer(name, description, role, energy);
                    break;
                default:
                    throw new InvalidCSVFormat(line);
            }
            if (monster != null) {
                monsters.add(monster);
            }
        }
        reader.close();
        return monsters;
    }
}