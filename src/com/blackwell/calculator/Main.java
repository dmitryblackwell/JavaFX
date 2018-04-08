package com.blackwell.calculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Main extends Application {

    private Label label;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Calculator");
        Scene scene = new Scene(root, 235, 300);
        primaryStage.setScene(scene);

        label = (Label) scene.lookup("#label");
        String[][] btnCtrlNames = { {"#btnAC", "AC"},
                {"#btnPlusMinus","+/-"},
                {"#btnDivide","/"},
                {"#btnPercent","%"},
                {"#btnMultiply","x"},
                {"#btnPlus","+"},
                {"#btnMinus","-"},
                {"#btnPoint","."},
                {"#btnEqual","="}};

        Button[] btnCtrl = new Button[btnCtrlNames.length];
        for(int i=0; i<btnCtrl.length; ++i){
            final int carriage = i;
            btnCtrl[i] = (Button) scene.lookup(btnCtrlNames[i][0]);
            btnCtrl[i].setOnAction(event -> add(btnCtrlNames[carriage][1]));
        }

        Button[] btnNumbers = new Button[10];
        for(int i=0;i<btnNumbers.length;++i) {
            final String CARRIAGE = String.valueOf(i);
            btnNumbers[i] = (Button) scene.lookup("#"+i);
            btnNumbers[i].setOnAction(event -> add(CARRIAGE));
        }

        primaryStage.show();
    }

    private void add(String s){

        String out = "";
        if(label.getText().equals("0"))
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
                Float getNum = Float.parseFloat(label.getText());
                double setNum = Math.pow(getNum,2d);
                out = String.valueOf(setNum);
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
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
