package statistics;

/**
 * Created by Justyna Gorczyca on 10.01.2018.
 */
public class FileRecord extends Record {

    FileRecord(String path, String name, String parent, long size, String lasModification) {
        this.path = path;
        this.name = name;
        this.parent = parent;
        this.size = size;
        this.lastModification = lasModification;
        this.percent = 0.0;
    }


    @Override
    public boolean addChild(Record r, String parentPath) {
        return false;
    }

    @Override
    public boolean removeChild(Record r) {
        return false;
    }

    @Override
    public Record getRecord(String path) {
        if (this.path.equals(path)) return this;
        else return null;
    }

    @Override
    public boolean modifyRecord(String path, long size, String lastModification) {
        if (this.path.equals(path)) {
            this.size = size;
            this.lastModification = lastModification;
            return true;
        }
        return false;
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public void updatePercent(long rootSize) {
        if(rootSize == 0)
            this.percent = 0;
        else
            this.percent = (size * 100) / rootSize;
    }

    @Override
    public String getPath() {
        return super.getPath();
    }
}
