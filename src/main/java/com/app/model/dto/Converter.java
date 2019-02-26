package com.app.model.dto;

import com.app.model.Book;
import org.springframework.stereotype.Component;

@Component
public class Converter {

    public BookDto fromBookToBookDto(Book book) {
        return book == null ? null : BookDto.builder()
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .subtitle(book.getSubtitle())
                .publisher(book.getPublisher())
                .publishedDate(book.getPublishedDate())
                .description(book.getDescription())
                .pageCount(book.getPageCount())
                .thumbnailUrl(book.getThumbnailUrl())
                .language(book.getLanguage())
                .previewLink(book.getPreviewLink())
                .averageRating(book.getAverageRating())
                .authors(book.getAuthors())
                .categories(book.getCategories())
                .build();
    }

    public Book fromBookDtoToBook(BookDto bookDto) {
        return bookDto == null ? null : Book.builder()
                .isbn(bookDto.getIsbn())
                .title(bookDto.getTitle())
                .subtitle(bookDto.getSubtitle())
                .publisher(bookDto.getPublisher())
                .publishedDate(bookDto.getPublishedDate())
                .description(bookDto.getDescription())
                .pageCount(bookDto.getPageCount())
                .thumbnailUrl(bookDto.getThumbnailUrl())
                .language(bookDto.getLanguage())
                .previewLink(bookDto.getPreviewLink())
                .averageRating(bookDto.getAverageRating())
                .authors(bookDto.getAuthors())
                .categories(bookDto.getCategories())
                .build();
    }
}
