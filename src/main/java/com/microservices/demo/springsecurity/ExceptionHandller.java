package com.microservices.demo.springsecurity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//1. The Validation Trigger (@Valid)
//When a request hits your controller, Spring uses the DataBinder to map the incoming JSON to your Java object (DTO).
//
//The Dependency: The hibernate-validator (inside the validation starter) provides the engine.
//
//The Process: 1.  Spring sees the @Valid annotation on your @RequestBody.
//2.  It pauses the execution and hands the DTO over to the Validator.
//3.  The Validator checks the constraints you defined (e.g., @NotNull, @NotEmpty).
//4.  If a check fails: It does not throw an exception immediately. Instead, it populates a BindingResult or Errors object.
//5.  Spring's MethodArgumentNotValidException is thrown automatically if the validation fails and you haven't manually handled the BindingResult.
//
//2. The Interceptor (@ControllerAdvice)
//Without a "Advice" class, the user would get a messy, standard 500 error or a default 400 with too much technical detail.
//
//The Proxy Pattern: @ControllerAdvice is an Intercepter. Internally, Spring uses AOP (Aspect-Oriented Programming). It "wraps" all your controllers in a proxy.
//
//The Mechanism:
//
//The exception (MethodArgumentNotValidException) travels up the call stack.
//
//Spring's ExceptionHandlerExceptionResolver scans all classes annotated with @ControllerAdvice.
//
//It looks for a method annotated with @ExceptionHandler(MethodArgumentNotValidException.class).
@ControllerAdvice
public class ExceptionHandller {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex) {
        return "An error occurred: " + ex.getMessage();
    }

    //Pattern,Where it lives,What it does
    //Strategy Pattern,DataBinder,Decides which validator to use for your DTO.
    //Visitor Pattern,Hibernate Validator,Walks through your DTO fields to check annotations.
    //Proxy Pattern (AOP),@ControllerAdvice,Intercepts the exception after the validator fails.
    //Adapter Pattern,Jackson / HttpMessageConverter,Converts the JSON from Bruno into the Java Object first.
}
