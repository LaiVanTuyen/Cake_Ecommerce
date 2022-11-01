package com.codefresher.entities;


import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "product_image")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;
    private String name;
    private String color;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @ToString.Exclude
    private Product productImage;

}