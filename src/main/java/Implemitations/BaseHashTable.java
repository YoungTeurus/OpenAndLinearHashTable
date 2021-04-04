package Implemitations;

import Interfaces.IHashTable;
import Interfaces.IKeyDataPair;

import java.lang.reflect.Array;

public abstract class BaseHashTable<Key, Data> implements IHashTable<Key, Data> {
    public static int DEFAULT_SIZE = 37;
    private IKeyDataPair<Key, Data>[] _hashTable;
    protected int _size;

    public BaseHashTable(){
        setHashTableSizeAndCreateArray(DEFAULT_SIZE);
    }

    public BaseHashTable(int size){
        setHashTableSizeAndCreateArray(size);
    }

    private void setHashTableSizeAndCreateArray(int size){
        _size = size;
        _hashTable = createNewArray(_size);
    }

    private IKeyDataPair<Key, Data>[] createNewArray(int size){
        // TODO: Опасное (?) приведение типов
        return (IKeyDataPair<Key, Data>[]) Array.newInstance(IKeyDataPair.class, size);
    }

    public void insert(Key key, Data data) {
        insert(new KeyDataPair<Key, Data>(key, data));
    }

    // Паттерн "Шаблонный метод": субкласс должен переопределить, как вставлять новый элемент в хеш-таблицу.
    public void insert(IKeyDataPair<Key, Data> keyDataPair) {
        Key keyOfPair = keyDataPair.getKey();
        int indexInHashTable = getNormalizedInSizeHashcodeOfKey(keyOfPair);

        placePairIntoHashTable(keyOfPair, indexInHashTable, keyDataPair);
    }

    // Метод должен вставлять элемент keyDataPair c ключём key на место indexInHashTable в _hashTable.
    protected abstract void placePairIntoHashTable(Key key, Integer indexInHashTable, IKeyDataPair<Key, Data> keyDataPair);

    protected void setPairIntoHashTable(int index, Object object){
        _hashTable[index] = object;
    }

    // TODO: следующие три метода, возможно, должны переопределяться в
    protected boolean isFreeOrSameKeyPlace(Key key, int indexInHashTable){
        return isPlaceEmpty(indexInHashTable) || isPlaceContainsSameKey(key, indexInHashTable);
    }

    protected boolean isPlaceEmpty(int index){
        return _hashTable[index] == null;
    }

    protected boolean isPlaceContainsSameKey(Key key, int index){
        return _hashTable[index].isKeyEqualsTo(key);
    }

    private int getNormalizedInSizeHashcodeOfKey(Key key){
        return getHashcodeOf(key) % _size;
    }

    private static int getHashcodeOf(Object object){
        if (object == null){
            return 0;
        }
        // object.hashCode может быть отрицательным, а индекс массива - нет.
        return Math.abs(object.hashCode());
    }

    public Data get(Key key) {
        IKeyDataPair<Key, Data> pair = getPair(key);
        if (pair != null){
            return pair.getData();
        }
        return null;
    }

    protected abstract IKeyDataPair<Key, Data> getPair(Key key);

    public abstract void remove(Key key);

    public abstract IHashTable<Key, Data> rehash(int size);
}
