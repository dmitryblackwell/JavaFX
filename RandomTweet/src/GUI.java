import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class GUI extends Application {

    public static void main(String[] args) { launch(args); }

    private Circle circle = new Circle(100,100,50);
    private Label tweetText = new Label();

    private void dataUpdate(String imgRoot, String name, String tweet){
        Image img = new Image(imgRoot);
        circle.setFill(new ImagePattern(img));
        tweetText.setText(name + "\n" + tweet);
    }

    private void tweetUpdate(String login){
        dataUpdate("https://pbs.twimg.com/profile_images/661330385465245696/3rnsJokZ_400x400.jpg", "Alex Hirsch",
                "Some say that having my signature in your home or barn will ward off witches/ keep your livestock from giving poisoned milk, don’t you know.");

    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.DECORATED);

        VBox vBox = new VBox();
        vBox.setSpacing(15);
        vBox.setAlignment(Pos.CENTER);

        HBox tweetBox = new HBox();
        tweetBox.setAlignment(Pos.CENTER);
        tweetText.setWrapText(true);
        tweetBox.getChildren().addAll(circle, tweetText);

        HBox inputBox = new HBox();
        inputBox.setAlignment(Pos.CENTER);

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
        Scene scene = new Scene(root, 400, 700);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        tweetUpdate("_AlexHirsch");
    }
}
