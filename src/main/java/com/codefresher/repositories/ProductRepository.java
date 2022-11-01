package com.codefresher.repositories;

import com.codefresher.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    //get 8 newest product
    List<Product> findTop8ByProductPromotionIsNullOrderByProductIdDesc();

    //get 8 sale product
    List<Product> findTop8ByProductPromotionNotNull();

    Page<Product> findAllByNameContaining(String name, Pageable pageable);
    Page<Product> findAllByCodeLike(String code, Pageable pageable);

    Page<Product> findAllByProductPromotionPromotionId(Long id, Pageable p);

    List<Product> findAllByCodeLike(String code);

    @Transactional
    List<Product> findAllByNameContainingOrCodeContaining(String name, String code);

    @Transactional
    Product findProductByProductId(Long id);

    @Transactional
    List<Product> findAllByProductPromotionPromotionId(Long id);

    List<Product> findAllByProductPromotionNull();

    @Modifying
    @Query("SELECT p from Product p where (p.name LIKE %?1% OR p.code LIKE %?1%) AND p.productPromotion.promotionId = ?2")
    List<Product> getProductIPByName(String name, Long promotionId);

    @Modifying
    @Query("SELECT p FROM Product  p WHERE (p.name LIKE %?1% OR p.code LIKE %?1%) AND p.productPromotion IS NULL")
    List<Product> getProductNIPByName(String name);
}