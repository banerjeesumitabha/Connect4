/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sumitabha_banerjee;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

/**
 *
 * @author SUMITABHA
 */
public class gameController implements Initializable {

    private static final int COLUMNS = 7;
    private static final int ROWS = 6;
    private static final int CIRCLE_DIAMETER = 80;
    private static final String DISC_COLOR1 = "#24303E";
    private static final String DISC_COLOR2 = "#4CAA88";

    private static String PLAYER_ONE = "Player One";
    private static String PLAYER_TWO = "Player Two";

    private boolean isPlayerOneTurn = true;
    private Disc[][] insertedDiscsArray = new Disc[ROWS][COLUMNS];

    @FXML
    private GridPane rootGridPane;
    @FXML
    private Pane insertedDiscsPane;
    @FXML
    private Label playerNameLabel;
    @FXML
    private Button setNamesBtn;
    @FXML
    private TextField playerOneName;
    @FXML
    private TextField playerTwoName;

    private boolean isAllowedToInsert = true;
    
    

    public void createPlayground() {
        Shape rectangleWithHoles = createGameStructuralGrid();
        rootGridPane.add(rectangleWithHoles, 0, 1);
        List<Rectangle> rectangleList = createClickableColumns();
        for (Rectangle rectangle : rectangleList) {
            rootGridPane.add(rectangle, 0, 1);
        }
        setNamesBtn.setOnAction((ActionEvent event) -> {
            if (!playerOneName.getText().isEmpty()) {
                PLAYER_ONE = playerOneName.getText();
            }
            if (!playerTwoName.getText().isEmpty()) {
                PLAYER_TWO = playerTwoName.getText();
            }
            if (isPlayerOneTurn) {
                playerNameLabel.setText(PLAYER_ONE);
            }
            else{
                playerNameLabel.setText(PLAYER_TWO);
            }            
//To change body of generated methods, choose Tools | Templates.
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    private Shape createGameStructuralGrid() {
        Shape rectangleWithHoles = new Rectangle((COLUMNS + 1) * CIRCLE_DIAMETER, (ROWS + 1) * CIRCLE_DIAMETER);

        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                Circle circle = new Circle(CIRCLE_DIAMETER / 2, CIRCLE_DIAMETER / 2, CIRCLE_DIAMETER / 2);
                circle.setTranslateX(column * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);
                circle.setTranslateY(row * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);
                circle.setSmooth(true);
                rectangleWithHoles = Shape.subtract(rectangleWithHoles, circle);
            }

        }
        rectangleWithHoles.setFill(Color.WHITE);
        return rectangleWithHoles;//To change body of generated methods, choose Tools | Templates.
    }

    private List<Rectangle> createClickableColumns() {
        List<Rectangle> rectangleList = new ArrayList<>();
        for (int column = 0; column < COLUMNS; column++) {
            Rectangle rectangle = new Rectangle(CIRCLE_DIAMETER, (ROWS + 1) * CIRCLE_DIAMETER);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setTranslateX(column * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);
            rectangle.setOnMouseEntered((MouseEvent event) -> {
                rectangle.setFill(Color.valueOf("#eeeeee26")); //To change body of generated methods, choose Tools | Templates.
            });
            rectangle.setOnMouseExited((MouseEvent event) -> {
                rectangle.setFill(Color.TRANSPARENT); //To change body of generated methods, choose Tools | Templates.
            });
            final int col = column;
            rectangle.setOnMouseClicked(((event) -> {
                if (isAllowedToInsert) {
                    isAllowedToInsert = false;
                    insertDisc(new Disc(isPlayerOneTurn), col);
                }
            }));
            rectangleList.add(rectangle);
        }

        return rectangleList;
    }

    private void insertDisc(Disc disc, int column) {

        int row = ROWS - 1;
        while (row >= 0) {
            if (getDiscIfPresent(row, column) == null) {
                break;
            } else {
                row--;
            }
        }
        if (row < 0) {
            return;
        }
        insertedDiscsArray[row][column] = disc;
        insertedDiscsPane.getChildren().add(disc);
        disc.setTranslateX(column * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);
        TranslateTransition fallEffect = new TranslateTransition(Duration.seconds(0.5), disc);
        fallEffect.setToY(row * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);
        int currentRow = row;
        fallEffect.setOnFinished((ActionEvent event) -> {
            isAllowedToInsert = true;
            if (gameEnded(currentRow, column)) {
                gameOver();
                return;
            }

            isPlayerOneTurn = !isPlayerOneTurn;
            playerNameLabel.setText(isPlayerOneTurn ? PLAYER_ONE : PLAYER_TWO);
//To change body of generated methods, choose Tools | Templates.
        });
        fallEffect.play();
//To change body of generated methods, choose Tools | Templates.
    }

    private boolean gameEnded(int row, int column) {

        List<Point2D> verticalPoints = IntStream.rangeClosed(row - 3, row + 3)
                .mapToObj(r -> new Point2D(r, column))
                .collect(Collectors.toList());

        List<Point2D> horizontalPoints = IntStream.rangeClosed(column - 3, column + 3)
                .mapToObj(c -> new Point2D(row, c))
                .collect(Collectors.toList());

        Point2D startPoint1 = new Point2D(row - 3, column + 3);
        List<Point2D> diagonal1Points = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> startPoint1.add(i, -i))
                .collect(Collectors.toList());

        Point2D startPoint2 = new Point2D(row - 3, column - 3);
        List<Point2D> diagonal2Points = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> startPoint2.add(i, i))
                .collect(Collectors.toList());

