package com.jpmc.midascore;

import com.jpmc.midascore.kafka.KafkaProducer;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.model.User;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(classes = MidasCoreApplication.class)
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class TaskThreeTests {
    static final Logger logger = LoggerFactory.getLogger(TaskThreeTests.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private FileLoader fileLoader;

    @Autowired
    private UserRepository userRepository;

    @Test
    void task_three_verifier() throws InterruptedException {
        String[] transactionLines = fileLoader.loadStrings("/test_data/poiuytrewq.uiop");

        for (String transactionLine : transactionLines) {
            kafkaProducer.send(transactionLine);
        }

        // Allow time for the Kafka listener to process messages
        logger.info("Waiting for Kafka listener to process messages...");
        Thread.sleep(10000); // 10 seconds

        // Fetch and log the balance of the user 'waldorf'
        User waldorf = userRepository.findById("waldorf").orElse(null);
        if (waldorf != null) {
            logger.info("----------------------------------------------------------");
            logger.info("Final Balance of 'waldorf': {}", (int) Math.floor(waldorf.getBalance()));
            logger.info("----------------------------------------------------------");
        } else {
            logger.warn("User 'waldorf' not found.");
        }
    }
}
