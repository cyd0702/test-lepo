package com.cyd.junitpjt.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.cyd.junitpjt.domain.Book;
import com.cyd.junitpjt.domain.BookRepository;
import com.cyd.junitpjt.util.MailSender;
import com.cyd.junitpjt.web.dto.request.BookSaveReqDto;
import com.cyd.junitpjt.web.dto.response.BookListResDto;
import com.cyd.junitpjt.web.dto.response.BookResDto;

@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MailSender mailSender;
    
    @Test
    public void 책등록하기_테스트(){
        //given
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("junit");
        dto.setAuthor("cyd");

        //stub 행동정의
        when(bookRepository.save(any())).thenReturn(dto.toEntity());
        when(mailSender.send()).thenReturn(true);

        //when
        BookResDto bookResDto = bookService.책등록(dto);

        //then
    //    assertEquals(dto.getTitle(), bookResDto.getTitle());
    //    assertEquals(dto.getAuthor(), bookResDto.getAuthor());
        assertThat(dto.getTitle()).isEqualTo(bookResDto.getTitle());
        assertThat(dto.getAuthor()).isEqualTo(bookResDto.getAuthor());
    }



    // @Test
    // public void 책등록하기_테스트(){
    //     //given
    //     BookSaveReqDto dto = new BookSaveReqDto();
    //     dto.setTitle("junit");
    //     dto.setAuthor("cyd");

    //     //stub
    //     MailSenderStub mailSenderStub = new MailSenderStub();

    //     //when
    //     BookService bookService = new BookService(bookRepository, mailSenderStub);
    //     BookResDto bookResDto = bookService.책등록(dto);

    //     //then
    //    assertEquals(dto.getTitle(), bookResDto.getTitle());
    //    assertEquals(dto.getAuthor(), bookResDto.getAuthor());

    // }


    @Test
    public void 책목록보기_테스트(){
        //given (파라미터 올 데이터)
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "junit", "cyd"));
        books.add(new Book(2L, "spring", "cyd"));

        //stub (가설)
        when(bookRepository.findAll()).thenReturn(books);

        //when
        BookListResDto bookListResDto = bookService.책목록보기();

        //then
        assertThat(bookListResDto.getItems().get(0).getTitle()).isEqualTo( "junit");
    }

    @Test
    public void 책한건보기_테스트(){
        //given
        Long id = 1L;

        //stub
        Book book = new Book(1L, "junit","cyd");
        Optional<Book> bookOP = Optional.of(book);
        when(bookRepository.findById(id)).thenReturn(bookOP);

        //when
        BookResDto bookResDto = bookService.책한건보기(id);

        //then
        assertThat(bookResDto.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookResDto.getAuthor()).isEqualTo(book.getAuthor());
    }

    @Test
    public void 책수정하기_테스트(){
        //given
        Long id = 1L;
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("jubit");
        dto.setAuthor("cyd");

        //stub
        Book book = new Book(1L, "junit","cyd");
        Optional<Book> bookOP = Optional.of(book);
        when(bookRepository.findById(id)).thenReturn(bookOP);

        //when
        BookResDto bookResDto = bookService.책수정하기(id, dto);

        //then
        assertThat(bookResDto.getTitle()).isEqualTo(dto.getTitle());
    }
}
