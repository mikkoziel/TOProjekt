package gui;

import javafx.embed.swing.JFXPanel;
import javafx.scene.*;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.stage.*;
import statistics.DirectoryRecord;
import statistics.Record;

import java.io.*;

public class CapacityAnalysis{
    public static Stage owner;
    static TreeView<Record> tree;

    public static void ShowCapacityAnalysis(DirectoryRecord rootRecord) throws IOException {

        new JFXPanel();
        owner = new Stage(StageStyle.DECORATED);
        HBox root = new HBox();
        root.setStyle("-fx-background-color: TRANSPARENT");
        Scene scene = new Scene(root, 1500, 900);
        owner.setScene(scene);
        owner.setTitle("Analiza pojemności");
        owner.setWidth(1100);
        owner.setHeight(900);
        owner.toBack();
        owner.show();

        root.setPadding(new Insets(15, 12, 15, 12));
        root.setSpacing(10);
        root.setStyle("-fx-background-color: #336699;");


        tree = FileTree.addFileTree(rootRecord);
        collapseTreeView(tree.getRoot(), tree.getRoot());

        VBox properties = Properties.addProperties(rootRecord, 0);
        tree.setPrefWidth(700);
        properties.setMaxWidth(400);
        root.getChildren().addAll(tree, properties);

    }

    public static void collapseTreeView(TreeItem<?> item, TreeItem<?> root){
        if(item != null && !item.isLeaf()){
            if(!item.equals(root)) {
                item.setExpanded(false);
            }
            for(TreeItem<?> child: item.getChildren()){
                collapseTreeView(child, root);
            }
        }
    }



}
