import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.api.TweetsResources;
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


    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    private void tweetUpdate(String login) {

        ConfigurationBuilder builder = new ConfigurationBuilder();
        // This keys is read only... sorry )))
        builder.setDebugEnabled(true)
                .setOAuthConsumerKey("o5Y3KTO80LuooTMrqioklMQYy")
                .setOAuthConsumerSecret("7cpWmhvu6iyPvvxJ4RHAfQdVlCzf73ASHZb6cH2lQisOw07yjU")
                .setOAuthAccessToken("4482344913-O2xj79vEictmpxIew4ldzQC07BOSOpKPCwTUqrO")
                .setOAuthAccessTokenSecret("i3XGeHnLZHbjg169m7eviAs0NtCRPB1GHKL5I78gfIudf");

        TwitterFactory factory = new TwitterFactory(builder.build());
        Twitter twitter = factory.getInstance();

        try {
            List<Status> statuses = twitter.getHomeTimeline();
            Random R = new Random();
            Status status = statuses.get(R.nextInt(statuses.size()));
            dataUpdate(status.getUser().getBiggerProfileImageURL(),
                    status.getUser().getScreenName(),
                    dateFormat.format(status.getCreatedAt()),
                    status.getText());
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.DECORATED);

        VBox vBox = new VBox();
        vBox.setSpacing(15);
        vBox.setAlignment(Pos.CENTER);

        HBox tweetBox = new HBox();
        tweetBox.setSpacing(30);
        tweetBox.setPadding(new Insets(20));
        tweetBox.setAlignment(Pos.CENTER);

        VBox tweetTextBox = new VBox();
        tweetLabel.setWrapText(true);
        tweetLabel.setFont(new Font(15));

        nameLabel.setFont(new Font(30));
        nameLabel.setStyle("-fx-font-weight: bold");
        timeLabel.setFont(new Font(10));

        tweetTextBox.getChildren().addAll(nameLabel, timeLabel,tweetLabel);
        tweetBox.getChildren().addAll(circle, tweetTextBox);

        HBox inputBox = new HBox();
        inputBox.setSpacing(10);
        inputBox.setAlignment(Pos.BOTTOM_CENTER);

        Label loginLabel = new Label("@");
        loginLabel.setFont(new Font(30));
        TextField textField = new TextField();
        Button btn = new Button("rand");
        btn.setOnAction(event -> tweetUpdate(textField.getText()));

        inputBox.getChildren().addAll(loginLabel, textField, btn);

        vBox.getChildren().addAll(tweetBox, inputBox);
        StackPane root = new StackPane();
        root.getChildren().addAll(vBox);
        primaryStage.setTitle("RandomTweet");
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        tweetUpdate("_AlexHirsch");
    }
}
