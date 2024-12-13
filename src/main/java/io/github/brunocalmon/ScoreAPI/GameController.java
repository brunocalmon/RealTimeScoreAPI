package io.github.brunocalmon.ScoreAPI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final KafkaProducerService kafkaProducerService;

    public GameController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping("/start")
    public ResponseEntity<GameSimulationResponse> startGame(@RequestParam int frequency, @RequestParam int shards) {
        int numThreads = Math.min(shards, 10);
        kafkaProducerService.startGameSimulation(frequency, numThreads);

        return ResponseEntity.accepted().body(new GameSimulationResponse(
                "success",
                "Game simulations started successfully.",
                numThreads,
                frequency,
                System.currentTimeMillis(),
                "games-start"));
    }
}
