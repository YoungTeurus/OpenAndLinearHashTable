package Implemitations;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SpeedTest {
    @Test
    void speedTest(){
        LinearOpenAddressHashtable<String, Integer> linearHashTable = new LinearOpenAddressHashtable<String, Integer>(150000);
        ChainedHashTable<String, Integer> chainedHashTable = new ChainedHashTable<String, Integer>(1024);
        HashMap<String, Integer> standartHashTable = new HashMap<String, Integer>(150000);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++){
            linearHashTable.insert(String.valueOf(i + "abs"), i);
        }
        System.out.println("Вставка 100000 в хештаблицу с линейным пробированием за " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++){
            chainedHashTable.insert(String.valueOf(i + "abs"), i);
        }
        System.out.println("Вставка 100000 в хештаблицу с цепочками за " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++){
            standartHashTable.put(String.valueOf(i + "abs"), i);
        }
        System.out.println("Вставка 100000 в стандартную хештаблицу за " + (System.currentTimeMillis() - start));

        System.out.println();

        start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++){
            assertEquals(i, linearHashTable.get(String.valueOf(i + "abs")));
        }
        System.out.println("Доставание 100000 из хештаблицы с линейным пробированием за " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++){
            assertEquals(i, chainedHashTable.get(String.valueOf(i + "abs")));
        }
        System.out.println("Доставание 100000 из хештаблицы с цепочками за " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++){
            assertEquals(i, standartHashTable.get(String.valueOf(i + "abs")));
        }
        System.out.println("Доставание 100000 из стандартной хештаблицы за " + (System.currentTimeMillis() - start));
    }
}