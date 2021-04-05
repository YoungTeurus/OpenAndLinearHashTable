package Implemitations;

import Interfaces.IKeyDataPair;

import java.lang.reflect.Array;

public abstract class OpenAddressHashTable<Key, Data> extends BaseHashTable<Key, Data> {
    private class KeyDataPairIndexCombo{
        IKeyDataPair<Key, Data> keyDataPair;
        int indexOfPair;

        KeyDataPairIndexCombo(){
            keyDataPair = null;
            indexOfPair = 0;
        }
    }


    private int sizeUsed;
    private int lastTryCount;

    public OpenAddressHashTable(){
        super();
        sizeUsed = 0;
    }

    public OpenAddressHashTable(int size){
        super(size);
        sizeUsed = 0;
    }

    @Override
    final protected Object[] createNewArray(int size) {
        return (Object[]) Array.newInstance(IKeyDataPair.class, size);
    }

    // Паттерн "Шаблонный метод": субкласс должен переопределить, как находить новый индекс элемента при коллизии.
    @Override
    protected void placePairIntoHashTable(Key key, Integer indexInHashTable, IKeyDataPair<Key, Data> keyDataPair) {
        Integer currentIndex = indexInHashTable;
        int wrongNextIndexTries = 0;

        while(!isFreeOrSameKeyPlace(key, currentIndex)){
            currentIndex = getNextIndex(key, currentIndex, wrongNextIndexTries);

            // TODO: Если мы вернулись в начальную позицию, настала необходимость расширить хеш-таблицу?
            if (indexInHashTable.equals(currentIndex)){
                return;
            }
        }
        setPairIntoHashTable(currentIndex, keyDataPair);
        increaseSizeUsedAndRehashIfNeeded();
    }

    private boolean isFreeOrSameKeyPlace(Key key, int indexInHashTable){
        return isPlaceEmpty(indexInHashTable) || isPlaceContainsSameKey(key, indexInHashTable);
    }

    private boolean isPlaceEmpty(int index){
        return getHashTableElementAt(index) == null;
    }

    private boolean isPlaceContainsSameKey(Key key, int index){
        return getHashTableElementOfCorrectTypeAt(index).isKeyEqualsTo(key);
    }

    private void increaseSizeUsedAndRehashIfNeeded(){
        sizeUsed++;
        if (sizeUsed > (float)2/3*_size){
            rehash(_size * 2);
        }
    }

    protected IKeyDataPair<Key, Data> getPair(Key key){
        KeyDataPairIndexCombo pairIndexCombo = getPairAndItsIndex(key);
        return pairIndexCombo.keyDataPair;
    }

    private KeyDataPairIndexCombo getPairAndItsIndex(Key key){
        KeyDataPairIndexCombo pairIndexCombo = new KeyDataPairIndexCombo();

        int indexInHashTable = getNormalizedInSizeHashcodeOfKey(key);

        Integer startIndex = indexInHashTable;
        int wrongNextIndexTries = 0;
        setLastTryCount(wrongNextIndexTries);

        while(!isPlaceEmpty(indexInHashTable)) {
            if (isPlaceContainsSameKey(key, indexInHashTable)) {
                pairIndexCombo.keyDataPair = getHashTableElementOfCorrectTypeAt(indexInHashTable);
                pairIndexCombo.indexOfPair = indexInHashTable;
                break;
            }

            indexInHashTable = getNextIndex(key, indexInHashTable, wrongNextIndexTries++);
            setLastTryCount(wrongNextIndexTries);

            if (startIndex.equals(indexInHashTable)) {
                break;
            }
        }

        return pairIndexCombo;
    }

    // Метод должен возвращать следующий индекс для ключа key, как если бы произошла коллизия на месте collisionIndex и
    // происходит tryCount попытка получить следующий индекс.
    // Передача tryCount позволяет
    protected abstract int getNextIndex(Key key, int collisionIndex, int tryCount);

    final protected int getNormalizedInSizeIndex(int index){
        return index % _size;
    }

    protected void setLastTryCount(int _lastTryCount){
        lastTryCount = _lastTryCount;
    }

