package org.example.KUtils;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.example.UserInterface;
import org.example.Messeging.Message;
import org.example.Messeging.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class KConsumer {
    KafkaConsumer<String, String> consumer;
    private final AtomicBoolean shutdown;
    private final CountDownLatch counter;
    private static final Logger log = LoggerFactory.getLogger(KConsumer.class);
    private User user;

    public KConsumer(String User) {
        try{
            Properties props = loadConfig("./client.properties");
            consumer = new KafkaConsumer<>(props);
            log.info(consumer.toString());
        } catch (IOException e){
            throw new RuntimeException(e);
        } finally{
            shutdown = new AtomicBoolean(false);
            counter = new CountDownLatch(1);
            user = new User(User);
        }
    }
    public Message recordToMessage(ConsumerRecord<String, String> record) {
        Message m = new Message(record.value(), record.key(), record.timestamp());
        return m;
    }
    public void run(UserInterface userInterface){
        try {
            consumer.subscribe(Collections.singleton(KProducer.TOPIC));
            while (!shutdown.get()){
                ConsumerRecords<String, String> records = consumer.poll(500);
                records.forEach(record -> {
                    Message m = recordToMessage(record);
                    log.info(m.getContent());
                    String myMessege = m.getTimestamp() + " " +
                            m.getFrom() + "\t| " + m.getContent() + "\n";
                    userInterface.appendToChat(myMessege);

                });
            }
        }finally {
            consumer.close();
            counter.countDown();
        }
    }
}
