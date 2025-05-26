package com.example.helloworldspringboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Indicates this class is a REST controller that handles HTTP requests
public class HelloController {
   public HelloController() {
       // Default constructor
   }

   @GetMapping({"/"}) // Maps HTTP GET requests for the root URL ("/") to this method
   public String hello() {
      return "Hello, World!"; // Returns a simple greeting as the HTTP response
   }
}
