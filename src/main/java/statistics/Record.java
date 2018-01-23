package statistics;


/**
 * Created by Justyna Gorczyca on 10.01.2018.
 */
public abstract class Record {
    protected String path;
    protected String name;
    protected String parent;
    protected long size;
    protected String lastModification;
    protected double percent;

    public abstract boolean addChild(Record r, String parentPath);
    public abstract boolean removeChild(Record r);
    public abstract Record getRecord(String path);
    public abstract boolean modifyRecord(String path, long size, String lastModification);
    public abstract long getSize();
    public abstract void updatePercent(long directorySize);

    @Override
    public String toString() {
        return this.name;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getLastModification() {
        return lastModification;
    }

    public double getPercent() {
        return percent;
    }
}
