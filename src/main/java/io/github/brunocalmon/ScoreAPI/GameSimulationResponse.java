package io.github.brunocalmon.ScoreAPI;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameSimulationResponse {

    private String status;
    private String message;
    private int threadsUsed;
    private int frequency;
    private long timestamp;
    private String action;

}
