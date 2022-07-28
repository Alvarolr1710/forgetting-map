package com.alvrod.assignment.classes;

import java.util.concurrent.atomic.AtomicInteger;

public class Association {

    private Object content;
    private AtomicInteger accessCounter = new AtomicInteger(0);

    public Association( Object content) {
        this.content = content;
    }


    public Object getContent() {
        return content;
    }

    public int getAccessCounter() {
        return accessCounter.intValue();
    }

    public void incrementAccessCounter(){
        accessCounter.getAndIncrement();
    }

    @Override
    public String toString() {
        return "Association{" +
                "content=" + content +
                ", accessCounter=" + accessCounter +
                '}';
    }
}
