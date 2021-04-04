package Implemitations;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ChainedHashTableTest {

    final Random random = new Random();

    @Test
    void insertAndGet() {
        ChainedHashTable<Integer, String> testHashTable = new ChainedHashTable<Integer, String>();

        // Простая запись:
        int randInt_1 = random.nextInt(),
                randInt_2 = random.nextInt(),
                randInt_3 = random.nextInt(),
                randInt_4 = random.nextInt(),
                randInt_5 = random.nextInt(),
                randInt_6 = random.nextInt();

        String string_1 = "Eggs",
                string_2 = "Butter",
                string_3 = "Ham";

        testHashTable.insert(randInt_1, string_1);
        testHashTable.insert(randInt_2, string_2);
        testHashTable.insert(randInt_3, string_3);

        assertEquals(string_1, testHashTable.get(randInt_1));
        assertEquals(string_2, testHashTable.get(randInt_2));
        assertEquals(string_3, testHashTable.get(randInt_3));

        // Использование пар:
        KeyDataPair<Integer, String> pair1 = new KeyDataPair<Integer, String>(randInt_4, string_1);
        KeyDataPair<Integer, String> pair2 = new KeyDataPair<Integer, String>(randInt_5, string_2);
        KeyDataPair<Integer, String> pair3 = new KeyDataPair<Integer, String>(randInt_6, string_3);

        testHashTable.insert(pair1);
        testHashTable.insert(pair2);
        testHashTable.insert(pair3);

        assertEquals(string_1, testHashTable.get(randInt_4));
        assertEquals(string_2, testHashTable.get(randInt_5));
        assertEquals(string_3, testHashTable.get(randInt_6));
    }

    @Test
    void remove() {
        ChainedHashTable<Integer, String> testHashTable = new ChainedHashTable<Integer, String>();

        // Простая запись:
        int randInt_1 = random.nextInt(),
                randInt_2 = random.nextInt(),
                randInt_3 = random.nextInt(),
                randInt_4 = random.nextInt(),
                randInt_5 = random.nextInt(),
                randInt_6 = random.nextInt();

        String string_1 = "Eggs",
                string_2 = "Butter",
                string_3 = "Ham";

        testHashTable.insert(randInt_1, string_1);
        testHashTable.insert(randInt_2, string_2);
        testHashTable.insert(randInt_3, string_3);
        testHashTable.insert(randInt_4, string_1);
        testHashTable.insert(randInt_5, string_2);
        testHashTable.insert(randInt_6, string_3);

        assertEquals(string_1, testHashTable.get(randInt_1));
        assertEquals(string_2, testHashTable.get(randInt_2));
        assertEquals(string_3, testHashTable.get(randInt_3));
        assertEquals(string_1, testHashTable.get(randInt_4));
        assertEquals(string_2, testHashTable.get(randInt_5));
        assertEquals(string_3, testHashTable.get(randInt_6));

        testHashTable.remove(randInt_1);
        assertNull(testHashTable.get(randInt_1));
        assertEquals(string_2, testHashTable.get(randInt_2));
        assertEquals(string_3, testHashTable.get(randInt_3));
        assertEquals(string_1, testHashTable.get(randInt_4));
        assertEquals(string_2, testHashTable.get(randInt_5));
        assertEquals(string_3, testHashTable.get(randInt_6));

        testHashTable.remove(randInt_4);
        assertNull(testHashTable.get(randInt_1));
        assertEquals(string_2, testHashTable.get(randInt_2));
        assertEquals(string_3, testHashTable.get(randInt_3));
        assertNull(testHashTable.get(randInt_4));
        assertEquals(string_2, testHashTable.get(randInt_5));
        assertEquals(string_3, testHashTable.get(randInt_6));
    }

    @Test
    void bigRemove(){
        int itemsToCreate = 100000;
        int itemsToDelete = 25000;

        ChainedHashTable<Integer, String> testHashTable = new ChainedHashTable<Integer, String>();


        Set<Integer> indexesToRemove = new HashSet<Integer>();
        for(int i = 0; i < itemsToDelete; i++){
            indexesToRemove.add(Math.abs(random.nextInt()) % itemsToCreate);
        }

        // Добавляем элементы:
        for (int i = 0; i < itemsToCreate; i++) {
            testHashTable.insert(i, "abc" + i);
        }
        // Удаляем элементы:
        for (int itemToRemove : indexesToRemove) {
            testHashTable.remove(itemToRemove);
        }
        // Пытаемся проверить элементы:
        for (int i = 0; i < itemsToCreate; i++) {
            if(indexesToRemove.contains(i)){
                assertNull(testHashTable.get(i));
            } else {
                assertEquals("abc"+i, testHashTable.get(i));
            }
        }
    }

    @Test
    void rehash() {
        ChainedHashTable<Integer, String> testHashTable = new ChainedHashTable<Integer, String>(3);

        // Простая запись:
        int randInt_1 = random.nextInt(),
                randInt_2 = random.nextInt(),
                randInt_3 = random.nextInt(),
                randInt_4 = random.nextInt(),
                randInt_5 = random.nextInt(),
                randInt_6 = random.nextInt();

        String string_1 = "Eggs",
                string_2 = "Butter",
                string_3 = "Ham";

        testHashTable.insert(randInt_1, string_1);
        testHashTable.insert(randInt_2, string_2);
        testHashTable.insert(randInt_3, string_3);

        assertEquals(string_1, testHashTable.get(randInt_1));
        assertEquals(string_2, testHashTable.get(randInt_2));
        assertEquals(string_3, testHashTable.get(randInt_3));

        testHashTable.insert(randInt_4, string_1);
        testHashTable.insert(randInt_5, string_2);
        testHashTable.insert(randInt_6, string_3);

        assertEquals(string_1, testHashTable.get(randInt_1));
        assertEquals(string_2, testHashTable.get(randInt_2));
        assertEquals(string_3, testHashTable.get(randInt_3));
        assertEquals(string_1, testHashTable.get(randInt_4));
        assertEquals(string_2, testHashTable.get(randInt_5));
        assertEquals(string_3, testHashTable.get(randInt_6));
    }
}