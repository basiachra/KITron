package gui;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Waiting {
    private Stage dialogStage;
    private final ProgressIndicator pin = new ProgressIndicator();

    public void Waiting() {
        dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.DECORATED);
        dialogStage.setMinWidth(570);
        dialogStage.setMaxWidth(570);
        dialogStage.setResizable(false);
        dialogStage.setAlwaysOnTop(true);
        dialogStage.setTitle("Loading");
        dialogStage.initModality(Modality.WINDOW_MODAL);

        // PROGRESS BAR
        final Label label = new Label();
        label.setText("Waiting  for  other  players");
        label.setId("tytul");
        label.setTextFill(Color.WHITE);
        pin.setProgress(-1F);

        final VBox hb = new VBox();
        hb.setSpacing(5);
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().addAll(label, pin);

        Scene scene = new Scene(hb, 530, 230);
        scene.getStylesheets().add
                (Waiting.class.getResource("stylesheets/default.css").toExternalForm());
        scene.getStylesheets().add
                (Waiting.class.getResource("stylesheets/waiting.css").toExternalForm());
        dialogStage.setScene(scene);


    }

    public void activateProgressBar(final Task<?> task)  {
        pin.progressProperty().bind(task.progressProperty());
        dialogStage.show();
    }

    public Stage getDialogStage() {
        return dialogStage;
    }
}