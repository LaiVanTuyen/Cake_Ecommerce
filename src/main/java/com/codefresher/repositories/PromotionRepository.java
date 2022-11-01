package com.codefresher.repositories;

import com.codefresher.entities.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long>{

    public List<Promotion> findTop3ByOrderByPromotionIdDesc();
    public Promotion findTop1ByOrderByPromotionIdAsc();
    public List<Promotion> findAll();
    public Promotion findByPromotionId(Long id);

}