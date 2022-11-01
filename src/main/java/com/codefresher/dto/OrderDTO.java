package com.codefresher.dto;

import lombok.*;

import java.sql.Date;
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class OrderDTO {
    private Long orderId;
    private String buyerName;
    private String phoneNumber;
    private Date buyingDate;
    private int payTotal;
    private int status;
    private String province;
    private String district;
    private String ward;
    private String detail;
    private String payTotalCurrency;
}
