package com.codefresher.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class UserDTO {
    private Long userId;
    private String password;
    private String role;
    private String fullname;
    private String phoneNumber;
    private Integer point;
}
