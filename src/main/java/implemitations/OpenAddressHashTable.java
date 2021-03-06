package implemitations;

import interfaces.KeyDataPair;

import java.lang.reflect.Array;

public abstract class OpenAddressHashTable<Key, Data> extends BaseHashTable<Key, Data> {
    private class KeyDataPairIndexCombo{
        KeyDataPair<Key, Data> keyDataPair;
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
        return (Object[]) Array.newInstance(KeyDataPair.class, size);
    }

    // Паттерн "Шаблонный метод": субкласс должен переопределить, как находить новый индекс элемента при коллизии.
    @Override
    protected void placePairIntoHashTable(Key key, Integer indexInHashTable, KeyDataPair<Key, Data> keyDataPair) {
        Integer currentIndex = indexInHashTable;
        resetLastTryCount();

        while(!isFreeOrSameKeyPlace(key, currentIndex)){
            currentIndex = getNextIndex(key, indexInHashTable);
            incrementLastTryCount();

            // TODO: Если мы вернулись в начальную позицию, настала необходимость расширить хеш-таблицу?
            if (indexInHashTable.equals(currentIndex)){
                rehash(_size * 2);
                placePairIntoHashTable(key, indexInHashTable, keyDataPair);
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

    protected KeyDataPair<Key, Data> getPairByKey(Key key){
        KeyDataPairIndexCombo pairIndexCombo = getPairAndItsIndexByKey(key);
        return pairIndexCombo.keyDataPair;
    }

    private KeyDataPairIndexCombo getPairAndItsIndexByKey(Key key){
        KeyDataPairIndexCombo pairIndexCombo = new KeyDataPairIndexCombo();

        int indexInHashTable = getNormalizedInSizeHashcodeOfKey(key);

        Integer startIndex = indexInHashTable;
        resetLastTryCount();

        while(!isPlaceEmpty(indexInHashTable)) {
            if (isPlaceContainsSameKey(key, indexInHashTable)) {
                pairIndexCombo.keyDataPair = getHashTableElementOfCorrectTypeAt(indexInHashTable);
                pairIndexCombo.indexOfPair = indexInHashTable;
                break;
            }

            indexInHashTable = getNextIndex(key, startIndex);
            incrementLastTryCount();

            if (startIndex.equals(indexInHashTable)) {
                break;
            }
        }

        return pairIndexCombo;
    }

    @Override
    protected KeyDataPair<Key, Data> getPairByData(Data data) {
        KeyDataPairIndexCombo pairIndexCombo = getPairAndItsIndexByData(data);
        return pairIndexCombo.keyDataPair;
    }

    private KeyDataPairIndexCombo getPairAndItsIndexByData(Data data){
        KeyDataPairIndexCombo pairIndexCombo = new KeyDataPairIndexCombo();

        for(int index = 0; index < _size; index++){
            KeyDataPair<Key, Data> currentPair = getHashTableElementOfCorrectTypeAt(index);
            if (currentPair != null && currentPair.isDataEqualsTo(data)){
                pairIndexCombo.keyDataPair = currentPair;
                pairIndexCombo.indexOfPair = index;
                break;
            }
        }

        return pairIndexCombo;
    }

    // Метод должен возвращать следующий индекс для ключа key, как если бы произошла коллизия на месте collisionIndex и
    // происходит tryCount попытка получить следующий индекс.
    // Передача tryCount позволяет
    protected abstract int getNextIndex(Key key, int collisionIndex);

    final protected int getNormalizedInSizeIndex(int index){
        return index % _size;
    }

    protected int getLastTryCount(){
        return lastTryCount;
    }

    private void resetLastTryCount(){
        lastTryCount = 0;
    }

    private void incrementLastTryCount(){
        lastTryCount++;
    }

    @Override
    public void remove(Key key) {
        KeyDataPairIndexCombo keyDataPairToRemoveIndexCombo = getPairAndItsIndexByKey(key);

        KeyDataPair<Key, Data> keyDataPairToRemove = keyDataPairToRemoveIndexCombo.keyDataPair;
        int indexOfPairToRemove = keyDataPairToRemoveIndexCombo.indexOfPair;

        if(keyDataPairToRemove != null){
            removePairAndCorrectAllNextKeyDataPairsWithSameHash(keyDataPairToRemove, indexOfPairToRemove);
        }
    }

    private void removePairAndCorrectAllNextKeyDataPairsWithSameHash(KeyDataPair<Key, Data> keyDataPairToRemove, int indexOfPairToRemove){
        Key keyOfPairToRemove = keyDataPairToRemove.getKey();
        int hashcodeOfKeyOfPairToRemove = getHashcodeOf(keyOfPairToRemove);

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
        int nextPairIndex = getNextIndex(keyOfRemovedPair, indexOfRemovedPair);

        while(!isPlaceEmpty(nextPairIndex)) {
            KeyDataPair<Key, Data> currentPair = getHashTableElementOfCorrectTypeAt(nextPairIndex);
            if(isNormalisedHashOfKeyOfPairEquals(currentPair, normalisedInSizeHashOfRemovedPair)){
                movePairToNewIndex(currentPair, nextPairIndex, lastPairIndex);

                lastPairIndex = nextPairIndex;
                incrementLastTryCount();
                nextPairIndex = getNextIndex(keyOfRemovedPair, lastPairIndex);
            } else {
                break;
            }
        }
    }

    private void movePairToNewIndex(KeyDataPair<Key, Data> pair, int currentIndex, int newIndex){
        setPairIntoHashTable(newIndex, pair);
        setPairIntoHashTable(currentIndex, null);
    }

    private boolean isNormalisedHashOfKeyOfPairEquals(KeyDataPair<Key, Data> pair, Integer expectedHash){
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
        for (KeyDataPair<Key, Data> keyDataPair : (KeyDataPair<Key, Data>[])getHashTable()) {
            newHashTable.insert(keyDataPair);
        }
    }

    private KeyDataPair<Key, Data> getHashTableElementOfCorrectTypeAt(int index){
        //noinspection unchecked
        return (KeyDataPair<Key, Data>)getHashTableElementAt(index);
    }
}
