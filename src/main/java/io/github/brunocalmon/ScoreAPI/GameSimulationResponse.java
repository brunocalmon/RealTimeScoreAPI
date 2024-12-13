package io.github.brunocalmon.ScoreAPI;

public class GameSimulationResponse {

    private String status;
    private String message;
    private int threadsUsed;
    private int frequency;
    private long timestamp;
    private String action;

    public GameSimulationResponse(String status, String message, int threadsUsed, int frequency, long timestamp,
            String action) {
        this.status = status;
        this.message = message;
        this.threadsUsed = threadsUsed;
        this.frequency = frequency;
        this.timestamp = timestamp;
        this.action = action;
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getThreadsUsed() {
        return threadsUsed;
    }

    public void setThreadsUsed(int threadsUsed) {
        this.threadsUsed = threadsUsed;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
