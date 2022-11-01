package com.codefresher.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class UserAddressDTO {
    private Long addressId;
    private String province;
    private String district;
    private String ward;
    private String detail;
    private String receiverName;
    private String phoneNumber;
    private Long userId;
}
