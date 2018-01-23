package stalker;

        /*
         * Created by Justyna Gorczyca on 14.12.2017.
         */
import gui.Notification;
import gui.ProgressDialog;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.util.Duration;
import statistics.DataProcessor;

import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.LinkOption.*;
import java.nio.file.attribute.*;
import java.io.*;
import java.util.*;

public class DirWatcher {

    private final WatchService watcher;
    private final Map<WatchKey,Path> keys;
    private boolean trace;
    public DataProcessor dataProcessor = new DataProcessor();
    public static String result, resultKey;
    //-----------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------
    DirWatcher(Path dir) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey,Path>();

        System.out.format("Scanning %s ...\n", dir);
        ProgressMaking(dir);
        System.out.println("Done.");


        this.trace = true;  // enable trace after initial registration
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    private static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------
    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        keys.put(key, dir);
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------
    private void registerAll(final Path start) throws IOException {
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attrs)
                    throws IOException
            {
                long fileSize = attrs.size();
                FileTime time = attrs.lastModifiedTime();
                dataProcessor.addNewRecord(path.toString(),fileSize,time.toString().substring(0,10),attrs.isDirectory());
                register(path);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                long fileSize = attrs.size();
                FileTime time = attrs.lastModifiedTime();
                dataProcessor.addNewRecord(path.toString(),fileSize,time.toString().substring(0,10),attrs.isDirectory());
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path path, IOException exc){
                return FileVisitResult.CONTINUE;
            }
        });
    }

    //-----------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------
    void processEvents (){
        while(true) {
            //Step 1
            //wait for key to be signalled
            //retreive key  - uzyskaj klucz
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException e) {
                return;
            }

            //pobieram scieżkę z mapy keys: Map<WatchKey,Path> keys;
            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            //Step 2
            //process events - przetwarzaj
            for (WatchEvent<?> event: key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();

                // TBD - provide example of how OVERFLOW event is handled
                if (kind == OVERFLOW) {
                    continue;
                }

                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);

                result= child.toString();
                resultKey=event.kind().name();
                Platform.runLater(()-> {
                    Notification note = null;
                    note.displayNote(result, resultKey);
                    PauseTransition delay = new PauseTransition(Duration.seconds(5));
                    delay.setOnFinished( even -> note.owner.close() );
                    delay.play();
                });

                if( kind == ENTRY_DELETE)
                    dataProcessor.removeRecord(child.toString());
                else try {
                    BasicFileAttributes attrs = Files.readAttributes(child, BasicFileAttributes.class);
                    long fileSize = attrs.size();
                    FileTime time = attrs.lastModifiedTime();
                    if(kind == ENTRY_CREATE && !attrs.isDirectory())  //if file (not directory) was created
                        dataProcessor.addNewRecord(child.toString(),fileSize,time.toString().substring(0,10),attrs.isDirectory());
                    if(kind == ENTRY_MODIFY)
                        dataProcessor.updateRecord(child.toString(),fileSize,time.toString().substring(0,10));
                } catch (IOException e) {
                    return;
                }

                // if directory is created, and watching recursively, then
                // register it and its sub-directories
                if (kind == ENTRY_CREATE) {
                    try {
                        if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                            registerAll(child);
                        }

                    } catch (IOException x) {
                        // ignore to keep sample readbale
                    }
                }
                // print out event
           //     System.out.format("%s: %s\n", event.kind().name(), child);
            }


            //Step 3
            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
            
        }
    }

    public void ProgressMaking(Path dir){
        ProgressDialog pForm = new ProgressDialog();
        pForm.ProgressForm();
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws InterruptedException, IOException {

                registerAll(dir);
                dataProcessor.updateRootSize();
                return null ;
            }
        };
        pForm.activateProgressBar(task);
        task.setOnSucceeded(event -> {
            pForm.getDialogStage().close();
        });
        pForm.getDialogStage().show();

        Thread thread = new Thread(task);
        thread.start();
    }
}