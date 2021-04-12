package implemitations;

public class LinearOpenAddressHashtable<Key, Data> extends OpenAddressHashTable<Key, Data> {
    LinearOpenAddressHashtable(){
        super();
    }

    LinearOpenAddressHashtable(int size){
        super(size);
    }

    protected final int getNextIndex(Key key, int collisionIndex) {
        return getNormalizedInSizeIndex(collisionIndex + 1 + getLastTryCount());
    }

    protected OpenAddressHashTable<Key, Data> createNewHashTable(int size) {
        return new LinearOpenAddressHashtable<Key, Data>(size);
    }
}
