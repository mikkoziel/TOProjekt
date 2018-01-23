package gui;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import statistics.DirectoryRecord;
import java.io.*;
import statistics.Record;

import static statistics.Convert.convertToStringRepresentation;


public class FileTree {
    public static TreeView<Record> tree;

    public static TreeView<Record> addFileTree(DirectoryRecord rootRecord) throws IOException {

        TreeItem<Record> rootItem;
        rootItem = addTreeItem(rootRecord);

        tree = new TreeView<Record> (rootItem);
        tree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue != oldValue) {
                Record selItem = tree.getSelectionModel().selectedItemProperty().getValue().getValue();
                Properties.name.setText("Name: " + selItem.getName());
                Properties.path.setText("Path: " + selItem.getPath());
                Properties.size.setText("Size: " + convertToStringRepresentation(selItem.getSize()));
                Properties.lastMod.setText("Last Modification: " + selItem.getLastModification());
                Properties.percentSize.setText(selItem.getPercent() + "%");
                Properties.chosen.setName(selItem.getName());
                Properties.chosen.setPieValue(selItem.getPercent());
                Properties.rest.setName("Rest of Capacity");
                Properties.rest.setPieValue(100 - selItem.getPercent());
            }
        });
        return tree;
    }

    public static TreeItem<Record> addTreeItem(DirectoryRecord record){
        TreeItem<Record> dirItem = new TreeItem<Record>(record);
        dirItem.setExpanded(true);
        for(Record x: record.getChildrenList()){
            TreeItem<Record> item;
            if(x.getClass().equals(record.getClass())) {
                item = addTreeItem((DirectoryRecord)x);
            }
            else {
                item = new TreeItem<Record>(x);
            }
            dirItem.getChildren().add(item);
        }
        return dirItem;
    }
}
