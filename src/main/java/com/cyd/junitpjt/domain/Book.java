package com.cyd.junitpjt.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.cyd.junitpjt.web.dto.response.BookResDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity

public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=50, nullable = false)
    private String title;
    @Column(length=50, nullable = false)
    private String author;

    @Builder
    public Book(Long id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public void update(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public BookResDto toDto(){
        return BookResDto.builder()
            .id(id)
            .title(title)
            .author(author)
            .build();
    }
}
