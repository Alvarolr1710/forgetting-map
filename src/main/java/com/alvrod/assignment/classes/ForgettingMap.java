package com.alvrod.assignment.classes;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ForgettingMap extends ConcurrentHashMap<Integer, Association> {
    private final int maxSize;

    public ForgettingMap(int maxSize) {
        this.maxSize = maxSize;
    }

    public void addAssociation(int key, Association association) {

        synchronized (this) {
            if (super.mappingCount() >= maxSize) {
                forgetLeastUsed();
            }
        }
        super.putIfAbsent(key, association);
    }

    private void forgetLeastUsed() {
        Association leastAccessed = null;
        Integer leastAccessedKey = null;
        for (Entry<Integer, Association> entry : this.entrySet()) {
            int actualAccessCounter = entry.getValue().getAccessCounter();
            if (Objects.isNull(leastAccessed) || (leastAccessed.getAccessCounter() > actualAccessCounter)) {
                leastAccessedKey = entry.getKey();
                leastAccessed = entry.getValue();
            }
        }
        if (Objects.nonNull(leastAccessed)) {
            super.remove(leastAccessedKey);
        }
    }

    public Association getAssociation(int key) {
        Association association = super.get(key);
        if (Objects.nonNull(association)) {
            association.incrementAccessCounter();
        }
        return association;
    }

}
