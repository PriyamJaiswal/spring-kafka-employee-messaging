package com.producer.controller;

import com.producer.dto.EmployeeDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@RestController
public class ProducerController {

    public static final String EMPLOYEE_TOPIC = "employee-topic";

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public ProducerController(KafkaTemplate<String, Object> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/publish")
    public String publishMessageToKafka(@RequestBody EmployeeDto employeeDto){
        String employeeAsString;
        try {
             employeeAsString = objectMapper.writeValueAsString(employeeDto);
        } catch (JacksonException e) {
            throw new RuntimeException(e);
        }
        kafkaTemplate.send(EMPLOYEE_TOPIC, employeeAsString);
        return "Message published to Kafka topic: ";
    }

}
