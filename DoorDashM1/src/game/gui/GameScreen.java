package game.gui;

import java.io.IOException;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import game.engine.*;
import game.engine.monsters.*;
import game.engine.cells.*;
import game.engine.cards.Card;

public class GameScreen {

    private Game game;
    private Board board;
    private DoorDashApp app;

    private GridPane boardGrid;
    private VBox leftInfo;
    private VBox rightInfo;
    private Label turnLabel;
    private Label diceLabel;
    private Label cardLabel;
    private Label powerupLabel;
    private Button rollBtn;
    private Button powerupBtn;
    private Label logArea;
    private Stage mainStage;
    private BorderPane bigRoot;

    private boolean powerupUsed;

    public GameScreen(Role role, DoorDashApp app, Stage s) throws IOException {
        this.app = app;
        this.mainStage = s;
        this.game = new Game(role);
        this.board = game.getBoard();
        this.powerupUsed = false;
    }

    public Scene buildScene() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("game-root");
        bigRoot = root;

        HBox top = new HBox();
        top.setSpacing(10);
        top.getStyleClass().add("top-bar");
        turnLabel = new Label("Turn: Your Turn");
        turnLabel.getStyleClass().addAll("top-label", "turn");
        diceLabel = new Label("");
        diceLabel.getStyleClass().add("top-label");
        cardLabel = new Label("");
        cardLabel.getStyleClass().add("top-label");
        Button menuBtn = new Button("Back to Menu");
        menuBtn.getStyleClass().add("game-button");
        DoorDashApp theApp = app;
        menuBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                theApp.goBackToStart();
            }
        });
        Button quitBtn = new Button("Quit");
        quitBtn.getStyleClass().add("game-button");
        quitBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainStage.close();
            }
        });
        top.getChildren().add(turnLabel);
        top.getChildren().add(diceLabel);
        top.getChildren().add(cardLabel);
        top.getChildren().add(menuBtn);
        top.getChildren().add(quitBtn);
        root.setTop(top);

        leftInfo = new VBox();
        leftInfo.setSpacing(5);
        leftInfo.getStyleClass().add("info-panel");
        root.setLeft(leftInfo);

        rightInfo = new VBox();
        rightInfo.setSpacing(5);
        rightInfo.getStyleClass().add("info-panel");
        root.setRight(rightInfo);

        VBox center = new VBox();
        center.setSpacing(10);
        boardGrid = new GridPane();
        drawBoard();
        center.getChildren().add(boardGrid);

        HBox controls = new HBox();
        controls.setSpacing(10);
        controls.getStyleClass().add("controls-bar");
        powerupBtn = new Button("Use Powerup (500 energy)");
        powerupBtn.getStyleClass().add("powerup-button");
        powerupLabel = new Label("");
        powerupLabel.getStyleClass().add("powerup-label");
        rollBtn = new Button("Roll Dice");
        rollBtn.getStyleClass().add("roll-button");
        GameScreen theScreen = this;
        powerupBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                theScreen.doPowerup();
            }
        });
        rollBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                theScreen.doRoll();
            }
        });
        controls.getChildren().add(powerupBtn);
        controls.getChildren().add(powerupLabel);
        controls.getChildren().add(rollBtn);
        center.getChildren().add(controls);
        root.setCenter(center);

        logArea = new Label();
        logArea.setText("");
        logArea.getStyleClass().add("log-area");
        root.setBottom(logArea);

        updateInfo();
        logArea.setText(logArea.getText() + "Game started! You are: " + game.getPlayer().getName() + "\n");
        logArea.setText(logArea.getText() + "Opponent: " + game.getOpponent().getName() + "\n");

        Scene scene = new Scene(root, 1100, 750);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        return scene;
    }

    private void drawBoard() {
        boardGrid.getChildren().clear();
        Cell[][] cells = board.getBoardCells();
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                int index;
                int numpart1Body = row * 10;
                if (row == 0 || row == 2 || row == 4 || row == 6 || row == 8) {
                    index = numpart1Body + col;
                } else {
                    index = numpart1Body + 9 - col;
                }

                StackPane cellPane = new StackPane();
                Circle bg = new Circle();
                bg.setRadius(20);
                Cell cell = cells[row][col];

                if (cell instanceof DoorCell) {
                    DoorCell dc = (DoorCell) cell;
                    if (dc.getRole() == Role.SCARER) {
                        if (dc.isActivated() == true) {
                            bg.setFill(Color.DARKVIOLET);
                        } else {
                            bg.setFill(Color.PURPLE);
                        }
                    } else {
                        if (dc.isActivated() == true) {
                            bg.setFill(Color.DARKBLUE);
                        } else {
                            bg.setFill(Color.BLUE);
                        }
                    }
                } else if (cell instanceof CardCell) {
                    bg.setFill(Color.YELLOW);
                } else if (cell instanceof MonsterCell) {
                    bg.setFill(Color.GREEN);
                } else if (cell instanceof ConveyorBelt) {
                    bg.setFill(Color.LIGHTBLUE);
                } else if (cell instanceof ContaminationSock) {
                    bg.setFill(Color.RED);
                } else {
                    bg.setFill(Color.LIGHTGRAY);
                }

                String cellTxt = "" + index;
                if (cell instanceof DoorCell) {
                    DoorCell dc = (DoorCell) cell;
                    cellTxt = cellTxt + "\nE:" + dc.getEnergy();
                }
                if (cell instanceof TransportCell) {
                    TransportCell tc = (TransportCell) cell;
                    cellTxt = cellTxt + "\n" + tc.getEffect();
                }
                if (cell.isOccupied() == true && cell.getMonster() != null) {
                    Monster m = cell.getMonster();
                    String mn = m.getName();
                    if (mn.length() > 5) {
                        mn = mn.substring(0, 5);
                    }
                    cellTxt = cellTxt + "\n" + mn;
                }

                Label cellLabel = new Label(cellTxt);
                cellLabel.getStyleClass().add("cell-index");
                cellPane.getChildren().addAll(bg, cellLabel);
                boardGrid.add(cellPane, col, row);
            }
        }
    }

    private void doPowerup() {
        Monster current = game.getCurrent();

        if (current.getEnergy() < 500) {
            logArea.setText(logArea.getText() + "no energy!! need 500 have " + current.getEnergy() + "\n");
            return;
        }

        try {
            game.usePowerup();
            powerupUsed = true;
            powerupLabel.setText("Powerup ready!");
            logArea.setText(logArea.getText() + current.getName() + " activated powerup!\n");
        } catch (Exception e) {
            logArea.setText(logArea.getText() + "powerup broke: " + e.getMessage() + "\n");
        }
    }

    private void doRoll() {
        Monster current = game.getCurrent();
        boolean isPlayerTurn = false;
        if (current == game.getPlayer()) {
            isPlayerTurn = true;
        }

        int cardsBefore = Board.getCards().size();

        if (powerupUsed == true) {
            powerupUsed = false;
            powerupLabel.setText("");
        }

        try {
            game.playTurn();
            logArea.setText(logArea.getText() + current.getName() + " rolled " + game.lastRoll + " went to cell " + current.getPosition() + "\n");
        } catch (Exception e) {
            logArea.setText(logArea.getText() + "move broke " + e.getMessage() + "\n");
            updateInfo();
            return;
        }

        diceLabel.setText("Rolled: " + game.lastRoll);

        if (Board.getCards().size() < cardsBefore && Board.lastDrawnCard != null) {
            Card card = Board.lastDrawnCard;
            cardLabel.setText("Drew card: " + card.getName() + " - " + card.getDescription());
            logArea.setText(logArea.getText() + "Drew card: " + card.getName() + "\n");
        } else {
            cardLabel.setText("");
        }

        if (game.getWinner() != null) {
            updateInfo();
            showWinScreen();
            return;
        }

        updateInfo();

        if (isPlayerTurn == true) {
            playOpponentTurn();
        }
    }

    private void playOpponentTurn() {
        Monster current = game.getCurrent();

        if (current == game.getPlayer()) {
            return;
        }

        int cardsBefore = Board.getCards().size();

        if (current.getEnergy() >= 500 && current.isFrozen() == false) {
            try {
                game.usePowerup();
            } catch (Exception e) {
            }
        }

        try {
            game.playTurn();
            logArea.setText(logArea.getText() + current.getName() + " rolled " + game.lastRoll + " went to cell " + current.getPosition() + "\n");
        } catch (Exception e) {
            updateInfo();
            return;
        }

        diceLabel.setText("Opponent rolled: " + game.lastRoll);

        if (Board.getCards().size() < cardsBefore && Board.lastDrawnCard != null) {
            Card card = Board.lastDrawnCard;
            cardLabel.setText("Opponent drew: " + card.getName());
            logArea.setText(logArea.getText() + "Opponent drew card: " + card.getName() + "\n");
        }

        if (game.getWinner() != null) {
            updateInfo();
            showWinScreen();
            return;
        }

        updateInfo();
    }

    private void updateInfo() {
        drawBoard();

        Monster current = game.getCurrent();
        if (current == game.getPlayer()) {
            turnLabel.setText("Turn: Your Turn");
        } else {
            turnLabel.setText("Turn: Opponent's Turn");
        }

        leftInfo.getChildren().clear();
        addMonsterInfo(leftInfo, game.getPlayer());

        rightInfo.getChildren().clear();
        addMonsterInfo(rightInfo, game.getOpponent());
    }

    private void addMonsterInfo(VBox box, Monster m) {
        Label nameLabel = new Label("Name: " + m.getName());
        Label typeLabel = new Label("Type: " + m.getClass().getName());
        Label origRoleLabel = new Label("Original Role: " + m.getOriginalRole());
        String roleText = "Current Role: " + m.getRole();
        if (m.getRole() != m.getOriginalRole()) {
            roleText = roleText + " (CONFUSED!)";
        }
        Label roleLabel = new Label(roleText);
        Label energyLabel = new Label("Energy: " + m.getEnergy());
        Label posLabel = new Label("Position: " + m.getPosition());
        box.getChildren().add(nameLabel);
        box.getChildren().add(typeLabel);
        box.getChildren().add(origRoleLabel);
        box.getChildren().add(roleLabel);
        box.getChildren().add(energyLabel);
        box.getChildren().add(posLabel);
        if (m.isShielded() == true) {
            Label shieldLabel = new Label("SHIELDED");
            shieldLabel.getStyleClass().add("status-shielded");
            box.getChildren().add(shieldLabel);
        }
        if (m.isFrozen() == true) {
            Label freezeLabel = new Label("FROZEN - skipping turn");
            freezeLabel.getStyleClass().add("status-frozen");
            box.getChildren().add(freezeLabel);
        }
        if (m.isConfused() == true) {
            Label confusedLabel = new Label("Confused: " + m.getConfusionTurns() + " turns left");
            confusedLabel.getStyleClass().add("status-confused");
            box.getChildren().add(confusedLabel);
        }
        if (m instanceof Dasher) {
            Dasher d = (Dasher) m;
            if (d.getMomentumTurns() > 0) {
                Label momLabel = new Label("Momentum: " + d.getMomentumTurns() + " turns");
                box.getChildren().add(momLabel);
            }
        }
        if (m instanceof MultiTasker) {
            MultiTasker mt = (MultiTasker) m;
            if (mt.getNormalSpeedTurns() > 0) {
                Label focusLabel = new Label("Focus Mode: " + mt.getNormalSpeedTurns() + " turns");
                box.getChildren().add(focusLabel);
            }
        }
    }

    private void showWinScreen() {
        Monster winner = game.getWinner();

        VBox boxxy = new VBox();
        boxxy.setSpacing(10);

        Label win1 = new Label("GAME OVERRR");
        Label win2 = new Label(winner.getName() + " WINS YAY");
        String dets = "Role: " + winner.getRole() + "  Pos: " + winner.getPosition() + "\n";
        dets = dets + game.getPlayer().getName() + " had " + game.getPlayer().getEnergy() + "\n";
        dets = dets + game.getOpponent().getName() + " had " + game.getOpponent().getEnergy();
        Label win3 = new Label(dets);

        Button backBtn = new Button("OK go back");
        DoorDashApp aa = app;
        backBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                aa.goBackToStart();
            }
        });

        boxxy.getChildren().add(win1);
        boxxy.getChildren().add(win2);
        boxxy.getChildren().add(win3);
        boxxy.getChildren().add(backBtn);

        bigRoot.setCenter(boxxy);
    }
}