        boolean isEnded = checkCombinations(verticalPoints) || checkCombinations(horizontalPoints)
                || checkCombinations(diagonal1Points) || checkCombinations(diagonal2Points);

        return isEnded;
    }

//To change body of generated methods, choose Tools | Templates.
    private void gameOver() {
        String winner = isPlayerOneTurn ? PLAYER_ONE : PLAYER_TWO;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Winner is " + winner);
        alert.setContentText("Want to play again?");
        ButtonType yesBtn = new ButtonType("Yes");
        ButtonType noBtn = new ButtonType("No, Exit");
        alert.getButtonTypes().setAll(yesBtn, noBtn);
        Platform.runLater(() -> {
            Optional<ButtonType> btnClicked = alert.showAndWait();
            if (btnClicked.isPresent() && btnClicked.get() == yesBtn) {
                resetGame();
            } else {
                Platform.exit();
                System.exit(0);
            }  //To change body of generated methods, choose Tools | Templates.
        });

//To change body of generated methods, choose Tools | Templates.
    }

    private boolean checkCombinations(List<Point2D> points) {
        int chain = 0;
        for (Point2D point : points) {

            int rowIndex = (int) point.getX();
            int colIndex = (int) point.getY();
            Disc disc = getDiscIfPresent(rowIndex, colIndex);
            if (disc != null && disc.isPlayerOneMove == isPlayerOneTurn) {
                chain++;
                if (chain == 4) {
                    return true;
                }
            } else {
                chain = 0;
            }

        }
        return false;
        //To change body of generated methods, choose Tools | Templates.
    }

    private Disc getDiscIfPresent(int row, int column) {
        if (row >= ROWS || row < 0 || column >= COLUMNS || column < 0) {
            return null;
        } else {
            return insertedDiscsArray[row][column];
        }
    }

    public void resetGame() {
        insertedDiscsPane.getChildren().clear();
        for (int row = 0; row < insertedDiscsArray.length; row++) {
            for (int column = 0; column < insertedDiscsArray[row].length; column++) {
                insertedDiscsArray[row][column] = null;
            }
        }
        isPlayerOneTurn = true;
        playerNameLabel.setText(PLAYER_ONE);
        createPlayground();
//To change body of generated methods, choose Tools | Templates.
    }

    private static class Disc extends Circle {

        private final boolean isPlayerOneMove;

        public Disc(boolean isPlayerOneMove) {
            this.isPlayerOneMove = isPlayerOneMove;
            setRadius(CIRCLE_DIAMETER / 2);
            setCenterX(CIRCLE_DIAMETER / 2);
            setCenterY(CIRCLE_DIAMETER / 2);
            setFill(isPlayerOneMove ? Color.valueOf(DISC_COLOR1) : Color.valueOf(DISC_COLOR2));
        }

    }

}
