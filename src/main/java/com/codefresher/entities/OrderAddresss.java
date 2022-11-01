package com.codefresher.entities;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
@Entity
@Table(name = "order_addresss")
public class OrderAddresss {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressID;
    private String province;
    private String district;
    private String village;
    private String detail;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order orderAddress;
}