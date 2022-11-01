package com.codefresher.entities;



import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "promotion")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promotionId;
    private String title;
    private String description;
    private String imgName;
    private int discount;

    @OneToMany(mappedBy = "productPromotion")
    @EqualsAndHashCode.Exclude
    private List<Product> promotionProducts;

}