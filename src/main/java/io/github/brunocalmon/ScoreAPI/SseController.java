package io.github.brunocalmon.ScoreAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class SseController {

    @Autowired
    private KafkaConsumerService kafkaConsumerService;

    @GetMapping("/stream")
    public SseEmitter streamEvents() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        kafkaConsumerService.getEmitters().add(emitter);

        emitter.onCompletion(() -> kafkaConsumerService.getEmitters().remove(emitter));
        emitter.onTimeout(() -> kafkaConsumerService.getEmitters().remove(emitter));

        return emitter;
    }
}
