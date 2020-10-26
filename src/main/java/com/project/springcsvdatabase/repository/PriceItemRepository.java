package com.project.springcsvdatabase.repository;

import com.project.springcsvdatabase.model.PriceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceItemRepository extends JpaRepository<PriceItem, Integer> {
}
