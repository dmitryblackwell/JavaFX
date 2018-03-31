package com.blackwell.elevenbytes;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GUI extends Application {

    private Button[][] cells = new Button[MapImpl.FIELD_SIZE][MapImpl.FIELD_SIZE];
    private Text scoreText = new Text();

    private Map map = new MapImpl();

    /** Delay before initial update if the map */
    private static final int INITIAL_DELAY = 1;
    /** Map is updating in window every PERIOD (in this case 10) */
    private static final int PERIOD = 10;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Create window and all nodes in it
     * Show this window
     * calling method that updates map
     * @param primaryStage stage that created
     */
    @Override
    public void start(Stage primaryStage) {
        // creating vertical box with and putting there elments
        VBox vBox = new VBox();

        // creating Text for displaying score
        HBox hTextBox = new HBox();
        hTextBox.setPrefHeight(120);
        scoreText.setText(String.valueOf(map.getScore()));
        scoreText.setFont(new Font(40));
        scoreText.setTextAlignment(TextAlignment.CENTER);
        hTextBox.getChildren().add(scoreText);
        hTextBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(hTextBox);
        
        for(int i = 0; i< MapImpl.FIELD_SIZE; ++i){
            HBox hBox = new HBox();
            for (int j = 0; j< MapImpl.FIELD_SIZE; ++j){
                cells[i][j] = new Button();
                cells[i][j].setText("0");
                cells[i][j].setStyle("-fx-background-color: #f3ffff");
                cells[i][j].setStyle("-fx-text-fill: #ff0000");
                cells[i][j].setFont(new Font(20));
                cells[i][j].setPrefWidth(70);
                cells[i][j].setPrefHeight(70);
                cells[i][j].setDisable(true);
                hBox.getChildren().add(cells[i][j]);
            }
            vBox.getChildren().add(hBox);
        }

        StackPane root = new StackPane();
        StackPane.setAlignment(scoreText, Pos.CENTER);

        root.setStyle("-fx-background-color: #f3f3f8");
        root.getChildren().add(vBox);

        primaryStage.setTitle("2048");
        Scene scene = new Scene(root, 280, 400);

        // keyReleasedHandler
        scene.setOnKeyReleased(event -> {
            switch (event.getCode()){
                case RIGHT:
                    map.moveRight();
                    break;
                case LEFT:
                    map.moveLeft();
                    break;
                case UP:
                    map.moveUp();
                    break;
                case DOWN:
                    map.moveDown();
                    break;
            }
        });

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        setUpdateOfMap();
    }

    /**
     * Run new thread that updating map on the screen
     * It is comes by all cells + score and drawing them in right oder
     */
    private void setUpdateOfMap(){
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(
                () -> Platform.runLater(() -> {
                    for(int i = 0; i< Map.FIELD_SIZE; ++i) {
                        for (int j = 0; j < Map.FIELD_SIZE; ++j) {
                            if (map.getCellValue(j,i) == 0)
                                cells[i][j].setVisible(false);
                            else {
                                cells[i][j].setText(String.valueOf(map.getCellValue(j, i)));
                                cells[i][j].setVisible(true);
                            }
                        }
                    }
                    scoreText.setText(String.valueOf(map.getScore()));
                }),
                INITIAL_DELAY,
                PERIOD,
                TimeUnit.MILLISECONDS);
    }
}
