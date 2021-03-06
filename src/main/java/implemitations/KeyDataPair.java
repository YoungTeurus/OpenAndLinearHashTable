package implemitations;

public class KeyDataPair<Key, Data> implements interfaces.KeyDataPair<Key, Data> {
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

    public boolean isKeyEqualsTo(Key other) {
        return _key.equals(other);
    }

    public boolean isDataEqualsTo(Data other) {
        return _data.equals(other);
    }
}
