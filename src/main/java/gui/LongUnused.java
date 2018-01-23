package gui;

import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import statistics.DirectoryRecord;
import javafx.scene.text.Text;
import statistics.Record;

import java.io.*;


public class LongUnused {
    static Text[] texts;
    static int m = 0;
    public static Stage owner;
    public static TableView<Record> table;

    public static void ShowLongUnused(DirectoryRecord rootRecord)  throws IOException {
        new JFXPanel();

        owner = new Stage(StageStyle.DECORATED);
        HBox root = new HBox();
        root.setStyle("-fx-background-color: TRANSPARENT");
        Scene scene = new Scene(root, 350, 350);
        owner.setScene(scene);
        owner.setTitle("Dawno Nieużywane");
        owner.setWidth(1500);
        owner.setHeight(900);
        owner.toBack();
        owner.show();

        table = new TableView<Record>();


        root.setPadding(new Insets(15, 12, 15, 12));
        root.setSpacing(10);
        root.setStyle("-fx-background-color: #336699;");


        table = TableUnused.addTableView(rootRecord);
        VBox properties = Properties.addProperties(rootRecord, 1);
        properties.setMaxWidth(400);

        root.getChildren().addAll(table, properties);

    }

}


