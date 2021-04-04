package Implemitations;

import Interfaces.IHashTable;
import Interfaces.IKeyDataPair;

import java.lang.reflect.Array;

public abstract class BaseHashTable<Key, Data> implements IHashTable<Key, Data> {
    public static int DEFAULT_SIZE = 37;
    protected static int CANT_INSERT_OR_FIND = -1;

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

    public void insert(Key key, Data data){
        insert(new KeyDataPair<Key, Data>(key, data));
    }

    // Паттерн "Шаблонный метод": субкласс должен переопределить, как находить новый индекс элемента при коллизии.
    public void insert(IKeyDataPair<Key, Data> keyDataPair){
        Key keyOfPair = keyDataPair.getKey();
        int indexInHashTable = getNormalizedInSizeHashcodeOfKey(keyOfPair);

        Integer startIndex = indexInHashTable;

        while(!isFreeOrSameKeyPlace(keyOfPair, indexInHashTable)){
            indexInHashTable = getNextIndex(keyOfPair, indexInHashTable);

            if (startIndex.equals(indexInHashTable)){
                // TODO: Если мы вернулись в начальную позицию, настала необходимость расширить хеш-таблицу
                return;
            }
        }

        _hashTable[indexInHashTable] = keyDataPair;
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

    private boolean isFreeOrSameKeyPlace(Key key, int indexInHashTable){
        return isPlaceEmpty(indexInHashTable) || isPlaceContainsSameKey(key, indexInHashTable);
    }

    private boolean isPlaceEmpty(int index){
        return _hashTable[index] == null;
    }

    private boolean isPlaceContainsSameKey(Key key, int index){
        return _hashTable[index].getKey().equals(key);
    }

    // Паттерн "Шаблонный метод": субкласс должен переопределить, как находить новый индекс элемента при коллизии.
    public Data get(Key key){
        IKeyDataPair<Key, Data> pair = getPair(key);
        if (pair != null){
            return pair.getData();
        }
        return null;
    }

    private IKeyDataPair<Key, Data> getPair(Key key){
        int indexInHashTable = getNormalizedInSizeHashcodeOfKey(key);

        Integer startIndex = indexInHashTable;

        while(!isPlaceEmpty(indexInHashTable)){
            if(isPlaceContainsSameKey(key, indexInHashTable)){
                return _hashTable[indexInHashTable];
            }
            indexInHashTable = getNextIndex(key, indexInHashTable);

            if (startIndex.equals(indexInHashTable)){
                break;
            }
        }
        return null;
    }

    // Метод должен возвращать следующий индекс для ключа key, как если бы произошла коллизия на месте collisionIndex.
    protected abstract int getNextIndex(Key key, int collisionIndex);

    protected int getNormalizedInSizeIndex(int index){
        return index % _size;
    }

    public void remove(Key key) {
        // TODO: реализовать!
    }

    public IHashTable<Key, Data> rehash(int size){
        // TODO: реализовать! Подумать, как возварщать (или хотя бы создавать) хеш-таблицу нужного типа
        // IHashTable<Key, Data> newHashTable;
        // IKeyDataPair<Key, Data>[] newHashTable = createNewArray(size);
        // for (IKeyDataPair<Key, Data> keyDataPair: _hashTable){
        //     int newIndexInHashTable = getHashcodeOfKey(keyDataPair);
        //     newHashTable[newIndexInHashTable] = keyDataPair;

        //     // onCollision...
        //     // Проще возвращать новую HashTable?
        // }
        return null;
    }

    private IKeyDataPair<Key, Data>[] createNewArray(int size){
        // TODO: Опасное (?) приведение типов
        return (IKeyDataPair<Key, Data>[])Array.newInstance(IKeyDataPair.class, DEFAULT_SIZE);
    }
}
