package gui;

import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.Socket;


public class GameOver {
    private Stage owner;
    private int widthScene=200;
    private int heightScene=200;
    private int widthStage=400;
    private int heightStage=200;
    private Scene scene;
    private VBox root;
    private int topMarg = 15;
    private int rightMarg = 12;
    private int bottomMarg = 15;
    private int leftMarg = 12;
    private int rootSpacing = 25;
    String hostName = "localhost";
    int portNumber = 12345;
    Socket socket = null;
    public BufferedReader in;
    public PrintWriter out;
    public String line= null;

    public GameOver(Socket socket) throws IOException {
        new JFXPanel();
        owner = new Stage(StageStyle.DECORATED);
        root = new VBox();
        scene = new Scene(root, widthScene, heightScene);
        scene.getStylesheets().add
                (Game.class.getResource("stylesheets/default.css").toExternalForm());
        scene.getStylesheets().add
                (GameOver.class.getResource("stylesheets/gameOver.css").toExternalForm());
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
        owner.setWidth(widthStage);
        owner.setHeight(heightStage);
        owner.initModality(Modality.WINDOW_MODAL);
        owner.toBack();
        owner.show();
    }

    public void setHBoxProperty() {
        root.setPadding(new Insets(topMarg, rightMarg, bottomMarg, leftMarg));
        root.setSpacing(rootSpacing);
        root.setAlignment(Pos.CENTER);
    }

    public void showGameOver(){
        Label lost = new Label("GAME OVER");
        Label won = new Label("YOU WON!");

        HBox hbox = setHBox();

        root.getChildren().addAll(lost, hbox);
    }

    public HBox setHBox(){
        HBox hbox = new HBox();

        Button playAgain = new Button("Play Again");
        playAgain.setId("playAgain");
        playAgain.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                owner.close();
            }
        });

        Button backToRooms = new Button("Change room");
        backToRooms.setId("back");
        backToRooms.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                RoomsView pokoje = null;
                out.println("Give me rooms");
                try {
                    line = in.readLine();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    pokoje = new RoomsView(line, socket);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    pokoje.showRoomsView();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                owner.close();
            }
        });

        hbox.setAlignment(Pos.BASELINE_CENTER);
        hbox.getChildren().addAll(playAgain, backToRooms);
        return hbox;
    }
}
