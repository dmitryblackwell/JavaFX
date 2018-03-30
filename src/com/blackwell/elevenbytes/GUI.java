package com.blackwell.elevenbytes;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GUI extends Application {

    Button[][] btns = new Button[Map.FIELD_SIZE][Map.FIELD_SIZE]; // TODO rename this shit
    Text scoreText = new Text();

    Map map = new Map();
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        VBox vBox = new VBox();

        for(int i = 0; i< Map.FIELD_SIZE; ++i){
            HBox hBox = new HBox();
            for (int j = 0; j< Map.FIELD_SIZE; ++j){
                btns[i][j] = new Button();
                btns[i][j].setText("0");
                Font font = new Font(20);
                btns[i][j].setFont(font);
                btns[i][j].setPrefWidth(70);
                btns[i][j].setPrefHeight(70);
                btns[i][j].setDisable(true);
                hBox.getChildren().add(btns[i][j]);
            }
            vBox.getChildren().add(hBox);
        }
        scoreText.setText(String.valueOf(map.getScore()));
        vBox.getChildren().add(scoreText);
        StackPane root = new StackPane();
        root.getChildren().add(vBox);

        primaryStage.setTitle("2048");
        Scene scene = new Scene(root, 280, 400);
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
        primaryStage.show();

        setUpdateOfMap();
    }

    private void setUpdateOfMap(){
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(
                new Runnable(){
                    int counter=0;
                    @Override
                    public void run() {
                        counter++;
                        Platform.runLater(new Runnable(){
                            @Override
                            public void run() {
                                for(int i=0; i<Map.FIELD_SIZE; ++i) {
                                    for (int j = 0; j < Map.FIELD_SIZE; ++j) {
                                        if (map.getCellValue(j,i) == 0)
                                            btns[i][j].setText("");
                                        else
                                            btns[i][j].setText(String.valueOf(map.getCellValue(j, i)));
                                    }
                                }
                                scoreText.setText(String.valueOf(map.getScore()));
                            }
                        });
                    }
                },
                80,
                80,
                TimeUnit.MILLISECONDS);
    }
}
