///usr/bin/env jbang "$0" "$@" ; exit $?

//JAVA 21
//JAVA_OPTIONS -Dserver.port=8081

//DEPS org.springframework.boot:spring-boot-starter-web:3.3.0
//DEPS org.springframework.boot:spring-boot-starter-data-jpa:3.3.0
//DEPS org.springframework.boot:spring-boot-starter-validation:3.3.0
//DEPS org.postgresql:postgresql:42.6.0
//DEPS org.springframework.boot:spring-boot-starter-actuator:3.3.0
//DEPS org.apache.commons:commons-text:1.14.0

package com.example.jbang;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@SpringBootApplication
public class AppScript {

    private static final Logger log = LoggerFactory.getLogger(AppScript.class);

    public static void main(String... args) {
        log.info("Starting application with args:[{}]", Arrays.toString(args));
        loadEnv(args);
        SpringApplication.run(AppScript.class, args);
    }

    private static void loadEnv(String... args) {
        String filepath = Arrays.stream(args)
                .filter(arg -> arg.contains(".env") && Files.exists(Paths.get(arg)))
                .findFirst()
                .orElse(".env");
        log.info("Loading environment variables from file:[{}]", filepath);
        try (Stream<String> lines = Files.lines(Paths.get(filepath))) {
            lines.filter(line -> !line.trim().isEmpty() && !line.startsWith("#"))
                    .map(line -> line.trim().split("=", 2))
                    .filter(parts -> parts.length == 2)
                    .filter(parts -> !parts[0].trim().isEmpty() && !parts[1].trim().isEmpty())
                    .forEach(parts -> System.setProperty(parts[0].trim(), parts[1].trim()));
        } catch (IOException e) {
            System.err.println("Error reading .env file: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

@RestController
@RequestMapping("/api")
class AppController {

    private static final Logger log = LoggerFactory.getLogger(AppController.class);

    private final AppService appService;

    AppController(AppService appService) {
        this.appService = appService;
    }

    @GetMapping(value = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sayHello(@RequestParam(value = "name", defaultValue = "World") String name) {
        log.info("Invoked sayHello method");
        log.debug("Invoked sayHello method for name: {}", name);
        String formattedName = StringEscapeUtils.builder(StringEscapeUtils.ESCAPE_HTML4).escape(name).toString();
        return ResponseEntity.ok()
                .body(Map.of("message", "Hello, %s!".formatted(formattedName)));
    }

    @GetMapping(value = "/data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getData() {
        log.info("Invoked getData method");
        return appService.getData();
    }
}

@Service
class AppService {
    private static final Logger log = LoggerFactory.getLogger(AppService.class);

    private final AppRepository appRepository;

    AppService(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public ResponseEntity<?> getData() {
        List<Bookmark> bookmarks = appRepository.findAll();
        log.debug("Invoked getData method, found [{}] bookmarks", bookmarks.size());
        return ResponseEntity.ok().body(bookmarks);
    }
}

@Repository
interface AppRepository extends PagingAndSortingRepository<Bookmark, Long> {
    List<Bookmark> findAll();
}


@Entity
@Table(name = "bookmarks")
class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Title cannot be NULL")
    @NotBlank(message = "Title cannot be Blank")
    @Column
    private String title;

    @NotNull(message = "URL cannot be NULL")
    @NotBlank(message = "URL cannot be Blank")
    @Column
    private String url;

    @Column
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return "Bookmark{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
