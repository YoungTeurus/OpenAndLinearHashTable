package Implemitations;

import Interfaces.IHashTable;
import Interfaces.IKeyDataPair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class ChainedHashTable<Key, Data> extends BaseHashTable<Key, Data> {
    public static int DEFAULT_SIZE = 37;
    private ArrayList<IKeyDataPair<Key, Data>>[] _hashTable;  // Массив списков
    protected int _size;

    ChainedHashTable(){
        _hashTable = createNewArray(DEFAULT_SIZE);
    }

    ChainedHashTable(int size){
        _hashTable = createNewArray(size);
    }

    private ArrayList<IKeyDataPair<Key, Data>>[] createNewArray(int size){
        // TODO: Опасное (?) приведение типов
        return (ArrayList<IKeyDataPair<Key, Data>>[]) Array.newInstance(ArrayList.class, size);
    }

    @Override
    protected void placePairIntoHashTable(Key key, Integer indexInHashTable, IKeyDataPair<Key, Data> keyDataPair) {
        if(_hashTable[indexInHashTable] == null){
            ArrayList<IKeyDataPair<Key, Data>> newArrayList = new ArrayList<IKeyDataPair<Key, Data>>();
            newArrayList.add(keyDataPair);
            setPairIntoHashTable(indexInHashTable, newArrayList);
        } else {
            _hashTable[indexInHashTable].add(keyDataPair);
        }
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

    @Override
    protected IKeyDataPair<Key, Data> getPair(Key key) {
        int indexInHashTable = getNormalizedInSizeHashcodeOfKey(key);

        if(_hashTable[indexInHashTable] != null){
            for (IKeyDataPair<Key, Data> keyDataPair : _hashTable[indexInHashTable]) {
                if(keyDataPair.isKeyEqualsTo(key)){
                    return keyDataPair;
                }
            }
        }
        return null;
    }

    @Override
    public void remove(Key key) {
        int indexInHashTable = getNormalizedInSizeHashcodeOfKey(key);
        if(_hashTable[indexInHashTable] != null){
            Iterator<IKeyDataPair<Key, Data>> it = _hashTable[indexInHashTable].iterator();
            while(it.hasNext()){
                IKeyDataPair<Key, Data> currentPair = it.next();
                if (currentPair.isKeyEqualsTo(key)){
                    it.remove();
                    break;
                }
            }
        }
    }

    public IHashTable<Key, Data> rehash(int size) {
        return null;
    }
}
