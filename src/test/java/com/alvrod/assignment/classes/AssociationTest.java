package com.alvrod.assignment.classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AssociationTest {

    @Test
    void shouldGetAssociationFields() {
        Association association = new Association("Some Object");

        assertEquals("Some Object", association.getContent());
        assertEquals(0, association.getAccessCounter());
    }
}