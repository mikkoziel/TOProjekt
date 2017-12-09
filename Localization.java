import java.io.IOException;
import java.nio.file.*;
import java.util.Scanner;

public class Localization {

    Path path;

    public Localization(){
        System.out.println("Please enter your Path:");
        Scanner odczyt = new Scanner(System.in); //obiekt do odebrania danych od użytkownika
        this.path = CheckPath(Paths.get(odczyt.nextLine()));
    }

    public Path CheckPath(Path name) {
    try {
        boolean pathExists = Files.exists(name, new LinkOption[]{ LinkOption.NOFOLLOW_LINKS});
        System.out.println(pathExists);
        if(!pathExists) {
            Path newDir = Files.createDirectory(name);
            return newDir;
        }
        } catch(FileAlreadyExistsException e){
        // the directory already exists.
        } catch (IOException e) {
        //something else went wrong
        e.printStackTrace();
        }
        return  name;
    }

    public Path getPath() {
        return path;
    }
}