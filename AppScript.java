///usr/bin/env jbang "$0" "$@" ; exit $?

//JAVA 21
//JAVA_OPTIONS -Dserver.port=8081

//DEPS org.springframework.boot:spring-boot-starter-web:3.2.5
//DEPS org.springframework.boot:spring-boot-starter-actuator:3.2.5

package com.example.jbang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@SpringBootApplication
public class AppScript {

    public static void main(String... args) {
        SpringApplication.run(AppScript.class, args);
    }
}

@RestController
@RequestMapping("/api")
class AppController {

    private static final Logger log = LoggerFactory.getLogger(AppController.class);

    @GetMapping(value = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sayHello(@RequestParam(value = "name", defaultValue = "World") String name) {
        log.info("Invoked sayHello method");
        log.debug("Invoked sayHello method for name: {}", name);
        return ResponseEntity.ok()
                .body(Map.of("message", "Hello, " + name + "!"));
    }
}
