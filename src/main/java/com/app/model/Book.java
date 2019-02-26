package com.app.model;

import lombok.*;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Book {
    @Id
    private String isbn;
    private String title;
    private String subtitle;
    private String publisher;
    private Long publishedDate;
    @Column(columnDefinition = "varchar(3000)")
    private String description;
    private Integer pageCount;
    private String thumbnailUrl;
    private String language;
    private String previewLink;
    private Double averageRating;
    private String[] authors;
    private String[] categories;
}
