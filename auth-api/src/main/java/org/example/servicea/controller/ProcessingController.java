package org.example.servicea.controller;

import lombok.RequiredArgsConstructor;
import org.example.servicea.service.ProcessService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/process")
@RequiredArgsConstructor
public class ProcessingController {

    private final ProcessService processService;
    public record StringRecord(String data) {}

    @PostMapping("/string")
    public String processData(
            @AuthenticationPrincipal String email,
            @RequestBody StringRecord request){
        return processService.processData(request, email);
    }
}
