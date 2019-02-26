package com.app.service;

import com.app.exception.ExceptionCode;
import com.app.exception.MyException;
import com.app.model.dto.BookDto;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class JSONService {
    private ServletContext servletContext;
    @Value("classpath:static/file/books.json")
    private String filePath;
    private BookService bookService;

    public JSONService(ServletContext servletContext, BookService bookService) {
        this.servletContext = servletContext;
        this.bookService = bookService;
    }

    public void ReadFile() {
        JSONParser parser = new JSONParser();

        try {
            File file = ResourceUtils.getFile("classpath:static/file/books.json");
            FileReader reader = new FileReader(file);
            Object object = parser.parse(reader);

            JSONObject jsonObject = (JSONObject) object;
            JSONArray bookList = (JSONArray) jsonObject.get("items");
            for (Object book : bookList) {
                JSONObject jsonBook = (JSONObject) book;
                JSONObject volumeInfo = (JSONObject) jsonBook.get("volumeInfo");
                JSONObject imageLinks = (JSONObject) volumeInfo.get("imageLinks");

//                insert isbn_13, if no isbn_13, insertion id
                JSONArray industryList = (JSONArray) volumeInfo.get("industryIdentifiers");
                String isbn = null;
                for (Object industry : industryList) {
                    JSONObject jsonIndustry = (JSONObject) industry;
                    if (jsonIndustry.get("type").equals("ISBN_13")) {
                        isbn = jsonIndustry.get("identifier").toString();
                    }
                }
                if (isbn == null) {
                    isbn = jsonBook.get("id").toString();
                }

//                Convert date to UNIX timestamp
                Long publishedDate = null;
                if (volumeInfo.get("publishedDate") != null) {
                    if (volumeInfo.get("publishedDate").toString().matches("\\d{4}-\\d{2}-\\d{2}")) {
                        publishedDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(volumeInfo.get("publishedDate").toString()).getTime() / 1000L;
                    } else {
                        publishedDate = new java.text.SimpleDateFormat("yyyy").parse(volumeInfo.get("publishedDate").toString()).getTime() / 1000L;
                    }
                }

//                add to database
                bookService.addOrUpdateBook(BookDto.builder()
                        .isbn(isbn)
                        .title(volumeInfo.get("title") != null ? volumeInfo.get("title").toString() : null)
                        .subtitle(volumeInfo.get("subtitle") != null ? volumeInfo.get("subtitle").toString() : null)
                        .publisher(volumeInfo.get("publisher") != null ? volumeInfo.get("publisher").toString() : null)
                        .publishedDate(publishedDate)
                        .description(volumeInfo.get("description") != null ? volumeInfo.get("description").toString() : null)
                        .pageCount(volumeInfo.get("pageCount") != null ? Integer.parseInt(volumeInfo.get("pageCount").toString()) : null)
                        .thumbnailUrl(imageLinks.get("thumbnail") != null ? imageLinks.get("thumbnail").toString() : null)
                        .language(volumeInfo.get("language") != null ? volumeInfo.get("language").toString() : null)
                        .previewLink(volumeInfo.get("previewLink") != null ? volumeInfo.get("previewLink").toString() : null)
                        .averageRating(volumeInfo.get("averageRating") != null ? Double.parseDouble(volumeInfo.get("averageRating").toString()) : null)
                        .authors(getArrays((JSONArray) volumeInfo.get("authors")))
                        .categories(getArrays((JSONArray) volumeInfo.get("categories")))
                        .build());
            }

            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    //    download arrays
    private static String[] getArrays(JSONArray jsonArray) {
        String[] arr = null;
        if (jsonArray != null) {
            List<String> authorsList = new ArrayList<>();
            JSONArray authorsJson = jsonArray;
            Iterator<String> iterator = authorsJson.iterator();
            while (iterator.hasNext()) {
                authorsList.add(iterator.next());
            }
            arr = new String[jsonArray.size()];
            for (int i = 0; i < authorsList.size(); i++) {
                arr[i] = authorsList.get(i);
            }
        }
        return arr;
    }
}
