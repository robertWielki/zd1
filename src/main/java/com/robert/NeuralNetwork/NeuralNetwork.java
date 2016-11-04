package com.robert.NeuralNetwork;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by RLUKAS on 15.10.2016.
 */
public class NeuralNetwork extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("MainWindow.fxml"));
        Scene scene = new Scene(root);
        System.out.println(String.valueOf(getClass().getClassLoader().getResource("style.css")));
        scene.getStylesheets().add(String.valueOf(getClass().getClassLoader().getResource("style.css")));
        primaryStage.setTitle("Neural Network");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
