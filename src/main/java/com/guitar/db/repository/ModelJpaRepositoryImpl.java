package com.guitar.db.repository;

public class ModelJpaRepositoryImpl implements ModelJpaRepositoryCustom {
    @Override
    public void aCustomMethod() {
        System.out.println("I am a custom method but would do a data operation");
    }
}
