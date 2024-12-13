package io.github.brunocalmon.ScoreAPI;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public CopyOnWriteArrayList<SseEmitter> getEmitters() {
        return emitters;
    }

    @KafkaListener(topics = "scores")
    public void consumeMessage(String message) {
        logger.info("Consumed message from Kafka: {}", message);

        Iterator<SseEmitter> iterator = emitters.iterator();
        while (iterator.hasNext()) {
            SseEmitter emitter = iterator.next();
            try {
                emitter.send(SseEmitter.event().data(message));
                logger.debug("Message sent to emitter: {}", emitter);
            } catch (Exception e) {
                logger.error("Failed to send message to emitter: {}", emitter, e);
                iterator.remove(); // Safely remove emitter
                logger.info("Emitter removed due to failure: {}", emitter);
            }
        }
    }
}
