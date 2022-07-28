package com.alvrod.assignment.classes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ForgettingMapTest {

    @Test
    void shouldAddAnAssociation() {
        ForgettingMap forgettingMap = new ForgettingMap(5);
        Association newAssociation = new Association("SomeRandomValue");
        forgettingMap.addAssociation(0, newAssociation);
        Association existingAssociation = forgettingMap.getAssociation(0);
        assertEquals(newAssociation, existingAssociation);
    }

    @Test
    void shouldIncrementTheAccessCounterOfAnAssociationWhenFetch() {
        ForgettingMap forgettingMap = new ForgettingMap(5);
        Association newAssociation = new Association("SomeRandomValue");
        int oldAccessCounter = newAssociation.getAccessCounter();
        forgettingMap.addAssociation(0, newAssociation);
        Association existingAssociation = forgettingMap.getAssociation(0);
        assertEquals(0, oldAccessCounter);
        assertEquals(1, existingAssociation.getAccessCounter());
    }

    @Test
    void shouldForgetLeastUsedValueWhenLimitReached() {
        ForgettingMap forgettingMap = new ForgettingMap(2);
        forgettingMap.addAssociation(0, new Association("SomeRandomValue"));

        forgettingMap.getAssociation(0);

        forgettingMap.addAssociation(1, new Association("SomeRandomValue2"));
        forgettingMap.addAssociation(2, new Association("randomValue3"));
        forgettingMap.addAssociation(3, new Association("randomValue5"));

        assertEquals(2, forgettingMap.size());
        assertNotNull(forgettingMap.get(0));
        assertNotNull(forgettingMap.get(3));
    }

    @MethodSource
    @ParameterizedTest(name = "{0}")
    public void shouldTestConcurrencyForGettingAndAddingAssociations(int numberOfThreads) throws InterruptedException {
        ForgettingMap forgettingMap = new ForgettingMap(3);

        forgettingMap.addAssociation(0, new Association("SomeRandomValue"));
        forgettingMap.getAssociation(0);
        forgettingMap.addAssociation(1, new Association("SomeRandomValue2"));

        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            service.submit(() -> {
                forgettingMap.addAssociation(numberOfThreads, new Association("Concurrent Value 1"));
                forgettingMap.getAssociation(1);
                forgettingMap.addAssociation(numberOfThreads + 1, new Association("Concurrent Value 2"));
                latch.countDown();
            });
        }
        latch.await();
        System.out.println(forgettingMap);
        assertEquals(3, forgettingMap.size());
        assertEquals(numberOfThreads, forgettingMap.get(1).getAccessCounter());
    }

    static Stream<Integer> shouldTestConcurrencyForGettingAndAddingAssociations() {
        return Stream.of(
                1, 10, 20, 50, 100, 200, 500);
    }

}