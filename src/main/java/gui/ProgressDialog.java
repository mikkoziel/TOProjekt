package gui;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ProgressDialog {
    private Stage dialogStage;
    private final ProgressIndicator pin = new ProgressIndicator();

    public void ProgressForm() {
        dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.TRANSPARENT);
        dialogStage.setMinWidth(300);
        dialogStage.setMaxWidth(300);
        dialogStage.setResizable(false);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setAlwaysOnTop(true);
        dialogStage.setTitle("Wczytywanie");

        // PROGRESS BAR
        final Text label = new Text();
        label.setText("Proszę czekać");
        final Text text = new Text("Trwa zbieranie danych\n");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        text.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        pin.setProgress(-1F);

        final VBox hb = new VBox();
        hb.setSpacing(5);
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().addAll(label, text, pin);

        Scene scene = new Scene(hb, 230, 230);
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
