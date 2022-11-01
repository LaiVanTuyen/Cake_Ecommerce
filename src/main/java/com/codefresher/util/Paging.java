package com.codefresher.util;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Paging {
    private static int PAGE_STEP = 2;

    private boolean nextEnable;
    private boolean prevEnable;
    private int pageSize;
    private int pageNumber;

    private List<PageItem> items = new ArrayList<>();

    public void addPageItem (int from, int to, int pageNumber){
        for (int i = from; i<=to; i++){
            items.add(PageItem.builder()
                            .active(pageNumber != i)
                            .index(i)
                            .build());
        }
    }

    public void last(int pageSize){
        items.add(PageItem.builder()
                        .active(true)
                        .index(pageSize)
                        .build());
    }

    public void first(){
        items.add(PageItem.builder()
                        .index(1)
                        .active(pageNumber != 1)
                        .build());
    }

    public static Paging of(int totalPages, int pageNumber, int pageSize){
        Paging paging = new Paging();
        paging.setPageSize(pageSize);
        paging.setPageNumber(pageNumber);
        paging.setNextEnable(pageNumber != totalPages);
        paging.setPrevEnable(pageNumber != 1);

        if(totalPages <= PAGE_STEP * 2 + 1){
            paging.addPageItem(1, totalPages, pageNumber);
        }
        else if(pageNumber < PAGE_STEP * 2){
            paging.addPageItem(1, PAGE_STEP * 2 + 1, pageNumber);
        }
        else{
            paging.addPageItem(pageNumber - PAGE_STEP, Math.min(pageNumber + PAGE_STEP, totalPages), pageNumber);
        }
        return paging;
        //  (pageNumber >= PAGE_STEP * 2)
    }
}
