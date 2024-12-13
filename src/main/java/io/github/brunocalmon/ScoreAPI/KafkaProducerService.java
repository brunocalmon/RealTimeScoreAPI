package io.github.brunocalmon.ScoreAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final List<String> teams;
    private final ExecutorService executorService;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.executorService = Executors.newFixedThreadPool(10);

        this.teams = Arrays.asList(
                "Flamengo", "Palmeiras", "São Paulo", "Internacional", "Atlético-MG", "Grêmio",
                "Fluminense", "Corinthians", "Athletico-PR", "Botafogo", "Cruzeiro", "Vasco",
                "Bahia", "Cuiabá", "Santos", "Gremio", "Fortaleza", "Bragantino", "América-MG", "Goias");
    }

    public void startGameSimulation(int frequency, int shards) {
        List<String[]> teamPairs = generateTeamPairs();
        
        // Dividir a lista de jogos (teamPairs) em partes, uma para cada shard (thread)
        int chunkSize = (int) Math.ceil((double) teamPairs.size() / shards);
        List<List<String[]>> partitions = new ArrayList<>();
    
        for (int i = 0; i < shards; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, teamPairs.size());
            partitions.add(teamPairs.subList(start, end));
        }
    
        // Submeter tarefas, uma para cada partição (cada thread)
        for (int i = 0; i < shards; i++) {
            List<String[]> partition = partitions.get(i);
            executorService.submit(() -> runGameSimulation(frequency, partition));
        }
    }

    private List<String[]> generateTeamPairs() {
        List<String[]> pairs = new ArrayList<>();
        Random random = new Random();
        List<String> availableTeams = new ArrayList<>(teams);

        while (availableTeams.size() > 1) {
            String teamA = availableTeams.remove(random.nextInt(availableTeams.size()));
            String teamB = availableTeams.remove(random.nextInt(availableTeams.size()));

            pairs.add(new String[] { teamA, teamB });
        }

        return pairs;
    }

    private void runGameSimulation(int frequency, List<String[]> teamPairs) {
        Random random = new Random();

        List<Game> games = new ArrayList<>();

        for (String[] match : teamPairs) {
            String teamA = match[0];
            String teamB = match[1];
            games.add(new Game(teamA, teamB));
        }

        for (int i = 0; i < frequency; i++) {
            Game gameToUpdate = games.get(random.nextInt(games.size()));

            if (random.nextBoolean()) {
                gameToUpdate.incrementScoreA();
            } else {
                gameToUpdate.incrementScoreB();
            }

            String message = String.format(
                    "{\"timeA\":\"%s\", \"timeB\":\"%s\", \"placarA\":%d, \"placarB\":%d}",
                    gameToUpdate.getTeamA(), gameToUpdate.getTeamB(), gameToUpdate.getScoreA(), gameToUpdate.getScoreB());

            kafkaTemplate.send("scores", message);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static class Game {
        private String teamA;
        private String teamB;
        private int scoreA;
        private int scoreB;

        public Game(String teamA, String teamB) {
            this.teamA = teamA;
            this.teamB = teamB;
            this.scoreA = 0;
            this.scoreB = 0;
        }

        public String getTeamA() {
            return teamA;
        }

        public String getTeamB() {
            return teamB;
        }

        public int getScoreA() {
            return scoreA;
        }

        public int getScoreB() {
            return scoreB;
        }

        public void incrementScoreA() {
            this.scoreA++;
        }

        public void incrementScoreB() {
            this.scoreB++;
        }
    }
}
