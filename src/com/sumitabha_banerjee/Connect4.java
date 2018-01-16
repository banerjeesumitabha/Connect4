/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sumitabha_banerjee;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author SUMITABHA
 */
public class Connect4 extends Application {
    
    private gameController controller;
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
        GridPane root = loader.load();
        Pane menuPane = (Pane) root.getChildren().get(0);
        MenuBar menuBar = createMenu();
        menuBar.prefWidthProperty().bind(stage.widthProperty());
        menuBar.prefHeightProperty().bind(menuPane.heightProperty());
        menuPane.getChildren().add(menuBar);
        controller = loader.getController();
        controller.createPlayground();
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Connect 4");
        stage.show();
    }
    
    private MenuBar createMenu()
    {
        Menu fileMenu = new Menu("File");
        MenuItem newGame = new MenuItem("New Game");
        MenuItem resetGame = new MenuItem("Reset Game");
        MenuItem exitGame = new MenuItem("Exit Game");
        newGame.setOnAction((ActionEvent event) -> {
            resetGame(); //To change body of generated methods, choose Tools | Templates.
        });
        resetGame.setOnAction((ActionEvent event) -> {
            resetGame(); //To change body of generated methods, choose Tools | Templates.
        });
        exitGame.setOnAction((ActionEvent event) -> {
            Platform.exit();
            System.exit(0);//To change body of generated methods, choose Tools | Templates.
        });
        fileMenu.getItems().addAll(newGame,resetGame,new SeparatorMenuItem(),exitGame);
        
        Menu helpMenu = new Menu("Help");
        MenuItem aboutGame = new MenuItem("About Connect4");
        MenuItem aboutMe = new MenuItem("About Me");
        aboutGame.setOnAction((event) -> {aboutGame();
        });
        aboutMe.setOnAction((ActionEvent event) -> {
            aboutMe(); //To change body of generated methods, choose Tools | Templates.
        });
        helpMenu.getItems().addAll(aboutGame,new SeparatorMenuItem(),aboutMe);
        
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu,helpMenu);
        return menuBar;
    }
    
    private void resetGame() {
                controller.resetGame();//To change body of generated methods, choose Tools | Templates.
            }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void aboutGame() {
        Alert alertDialog = new Alert(Alert.AlertType.INFORMATION);
        alertDialog.setTitle("About Connect4");
        alertDialog.setHeaderText("How To Play?");
        alertDialog.setContentText("Connect4 is a two-player connection game in which the players "
                + "first choose a color and then take turns dropping colored discs from the top into a "
                + "seven-column, six-row vertically suspended grid. The pieces fall straight down, "
                + "occupying the next available space within the column. The objective of the game is to "
                + "be the first to form a horizontal, vertical, or diagonal line of four of one's own discs."
                + " Connect4 is a solved game. The first player can always win by playing the right moves.");
        alertDialog.show(); //To change body of generated methods, choose Tools | Templates.
    }

    private void aboutMe() {
        Alert alertDialog = new Alert(Alert.AlertType.INFORMATION);
        alertDialog.setTitle("About The Developer");
        alertDialog.setHeaderText("Sumitabha Banerjee");
        alertDialog.setContentText("I love creating new applications for desktop, mobile and web."
                + "Connect4 is my first JavaFX game. Visit my webpage at www.sumitabha-banerjee.dx.am");
        alertDialog.show(); //To change body of generated methods, choose Tools | Templates.
    }
    
}
