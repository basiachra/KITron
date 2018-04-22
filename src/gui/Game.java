package gui;

import game.DrawPixels;
import game.Map;
import game.MapReceiver;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import javafx.scene.canvas.Canvas;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Game {
    private Stage owner;
    private int widthScene=650;
    private int heightScene=850;
    private int widthStage=650;
    private int heightStage=850;
    private String title = "KI TRON";
    private Scene scene;
    private VBox root;
    private int topMarg = 15;
    private int rightMarg = 12;
    private int bottomMarg = 15;
    private int leftMarg = 12;
    private int rootSpacing = 10;
    String hostName = "localhost";
    int portNumber = 12345;
    Socket socket = null;
    public BufferedReader in;
    public PrintWriter out;

    public Game(Socket socket) throws IOException {
        new JFXPanel();
        owner = new Stage(StageStyle.DECORATED);
        root = new VBox();
        scene = new Scene(root, widthScene, heightScene);
        scene.getStylesheets().add
                (Game.class.getResource("stylesheets/default.css").toExternalForm());
        scene.getStylesheets().add
                (Game.class.getResource("stylesheets/gameView.css").toExternalForm());
        setStageProperty();
        setHBoxProperty();

        // create socket
        this.socket = socket;

        // in & out streams
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void setStageProperty(){
        owner.setScene(scene);
        owner.setTitle(title);
        owner.setWidth(widthStage);
        owner.setHeight(heightStage);
        owner.toBack();
        owner.show();
    }

    public void setHBoxProperty() {
        root.setPadding(new Insets(topMarg, rightMarg, bottomMarg, leftMarg));
        root.setSpacing(rootSpacing);
    }

    public void showActualGame(){


        Canvas canvas = getCanvas();

        HBox hbox = setButtonHbox();
        hbox.setAlignment(Pos.BOTTOM_CENTER);
        root.getChildren().addAll(canvas, hbox);

    }

    public HBox setButtonHbox(){

        HBox hbox = new HBox();

        Button endGame = new Button("End Game");
        endGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                GameOver gameOver = null;
                try {
                    gameOver = new GameOver(socket);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                gameOver.showGameOver(getOwner());
            }
        });

        Button returnButton = new Button("Return");
        returnButton.setId("back");
        returnButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                Menu menu = new Menu();
                menu.showMenu();
                owner.close();
            }
        });

        hbox.setAlignment(Pos.BOTTOM_RIGHT);
        hbox.getChildren().addAll(returnButton, endGame);
        return hbox;
    }

    public Canvas getCanvas(){


        DrawPixels drawPixels = new DrawPixels();
        final Canvas canvas = drawPixels.setRoad();
        return  canvas;
    }
    public Stage getOwner() {
        return owner;
    }
}
