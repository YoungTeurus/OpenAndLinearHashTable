package Implemitations;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class SpeedTest {
    @Test
    void speedTest(){
        LinearOpenAddressHashtable<String, Integer> linearHashTable = new LinearOpenAddressHashtable<String, Integer>();
        ChainedHashTable<String, Integer> chainedHashTable = new ChainedHashTable<String, Integer>();
        HashMap<String, Integer> standartHashTable = new HashMap<String, Integer>();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++){
            linearHashTable.insert(i + "abs", i);
        }
        System.out.println("Вставка 100000 в хештаблицу с линейным пробированием за " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++){
            chainedHashTable.insert(i + "abs", i);
        }
        System.out.println("Вставка 100000 в хештаблицу с цепочками за " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++){
            standartHashTable.put(i + "abs", i);
        }
        System.out.println("Вставка 100000 в стандартную хештаблицу за " + (System.currentTimeMillis() - start));

        System.out.println();

        start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++){
            assertEquals(i, linearHashTable.get(i + "abs"));
        }
        System.out.println("Доставание 100000 из хештаблицы с линейным пробированием за " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++){
            assertEquals(i, chainedHashTable.get(i + "abs"));
        }
        System.out.println("Доставание 100000 из хештаблицы с цепочками за " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++){
            assertEquals(i, standartHashTable.get(i + "abs"));
        }
        System.out.println("Доставание 100000 из стандартной хештаблицы за " + (System.currentTimeMillis() - start));

        System.out.println();

        start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++){
            linearHashTable.remove(i + "abs");
        }
        System.out.println("Удаление 100000 из хештаблицы с линейным пробированием за " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++){
           chainedHashTable.remove(i + "abs");
        }
        System.out.println("Удаление 100000 из хештаблицы с цепочками за " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++){
            standartHashTable.remove(i + "abs");
        }
        System.out.println("Удаление 100000 из стандартной хештаблицы за " + (System.currentTimeMillis() - start));

        // Проверка удаления:
        for (int i = 0; i < 100000; i++){
            assertNull(linearHashTable.get(i + "abs"));
        }
        for (int i = 0; i < 100000; i++){
            assertNull(chainedHashTable.get(i + "abs"));
        }
        for (int i = 0; i < 100000; i++){
            assertNull(standartHashTable.get(i + "abs"));
        }
    }
}