package com.cyd.junitpjt.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.cyd.junitpjt.domain.Book;
import com.cyd.junitpjt.domain.BookRepository;
import com.cyd.junitpjt.service.BookService;
import com.cyd.junitpjt.web.dto.request.BookSaveReqDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

// 통합테스트 (Controller, Service, Repo)
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookApiControllerTest {

    @Autowired  //오토 와이어드로 띄워야한다 , final + requird로 안뜸
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestRestTemplate rt;

    
    private static ObjectMapper om;
    private static HttpHeaders headers;
    
    @BeforeAll
    public static void init(){
        om = new ObjectMapper();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

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
    
    @Test
    public void saveBook_test() throws Exception{
        //given
        BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
        bookSaveReqDto.setTitle("junit");
        bookSaveReqDto.setAuthor("cyd");

        String body = om.writeValueAsString(bookSaveReqDto);
        // System.out.println("==========================");
        // System.out.println(body);
        // System.out.println("==========================");

        //when
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book", HttpMethod.POST, request, String.class);

        //then
        DocumentContext dc = JsonPath.parse(response.getBody());
        String title = dc.read("$.body.title");
        String author = dc.read("$.body.author");

        assertThat(title).isEqualTo("junit");
        assertThat(author).isEqualTo("cyd");
    }

    @Sql("classpath:db/tableInit.sql")
    @Test 
    public void getBookList_test(){
        //given

        //when
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book", HttpMethod.GET, request, String.class);

        //then
        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer code = dc.read("$.code");
        String title = dc.read("$.body.items[0].title");

        assertThat(code).isEqualTo(1);
        assertThat(title).isEqualTo("junit");
    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void getBookOne_test(){
        //given
        Integer id = 1;

        //when
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/"+id, HttpMethod.GET, request, String.class);

        //then
        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer code = dc.read("$.code");
        String title = dc.read("$.body.title");

        assertThat(code).isEqualTo(1);
        assertThat(title).isEqualTo("junit");
    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void deleteBook_test(){
        //given
        Integer id = 1;

        //when
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/"+id, HttpMethod.DELETE, request, String.class);

        //then
        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer code = dc.read("$.code");

        assertThat(code).isEqualTo(1);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void updateBook_test() throws Exception{
        //given
        Integer id = 1;
        BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
        bookSaveReqDto.setTitle("junits");
        bookSaveReqDto.setAuthor("cyds");

        String body = om.writeValueAsString(bookSaveReqDto);

        //when
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/"+id, HttpMethod.PUT, request, String.class);

        //then
        DocumentContext dc = JsonPath.parse(response.getBody());
        String title = dc.read("$.body.title");

        assertThat(title).isEqualTo("junits");
    }

}
