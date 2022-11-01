package com.codefresher.entities;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;
    private String color;
    private String size;
    private int price;
    private int quantity;

    //n-1 order
    @ManyToOne
    @JoinColumn(name = "order_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Order orderItem;

    //n-1 product
    @ManyToOne
    @JoinColumn(name = "quantity_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private ProductQuantity quantityItem;
}