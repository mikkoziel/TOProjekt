package statistics;
/**
 * Created by Justyna Gorczyca on 14.12.2017.
 */
public class DataProcessor {

    private static Record root;
    private long rootSize;
    private boolean rootFound;

    public DataProcessor() {
        rootFound = false; //root is not found yet
    }

    public static Record getRoot() {
        return root;
    }

    public void setRoot(String path, String name, String parent, long size, String lasModification) {
        root = new DirectoryRecord(path, name, parent, size, lasModification);
        rootFound = true;
    }

    public long getRootSize() {
        return rootSize;
    }

    public void updateRootSize() {
        rootSize = root.getSize();
        root.updatePercent(rootSize);
    }

    public void addNewRecord(String path, long size, String lasModification, boolean isDirectory) {
        String name =  path.substring(path.lastIndexOf("\\")+1);
        String parentPath = path.substring(0,path.lastIndexOf("\\")); //parent path
        String parent = parentPath.substring(parentPath.lastIndexOf("\\")+1);

        if(!rootFound && isDirectory) setRoot(path,name,parent,size,lasModification);
        else if(isDirectory) {
            DirectoryRecord directory = new DirectoryRecord(path,name,parent,size,lasModification);
            root.addChild(directory,parentPath);
        } else {
            FileRecord file = new FileRecord(path,name,parent,size,lasModification);
            root.addChild(file,parentPath);
        }
    }

    public void removeRecord (String path){
        Record recordToRemove = root.getRecord(path);
        if(recordToRemove != null)
            root.removeChild(recordToRemove);
    }

    public void updateRecord (String path, long size, String lasModification) {
        root.modifyRecord(path,size,lasModification);
    }

}
