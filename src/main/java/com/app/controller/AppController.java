package com.app.controller;

import com.app.model.dto.AuthorDto;
import com.app.model.dto.BookDto;
import com.app.service.BookService;
import com.app.service.JSONService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AppController {

    private BookService bookService;
    private JSONService jsonService;

    public AppController(BookService bookService, JSONService jsonService) {
        this.bookService = bookService;
        this.jsonService = jsonService;
    }

    @GetMapping("/json")
    public void getJson() {
        jsonService.ReadFile();
    }

    @GetMapping("/book")
    public List<BookDto> getAllBooks() {
        return bookService.getAllBook();
    }

    @GetMapping("/book/{isbn}")
    public BookDto getOneBook(@PathVariable String isbn) {
        return bookService.getOneBookByIsbn(isbn);
    }

    @GetMapping("category/{categoryName}/books")
    public List<BookDto> getALLBooksByCategory(@PathVariable String categoryName) {
        return bookService.getALLBookByCategory(categoryName);
    }

    @GetMapping("/rating")
    public List<AuthorDto> getAllAuthors() {
        return bookService.getAllAuthor();
    }
}
