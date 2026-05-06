package com.microservices.demo.springsecurity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

//In the context of the API versioning you're studying, vnd stands for vendor.
//
//It is a prefix used in Media Types (MIME types) to indicate that the format is specific to a particular "vendor" or organization, rather than being a generic public standard like application/json or application/xml.
//
//What vnd represents
//When you see a header like Accept: application/vnd.myapi.v1+json, it follows a specific structure:
//
//application: The top-level type.
//
//vnd: The tree indicator (meaning "vendor-specific").
//
//myapi: The subtype (your specific application or company name).
//
//v1: The version of the data structure.
//
//+json: The "suffix," telling the system that while this is a custom vendor type, the underlying syntax is still formatted as JSON.
//
//How it works "Internally"
//Internally, within a Spring Boot application (like the one in your course), the process follows these steps:
//
//1. The Mapping Match
//When a request hits your controller, Spring’s RequestMappingHandlerMapping looks at the produces attribute of your @GetMapping or @PostMapping.
//
//Java
//@GetMapping(value = "/tweets", produces = "application/vnd.myapi.v1+json")
//public TweetResponseV1 getTweetsV1() { ... }
//
//@GetMapping(value = "/tweets", produces = "application/vnd.myapi.v2+json")
//public TweetResponseV2 getTweetsV2() { ... }
//2. Header Extraction
//The server extracts the Accept header from the incoming HTTP request. It doesn't just look at the URL; it looks at the "metadata" of what the client says it can handle.
//
//3. Content Negotiation Manager
//Spring uses a component called the ContentNegotiationManager. It compares the Accept header from the client against the produces values defined in your controllers.
//
//If the client sent Accept: application/vnd.myapi.v1+json, Spring routes the request to the V1 method.
//
//If the client sent Accept: application/vnd.myapi.v2+json, Spring routes it to the V2 method.
//
//4. Message Conversion
//Once the correct method is chosen, the HttpMessageConverter (usually Jackson for JSON) takes your Java object and converts it into a JSON string. It then attaches the Content-Type: application/vnd.myapi.v1+json to the response so the client knows exactly which version it received.
//
//Why use this instead of the URL?
//Using vnd in the headers (known as Media Type Versioning) is considered "cleaner" by many architects because:
//
//URL Stability: The URL (/tweets) stays the same even as the API evolves.
//
//Resource Identity: A "Tweet" is a resource; the version is just a different representation of that resource.
@RestController
@RequestMapping(value = "/api", produces = "application/vnd.api.v1+json")
public class ControllerVersioningLearning {
    @Operation(summary = "Get Home Message", description = "Returns a welcome message for the Spring Security Demo.")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of home message", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/vnd.api.v1+json"))
    @GetMapping("/home")
    public String home() {
        return "Welcome to the Spring Security Demo!";
    }

    @GetMapping(value = "/home", produces = "application/vnd.api.v2+json")
    @Operation(summary = "Get Home Message", description = "Returns a welcome message for the Spring Security Demo.")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of home message", content=  @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/vnd.api.v2+json"))
    public String home2() {
        return "Welcome to the Spring Security Demo content negotiation!";
    }

    @PostMapping("/home")
    public String postHome(@RequestBody @NotNull String request) {
        return "Welcome to the Spring Security Demo POST!";
    }
    // URI Versioning
    @PostMapping("/home/v2")
    public ResponseEntity<HomeRecord> postHome(@RequestBody @Valid HomeRecord request) {
        HomeRecord h= new HomeRecord(request.getId(), request.getName());
        h.add(linkTo(methodOn(ControllerVersioningLearning.class).postHome(request)).withSelfRel());
        h.add(linkTo(methodOn(ControllerVersioningLearning.class).home()).withRel("home"));
        h.add(linkTo(methodOn(ControllerVersioningLearning.class).postHome(request)).withRel("homePost"));
        return ResponseEntity.ok(h);
    }
}
