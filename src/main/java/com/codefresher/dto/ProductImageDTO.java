package com.codefresher.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ProductImageDTO {
    private Long productId;
    private MultipartFile[] imageFile;
    private List<String> colors;
}
