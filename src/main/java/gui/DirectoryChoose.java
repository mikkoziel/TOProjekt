package gui;
import java.io.File;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
/**
 *
 * @web http://java-buddy.blogspot.com/
 */
public class DirectoryChoose {
    public static String DirectoryChooser(Stage stage){
        DirectoryChooser DChooser = new DirectoryChooser();
        DChooser.setTitle("Open Resource File");
        File selectedFile = DChooser.showDialog(stage);
        if (selectedFile != null) {
            return selectedFile.getAbsolutePath();
        }
        return "tmp";
    }



}

