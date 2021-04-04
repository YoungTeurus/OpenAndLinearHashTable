package Implemitations;

import Interfaces.IHashTable;
import Interfaces.IKeyDataPair;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class ChainedHashTable<Key, Data> extends BaseHashTable<Key, Data> {
    static float CRITICAL_USED_SIZE_TO_SIZE_RATIO_TO_REHASH = (float)1/5;

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
        ArrayList<IKeyDataPair<Key, Data>> currentKeyDataList = hashTable[indexInHashTable];
        if(currentKeyDataList == null){
            ArrayList<IKeyDataPair<Key, Data>> newArrayList = new ArrayList<IKeyDataPair<Key, Data>>();
            newArrayList.add(keyDataPair);
            setPairIntoHashTable(indexInHashTable, newArrayList);
        } else {
            currentKeyDataList.add(keyDataPair);
            checkListUsedLengthAndRehashIfNeeded(currentKeyDataList);
        }
    }

    private void checkListUsedLengthAndRehashIfNeeded(ArrayList<IKeyDataPair<Key, Data>> keyDataList){
        if (keyDataList.size() > CRITICAL_USED_SIZE_TO_SIZE_RATIO_TO_REHASH * _size){
            rehash(_size * 2);
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

    protected final Object[] createNewHashTableFromCurrentWithNewSize(Object[] currentHashTable, int newSize) {
        ChainedHashTable<Key, Data> newHashTable = new ChainedHashTable<Key, Data>(newSize);
        iterateThroughOldHashTableAndPopulateNew(newHashTable);
        return newHashTable.getHashTable();
    }

    private void iterateThroughOldHashTableAndPopulateNew(ChainedHashTable<Key, Data> newHashTable){
        for (ArrayList<IKeyDataPair<Key, Data>> keyDataPairList : (ArrayList<IKeyDataPair<Key, Data>>[])getHashTable()) {
            iterateThroughListOfPairsAndPopulateHashtable(keyDataPairList, newHashTable);
        }
    }

    private void iterateThroughListOfPairsAndPopulateHashtable(ArrayList<IKeyDataPair<Key, Data>> keyDataPairList, ChainedHashTable<Key, Data> newHashTable){
        if(keyDataPairList != null){
            for(IKeyDataPair<Key, Data> keyDataPair : keyDataPairList){
                newHashTable.insert(keyDataPair);
            }
        }
    }

    private ArrayList<IKeyDataPair<Key, Data>>[] getHashTableOfCorrectType(){
        return (ArrayList<IKeyDataPair<Key, Data>>[])getHashTable();
    }
}
