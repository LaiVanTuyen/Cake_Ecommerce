package com.codefresher.entities;



import lombok.*;

import javax.persistence.*;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String name;
    private int price;
    private String code;

    //1-n product image
    @OneToMany(mappedBy = "productImage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<ProductImage> productImages;

    //1-n: product quantity
    @OneToMany(mappedBy = "productQuantity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<ProductQuantity> productQuantities;


    //n-1 product promotion
    @ManyToOne
    @JoinColumn(name = "promotion_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Promotion productPromotion;


}
