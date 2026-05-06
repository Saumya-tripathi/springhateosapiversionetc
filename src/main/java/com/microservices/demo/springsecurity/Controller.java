package com.microservices.demo.springsecurity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
public class Controller {
    @Operation(summary = "Get Home Message", description = "Returns a welcome message for the Spring Security Demo.")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of home message")
    @GetMapping("/home")
    public String home() {
        return "Welcome to the Spring Security Demo!";
    }

    @PostMapping("/home")
    public String postHome(@RequestBody @NotNull String request) {
        return "Welcome to the Spring Security Demo POST!";
    }

    // URI Versioning
    @PostMapping("/home/v2")
    @Operation(summary = "Post Home Record", description = "Accepts a HomeRecord and returns it with HATEOAS links.")
    @ApiResponse(responseCode = "200", description = "Successful creation of HomeRecord with HATEOAS links",
            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/vnd.api.v1+json",
            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = HomeRecord.class)))

    public ResponseEntity<HomeRecord> postHome(@RequestBody @Valid HomeRecord request) {
        HomeRecord h = new HomeRecord(request.getId(), request.getName());
        h.add(linkTo(methodOn(Controller.class).postHome(request)).withSelfRel());
        h.add(linkTo(methodOn(Controller.class).home()).withRel("home"));
        h.add(linkTo(methodOn(Controller.class).postHome(request)).withRel("homePost"));
        return ResponseEntity.ok(h);
    }
    //The design pattern behind HATEOAS is effectively the State Machine pattern for APIs. Instead of the client knowing every "street" in your API, the API acts like a GPS, providing the next available turns (links) based on the current resource state.
    //## The Core Concept: Hypermedia
    //In a standard REST API, the client hardcodes URLs (e.g., /orders/123/cancel). In HATEOAS, the client only needs to know the Entry Point. From there, the server provides links telling the client what they can do next.
    //------------------------------
    //## What is rel?
    //rel stands for Relation. It is the "key" or "label" for a link. It tells the client why this link exists and what it leads to, without the client needing to parse the URL itself.
    //
    //* self: The most common rel. It points to the resource itself (the unique identity of the data you just fetched).
    //* collection: Points to a list of resources of the same type.
    //* Custom Rels: You can define your own, like cancel_order, next_page, or upgrade_membership.
    //
    //Why use it? If you change your URL from /api/v1/employees to /api/v2/staff, a client looking for the rel="employees" link won't break—it just follows the new href provided by the server.
    //------------------------------
    //## Key Objects in Spring HATEOAS
    //
    //   1. EntityModel<T>:
    //   Think of this as a "Data + Links" envelope. It wraps a single POJO (like a User) and adds a list of Link objects to the JSON output.
    //   2. CollectionModel<T>:
    //   A wrapper for a list of resources. It allows you to add links to the entire list (like "pagination" links) rather than just the individual items.
    //   3. WebMvcLinkBuilder:
    //   A utility that "spies" on your Controller's @RequestMapping. Instead of typing strings like "http://localhost:8080/...", you use linkTo(methodOn(Controller.class).method()). This ensures that if you change a @GetMapping path, your HATEOAS links update automatically.
    //
    //Would you like to see how to implement a RepresentationModelAssembler to clean up the controller logic?
}
