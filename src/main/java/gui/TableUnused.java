package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import statistics.DirectoryRecord;
import statistics.Record;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

import static java.util.Locale.ENGLISH;
import static statistics.Convert.convertToStringRepresentation;

public class TableUnused {


    public static TableView<Record> addTableView(DirectoryRecord rootRecord){

        Comparator<Record> comparator= new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                String dateA = o1.getLastModification();
                String dateB = o2.getLastModification();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", ENGLISH);
                LocalDate date1 = LocalDate.parse(dateA, formatter);
                LocalDate date2 = LocalDate.parse(dateB, formatter);
                if (date1 == null || date2 == null)
                    return 0;
                return date1.compareTo(date2);
            }
        };

        TableView<Record> table = new TableView<Record>();
        ObservableList<Record> tItems = FXCollections.observableArrayList();;

        tItems = createlist(rootRecord, tItems);
        FXCollections.sort(tItems, comparator);

        TableColumn<Record, String> pathCol = new TableColumn<Record, String>("Path");
        pathCol.setPrefWidth(700);

        TableColumn<Record, String> lastCol = new TableColumn<Record, String>("Last Modified");
        lastCol.setPrefWidth(200);

        pathCol.setCellValueFactory(new PropertyValueFactory<Record, String>("path"));
        lastCol.setCellValueFactory(new PropertyValueFactory<Record, String>("lastModification"));

        table.getColumns().addAll(pathCol, lastCol);
        table.setItems(tItems);

        table.setRowFactory(tv -> {
            TableRow<Record> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && (event.getButton() == MouseButton.PRIMARY)) {
                    Record clickedRow = row.getItem();
                    printRow(clickedRow);
                }
            });
            return row ;
        });

        return table;
    }

    public static ObservableList<Record> createlist(DirectoryRecord record, ObservableList<Record> items){
        items.addAll(record);
        for(Record x: record.getChildrenList()){
            if(x.getClass().equals(record.getClass())) {
                createlist((DirectoryRecord)x, items);
            }
            else items.addAll(x);
        }
        return items;
    }

    public static void printRow(Record o) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", ENGLISH);
        LocalDate date1 = LocalDate.parse(o.getLastModification(), formatter);
        LocalDate date2 = LocalDate.now();
        Period days = getPeriod(date1, date2);

        Properties.name.setText("Name: " + o.getName());
        Properties.path.setText("Path: " + o.getPath());
        Properties.size.setText("Size: " + convertToStringRepresentation(o.getSize()));
        Properties.lastMod.setText("Last Modification: " + o.getLastModification());
        Properties.timeFrom.setText(days.getYears() + " years\n" + days.getMonths() + " months\n" + days.getDays() + " days\n");
    }

    public static Period getPeriod(LocalDate date1, LocalDate date2) {
        return Period.between(date1, date2);
    }


}
