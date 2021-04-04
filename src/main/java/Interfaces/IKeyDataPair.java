package Interfaces;

public interface IKeyDataPair<Key, Data> {
    Key getKey();
    Data getData();

    boolean isKeyEqualsTo(Key other);
}
