package Implemitations;

import Interfaces.IHashTable;
import Interfaces.IKeyDataPair;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class ChainedHashTable<Key, Data> extends BaseHashTable<Key, Data> {
    ChainedHashTable(){
        super();
    }

    ChainedHashTable(int size){
        super(size);
    }

    @Override
    final protected Object[] createNewArray(int size) {
        return (Object[]) Array.newInstance(ArrayList.class, size);
    }

    @Override
    protected void placePairIntoHashTable(Key key, Integer indexInHashTable, IKeyDataPair<Key, Data> keyDataPair) {
        ArrayList<IKeyDataPair<Key, Data>>[] hashTable = getHashTableOfCorrectType();
        if(hashTable[indexInHashTable] == null){
            ArrayList<IKeyDataPair<Key, Data>> newArrayList = new ArrayList<IKeyDataPair<Key, Data>>();
            newArrayList.add(keyDataPair);
            setPairIntoHashTable(indexInHashTable, newArrayList);
        } else {
            hashTable[indexInHashTable].add(keyDataPair);
        }
    }

    @Override
    protected IKeyDataPair<Key, Data> getPair(Key key) {
        ArrayList<IKeyDataPair<Key, Data>>[] hashTable = getHashTableOfCorrectType();
        int indexInHashTable = getNormalizedInSizeHashcodeOfKey(key);

        if(hashTable[indexInHashTable] != null){
            for (IKeyDataPair<Key, Data> keyDataPair : hashTable[indexInHashTable]) {
                if(keyDataPair.isKeyEqualsTo(key)){
                    return keyDataPair;
                }
            }
        }
        return null;
    }

    @Override
    public void remove(Key key) {
        ArrayList<IKeyDataPair<Key, Data>>[] hashTable = getHashTableOfCorrectType();
        int indexInHashTable = getNormalizedInSizeHashcodeOfKey(key);

        if(hashTable[indexInHashTable] != null){
            Iterator<IKeyDataPair<Key, Data>> it = hashTable[indexInHashTable].iterator();
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
        throw new NotImplementedException();
    }

    private ArrayList<IKeyDataPair<Key, Data>>[] getHashTableOfCorrectType(){
        return (ArrayList<IKeyDataPair<Key, Data>>[])getHashTable();
    }
}
