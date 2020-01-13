package com.guitar.db.repository;

import com.guitar.db.model.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ModelJpaRepository extends JpaRepository<Model, Long>, ModelJpaRepositoryCustom {
    List<Model> findByPriceGreaterThanEqualAndPriceLessThanEqual(BigDecimal low,  BigDecimal high);

    List<Model> findByModelTypeNameIn(List<String> types);

    //Below is a Native Query
    //@Query(value = "select * from Model m where m.price >= :lowest and m.price <= :highest and m.woodType like :wood", nativeQuery = true)
    //Below uses JPQL
    @Query(value = "select m from Model m where m.price >= :lowest and m.price <= :highest and m.woodType like :wood", nativeQuery = false)
    Page<Model> queryByPriceRangeAndWoodType(@Param("lowest") BigDecimal lowest, @Param("highest") BigDecimal highest,
                                             @Param("wood") String wood,
                                             Pageable page);

    List<Model> findAllModelsByType(@Param("name") String name);
}
