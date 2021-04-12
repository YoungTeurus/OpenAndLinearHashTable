package implemitations;

import interfaces.KeyDataPair;

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
    protected void placePairIntoHashTable(Key key, Integer indexInHashTable, KeyDataPair<Key, Data> keyDataPair) {
        ArrayList<KeyDataPair<Key, Data>>[] hashTable = getHashTableOfCorrectType();
        ArrayList<KeyDataPair<Key, Data>> currentKeyDataList = hashTable[indexInHashTable];
        if(currentKeyDataList == null){
            createNewArrayListAtIndexAndPlacePairIntoIt(indexInHashTable, keyDataPair);
        } else {
            appendPairIntoListAndRehashIfNeeded(currentKeyDataList, keyDataPair);
        }
    }

    private void createNewArrayListAtIndexAndPlacePairIntoIt(Integer indexInHashTable, KeyDataPair<Key, Data> keyDataPair){
        ArrayList<KeyDataPair<Key, Data>> newArrayList = new ArrayList<KeyDataPair<Key, Data>>();
        newArrayList.add(keyDataPair);
        setPairIntoHashTable(indexInHashTable, newArrayList);
    }

    private void appendPairIntoListAndRehashIfNeeded(ArrayList<KeyDataPair<Key, Data>> keyDataList, KeyDataPair<Key, Data> keyDataPair){
        keyDataList.add(keyDataPair);
        checkListUsedLengthAndRehashIfNeeded(keyDataList);
    }

    private void checkListUsedLengthAndRehashIfNeeded(ArrayList<KeyDataPair<Key, Data>> keyDataList){
        if (keyDataList.size() > CRITICAL_USED_SIZE_TO_SIZE_RATIO_TO_REHASH * _size){
            rehash(_size * 2);
        }
    }

    @Override
    protected KeyDataPair<Key, Data> getPairByKey(Key key) {
        ArrayList<KeyDataPair<Key, Data>>[] hashTable = getHashTableOfCorrectType();
        int indexInHashTable = getNormalizedInSizeHashcodeOfKey(key);
        ArrayList<KeyDataPair<Key, Data>> pairListToIterate = hashTable[indexInHashTable];

        return iterateThroughPairListAndReturnPairWithKeyEqualsTo(pairListToIterate, key);
    }

    private KeyDataPair<Key, Data> iterateThroughPairListAndReturnPairWithKeyEqualsTo(ArrayList<KeyDataPair<Key, Data>> pairList, Key key){
        // TODO: вынести алгоритм пробега по pairList в отдельную функцию.
        if(pairList != null){
            for (KeyDataPair<Key, Data> keyDataPair : pairList) {
                if(keyDataPair.isKeyEqualsTo(key)){
                    return keyDataPair;
                }
            }
        }
        return null;
    }

    @Override
    protected KeyDataPair<Key, Data> getPairByData(Data data) {
        //noinspection UnnecessaryLocalVariable
        KeyDataPair<Key, Data> foundPair = iterateThroughAllPairListsAndReturnPairWithDataEqualsTo(data);
        return foundPair;
    }

    private KeyDataPair<Key, Data> iterateThroughAllPairListsAndReturnPairWithDataEqualsTo(Data data){
        ArrayList<KeyDataPair<Key, Data>>[] hashTable = getHashTableOfCorrectType();

        for(ArrayList<KeyDataPair<Key, Data>> pairList : hashTable){
            // TODO: вынести алгоритм пробега по pairList в отдельную функцию.
            if(pairList != null){
                for (KeyDataPair<Key, Data> keyDataPair : pairList) {
                    if(keyDataPair.isDataEqualsTo(data)){
                        return keyDataPair;
                    }
                }
            }

        }

        return null;
    }

    @Override
    public void remove(Key key) {
        ArrayList<KeyDataPair<Key, Data>>[] hashTable = getHashTableOfCorrectType();
        int indexInHashTable = getNormalizedInSizeHashcodeOfKey(key);
        ArrayList<KeyDataPair<Key, Data>> pairListWithItemToRemove = hashTable[indexInHashTable];

        iterateThroughPairListAndRemovePairWithKeyEqualsTo(pairListWithItemToRemove, key);
    }

    private void iterateThroughPairListAndRemovePairWithKeyEqualsTo(ArrayList<KeyDataPair<Key, Data>> pairList, Key key){
        if(pairList != null){
            Iterator<KeyDataPair<Key, Data>> it = pairList.iterator();
            while(it.hasNext()){
                KeyDataPair<Key, Data> currentPair = it.next();
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
        for (ArrayList<KeyDataPair<Key, Data>> keyDataPairList : (ArrayList<KeyDataPair<Key, Data>>[])getHashTable()) {
            iterateThroughListOfPairsAndPopulateHashtable(keyDataPairList, newHashTable);
        }
    }

    private void iterateThroughListOfPairsAndPopulateHashtable(ArrayList<KeyDataPair<Key, Data>> keyDataPairList, ChainedHashTable<Key, Data> newHashTable){
        if(keyDataPairList != null){
            for(KeyDataPair<Key, Data> keyDataPair : keyDataPairList){
                newHashTable.insert(keyDataPair);
            }
        }
    }

    private ArrayList<KeyDataPair<Key, Data>>[] getHashTableOfCorrectType(){
        //noinspection unchecked
        return (ArrayList<KeyDataPair<Key, Data>>[])getHashTable();
    }
}
