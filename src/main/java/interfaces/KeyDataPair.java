package interfaces;

public interface KeyDataPair<Key, Data> {
    /**
     * Возвращает ключ записи.
     * @return Ключ записи.
     */
    Key getKey();

    /**
     * Возвращает данные записи.
     * @return Данные записи.
     */
    Data getData();

    /**
     * Сравнивает ключ записи с ключом, переданным в качестве аргумента.
     * @param other Ключ для сравнения.
     * @return Результат выполнения equals для ключа записи и other.
     */
    boolean isKeyEqualsTo(Key other);

    /**
     * Сравнивает данные записи с данными, переданными в качестве аргумента.
     * @param other Данные для сравнения.
     * @return Результат выполнения equals для данных записи и other.
     */
    boolean isDataEqualsTo(Data other);
}
