///usr/bin/env jbang "$0" "$@" ; exit $?

//JAVA 21
//JAVA_OPTIONS -Dserver.port=8081

//DEPS org.springframework.boot:spring-boot-starter-web:3.2.5

package com.example.jbang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppScript {

    public static void main(String... args) {
        SpringApplication.run(AppScript.class, args);
    }
}
