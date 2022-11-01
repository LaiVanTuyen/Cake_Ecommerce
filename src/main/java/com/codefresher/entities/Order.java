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
@Table(name = "ordertbl")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private String buyerName;
    private String phoneNumber;
    private String buyingDate;
    private int payTotal;
    private int status;
    private String province;
    private String district;
    private String ward;
    private String detail;

    //n-1 user;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User userOrder;

//    1-n order
    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;
}