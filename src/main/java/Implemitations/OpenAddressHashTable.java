package Implemitations;

import Interfaces.IKeyDataPair;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Array;

public abstract class OpenAddressHashTable<Key, Data> extends BaseHashTable<Key, Data> {
    int sizeUsed;

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
        int indexInHashTable = getNormalizedInSizeHashcodeOfKey(key);

        Integer startIndex = indexInHashTable;
        int wrongNextIndexTries = 0;

        while(!isPlaceEmpty(indexInHashTable)){
            if(isPlaceContainsSameKey(key, indexInHashTable)){
                return getHashTableElementOfCorrectTypeAt(indexInHashTable);
            }
            indexInHashTable = getNextIndex(key, indexInHashTable, wrongNextIndexTries);

            if (startIndex.equals(indexInHashTable)){
                break;
            }
        }
        return null;
    }

    // Метод должен возвращать следующий индекс для ключа key, как если бы произошла коллизия на месте collisionIndex и
    // происходит tryCount попытка получить следующий индекс.
    // Передача tryCount позволяет
    protected abstract int getNextIndex(Key key, int collisionIndex, int tryCount);

    final protected int getNormalizedInSizeIndex(int index){
        return index % _size;
    }

    @Override
    public void remove(Key key) {
        throw new NotImplementedException();
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
