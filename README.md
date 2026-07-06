# Kafka Producer-Consumer Example with Spring Boot

A simple demonstration of Apache Kafka integration with Spring Boot, featuring a producer microservice that sends employee data to a Kafka topic and a consumer microservice that listens and processes the messages.

## 📋 Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Setup and Installation](#setup-and-installation)
- [Running the Application](#running-the-application)
- [Usage](#usage)
- [Configuration](#configuration)
- [Technologies Used](#technologies-used)
- [Contributing](#contributing)
- [License](#license)

## ✨ Features

- **Producer Service**: RESTful API endpoint to publish employee data to Kafka topic
- **Consumer Service**: Automatic message consumption and processing from Kafka topic
- **DTOs**: Structured data transfer objects for Employee and Address entities
- **JSON Serialization**: Seamless conversion between Java objects and JSON messages
- **Spring Boot Integration**: Leverages Spring Kafka for easy configuration and usage

## 📋 Prerequisites

Before running this application, ensure you have the following installed:

- **Java 21** or higher
- **Maven 3.x** or use the included Maven wrapper (`mvnw`)
- **Apache Kafka** (running on `localhost:9092`)

## 🏗️ Project Structure

```
kafka/
├── KAFKA-PRODUCER/          # Producer microservice
│   ├── src/
│   │   ├── main/java/com/producer/
│   │   │   ├── KafkaProducerApplication.java
│   │   │   ├── controller/ProducerController.java
│   │   │   └── dto/
│   │   │       ├── EmployeeDto.java
│   │   │       └── AddressDto.java
│   │   └── resources/application.properties
│   └── pom.xml
├── KAFKA-CONSUMER/          # Consumer microservice
│   ├── src/
│   │   ├── main/java/com/consumer/
│   │   │   ├── KafkaConsumerApplication.java
│   │   │   ├── consumer/KafkaConsumer.java
│   │   │   └── dto/
│   │   │       ├── EmployeeDto.java
│   │   │       └── AddressDto.java
│   │   └── resources/application.properties
│   └── pom.xml
└── README.md
```

## 🚀 Setup and Installation

### 1. Start Apache Kafka

#### Option A: Using Docker Compose (Recommended)

You can start Kafka using Docker Compose. Create a `docker-compose.yml` file in the project root:

```yaml
version: '3.8'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.4.0
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    ports:
      - "2181:2181"

  kafka:
    image: confluentinc/cp-kafka:7.4.0
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1
```

Run the following command to start Kafka:

```bash
docker-compose up -d
```

#### Option B: Manual Setup (Without Docker)

1. **Download Apache Kafka**:
   - Visit the [Apache Kafka downloads page](https://kafka.apache.org/downloads)
   - Download the latest binary release (e.g., `kafka_2.13-3.6.0.tgz`)

2. **Extract and Navigate**:
   ```bash
   tar -xzf kafka_2.13-3.6.0.tgz
   cd kafka_2.13-3.6.0
   ```

3. **Start Zookeeper**:
   ```bash
   bin/zookeeper-server-start.sh config/zookeeper.properties
   ```
   (Keep this terminal open)

4. **Start Kafka Broker** (in a new terminal):
   ```bash
   bin/kafka-server-start.sh config/server.properties
   ```
   (Keep this terminal open)

5. **Create the Topic** (optional, as Spring Kafka can auto-create):
   ```bash
   bin/kafka-topics.sh --create --topic employee-topic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
   ```

Kafka will now be running on `localhost:9092`.

### 2. Clone and Build the Project

```bash
git clone <repository-url>
cd kafka

# Build both modules
cd KAFKA-PRODUCER
./mvnw clean install

cd ../KAFKA-CONSUMER
./mvnw clean install
```

## ▶️ Running the Application

### Start the Consumer Service

```bash
cd KAFKA-CONSUMER
./mvnw spring-boot:run
```

The consumer will start on port `8182` and begin listening for messages on the `employee-topic`.

### Start the Producer Service

```bash
cd KAFKA-PRODUCER
./mvnw spring-boot:run
```

The producer will start on port `8181` and expose a REST API endpoint.

## 📖 Usage

### Publishing a Message

Send a POST request to the producer service with employee data:

```bash
curl -X POST http://localhost:8181/publish \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "company": "Tech Corp",
    "address": {
      "id": 1,
      "city": "New York"
    }
  }'
```

### Expected Response

The producer will return: `"Message published to Kafka topic"`

### Consumer Output

The consumer service will log the received employee data to the console:

```
==================
1
John Doe
john.doe@example.com
Tech Corp
1
==================
```

## ⚙️ Configuration

### Producer Configuration (`KAFKA-PRODUCER/src/main/resources/application.properties`)

```properties
spring.application.name=KAFKA-PRODUCER
server.port=8181

# Kafka Producer Configuration
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer
```

### Consumer Configuration (`KAFKA-CONSUMER/src/main/resources/application.properties`)

```properties
spring.application.name=KAFKA-CONSUMER
server.port=8182

spring.kafka.consumer.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=employee-group-new
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
```

## 🛠️ Technologies Used

- **Spring Boot 4.0.5**: Framework for building the microservices
- **Spring Kafka**: Integration with Apache Kafka
- **Spring Web MVC**: RESTful API support
- **Jackson**: JSON serialization/deserialization
- **Maven**: Dependency management and build tool
- **Java 21**: Programming language


