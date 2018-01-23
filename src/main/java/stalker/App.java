package stalker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import gui.CapacityAnalysis;
import gui.DirectoryChoose;
import gui.LongUnused;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import statistics.DataProcessor;
import statistics.DirectoryRecord;
import statistics.Record;

import javax.imageio.ImageIO;

/**
 * Created by Justyna Gorczyca on 17.12.2017.
 */
public class App extends Application{

    public static String path = "tmp";
    private static final String iconImageLoc =
            "http://icons.iconarchive.com/icons/scafer31000/bubble-circle-3/16/GameCenter-icon.png";

    // application stage is stored so that it can be shown and hidden based on system tray icon operations.
    private Stage stage;
    public static String pathCA = "tmp", pathLU = "tmp";
    public static Thread thread;


    public static void main(String[] args) {
        launch(App.class,(java.lang.String[])null);


    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        // instructs the javafx system not to exit implicitly when the last application window is shut.
        Platform.setImplicitExit(false);

        // sets up the tray icon (using awt code run on the swing thread).
        javax.swing.SwingUtilities.invokeLater(this::addAppToTray);

        // out stage will be translucent, so give it a transparent style.
        stage.initStyle(StageStyle.TRANSPARENT);

    }

    private void addAppToTray() {
        try {
            // ensure awt toolkit is initialized.
            java.awt.Toolkit.getDefaultToolkit();

            // app requires system tray support, just exit if there is no support.
            if (!java.awt.SystemTray.isSupported()) {
                System.out.println("No system tray support, application exiting.");
                Platform.exit();
            }

            // set up a system tray icon.
            java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
            URL imageLoc = new URL(
                    iconImageLoc
            );
            java.awt.Image image = ImageIO.read(imageLoc);
            java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image);

            // if the user double-clicks on the tray icon, show the main app stage.
            trayIcon.addActionListener(event -> Platform.runLater(this::showStage));

            java.awt.MenuItem StopItem = new java.awt.MenuItem("Stop");
            StopItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Platform.runLater(()->{
                                path="tmp";
                                thread.interrupt();
                            }
                    );
                }

            });

            java.awt.MenuItem PauseItem = new java.awt.MenuItem("Start");
            PauseItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Platform.runLater(()->{
                                if(PauseItem.getLabel().equals("Start")){
                                    PauseItem.setLabel("Pause");
                                    Path dir = Paths.get(path);
                                    DirWatcher watcher = null;
                                    try {
                                        watcher = new DirWatcher(dir);
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }

                                    DirWatcher finalWatcher = watcher;
                                    thread = new Thread(() -> {
                                        finalWatcher.processEvents();
                                    });
                                    thread.start();
                                }
                                else {
                                    PauseItem.setLabel("Start");
                                    thread.interrupt();


                                }
                            }
                    );
                }

            });

            java.awt.MenuItem openItem = new java.awt.MenuItem("Lokalizacja");
            openItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                     Platform.runLater(()->{
                                path = DirectoryChoose.DirectoryChooser(stage);
                                javafx.scene.control.Label label = new javafx.scene.control.Label(path);
                                Path dir = Paths.get(path);
                                openItem.setLabel(path);
                                if(path.equals("tmp")) return;

                                PauseItem.setEnabled(true);
                                StopItem.setEnabled(true);
                                PauseItem.setLabel("Pause");

                                DirWatcher watcher = null;
                                try {
                                    watcher = new DirWatcher(dir);
                                    DirWatcher finalWatcher = watcher;
                                    thread = new Thread(() -> finalWatcher.processEvents());
                                    thread.start();

                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }


                            }
                    );
                }

            });

            StopItem.setEnabled(false);
            PauseItem.setEnabled(false);
            if(!path.equals("tmp")){
                StopItem.setEnabled(true);

            }

            // the convention for tray icons seems to be to set the default icon for opening
            // the application stage in a bold font.
            java.awt.Font defaultFont = java.awt.Font.decode(null);
            java.awt.Font boldFont = defaultFont.deriveFont(java.awt.Font.BOLD);
            openItem.setFont(boldFont);


            java.awt.Menu statItem = new java.awt.Menu("Statistics");

            java.awt.MenuItem analizaItem = new java.awt.MenuItem("Analiza Pojemości");
            analizaItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Platform.runLater(()->{
                                if(path.equals("tmp")){
                                    path = DirectoryChoose.DirectoryChooser(stage);
                                    Path dir = Paths.get(path);
                                    openItem.setLabel(path);
                                    if(path.equals("tmp")) return;
                                    PauseItem.setEnabled(true);
                                    StopItem.setEnabled(true);
                                    PauseItem.setLabel("Pause");

                                    DirWatcher watcher = null;
                                    try {
                                        watcher = new DirWatcher(dir);
                                        DirWatcher finalWatcher = watcher;
                                        thread = new Thread(() -> finalWatcher.processEvents());
                                        thread.start();

                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }

                                }
                                try {
                                    CapacityAnalysis.ShowCapacityAnalysis((DirectoryRecord) DataProcessor.getRoot());
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }


                    });
                }

            });


            java.awt.MenuItem dawnoItem = new java.awt.MenuItem("Dawno Nieużywane");
            dawnoItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Platform.runLater(()->{
                        if(path.equals("tmp")){
                            path = DirectoryChoose.DirectoryChooser(stage);
                            Path dir = Paths.get(path);
                            openItem.setLabel(path);
                            if(path.equals("tmp")) return;
                            PauseItem.setEnabled(true);
                            StopItem.setEnabled(true);
                            PauseItem.setLabel("Pause");

                            DirWatcher watcher = null;
                            try {
                            watcher = new DirWatcher(dir);
                            DirWatcher finalWatcher = watcher;
                            thread = new Thread(() -> finalWatcher.processEvents());
                            thread.start();
                            } catch (IOException e1) {
                            e1.printStackTrace();
                            }


                        }
                        try {
                            LongUnused.ShowLongUnused((DirectoryRecord) DataProcessor.getRoot());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                            }
                    );
                }

            });



            java.awt.MenuItem exitItem = new java.awt.MenuItem("Exit");
            exitItem.addActionListener((ActionEvent event) -> {
                Platform.exit();
                tray.remove(trayIcon);
                System.exit(0);
            });

            // setup the popup menu for the application.
            final java.awt.PopupMenu popup = new java.awt.PopupMenu();

            popup.add(openItem);
            popup.addSeparator();
            popup.add(PauseItem);
            popup.add(StopItem);
            popup.addSeparator();
            popup.add(statItem);
            statItem.add(analizaItem);
            statItem.add(dawnoItem);
            popup.addSeparator();
            popup.add(exitItem);
            trayIcon.setPopupMenu(popup);


            tray.add(trayIcon);
        } catch (java.awt.AWTException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the application stage and ensures that it is brought ot the front of all stages.
     */
    private void showStage() {
        if (stage != null) {
            stage.show();
            stage.toFront();
        }
    }

}
