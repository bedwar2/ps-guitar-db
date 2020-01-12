package com.guitar.db.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guitar.db.model.ModelType;

@Repository
public interface ModelTypeRepository extends JpaRepository<ModelType, Long> {
}
