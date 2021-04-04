package Implemitations;

public class LinearOpenAddressHashtable<Key, Data> extends OpenAddressHashTable<Key, Data> {
    LinearOpenAddressHashtable(){
        super();
    }

    LinearOpenAddressHashtable(int size){
        super(size);
    }

    protected final int getNextIndex(Key key, int collisionIndex, int tryCount) {
        return getNormalizedInSizeIndex(collisionIndex + 1);
    }
}
