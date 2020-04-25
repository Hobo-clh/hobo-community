package com.ccsu.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 华华
 */
@Data
public class PaginationDTO<T> {
    private List<T> ObjectList;
    /**
     * 展示前一页按钮
     */
    private Boolean showPrevious;
    /**
     * 展示首页按钮
     */
    private Boolean showFirstPage;
    /**
     * 展示下一页按钮
     */
    private Boolean showNext;
    /**
     * 展示最后一页按钮
     */
    private Boolean showEndPage;
    /**
     * 当前页
     */
    private Integer page;
    /**
     * 总页数
     */
    private Integer totalPage;
    private List<Integer> pages;


    public void setPagination(Integer totalCount, Integer page, Integer size) {
        this.page = page;
        totalPage = 0;
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if(totalPage==0){
            totalPage=1;
        }
        if(page>totalPage){
            page=totalPage;
        }
        if(page<1){
            page=1;
        }
        pages = new ArrayList<>();
        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                pages.add(0,page-i);
            }
            if(page+i<=totalPage){
                pages.add(page+i);
            }
        }

        //是否展示上一页
        if (page == 1) {
            showPrevious = false;
        } else {
            showPrevious = true;
        }

        //是否展示下一页
        if (page == totalPage) {
            showNext = false;
        } else {
            showNext = true;
        }
        //是否展示第一页
        if (pages.contains(1)) {
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }

        //是否展示最后一页
        if (pages.contains(totalPage)) {
            showEndPage = false;
        } else {
            showEndPage = true;
        }
    }
}
