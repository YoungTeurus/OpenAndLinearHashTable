package Implemitations;

public class OpenAddressHashtable<Key, Data> extends BaseHashTable<Key, Data> {
    protected final int getNextIndex(Object o, int collisionIndex) {
        return getNormalizedInSizeIndex(collisionIndex + 1);
    }
}
