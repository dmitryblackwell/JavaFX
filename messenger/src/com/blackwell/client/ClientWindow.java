package com.blackwell.client;

import com.blackwell.network.TCPConnection;
import com.blackwell.network.TCPConnectionListener;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;

public class ClientWindow extends Application implements TCPConnectionListener {
    private static final int WIDTH = 520;
    private static final int HEIGHT = 800;
    private static final String BTN_ID = "btn_connect";
    private static final String PANE_ID = "pane";
    private static final String LOG_ID = "log";
    private static final String STYLE_FILE_PATH = "messenger/resource/style.css";
    private static final String START_NAME = "enter your name";

    public static void main(String[] args) { launch(args); }

    private final TextArea log = new TextArea();
    private final TextField fieldInput = new TextField();

    private TCPConnection connection;
    private String ServerIpAddress = "192.168.31.142"; // ip address of the server
    private String name = START_NAME;
    //private String avatar = Avatar.GAME_GIRL.get();

    private Stage stage;
    private StackPane root;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        displayInfoWindow();
    }

    private void displayInfoWindow(){
        stage.initStyle(StageStyle.DECORATED);
        // -fx-background-color: derive(#FF1d1d,20%);

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10d);

        TextField nameField = new TextField(name);
        TextField serverIPField = new TextField(ServerIpAddress);
        nameField.setAlignment(Pos.CENTER);
        serverIPField.setAlignment(Pos.CENTER);
        nameField.setMaxWidth(WIDTH/1.5);
        serverIPField.setMaxWidth(WIDTH/1.5);
        nameField.setId(LOG_ID);
        serverIPField.setId(LOG_ID);

        Button btn = new Button("Connect");
        btn.setAlignment(Pos.CENTER);
        btn.setId(BTN_ID);
        EventHandler<ActionEvent> handler = event -> {
            name = nameField.getText().trim();
            if ("".equals(name) || START_NAME.equals(name)) return;
            ServerIpAddress = serverIPField.getText().trim();

            displayMessengerWindow();
        };
        btn.setOnAction(handler);
        nameField.setOnAction(handler);
        vBox.getChildren().addAll(nameField, serverIPField, btn);

        root = new StackPane();
        root.getChildren().add(vBox);
        root.setId(PANE_ID);

        stage.setTitle("Sign in");
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        File f = new File(STYLE_FILE_PATH);
        scene.getStylesheets().clear();
        scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));

        stage.setScene(scene);
        stage.show();
    }

    private void displayMessengerWindow(){
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.BOTTOM_CENTER);

        fieldInput.setId("field");
        fieldInput.setOnAction(event -> {
            String msg = fieldInput.getText();
            if ("".equals(msg)) return;


            fieldInput.setText(null);
            connection.sendString(name +": "+ msg);
        });

        log.setEditable(false);
        log.setPrefHeight(HEIGHT);
        //log.setId("log");
        //log.setStyle("-fx-text-inner-color: white; -fx-background-color: green;");
        log.setId(LOG_ID);
        log.setFocusTraversable(false);
        vBox.getChildren().addAll(log,fieldInput);

        root.getChildren().clear();
        root.getChildren().add(vBox);

        stage.setTitle("messenger");

        try {
            connection = new TCPConnection(this, ServerIpAddress, PORT);
            connection.sendString(">>> " + name +" connected");
        } catch (IOException e) {
            printMessage(">>> Connection exception: " + e);
        }
    }


    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMessage(">>> Connection ready...");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        printMessage(value);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMessage(">>> Connection close.");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception ex) {
        printMessage(">>> Connection exception: " + ex);
    }

    private synchronized void printMessage(String msg){
        log.setText(log.getText() + msg + EOL);
    }

}
// Allele - Closer To Habit
// Everlife - reflection