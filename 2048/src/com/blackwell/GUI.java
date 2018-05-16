package com.blackwell;

import com.blackwell.aiset.AI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GUI extends Application {

    private Button[][] cells = new Button[GameMap.FIELD_SIZE][GameMap.FIELD_SIZE];
    private Text scoreText = new Text();
    private Text bestScoreText = new Text("0");
    private Map map = new GameMap();
    private boolean isAiEnable = true;

    /** Delay before initial update if the map */
    private static final int INITIAL_DELAY = 1;
    /** Map is updating in window every PERIOD (in this case 10) */
    private static final int PERIOD = 50;
    /** Stable size of each cell. */
    private static final int CELL_SIZE = 60;
    private static final int WIDTH = 280;
    private static final int HEIGHT = 400;

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
        primaryStage.initStyle(StageStyle.UTILITY);

        // creating vertical box with and putting there elments
        VBox vBox = new VBox();
        vBox.setSpacing(10d);
        vBox.setAlignment(Pos.BOTTOM_CENTER);

        // Top pane with NewGame-button.

        bestScoreText.setText(map.loadGame());
        bestScoreText.setFont(new Font(30));

        Button newGameBtn = new Button("New game");
        newGameBtn.setTextFill(Paint.valueOf(Bg.SLIGHTLY.toString()));
        newGameBtn.setStyle("-fx-background-color: "+Bg.RED_FULL+";");
        newGameBtn.setOnAction(event -> map = new GameMap());

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        HBox top = new HBox(newGameBtn, region, bestScoreText);

        vBox.getChildren().addAll(top);

        // creating Text for displaying score
        HBox hTextBox = new HBox();
        hTextBox.setPrefHeight(WIDTH);

        scoreText.setText(String.valueOf(map.getScore()));
        scoreText.setFont(new Font(50));
        scoreText.setTextAlignment(TextAlignment.CENTER);

        hTextBox.getChildren().addAll(scoreText);
        hTextBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(hTextBox);
        
        for(int i = 0; i< Map.FIELD_SIZE; ++i){
            HBox hBox = new HBox();
            hBox.setSpacing(10d);
            hBox.setAlignment(Pos.CENTER);
            for (int j = 0; j< Map.FIELD_SIZE; ++j){
                cells[i][j] = new Button();
                cells[i][j].setText("0");
                // For stable size (making nor resizeable buttons)
                cells[i][j].setMaxSize(CELL_SIZE,CELL_SIZE);
                cells[i][j].setPrefSize(CELL_SIZE,CELL_SIZE);
                cells[i][j].setMinSize(CELL_SIZE,CELL_SIZE);
                //cells[i][j].setDisable(true);
                hBox.getChildren().add(cells[i][j]);
            }
            vBox.getChildren().add(hBox);
        }




        StackPane root = new StackPane();
//        root.getChildren().add(newGameBtn);
//        StackPane.setAlignment(newGameBtn, Pos.TOP_LEFT);

        root.setStyle("-fx-background-color: "+Bg.SLIGHTLY+";");
        root.getChildren().add(vBox);

        primaryStage.setTitle("2048");
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        // keyReleasedHandler
        scene.setOnKeyReleased(event -> {
            switch (event.getCode()){
                case D:
                case RIGHT:
                    map.moveRight();
                    break;
                case A:
                case LEFT:
                    map.moveLeft();
                    break;
                case W:
                case UP:
                    map.moveUp();
                    break;
                case S:
                case DOWN:
                    map.moveDown();
                    break;
            }
            map.saveGame(bestScoreText.getText());
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

                            int value = Integer.parseInt(cells[i][j].getText());
                            if (value >= 16 && value < 128)
                                setButtonStyle(i,j,20, Bg.BLUE_LIGHT);
                            else if (value>=128 && value<512)
                                setButtonStyle(i,j,15,Bg.VIOLET_LIGHT);
                            else if (value>=512 && value<2048)
                                setButtonStyle(i,j,15,Bg.RED_FULL);
                            else if (value>=2048)
                                setButtonStyle(i,j,15,Bg.BLACK);
                            else
                                setButtonStyle(i,j,30, Bg.STANDART);
                        }
                    }

                    if (map.getScore() > (Integer.parseInt(bestScoreText.getText())))
                        bestScoreText.setText(String.valueOf(map.getScore()));
                    scoreText.setText(String.valueOf(map.getScore()));

                    // If you wanna have some fun here
                    //if (isAiEnable)
                        //AI.move(map);

                }),
                INITIAL_DELAY,
                PERIOD,
                TimeUnit.MILLISECONDS);
    }

    /**
     * This method is changing Text Color, BackgroundColor, and Font depends on the value of button.
     * @param i y of button
     * @param j x of button
     * @param FontSize size of text
     * @param color color in HEX
     */
    private void setButtonStyle(int i,int j, int FontSize, Bg color){
        if (FontSize < 512)
            cells[i][j].setTextFill(Paint.valueOf(Bg.SLIGHTLY.toString()));
        else
            cells[i][j].setTextFill(Paint.valueOf(Bg.BLACK.toString()));

        cells[i][j].setFont(new Font(FontSize));
        Color BgColor = Color.web(color.toString());

        cells[i][j].setBackground(new Background(new BackgroundFill(
                BgColor,
                new CornerRadii(2),
                new Insets(2))));
    }
}
