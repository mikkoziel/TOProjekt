package gui;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.io.IOException;
import java.lang.String;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.chart.PieChart;
import javafx.collections.*;
import statistics.DirectoryRecord;
import statistics.FileRecord;
import statistics.Record;

import static java.util.Locale.ENGLISH;
import static statistics.Convert.convertToStringRepresentation;

public class Properties {
    public static Text name, path, size, lastMod, percentSize, timeFrom;
    public static PieChart.Data rest, chosen;

    public static VBox addProperties(DirectoryRecord rootItem, int chk){

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(5));
        vbox.setSpacing(8);
        vbox.setStyle("-fx-background-color: #336699;");

        VBox vbox1 = new VBox();
        vbox1.setPadding(new Insets(5));
        vbox1.setSpacing(8);
        vbox1.setStyle("-fx-background-color: #336699;");

        VBox vbox2 = new VBox();
        vbox2.setPadding(new Insets(5));
        vbox2.setSpacing(8);
        vbox2.setStyle("-fx-background-color: #336699;");


        name = new Text();
        name.setText("Name: " + rootItem.getName());
        name.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        path= new Text();
        path.setText("Path: " + rootItem.getPath());
        path.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        size = new Text();
        size.setText("Size: " + convertToStringRepresentation(rootItem.getSize()));
        size.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        lastMod = new Text();
        lastMod.setText("Last Modification: " + rootItem.getLastModification());
        lastMod.setFont(Font.font("Arial", FontWeight.BOLD, 14));


        Button delete = new Button("Delete");
        delete.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                String pathToRemove = path.getText().substring(("Path: ").length());
                removeFiles((DirectoryRecord) rootItem.getRecord(pathToRemove));


                switch (chk){
                    case 0: TreeItem<?> removeTI;
                            removeTI = CapacityAnalysis.tree.getSelectionModel().getSelectedItem();
                            removeTI.getParent().getChildren().remove(removeTI);
                            break;

                    case 1: ObservableList<Record> tableRows = LongUnused.table.getItems();
                            List<Record> toRemove = new ArrayList<Record>();
                            for(Record x: tableRows){
                                if(x.getPath().startsWith(pathToRemove))
                                    toRemove.add(x);
                            }
                            for(Record ax: toRemove){
                                LongUnused.table.getItems().remove(ax);
                            }


                            break;
                }
            }
        });

        Button open = new Button("Open");
        open.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                File openFile = new File(path.getText().substring(("Path: ").length()));
                File oFile;
                if(!openFile.isDirectory()){
                    oFile = new File(openFile.getAbsolutePath().substring(0, openFile.getAbsolutePath().lastIndexOf("\\")));
                }
                else oFile = openFile;
                try {
                    Desktop.getDesktop().open(oFile);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        delete.setMinWidth(400);
        open.setMinWidth(400);
        vbox2.setAlignment(Pos.CENTER);

        name.setWrappingWidth(400);
        path.setWrappingWidth(400);

        switch(chk){
            case 0: vbox2 = addPercent(vbox2, rootItem);
                    break;
            case 1: vbox2 = addText(vbox2, lastMod);
                    break;
        }

        vbox1.setPrefHeight(300);
        vbox1.getChildren().addAll(name, path, size, lastMod, open, delete);
        vbox.getChildren().addAll(vbox1, vbox2);

        return vbox;

    }

    public static VBox addPercent(VBox vbox, DirectoryRecord rootItem){
        Text textPerc = new Text("Size percentage of source: " );
        textPerc.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        double percent = rootItem.getPercent();

        percentSize = new Text(percent + "%");
        percentSize.setFont(Font.font("Arial", FontWeight.BOLD, 100));

        rest = new PieChart.Data("Rest of Capacity", 100- percent);
        chosen = new PieChart.Data(rootItem.getName(), percent);

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        chosen, rest);
        PieChart chart = new PieChart(pieChartData);
        chart.setLabelsVisible(false);
        chart.setMinHeight(400);
        vbox.getChildren().addAll(textPerc, percentSize, chart);
        return vbox;

    }

    public static VBox addText(VBox vbox, Text lastMod){
        Text textHow = new Text("Since the last use has passed" );
        textHow.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        String date = lastMod.getText().substring("Last Modification: ".length());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", ENGLISH);
        LocalDate date1 = LocalDate.parse(date, formatter);
        LocalDate date2 = LocalDate.now();
        Period days = getPeriod(date1, date2);

        timeFrom = new Text( days.getYears() +" years\n" +
                days.getMonths() + " months\n" + days.getDays() + " days\n");
        timeFrom.setFont(Font.font("Arial", FontWeight.BOLD, 100));

        vbox.getChildren().addAll(textHow, timeFrom);
        return vbox;
    }

    public static Period getPeriod(LocalDate date1, LocalDate date2) {
        return Period.between(date1, date2);
    }

    public static void removeFiles(DirectoryRecord item){
        for(Record x: item.getChildrenList()){
            if(x.getClass().equals(item.getClass())) {
                removeFiles((DirectoryRecord) x);
            }else{
                File removeFile = new File(x.getPath());
                removeFile.delete();
            }
        }
        File removeFile = new File(item.getPath());
        removeFile.delete();
    }

}

