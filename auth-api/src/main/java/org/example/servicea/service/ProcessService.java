package org.example.servicea.service;

import lombok.RequiredArgsConstructor;
import org.example.servicea.controller.ProcessingController;
import org.example.servicea.model.ProcessingLog;
import org.example.servicea.model.UserEntity;
import org.example.servicea.repository.ProcessingLogRepository;
import org.example.servicea.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProcessService {

    private final WebClient webClient;
    private final UserRepository userRepository;
    private final ProcessingLogRepository processingLogRepository;

    @Value("${serviceB.url}")
    private String serviceBUrl;

    @Value("${security.secret.api.token}")
    private String apiSecretToken;

    public String processData(ProcessingController.StringRecord request, String email) {

        Optional<UserEntity> user = userRepository.findByEmail(email);


        ResponseEntity<String> response = webClient.post()
                .uri(serviceBUrl + "/api/transform/text")
                .header("X-Internal-Token", apiSecretToken)
                .bodyValue(request)
                .retrieve()
                .toEntity(String.class)
                .block();

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        ProcessingLog log = ProcessingLog.builder()
                .inputText(request.data())
                .createdAt(Instant.now())
                .userId(user.get())
                .outputText(response.getBody())
                .build();

        processingLogRepository.save(log);

        return response.getBody();
    }


}
