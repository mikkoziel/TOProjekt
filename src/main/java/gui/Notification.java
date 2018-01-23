package gui;

import javafx.animation.PauseTransition;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;


public class Notification {
    public static Stage  owner;

    public static void displayNote(String result, String resultKey) {
        new JFXPanel();

        owner = new Stage(StageStyle.TRANSPARENT);
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: TRANSPARENT");
        Scene scene = new Scene(root, 1, 1);
        scene.setFill(Color.TRANSPARENT);
        owner.setScene(scene);
        owner.setWidth(1);
        owner.setHeight(100);
        owner.toBack();
        owner.show();

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        owner.setX(primaryScreenBounds.getMinX());
        owner.setY(primaryScreenBounds.getMinY());
        owner.setWidth(primaryScreenBounds.getWidth());
        owner.setHeight(primaryScreenBounds.getHeight());

        Notifications tray = null;

        switch(resultKey){
            case "ENTRY_DELETE": tray.create()
                                .title("Usunięto:")
                                .text(result)
                                .hideAfter(Duration.seconds(5))
                                .owner(owner)
                                .position(Pos.BOTTOM_RIGHT)
                                .showConfirm();
                                break;
            case "ENTRY_CREATE": tray.create()
                                .title("Stworzono:")
                                .text(result)
                                .hideAfter(Duration.seconds(5))
                                .owner(owner)
                                .position(Pos.BOTTOM_RIGHT)
                                .showConfirm();
                                break;
            case "ENTRY_MODIFY": tray.create()
                                .title("Zmodyfikowano:")
                                .text(result)
                                .hideAfter(Duration.seconds(5))
                                .owner(owner)
                                .position(Pos.BOTTOM_RIGHT)
                                .showConfirm();
                break;

        }

        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished( event -> owner.close() );
        delay.play();

    }

}