///usr/bin/env jbang "$0" "$@" ; exit $?

//JAVA 21
//JAVA_OPTIONS -Dserver.port=8081

//DEPS org.springframework.boot:spring-boot-starter-web:3.3.0
//DEPS org.springframework.boot:spring-boot-starter-actuator:3.3.0
//DEPS org.springframework.ai:spring-ai-openai-spring-boot-starter:1.0.0-M6

package com.example.jbang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static java.util.Objects.requireNonNull;

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

@RestController
@RequestMapping(path = "/ai")
class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    private static final String SYSTEM_PROMPT = """
            Answer the question in plain text in one sentence.
            Answer the question in a concise manner.
            """;

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        InMemoryChatMemory memory = new InMemoryChatMemory();
        this.chatClient = builder
                .defaultAdvisors(new MessageChatMemoryAdvisor(memory))
                .build();
    }

    @PostMapping(value = "/chat", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> chat(@RequestBody String prompt) {
        try {
            log.info("Invoked chat method with prompt: {}", prompt);
            String content = chatClient.prompt()
                    .system(SYSTEM_PROMPT)
                    .user(prompt)
                    .call()
                    .content();

            return ResponseEntity.ok()
                    .body((Map.of("response", requireNonNull(content, "Response content is NULL"))));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Some error occurred , due to: " + e.getMessage()));
        }
    }
}
