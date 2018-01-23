package statistics;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justyna Gorczyca on 10.01.2018.
 */
public class DirectoryRecord extends Record {

    List<Record> childrenList;

    DirectoryRecord(String path, String name, String parent, long size, String lasModification) {
        this.path = path;
        this.name = name;
        this.parent = parent;
        this.size = size;
        this.lastModification = lasModification;
        this.percent = 0.0;
        this.childrenList = new ArrayList<>();
    }

    @Override
    public boolean removeChild(Record r){
        for(Record record : childrenList) {
            if (record.path.equals(r.path)) {
                childrenList.remove(r); //if paths are equal, I can remove record from this list
                return true;
            } else if (record.removeChild(r)) return true;
        }
        return false;
    }

    @Override
    public Record getRecord(String path) {
        Record toReturn;
        if(this.path.equals(path)) return this;
        else
            for(Record record : childrenList) {
                toReturn = record.getRecord(path);
                if(toReturn != null) return toReturn;
            }
        return null;}

    @Override
    public boolean modifyRecord(String path, long size, String lastModification) {
        if(this.path.equals(path)) {
            this.size = size;
            this.lastModification = lastModification;
            return true;
        } else
            for(Record record : childrenList)
                if(record.modifyRecord(path,size,lastModification)) return true;
        return false;
    }

    @Override
    public long getSize() {
        long sizeToAdd = 0;
        for(Record record : childrenList) {
            sizeToAdd += record.getSize();
        }
        this.size = sizeToAdd;
        return this.size;
    }

    @Override
    public boolean addChild(Record r, String parentPath){
        if(parentPath.equals(this.path)) { //if record's parent is this directory
            childrenList.add(r);
            return true;
        }
        else {
            for(Record record : childrenList) {
                if (record.addChild(r,parentPath)) return true;
            }
        }
        return false;
    }

    @Override
    public void updatePercent(long rootSize) {
        if(rootSize == 0)
            this.percent = 0;
        else
            this.percent = (size * 100)/rootSize;
        for(Record record : childrenList)
            record.updatePercent(rootSize);
    }


    public List<Record> getChildrenList() {
        return childrenList;
    }

    @Override
    public String getPath() {
        return super.getPath();
    }
}
