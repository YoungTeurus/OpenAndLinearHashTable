package Interfaces;

public interface IHashTable<Key, Data> {
    void insert(Key key, Data data);
    void insert(IKeyDataPair<Key, Data> keyDataPair);

    Data get(Key key);

    void remove(Key key);

    IHashTable<Key, Data> rehash(int size);
}
