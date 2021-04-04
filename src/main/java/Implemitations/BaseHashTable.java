package Implemitations;

import Interfaces.IHashTable;
import Interfaces.IKeyDataPair;

import java.lang.reflect.Array;

public abstract class BaseHashTable<Key, Data> implements IHashTable<Key, Data> {
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

    // Паттерн "Шаблонный метод": субкласс должен переопределить,
    // как находить новый индекс элемента при коллизии (метод getPair).
    final public Data get(Key key) {
        IKeyDataPair<Key, Data> pair = getPair(key);
        if (pair != null){
            return pair.getData();
        }
        return null;
    }

    // Метод должен возвращать пару, соответствующую ключу key или null, если такая не была найдена.
    protected abstract IKeyDataPair<Key, Data> getPair(Key key);

    // Метод должен удалять из хеш-таблицы элемент, соответствующий ключу key.
    public abstract void remove(Key key);

    // TODO: подумать насчёт целесобразности работы данного метода.
    // Метод должен расширять исходную хеш-таблицу, возвращая новую хеш-таблицу.
    public abstract IHashTable<Key, Data> rehash(int size);

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
