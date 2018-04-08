package com.blackwell.calculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {

    private Label label;


    // Do NOT fucking add any brackets here!!!
    // Otherwise everything goes to hell
    private static final String MATERIAL_BUTTON = ".button-raised{" +
            "    -fx-padding: 0em 0em;" +
            "    -fx-font-size: 14px;" +
            "    -jfx-button-type: RAISED;" +
            "    -fx-background-color: rgb(0,166,178);" +
            "    -fx-pref-width: 55;" +
            "    -fx-pref-height: 40;" +
            "    -fx-text-fill: WHITE;" +
            "    -fx-alignment: center;"  +
            "    -fx-border-insets: 10px;}";
    private static final String MATERIAL_CTRL = ".button-raised{-fx-padding: 0em 0em;" +
            "-fx-font-size: 14px;-jfx-button-type: RAISED;" +
            " -fx-background-color: rgb(0,144,178);" +
            " -fx-pref-width: 55;" +
            "-fx-pref-height: 40;" +
            "-fx-text-fill: WHITE;" +
            "-fx-alignment: center;" +
            "-fx-border-insets: 10px;}";
    private static final String MATERIAL_ZERRO = ".button-raised{" +
            "    -fx-padding: 0em 0em;" +
            "    -fx-font-size: 14px;" +
            "    -jfx-button-type: RAISED;" +
            "    -fx-background-color: rgb(0,166,178);" +
            "    -fx-pref-width: 110;" +
            "    -fx-pref-height: 40;" +
            "    -fx-text-fill: WHITE;" +
            "    -fx-alignment: center;"  +
            "    -fx-border-insets: 10px;}";
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.initStyle(StageStyle.UTILITY);
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Calculator");
        Scene scene = new Scene(root, 235, 300);
        primaryStage.setScene(scene);

        GridPane pane = (GridPane) scene.lookup("#GridPane");
        pane.setStyle("-fx-background-color: rgb(244,254,255);-fx-alignment: center;");

        label = (Label) scene.lookup("#label");
        label.setStyle("-fx-text-fill: rgb(134,140,140); -fx-background-color: rgb(244,254,255);"); //244,254,255 //195,203,204

        label.setFont(Font.font ("Verdana", 30));

        String[][] btnCtrlNames = { {"#btnAC", "AC"},
                {"#btnPlusMinus","+/-"},
                {"#btnPercent","%"},
                {"#btnPoint","."},
                {"#btnEqual","="},
                {"#btnMultiply","x"},
                {"#btnPlus","+"},
                {"#btnMinus","-"},
                {"#btnDivide","/"}};

        Button[] btnCtrl = new Button[btnCtrlNames.length];
        for(int i=0; i<btnCtrl.length; ++i){
            final int carriage = i;
            btnCtrl[i] = (Button) scene.lookup(btnCtrlNames[i][0]);
            if (i>=4)
                btnCtrl[i].setStyle(MATERIAL_CTRL);
            else
                btnCtrl[i].setStyle(MATERIAL_BUTTON);
            btnCtrl[i].setOnAction(event -> add(btnCtrlNames[carriage][1]));
        }

        Button[] btnNumbers = new Button[10];
        for(int i=0;i<btnNumbers.length;++i) {
            final String CARRIAGE = String.valueOf(i);
            btnNumbers[i] = (Button) scene.lookup("#"+i);
            btnNumbers[i].setStyle(MATERIAL_BUTTON);
            btnNumbers[i].setOnAction(event -> add(CARRIAGE));
        }
        btnNumbers[0].setStyle(MATERIAL_ZERRO);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void add(String s){
        String out = "";
        if(label.getText().equals("0") || label.getText().equals("Wrong Input"))
            label.setText("");

        switch (s){
            case "AC":
                out = "0";
                break;
            case "+/-":
                try {
                    out = String.valueOf(Integer.parseInt(label.getText()) * (-1));
                }catch (NumberFormatException e){/*fucking exception*/}
                break;
            case "+":
            case "-":
            case "x":
            case "/":
                out = label.getText() + " "+ s +" ";
                break;
            case "%":
                try {
                    Float getNum = Float.parseFloat(label.getText());
                    double setNum = Math.pow(getNum,2d);
                    if (setNum%1 == 0)
                        out = String.valueOf((int)setNum);
                    else
                        out = String.valueOf(setNum);
                }catch (NumberFormatException e){
                    System.err.println("Wrong Input");
                    out = label.getText();
                }
                break;
            case "=":
                out = getResult();
                break;
            default:
                out = label.getText()+s;
                break;
        }
        label.setText(out);
    }

    private String getResult(){
        try {
            String[] labelLine = label.getText().split(" ");
            float result = Float.parseFloat(labelLine[0]);
            for (int i = 2; i < labelLine.length; i += 2) {
                float element = Float.parseFloat(labelLine[i]);
                switch (labelLine[i - 1]) {
                    case "+":
                        result += element;
                        break;
                    case "-":
                        result -= element;
                        break;
                    case "/":
                        result = element / result;
                    case "x":
                        result *= element;
                }
            }
            if (result%1 == 0)
                return String.valueOf((int) result);
            else
                return String.valueOf(result);
        }catch (NumberFormatException e){
            //e.printStackTrace();
            System.err.println("Wrong Input or Empty String");
            return "Wrong Input";
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
