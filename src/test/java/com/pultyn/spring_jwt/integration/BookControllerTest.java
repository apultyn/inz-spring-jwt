package com.pultyn.spring_jwt.integration;

import com.pultyn.spring_jwt.model.Book;
import com.pultyn.spring_jwt.model.Role;
import com.pultyn.spring_jwt.model.UserEntity;
import com.pultyn.spring_jwt.repository.BookRepository;
import com.pultyn.spring_jwt.repository.RoleRepository;
import com.pultyn.spring_jwt.repository.UserRepository;
import com.pultyn.spring_jwt.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class BookControllerTest {
    @Container
    static final MySQLContainer<?> db =
            new MySQLContainer<>("mysql:8.4")
                    .withDatabaseName("test-db")
                    .withUsername("test")
                    .withPassword("test")
                    .withReuse(true);

    @DynamicPropertySource
    static void mysqlProps(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", db::getJdbcUrl);
        r.add("spring.datasource.username", db::getUsername);
        r.add("spring.datasource.password", db::getPassword);
        r.add("spring.datasource.driver-class-name", () ->
                "com.mysql.cj.jdbc.Driver");
    }

    @Autowired
    MockMvc mvc;
    @Autowired
    JwtService jwtService;
    @Autowired
    RoleRepository roleRepo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    BookRepository bookRepo;

    @BeforeEach
    void purgeDb() {
        bookRepo.deleteAll();
        userRepo.deleteAll();
        roleRepo.deleteAll();
    }

    @Test
    void deleteBook_success() throws Exception {
        Role adminRole = roleRepo.save(new Role(null, "ADMIN"));

        UserEntity admin = userRepo.save(UserEntity.builder()
                .email("admin@example.com")
                .password("{noop}dummy")
                .roles(List.of(adminRole))
                .build());

        Book book = bookRepo.save(Book.builder()
                .title("Clean Code").author("Robert C. Martin").build());
        String token = jwtService.generateToken(admin);

        mvc.perform(delete("/api/books/{id}", book.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteBook_user() throws Exception {
        Role userRole = roleRepo.save(new Role(null, "USER"));

        UserEntity user = userRepo.save(UserEntity.builder()
                .email("user@example.com")
                .password("{noop}pw")
                .roles(List.of(userRole))
                .build());

        Book book = bookRepo.save(Book.builder()
                .title("DDD").author("Eric Evans").build());

        String token = jwtService.generateToken(user);

        mvc.perform(delete("/api/books/{id}", book.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isForbidden());
    }
}
