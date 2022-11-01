package com.codefresher.entities;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User cartUser;

    @ManyToOne
    @JoinColumn(name = "quantity_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private ProductQuantity cartProductQuantity;
}
