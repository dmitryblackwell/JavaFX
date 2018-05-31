package com.blackwell;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;


public class GUI extends Application {

    public static void main(String[] args) { launch(args); }

    private Circle circle = new Circle(100,100,50);
    private Label tweetLabel = new Label();
    private Label nameLabel = new Label();
    private Label timeLabel = new Label();

    private void dataUpdate(String imgRoot, String name, String time, String tweet){
        Image img = new Image(imgRoot);
        circle.setFill(new ImagePattern(img));
        nameLabel.setText(name);
        timeLabel.setText(time);
        tweetLabel.setText(tweet);
    }

    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static final double SPACING = 15d;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final Insets PADDING = new Insets(20);
    private static final String TWEET_LABEL_STYLE = "-fx-text-fill: #ffffff";
    private static final String NAME_LABEL_STYLE = "-fx-font-weight: bold; -fx-text-fill: #ffffff;";
    private static final String ROOT_STYLE = "-fx-background-color: #0084b4;";
    private static final String BTN_STYLE = "-fx-background-color: #c0deed;";
    private static final String BTN_TEXT = "rand";
    private static final String DEFAULT_LOGIN = "_AlexHirsch";
    private static final String APP_TITLE= "RandomTweet";
    private static final Font NAME_FONT = new Font(30);
    private static final Font TIME_FONT = new Font(10);
    private static final Font TWEET_FONT = new Font(15);


    private void tweetUpdate(String login) {

        ConfigurationBuilder builder = new ConfigurationBuilder();

        // put in TwitterKeys your tokens
        builder.setDebugEnabled(true)
                .setOAuthConsumerKey(TwitterKeys.ConsumerKey)
                .setOAuthConsumerSecret(TwitterKeys.ConsumerSecret)
                .setOAuthAccessToken(TwitterKeys.AccessToken)
                .setOAuthAccessTokenSecret(TwitterKeys.AccessTokenSecret);

        TwitterFactory factory = new TwitterFactory(builder.build());
        Twitter twitter = factory.getInstance();

        try {
            Paging paging = new Paging(1, 200);
            List<Status> statuses = twitter.getUserTimeline(login, paging);
            Random R = new Random();
            statuses.size(); // !!! do not delete - without this nothing working
            Status status = statuses.get(R.nextInt(statuses.size()));
            dataUpdate(status.getUser().getBiggerProfileImageURL(),
                    status.getUser().getName(),
                    dateFormat.format(status.getCreatedAt()),
                    status.getText());
        } catch (TwitterException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Wrong input data");
            alert.setHeaderText(null);
            alert.setContentText("Probably you type wrong username");

            alert.showAndWait();
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.UTILITY);

        VBox vBox = new VBox();
        vBox.setSpacing(SPACING);
        vBox.setAlignment(Pos.CENTER);

        HBox tweetBox = new HBox();
        tweetBox.setSpacing(SPACING*2);
        tweetBox.setPadding(PADDING);
        tweetBox.setAlignment(Pos.CENTER);

        VBox tweetTextBox = new VBox();
        tweetLabel.setWrapText(true);
        tweetLabel.setFont(TWEET_FONT);
        tweetLabel.setStyle(TWEET_LABEL_STYLE);

        nameLabel.setFont(NAME_FONT);
        nameLabel.setStyle(NAME_LABEL_STYLE);
        timeLabel.setFont(TIME_FONT);

        tweetTextBox.getChildren().addAll(nameLabel, timeLabel,tweetLabel);
        tweetBox.getChildren().addAll(circle, tweetTextBox);

        HBox inputBox = new HBox();
        inputBox.setSpacing(SPACING);
        inputBox.setAlignment(Pos.BOTTOM_CENTER);

        Label loginLabel = new Label("@");
        loginLabel.setFont(TWEET_FONT);
        loginLabel.setStyle(NAME_LABEL_STYLE);
        TextField textField = new TextField();
        textField.setText(DEFAULT_LOGIN);
        Button btn = new Button(BTN_TEXT);
        btn.setStyle(BTN_STYLE);
        btn.setOnAction(event -> tweetUpdate(textField.getText()));

        inputBox.getChildren().addAll(loginLabel, textField, btn);

        vBox.getChildren().addAll(tweetBox, inputBox);
        StackPane root = new StackPane();
        root.getChildren().addAll(vBox);
        root.setStyle(ROOT_STYLE);
        primaryStage.setTitle(APP_TITLE);
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        tweetUpdate(DEFAULT_LOGIN);
    }
}
