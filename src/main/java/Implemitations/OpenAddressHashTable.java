package Implemitations;

import Interfaces.IHashTable;
import Interfaces.IKeyDataPair;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Array;

public abstract class OpenAddressHashTable<Key, Data> extends BaseHashTable<Key, Data> {

    public OpenAddressHashTable(){
        super();
    }

    public OpenAddressHashTable(int size){
        super(size);
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

            if (indexInHashTable.equals(currentIndex)){
                // TODO: Если мы вернулись в начальную позицию, настала необходимость расширить хеш-таблицу
                return;
            }
        }
        setPairIntoHashTable(currentIndex, keyDataPair);
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

    private IKeyDataPair<Key, Data> getHashTableElementOfCorrectTypeAt(int index){
        return (IKeyDataPair<Key, Data>)getHashTableElementAt(index);
    }
}
