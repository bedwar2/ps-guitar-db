package com.guitar.db.repository;

import com.guitar.db.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationJpaRepository  extends JpaRepository<Location, Long> {
    List<Location> findByStateLike(String stateName);
    List<Location> findByStateStartingWithIgnoreCase(String stateName);
    List<Location> findByStateNotLikeOrderByStateAsc(String stateName);

    List<Location> findByStateOrCountry(String State, String Country);
    List<Location> findByStateAndCountry(String State, String Country);
    List<Location> findByStateIn(List<String> states);
}
