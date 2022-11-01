package com.codefresher.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class PromotionDTO {
    private String title;
    private String description;
    private String imgName;
    private int discount;
    private MultipartFile image;
}