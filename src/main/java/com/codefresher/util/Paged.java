package com.codefresher.util;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paged<T> {
    private Page<T> page;
    private Paging paging;
}
