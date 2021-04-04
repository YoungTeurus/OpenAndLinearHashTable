package Implemitations;

import Interfaces.IKeyDataPair;

public class KeyDataPair<Key, Data> implements IKeyDataPair<Key, Data> {
    private Key _key;
    private Data _data;

    KeyDataPair(Key key, Data data){
        _key = key;
        _data = data;
    }

    public Key getKey() {
        return _key;
    }

    public Data getData() {
        return _data;
    }
}
