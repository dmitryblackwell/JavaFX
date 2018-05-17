package com.blackwell.client;

import com.blackwell.network.TCPConnection;
import com.blackwell.network.TCPConnectionListener;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ClientWindow extends Application implements TCPConnectionListener {
    private static final int WIDTH = 266;
    private static final int HEIGHT = 400;

    public static void main(String[] args) {
        /*  Getting your IP.
        InetAddress ip;
        try {

            ip = InetAddress.getLocalHost();
            System.out.println("Current IP address : " + ip.getHostAddress());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        */

        launch(args);
    }

    private final Text log = new Text();
    private final TextField fieldInput = new TextField();

    private TCPConnection connection;
    private  String ServerIpAddress = "192.168.31.142"; // ip address of the server
    private String name = "anonymous";

    @Override
    public void start(Stage primaryStage) throws Exception {

        displayInfoWindow(primaryStage);

    }

    private void displayInfoWindow(Stage primaryStage){
        primaryStage.initStyle(StageStyle.UTILITY);
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10d);

        Label nameLabel = new Label();
        Label serverIpLabel = new Label();

        TextField nameField = new TextField(name);
        TextField serverIPField = new TextField(ServerIpAddress);

        Button btn = new Button("Connect");
        btn.setOnAction(event -> {
            name = nameField.getText().trim();
            ServerIpAddress = serverIPField.getText().trim();

            displayMessengerWindow(primaryStage);
        });

        HBox nameBox = new HBox(nameLabel, nameField);
        nameBox.setAlignment(Pos.CENTER);
        HBox serverBox = new HBox(serverIpLabel, serverIPField);
        serverBox.setAlignment(Pos.CENTER);
        HBox btnBox = new HBox(btn);
        btnBox.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(nameBox,serverBox,btnBox);

        StackPane root = new StackPane();
        root.getChildren().add(vBox);

        primaryStage.setTitle("Sign in");
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void displayMessengerWindow(Stage stage){
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.BOTTOM_CENTER);

        fieldInput.setOnAction(event -> {
            String msg = fieldInput.getText();
            if ("".equals(msg)) return;

            fieldInput.setText(null);
            connection.sendString(name +": "+ msg);
        });

        fieldInput.setMaxWidth(WIDTH);
        vBox.getChildren().addAll(log,fieldInput);

        StackPane root = new StackPane();
        root.getChildren().add(vBox);

        stage.setTitle("messenger");
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.show();

        try {
            connection = new TCPConnection(this, ServerIpAddress, PORT);
            connection.sendString(name +" connected");
        } catch (IOException e) {
            printMessage("Connection exception: " + e);
        }
    }


    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMessage("Connection ready...");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        printMessage(value);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMessage("Connection close.");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception ex) {
        printMessage("Connection exception: " + ex);
    }

    private synchronized void printMessage(String msg){
        log.setText(log.getText() + msg + EOL);
//        log.setCaretPosition(log.getDocument().getLength());
    }

}
