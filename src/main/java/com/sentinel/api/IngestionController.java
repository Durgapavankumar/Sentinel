package com.sentinel.api;

import com.sentinel.domain.EventLog;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class IngestionController {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "event-stream";

    @PostMapping
    public ResponseEntity<String> ingestEvent(@RequestBody String payload) {
        String eventId = UUID.randomUUID().toString();
        // Simple structure for demo
        EventLog event = EventLog.builder()
                .eventId(eventId)
                .eventType("STANDARD")
                .source("API")
                .payload(payload)
                .timestamp(LocalDateTime.now())
                .build();
        
        kafkaTemplate.send(TOPIC, eventId, event);
        return ResponseEntity.accepted().body("Event queued: " + eventId);
    }
}
