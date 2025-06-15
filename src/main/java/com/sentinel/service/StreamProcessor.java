package com.sentinel.service;

import com.sentinel.domain.EventLog;
import com.sentinel.domain.EventLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class StreamProcessor {

    private final EventLogRepository repository;
    private final StringRedisTemplate redisTemplate;
    private static final String WINDOW_KEY_PREFIX = "window:";
    private static final int WINDOW_SIZE_SECONDS = 60;
    private static final int THRESHOLD = 100; // Max events per minute

    @KafkaListener(topics = "event-stream", groupId = "sentinel-group")
    public void processEvent(EventLog event) {
        log.info("Processing event: {}", event.getEventId());

        boolean isAnomaly = checkSlidingWindow(event.getSource());
        event.setAnomaly(isAnomaly);

        if (isAnomaly) {
            log.warn("Anomaly detected for source: {}", event.getSource());
        }

        saveAsync(event);
    }

    private boolean checkSlidingWindow(String source) {
        String key = WINDOW_KEY_PREFIX + source;
        long currentTime = Instant.now().getEpochSecond();
        long windowStart = currentTime - WINDOW_SIZE_SECONDS;

        // Add current event to sorted set (score = timestamp, value = UUID)
        redisTemplate.opsForZSet().add(key, String.valueOf(System.nanoTime()), currentTime);
        
        // Remove old events
        redisTemplate.opsForZSet().removeRangeByScore(key, 0, windowStart);
        
        // Count events in current window
        Long count = redisTemplate.opsForZSet().zCard(key);
        redisTemplate.expire(key, Duration.ofSeconds(WINDOW_SIZE_SECONDS + 10));

        return count != null && count > THRESHOLD;
    }

    @Async
    public void saveAsync(EventLog event) {
        repository.save(event);
    }
}
