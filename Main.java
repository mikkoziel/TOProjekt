import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException,
            InterruptedException {
        // Folder we are going to watch
        Localization path = new Localization();
        WatchPath service = new WatchPath(path.getPath());
        service.watchDirectoryPath();
    }
}
