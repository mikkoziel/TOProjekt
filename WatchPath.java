import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

public class WatchPath {

    public static Path path;

    public WatchPath(Path name){
        path= name;
    }

    public static void watchDirectoryPath() {
        // Sanity check - Check if path is a folder
        try {
            Boolean isFolder = (Boolean) Files.getAttribute(path,
                    "basic:isDirectory", NOFOLLOW_LINKS);
            if (!isFolder) {
                throw new IllegalArgumentException("Path: " + path + " is not a folder");
            }
        } catch (IOException ioe) {
            // Folder does not exists
            ioe.printStackTrace();
        }

        System.out.println("Watching path: " + path);

        // We obtain the file system of the Path
        FileSystem fs = path.getFileSystem ();

        // We create the new WatchService using the new try() block
        try(WatchService service = fs.newWatchService()) {

            // We register the path to the service
            // We watch for creation events
            path.register(service, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

            // Start the infinite polling loop
            WatchKey key = null;
            while(true) {
                key = service.take();

                // Dequeueing events
                WatchEvent.Kind<?> kind = null;
                for(WatchEvent<?> watchEvent : key.pollEvents()) {
                    // Get the type of the event
                    kind = watchEvent.kind();
                   if (OVERFLOW == kind) {
                        continue; //loop
                    } else if (ENTRY_CREATE == kind) {
                        // A new Path was created
                        Path newPath = ((WatchEvent<Path>) watchEvent).context();
                        // Output
                        System.out.println("New path created: " + newPath);
                    } else if (ENTRY_DELETE == kind) {
                    // A new Path was created
                    Path newPath = ((WatchEvent<Path>) watchEvent).context();
                    // Output
                    System.out.println("Path deleted: " + newPath);
                     } else if (ENTRY_MODIFY == kind) {
                    // A new Path was created
                    Path newPath = ((WatchEvent<Path>) watchEvent).context();
                    // Output
                    System.out.println("Path modified: " + newPath);
                    }
                }

                if(!key.reset()) {
                    break; //loop
                }
            }

        } catch(IOException ioe) {
            ioe.printStackTrace();
        } catch(InterruptedException ie) {
            ie.printStackTrace();
        }

    }
}
