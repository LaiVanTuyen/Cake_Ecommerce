package com.codefresher.entities;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "user_address")
@Builder
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
    private String province;
    private String district;
    private String ward;
    private String detail;
    private String receiverName;
    private String phoneNumber;
        @ManyToOne
        @JoinColumn(name = "user_id")
        @EqualsAndHashCode.Exclude
        @ToString.Exclude
        private User user;
}
