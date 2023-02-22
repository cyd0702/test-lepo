package com.cyd.junitpjt.web.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BookListResDto {
    List<BookResDto> items;

    @Builder
    public BookListResDto(List<BookResDto> bookList) {
        this.items = bookList;
    }
}
