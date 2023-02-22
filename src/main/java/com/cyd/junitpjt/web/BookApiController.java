package com.cyd.junitpjt.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.hibernate.event.spi.PreUpdateEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cyd.junitpjt.service.BookService;
import com.cyd.junitpjt.web.dto.request.BookSaveReqDto;
import com.cyd.junitpjt.web.dto.response.BookListResDto;
import com.cyd.junitpjt.web.dto.response.BookResDto;
import com.cyd.junitpjt.web.dto.response.CMResDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
public class BookApiController {

    private final BookService bookService;
    
    // 1. 책등록
    @PostMapping("/api/v1/book")
    public ResponseEntity<?> saveBook(@RequestBody @Valid BookSaveReqDto bookSaveReqDto, BindingResult bindingResult){

        // aop 해야함
        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = new HashMap<>();
            for(FieldError fe : bindingResult.getFieldErrors()){
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            }
            System.out.println("=============================");
            System.out.println(errorMap.toString());
            System.out.println("========================");

            throw new RuntimeException(errorMap.toString()); // global exception 으로 보낸다
        }

        BookResDto bookResDto = bookService.책등록(bookSaveReqDto);
        //CMResDto<?> cmResDto = CMResDto.builder().code(1).msg("글저장성공").body(bookResDto).build();
        return new ResponseEntity<>(CMResDto.builder().code(1).msg("글저장성공").body(bookResDto).build(), HttpStatus.CREATED); // 201 = insert
    }

    // 2. 책 목록보기
    @GetMapping("/api/v1/book")
    public ResponseEntity<?> getBookList(){
        BookListResDto bookListResDto = bookService.책목록보기();
        return new ResponseEntity<>(CMResDto.builder().code(1).msg("글 목록보기 성공").body(bookListResDto).build(), HttpStatus.OK); //200 = ok
    }

    // 3. 책 한건보기
    @GetMapping("/api/v1/book/{id}")
    public ResponseEntity<?> getbookOne(@PathVariable Long id){
       BookResDto bookResDto = bookService.책한건보기(id);
       return new ResponseEntity<>(CMResDto.builder().code(1).msg("글 한건보기").body(bookResDto).build(), HttpStatus.OK); //200 = ok
    }

    // 4. 책 삭제하기
    @DeleteMapping("/api/v1/book/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id){
        bookService.책삭제하기(id);
        return new ResponseEntity<>(CMResDto.builder().code(1).msg("글 삭제하기").body(null).build(), HttpStatus.OK); //200
    }

    // 5. 책 수정하기
    @PutMapping("/api/v1/book/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody @Valid BookSaveReqDto bookSaveReqDto, BindingResult bindingResult){

        // aop 해야함
        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = new HashMap<>();
            for(FieldError fe : bindingResult.getFieldErrors()){
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            }
            System.out.println("=============================");
            System.out.println(errorMap.toString());
            System.out.println("========================");

            throw new RuntimeException(errorMap.toString()); // global exception 으로 보낸다
        }

        BookResDto bookResDto =  bookService.책수정하기(id, bookSaveReqDto);
        return new ResponseEntity<>(CMResDto.builder().code(1).msg("글 수정하기 성공").body(bookResDto).build(), HttpStatus.OK); //200
    }
}
