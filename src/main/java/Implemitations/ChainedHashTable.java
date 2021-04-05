package Implemitations;

import Interfaces.IKeyDataPair;

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
            createNewArrayListAtIndexAndPlacePairIntoIt(indexInHashTable, keyDataPair);
        } else {
            appendPairIntoListAndRehashIfNeeded(currentKeyDataList, keyDataPair);
        }
    }

    private void createNewArrayListAtIndexAndPlacePairIntoIt(Integer indexInHashTable, IKeyDataPair<Key, Data> keyDataPair){
        ArrayList<IKeyDataPair<Key, Data>> newArrayList = new ArrayList<IKeyDataPair<Key, Data>>();
        newArrayList.add(keyDataPair);
        setPairIntoHashTable(indexInHashTable, newArrayList);
    }

    private void appendPairIntoListAndRehashIfNeeded(ArrayList<IKeyDataPair<Key, Data>> keyDataList, IKeyDataPair<Key, Data> keyDataPair){
        keyDataList.add(keyDataPair);
        checkListUsedLengthAndRehashIfNeeded(keyDataList);
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
        ArrayList<IKeyDataPair<Key, Data>> pairListToIterate = hashTable[indexInHashTable];

        return iterateThroughPairListAndReturnPairWithKeyEqualsTo(pairListToIterate, key);
    }

    private IKeyDataPair<Key, Data> iterateThroughPairListAndReturnPairWithKeyEqualsTo(ArrayList<IKeyDataPair<Key, Data>> pairList, Key key){
        if(pairList != null){
            for (IKeyDataPair<Key, Data> keyDataPair : pairList) {
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
        ArrayList<IKeyDataPair<Key, Data>> pairListWithItemToRemove = hashTable[indexInHashTable];

        iterateThroughPairListAndRemovePairWithKeyEqualsTo(pairListWithItemToRemove, key);
    }

    private void iterateThroughPairListAndRemovePairWithKeyEqualsTo(ArrayList<IKeyDataPair<Key, Data>> pairList, Key key){
        if(pairList != null){
            Iterator<IKeyDataPair<Key, Data>> it = pairList.iterator();
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
        //noinspection unchecked
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
        //noinspection unchecked
        return (ArrayList<IKeyDataPair<Key, Data>>[])getHashTable();
    }
}
