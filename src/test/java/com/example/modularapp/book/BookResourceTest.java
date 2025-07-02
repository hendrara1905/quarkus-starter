package com.example.modularapp.book;

import com.example.auth.TestAuthUtil;
import com.example.modularapp.author.Author;
import com.example.modularapp.author.AuthorRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class BookResourceTest {
    @Inject
    BookRepository bookRepository;

    @Inject
    AuthorRepository authorRepository;

    @Inject
    TestAuthUtil testAuthUtil;

    private Long authorId;


    @BeforeEach
    @Transactional
    public void setUp() {
        //bookRepository.deleteAll(); // ini akan membersihkan semua data dari tabel Book

        Author author = new Author();
        author.name = "Test Author";
        authorRepository.persist(author);
        authorId = author.id;


    }


    @Test
    public void testCreateBookEndpoint() {
        String token = testAuthUtil.getToken("admin", "admin", "ADMIN");
        System.out.println("TOKEN" + token);
        BookDTO dto = new BookDTO();
        dto.title = "REST Book";
        dto.year = 2022;
        dto.authorId = authorId;

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(dto)

                .when()
                .post("/cms/books")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("title", is("REST Book"))
                .body("year", is(2022))
                .body("author.id", is(authorId.intValue()));
    }

    @Test
    public void testGetBooksEndpoint() {

        String token = testAuthUtil.getToken("admin", "admin", "ADMIN");
        System.out.println("TOKEN" + token);

        // Create a book first
        BookDTO dto = new BookDTO();
        dto.title = "GET Book";
        dto.year = 2021;
        dto.authorId = authorId;

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(dto)
                .post("/cms/books")
                .then()
                .statusCode(200);

        given()
                .when()
                .get("/cms/books")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1))
                .body("title", hasItem("GET Book"));
    }
}
