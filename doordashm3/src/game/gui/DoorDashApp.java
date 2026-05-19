package game.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import game.engine.Role;

public class DoorDashApp extends Application {

    private Stage primaryStage;
    private GameScreen gameScreen;
    private RadioButton scarerBtn;
    private RadioButton laugherBtn;
    private Label errshow;

    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("DoorDasH - Scare vs Laugh Touchdown");
        showStartScreen();
        primaryStage.show();
    }

    private void showStartScreen() {
        VBox root = new VBox();
        root.setSpacing(15);
        root.getStyleClass().add("start-screen");

        Label title = new Label("DoorDasH");
        title.getStyleClass().add("title-label");
        Label subtitle = new Label("Scare vs Laugh Touchdown");
        subtitle.getStyleClass().add("subtitle-label");

        Label selectLabel = new Label("Select your side:");
        selectLabel.getStyleClass().add("select-label");

        scarerBtn = new RadioButton("SCARER");
        scarerBtn.getStyleClass().add("radio-button");
        laugherBtn = new RadioButton("LAUGHER");
        laugherBtn.getStyleClass().add("radio-button");

        HBox radioBox = new HBox();
        radioBox.setSpacing(20);
        radioBox.getChildren().addAll(scarerBtn, laugherBtn);

        Button startBtn = new Button("Start Game");
        startBtn.getStyleClass().add("start-button");

        Label rulesLabel = new Label();
        rulesLabel.getStyleClass().add("rules-area");
        String rulesTextt = "RULES:\n" +
            "1. Reach cell 99 with 1000+ energy to win\n" +
            "2. Roll dice to move your monster\n" +
            "3. Use powerup before rolling (costs 500 energy)\n" +
            "4. Land on doors to gain or lose energy\n" +
            "5. Card cells give you special cards\n" +
            "6. Conveyor belts move you forward\n" +
            "7. Contamination socks move you back\n" +
            "8. Monster cells trigger special effects\n\n" +
            "MONSTER TYPES:\n" +
            "- Dasher: 2x speed\n" +
            "- Dynamo: 2x energy gain\n" +
            "- MultiTasker: 0.5x speed but +200 bonus\n" +
            "- Schemer: steals +10 energy";
        rulesLabel.setText(rulesTextt);

        errshow = new Label();

        DoorDashApp app = this;

        startBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (scarerBtn.isSelected() == false && laugherBtn.isSelected() == false) {
                    errshow.setText("pick a side!!");
                    return;
                }

                Role role = null;
                if (scarerBtn.isSelected() == true) {
                    role = Role.SCARER;
                } else {
                    role = Role.LAUGHER;
                }

                try {
                    gameScreen = new GameScreen(role, app, primaryStage);
                    Scene scene = gameScreen.buildScene();
                    primaryStage.setScene(scene);
                } catch (Exception e) {
                    errshow.setText("oops " + e.getMessage());
                }
            }
        });

        root.getChildren().add(title);
        root.getChildren().add(subtitle);
        root.getChildren().add(selectLabel);
        root.getChildren().add(radioBox);
        root.getChildren().add(startBtn);
        root.getChildren().add(errshow);
        root.getChildren().add(rulesLabel);

        Scene scene = new Scene(root, 500, 600);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    public void goBackToStart() {
        showStartScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
