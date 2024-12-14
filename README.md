# Score API - Real-Time Game Simulation with Kafka and SSE

## Overview

The **Score API** is a study case designed to demonstrate the use of **Kafka** for real-time messaging and **Server-Sent Events (SSE)** to push live updates to the front-end. This API simulates football match scores between teams in the Brazilian Serie A and streams these updates in real-time. It uses **Kafka** to send score updates and **SSE** to push these updates to the front-end as they occur.

This project highlights how to use **Kafka** to handle real-time data streams and how **SSE** can be used as a lightweight alternative to WebSockets to deliver continuous updates from a backend to the front-end.

---

## Architecture

The system architecture leverages the following components:

1. **Spring Boot**: Provides the RESTful API to manage the game simulations and stream data.
2. **Kafka**: A distributed streaming platform that handles score updates as messages, allowing the front-end to consume the data in real-time.
3. **SSE (Server-Sent Events)**: A mechanism for the API to push real-time updates to the front-end using a simple HTTP connection.
4. **Docker Compose**: Used to containerize the services, including Kafka and Zookeeper, and manage the required services.

---

## Requirements

Before running the project, ensure the following tools are installed:

- **Docker**: For running Kafka, Zookeeper, and other containers.
- **Java 11+**: Required to build and run the Spring Boot application.
- **Maven**: Required for building the Spring Boot application.

---

## Setup and Installation

### 1. Clone the Repository

Start by cloning the repository:

```bash
git clone <repository_url>
cd <repository_name>
```

### 2. Build the Project

Build the project using Maven. This will install all dependencies and package the application:

```bash
mvn clean install -DskipTests
```

### 3. Start Docker Containers

To run Kafka and Zookeeper, use Docker Compose to start the services:

```bash
docker compose up --build -d
```

This command will start the services in detached mode.

### 4. Start the Spring Boot Application

Start the Spring Boot application with the following command:

```bash
mvn spring-boot:start
```

---

## Real-Time Data Streaming

### Kafka Integration

The **Score API** sends football match score updates to a Kafka topic named `scores`. These updates are generated and published by the Kafka producer, which simulates random score changes over time. The Kafka topic serves as the central data stream, and any consumer (including the front-end) can subscribe to this topic to receive updates.

### SSE for Real-Time Updates

The API uses **Server-Sent Events (SSE)** to stream updates to the front-end. SSE allows the server to push updates to the browser over a single HTTP connection, making it suitable for real-time updates like live scores in a game.

- **SSE Endpoint**: `/stream`
- The front-end can open a connection to this endpoint and receive real-time updates as JSON objects containing the game scores.

---

## API Endpoints

### 1. **Start Game Simulation**

- **Endpoint**: `/api/games/start`
- **Method**: `POST`
- **Query Parameters**:
  - `frequency`: The interval (in seconds) at which the score should be updated.
  - `shards`: The number of parallel game simulations (threads) to run.

- **Example Request**:

  ```bash
  curl -X POST "http://localhost:3000/api/games/start?frequency=5&shards=3"
  ```

- **Description**: Starts the game simulation with a specified frequency (time between score updates) and number of shards (parallel game simulations). This sends real-time score updates to Kafka and the front-end via SSE.

### 2. **Stream Game Scores (SSE)**

- **Endpoint**: `/stream`
- **Method**: `GET`
  
- **Description**: This endpoint streams real-time game score updates via SSE. The front-end can subscribe to this endpoint to receive continuous updates about the games.

---

## Commands

### 1. **Run Docker Compose**

To bring up the required services (Kafka and Zookeeper) in Docker containers, run:

```bash
docker compose up --build -d
```
OR
```bash
docker compose --profile api up --build -d
```

To stop the services:

```bash
docker compose down
```
OR
```bash
docker compose --profile api down
```

### 2. **Build the Project (Maven)**

Build the Spring Boot application with Maven:

```bash
mvn clean install -DskipTests
```

### 3. **Start the Spring Boot Application**

Start the Spring Boot application:

```bash
mvn spring-boot:start
```

### 4. **Kafka Console Producer**

To interact with Kafka and produce messages (score updates), run:

```bash
docker exec -it kafka bash
```

Then, use the Kafka console producer to send messages to the `scores` topic:

```bash
kafka-console-producer --broker-list kafka:9092 --topic scores
```

### 5. **Trigger the Game Simulation via API**

To start the simulation and send real-time score updates to Kafka and SSE, use the following POST request:

```bash
curl -X POST "http://localhost:3000/api/games/start?frequency=5&shards=3"
```

This triggers the game simulation with a frequency of 5 seconds for score updates and 3 parallel threads running the simulation.

---

## Front-End Integration (SSE)

To receive the real-time score updates in the front-end, you can connect to the `/stream` endpoint using JavaScript's **EventSource** API:

```javascript
const eventSource = new EventSource('http://localhost:3000/stream');

eventSource.onmessage = function(event) {
    const scoreUpdate = JSON.parse(event.data);
    console.log('Received score update:', scoreUpdate);
    // Update the UI with the score update (e.g., update match scores in the UI)
};
```

This will automatically receive and handle incoming updates as they are sent from the server via SSE.

---

## Kafka Topic: `scores`

The messages sent to the `scores` Kafka topic follow this JSON format:

```json
{
  "timeA": "Vasco",
  "timeB": "Gremio",
  "placarA": 1,
  "placarB": 0
}
```

- `timeA` and `timeB`: The teams playing in the match.
- `placarA` and `placarB`: The current scores of the respective teams.

These messages are sent periodically as the game simulations progress.

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
