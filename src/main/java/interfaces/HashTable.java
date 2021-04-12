package interfaces;

public interface HashTable<Key, Data> {
    /**
     * Вставляет новую запись в хеш-таблицу.
     * @param key Ключ записи.
     * @param data Данные записи.
     */
    void insert(Key key, Data data);

    /**
     * Вставляет новую запись в хеш-таблицу.
     * @param keyDataPair Запись для вставки.
     */
    void insert(KeyDataPair<Key, Data> keyDataPair);

    /**
     * Производит поиск и получение записи из хеш-таблицы по указанному ключу.
     * @param key Ключ записи.
     * @return Данные найденной записи или null, если запись не была найдена.
     */
    Data get(Key key);

    /**
     * Определяет, содержится ли запись с заданным ключом в хеш-таблице.
     * @param key Ключ записи для поиска.
     * @return True, если запись с заданным ключом содержится в хеш-таблице, иначе - false.
     */
    boolean containsKey(Key key);

    /**
     * Определяет, содержится ли запись с заданными данными в хеш-таблице.
     * @param data Данные записи для поиска.
     * @return True, если запись с заданным данными содержится в хеш-таблице, иначе - false.
     */
    boolean containsData(Data data);

    /**
     * Удаляет запись из хеш-таблицу по укзанному ключу.
     * @param key Ключ записи для удаления.
     */
    void remove(Key key);
}
