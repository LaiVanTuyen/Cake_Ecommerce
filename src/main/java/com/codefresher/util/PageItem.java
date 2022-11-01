package com.codefresher.util;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageItem {
    private int index;
    private boolean active;
}
