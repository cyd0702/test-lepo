package com.cyd.junitpjt.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles("dev")
@DataJpaTest
public class BookRepositoryTest {
    
    @Autowired
    private BookRepository bookRepository;

    //@BeforeAll //테스트 시작전에 한번만 실행
    @BeforeEach //각가의 테스트 시작전에 한번씩 실행
    public void 데이터준비(){
        String title = "junit";
        String author = "cyd";

        Book book = Book.builder()
            .title(title)
            .author(author)
            .build();
        
        bookRepository.save(book);
    }

    //책 등록
    @Test
    public void 책등록_test(){
        //given
        String title="junit5";
        String author = "코딩";

        Book book = Book.builder()
            .title(title)
            .author(author)
            .build();
        
        //when
        Book bookPS = bookRepository.save(book);

        //then
        assertEquals(title, bookPS.getTitle());
        assertEquals(author, bookPS.getAuthor());

        System.out.println("책등록 실행");
    }

    // 책 목록보기
    @Test
    public void 책목록보기_test(){
        //given
        String title = "junit";
        String author = "cyd";
        //when
        List<Book> booksPS =bookRepository.findAll();

        //then
        assertEquals(title, booksPS.get(0).getTitle());
    }

    // 책 한건보기
    @Sql("classpath:db/tableInit.sql")
    @Test
    public void 책한건보기_test(){
        //given
        String title = "junit";
        String author = "cyd";

        //when
        Book bookPS = bookRepository.findById(1L).get();

        //then
        assertEquals(title, bookPS.getTitle());
    }

    // 책 삭제
    @Sql("classpath:db/tableInit.sql")
    @Test
    public void 책삭제_test(){
        //given
        Long id = 1L;

        //when
        bookRepository.deleteById(id);
        
        //then
        Optional<Book> bookPS = bookRepository.findById(id);
        assertFalse(bookPS.isPresent());
       
    }

    //책 수정
    @Sql("classpath:db/tableInit.sql")
    @Test
    public void 책수정_test(){
        //given
        Long id = 1L;
        String title = "junit1";
        String author = "cyd1";
        Book book = new Book(id, title, author);

        //when
        Book bookPS = bookRepository.save(book);
    
        //then
        assertEquals(id, bookPS.getId());
        assertEquals(title, bookPS.getTitle());
        assertEquals(author, bookPS.getAuthor());
       
    }
}
