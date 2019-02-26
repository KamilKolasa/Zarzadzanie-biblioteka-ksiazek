package com.app.service;

import com.app.exception.ExceptionCode;
import com.app.exception.MyException;
import com.app.model.Book;
import com.app.model.dto.AuthorDto;
import com.app.model.dto.BookDto;
import com.app.model.dto.Converter;
import com.app.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {

    private BookRepository bookRepository;
    private Converter converter;

    public BookService(BookRepository bookRepository, Converter converter) {
        this.bookRepository = bookRepository;
        this.converter = converter;
    }

    //    add or update record to database
    public BookDto addOrUpdateBook(BookDto bookDto) {
        try {
            Book book = converter.fromBookDtoToBook(bookDto);
            return converter.fromBookToBookDto(bookRepository.save(book));
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "ADD or UPDATE BOOK");
        }
    }

    //    delete record with database
    public BookDto deleteBook(String isbn) {
        try {
            Book book = bookRepository.getOne(isbn);
            bookRepository.delete(book);
            return converter.fromBookToBookDto(book);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "DELETE BOOK");
        }
    }

    //    download 1 record with database by isbn
    public BookDto getOneBookByIsbn(String isbn) {
        try {
            return bookRepository
                    .findById(isbn)
                    .map(converter::fromBookToBookDto)
                    .orElseThrow(() -> new NullPointerException("NOT FOUND BOOK ISBN " + isbn));
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "get ONE by ISBN BOOK");
        }
    }

    //    download all records with database
    public List<BookDto> getAllBook() {
        try {
            return bookRepository
                    .findAll()
                    .stream()
                    .map(p -> converter.fromBookToBookDto(p))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "get ALL BOOK");
        }
    }

    //    download all records from the database in given category
    public List<BookDto> getALLBookByCategory(String category) {
        try {
            if (category.isEmpty()) {
                return getAllBook();
            } else {
                return bookRepository
                        .findAll()
                        .stream()
                        .filter(c -> Arrays.toString(c.getCategories()).matches(".*" + stringToRegex(category) + ".*"))
                        .map(p -> converter.fromBookToBookDto(p))
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "get ALL by CATEGORY BOOK");
        }
    }

    //    download all authors and ratings (descending)
    public List<AuthorDto> getAllAuthor() {
        try {
            List<AuthorDto> authorList = new ArrayList<>();
            List<BookDto> bookList = getAllBook();
            for (BookDto book : bookList) {
                if (book.getAuthors() != null && book.getAverageRating() != null) {
                    for (String s : book.getAuthors()) {
                        authorList.add(AuthorDto.builder()
                                .author(s)
                                .averageRating(book.getAverageRating())
                                .build());
                    }
                }
            }
            return authorList
                    .stream()
                    .sorted(Comparator.comparing(AuthorDto::getAverageRating).reversed())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "get ALL RATING AUTHOR");
        }
    }

    //    download all categories
    public Set<String> getAllCategories() {
        try {
            Set<String> categories = new TreeSet<>();
            List<BookDto> bookList = getAllBook();
            for (BookDto book : bookList) {
                if (book.getCategories() != null) {
                    for (String s : book.getCategories()) {
                        categories.add(s);
                    }
                }
            }
            return categories;
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "get ALL CATEGORIES");
        }
    }

    //    convert String to not use special characters, Regex
    private static String stringToRegex(String category) {
        String[] charArray = new String[]{"\\\\", "\\^", "\\$", "\\{", "\\}", "\\[", "\\]", "\\(", "\\)", "\\.", "\\*", "\\+", "\\?", "\\|", "\\<", "\\>", "\\-", "\\&"};
        for (String s : charArray) {
            category = category.replaceAll(s, "\\\\" + s);
        }
        return category;
    }
}
