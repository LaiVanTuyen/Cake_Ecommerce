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
@Table(name = "product_quantity")
public class ProductQuantity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quantityId;
    private String color;
    private String size;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Product productQuantity;

    @OneToMany(mappedBy = "cartProductQuantity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Cart> productQuantityCartList;

    //1-n product order item
    @OneToMany(mappedBy = "quantityItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<OrderItem> orderItems;
}