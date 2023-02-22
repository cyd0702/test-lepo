package com.cyd.junitpjt.web.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.cyd.junitpjt.domain.Book;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookSaveReqDto {

    @Size(min= 1, max= 50)
    @NotBlank // null과 공백검사 (spring starter validation)
    private String title;

    @Size(min= 2, max= 20)
    @NotBlank
    private String author;

    public Book toEntity(){
        return Book.builder()
            .title(title)
            .author(author)
            .build();
    }
}
