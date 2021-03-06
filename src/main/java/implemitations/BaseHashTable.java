package implemitations;

import interfaces.HashTable;
import interfaces.KeyDataPair;

public abstract class BaseHashTable<Key, Data> implements HashTable<Key, Data> {
    public static int DEFAULT_SIZE = 37;
    private Object[] _hashTable;  // Это может быть либо массив списков, либо массив пар элементов: зависит от субкласса.
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

    // Метод должен создавать массив _hashTable необходимого типа.
    protected abstract Object[] createNewArray(int size);

    public void insert(Key key, Data data) {
        insert(new implemitations.KeyDataPair(key, data));
    }

    // Паттерн "Шаблонный метод": субкласс должен переопределить, как вставлять новый элемент в хеш-таблицу.
    public void insert(KeyDataPair<Key, Data> keyDataPair) {
        if (keyDataPair == null){
            return;
        }
        Key keyOfPair = keyDataPair.getKey();
        int indexInHashTable = getNormalizedInSizeHashcodeOfKey(keyOfPair);

        placePairIntoHashTable(keyOfPair, indexInHashTable, keyDataPair);
    }

    // Метод должен вставлять элемент keyDataPair c ключём key на место indexInHashTable в _hashTable.
    protected abstract void placePairIntoHashTable(Key key, Integer indexInHashTable, KeyDataPair<Key, Data> keyDataPair);

    // Паттерн "Шаблонный метод": субкласс должен переопределить, как находить новый индекс элемента при коллизии (метод getPair).
    final public Data get(Key key) {
        KeyDataPair<Key, Data> pair = getPairByKey(key);
        if (pair != null){
            return pair.getData();
        }
        return null;
    }

    // Метод должен возвращать пару, соответствующую ключу key или null, если такая не была найдена.
    protected abstract KeyDataPair<Key, Data> getPairByKey(Key key);
    // Метод должен возвращать пару, содержащую данные data или null, если такая не была найдена.
    protected abstract KeyDataPair<Key, Data> getPairByData(Data data);

    public boolean containsKey(Key key) {
        KeyDataPair<Key, Data> pairWithSetKey = getPairByKey(key);
        return pairWithSetKey != null;
    }

    public boolean containsData(Data data) {
        KeyDataPair<Key, Data> pairWithSetData = getPairByData(data);
        return pairWithSetData != null;
    }

    // Метод должен удалять из хеш-таблицы элемент, соответствующий ключу key.
    public abstract void remove(Key key);

    // Субклассы самостоятельно принимают решение о расширении хеш-таблицы!
    final protected void rehash(int newSize){
        if(newSize <= _size){
            return;
        }
        _hashTable = createNewHashTableFromCurrentWithNewSize(_hashTable, newSize);
        _size = _hashTable.length;
    }

    // Метод должен возвращать новый массив _hashTable, заполненный элементами из старой _hashTable.
    protected abstract Object[] createNewHashTableFromCurrentWithNewSize(Object[] currentHashTable, int newSize);

    // Вспомогательные методы для субклассов:
    protected int getNormalizedInSizeHashcodeOfKey(Key key){
        return getHashcodeOf(key) % _size;
    }
    protected static int getHashcodeOf(Object object){
        if (object == null){
            return 0;
        }
        // object.hashCode может быть отрицательным, а индекс массива - нет.
        return Math.abs(object.hashCode());
    }
    protected void setPairIntoHashTable(int index, Object object){
        _hashTable[index] = object;
    }
    protected Object[] getHashTable(){
        return _hashTable;
    }
    protected Object getHashTableElementAt(int index){
        return _hashTable[index];
    }
}
