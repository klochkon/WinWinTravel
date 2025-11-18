package org.example.servicea.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "processing_log")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcessingLog {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity userId;

    private String inputText;
    private String outputText;
    private Instant createdAt;

}
