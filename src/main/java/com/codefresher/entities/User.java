package com.codefresher.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String username;

    private String password;
    private String role;
    private String fullname;
    private String phoneNumber;
    private Integer point;
    private String email;

    //1-n orders
    @OneToMany(mappedBy = "userOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Order> userOrders;


    //1-n user-address
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<UserAddress> userAddressList;

    // 1 - n user-cart
    @OneToMany(mappedBy = "cartUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Cart> userCartList;
}
