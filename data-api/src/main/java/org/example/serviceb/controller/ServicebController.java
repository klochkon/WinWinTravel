package org.example.serviceb.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transform")
public class ServicebController {

    @Value("${security.secret.api.token}")
    private String expectedToken;

    public record StringRecord(String data) {}



    @PostMapping("text")
    public ResponseEntity<String> transformText(@RequestBody StringRecord request,
    @RequestHeader(value = "X-Internal-Token", required = false) String receivedToken) {

        if (receivedToken == null) {
            return new ResponseEntity<>("Missing X-Internal-Token header", HttpStatus.FORBIDDEN);
        }

        if (!receivedToken.equals(expectedToken)) {
            return new ResponseEntity<>("Invalid X-Internal-Token", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(request.data.toUpperCase(), HttpStatus.OK);
    }
}