    protected int getLastTryCount(){
        return lastTryCount;
    }

    @Override
    public void remove(Key key) {
        IKeyDataPair<Key, Data> keyDataPairToRemove = getPair(key);
        if(keyDataPairToRemove != null){
            removePairAndCorrectAllNextKeyDataPairsWithSameHash(keyDataPairToRemove);
        }
    }

    private void removePairAndCorrectAllNextKeyDataPairsWithSameHash(IKeyDataPair<Key, Data> keyDataPairToRemove){
        Key keyOfPairToRemove = keyDataPairToRemove.getKey();
        int hashcodeOfKeyOfPairToRemove = getHashcodeOf(keyOfPairToRemove);
        int indexOfPairToRemove = getNormalizedInSizeIndex(hashcodeOfKeyOfPairToRemove);

        removePairFromHashTable(indexOfPairToRemove);
        correctAllNextKeyDataPairsWithSameHash(keyOfPairToRemove, hashcodeOfKeyOfPairToRemove, indexOfPairToRemove);
    }

    private void removePairFromHashTable(int index){
        setPairIntoHashTable(index, null);
        sizeUsed--;
    }

    private void correctAllNextKeyDataPairsWithSameHash(Key keyOfRemovedPair, int hashcodeOfKeyOfRemovedPair,
                                                        int indexOfRemovedPair){
        int normalisedInSizeHashOfRemovedPair = getNormalizedInSizeIndex(hashcodeOfKeyOfRemovedPair);
        int lastPairIndex = indexOfRemovedPair;
        int nextPairIndex = getNextIndex(keyOfRemovedPair, indexOfRemovedPair, getLastTryCount());

        while(!isPlaceEmpty(nextPairIndex)) {
            IKeyDataPair<Key, Data> currentPair = getHashTableElementOfCorrectTypeAt(nextPairIndex);
            if(isNormalisedHashOfKeyOfPairEquals(currentPair, normalisedInSizeHashOfRemovedPair)){
                movePairToNewIndex(currentPair, nextPairIndex, lastPairIndex);

                lastPairIndex = nextPairIndex;
                setLastTryCount(getLastTryCount() + 1);
                nextPairIndex = getNextIndex(keyOfRemovedPair, lastPairIndex, getLastTryCount());
            } else {
                break;
            }
        }
    }

    private void movePairToNewIndex(IKeyDataPair<Key, Data> pair, int currentIndex, int newIndex){
        setPairIntoHashTable(newIndex, pair);
        setPairIntoHashTable(currentIndex, null);
    }

    private boolean isNormalisedHashOfKeyOfPairEquals(IKeyDataPair<Key, Data> pair, Integer expectedHash){
        Key keyOfPair = pair.getKey();
        int normalizedInSizeHashOfKey = getNormalizedInSizeHashcodeOfKey(keyOfPair);
        return expectedHash.equals(normalizedInSizeHashOfKey);
    }

    @Override
    protected final Object[] createNewHashTableFromCurrentWithNewSize(Object[] currentHashTable, int newSize) {
        OpenAddressHashTable<Key, Data> newHashTable = createNewHashTable(newSize);
        iterateThroughOldHashTableAndPopulateNew(newHashTable);
        return newHashTable.getHashTable();
    }

    // Метод должен возвращать новый экземпляр хеш-таблицы типа субкласса, приведённого к данному классу.
    protected abstract OpenAddressHashTable<Key, Data> createNewHashTable(int size);

    private void iterateThroughOldHashTableAndPopulateNew(OpenAddressHashTable<Key, Data> newHashTable){
        //noinspection unchecked
        for (IKeyDataPair<Key, Data> keyDataPair : (IKeyDataPair<Key, Data>[])getHashTable()) {
            newHashTable.insert(keyDataPair);
        }
    }

    private IKeyDataPair<Key, Data> getHashTableElementOfCorrectTypeAt(int index){
        //noinspection unchecked
        return (IKeyDataPair<Key, Data>)getHashTableElementAt(index);
    }
}
