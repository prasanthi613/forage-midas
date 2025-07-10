package com.jpmc.midascore;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.model.Incentive;
import com.jpmc.midascore.model.Transaction;
import com.jpmc.midascore.model.TransactionRecord;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TransactionListener {

    private final DatabaseConduit databaseConduit;
    private final TransactionRecordRepository transactionRepo;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    public TransactionListener(DatabaseConduit databaseConduit,
                               TransactionRecordRepository transactionRepo,
                               UserRepository userRepository,
                               RestTemplate restTemplate) {
        this.databaseConduit = databaseConduit;
        this.transactionRepo = transactionRepo;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    @KafkaListener(topics = "transactions", groupId = "midas")
    public void listen(Transaction transaction) {
        String senderId = String.valueOf(transaction.getSenderId());
        String recipientId = String.valueOf(transaction.getRecipientId());

        UserRecord sender = databaseConduit.findUser(senderId);
        UserRecord recipient = databaseConduit.findUser(recipientId);

        if (sender == null || recipient == null || sender.getBalance() < transaction.getAmount()) {
            return;
        }

        Incentive incentive = restTemplate.postForObject(
                "http://localhost:8080/incentive",
                transaction,
                Incentive.class
        );

        float incentiveAmount = (incentive != null) ? (float) incentive.getAmount() : 0f;

        sender.setBalance(sender.getBalance() - (float) transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + (float) transaction.getAmount() + incentiveAmount);

        userRepository.save(sender);
        userRepository.save(recipient);

        TransactionRecord record = new TransactionRecord(
                transaction.getSenderId(),
                transaction.getRecipientId(),
                (float) transaction.getAmount(),
                incentiveAmount
        );

        transactionRepo.save(record);
    }
}
