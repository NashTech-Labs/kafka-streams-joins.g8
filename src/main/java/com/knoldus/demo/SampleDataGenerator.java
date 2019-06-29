package com.knoldus.demo;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class SampleDataGenerator {
    private static final Logger LOGGER = Logger.getLogger(SampleDataGenerator.class);
    private static final Config CONF = ConfigFactory.load();
    private static final String CUSTOMER_TOPIC = CONF.getString("stream.topic.customer.source");
    private static final String ADDRESS_TOPIC = CONF.getString("stream.topic.address.source");
    
    private static Properties loadProperties() {
        String schemaRegistryUrl = "http://localhost:8081";
        Properties properties = new Properties();
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put("schema.registry.url", schemaRegistryUrl);
        return properties;
    }
    
    public static void main(String[] args) {
        
        KafkaProducer<String, String> addressProducer =
                new KafkaProducer<String, String>(loadProperties());
        
        for (int i = 0; i < 100; i++) {
            Long addressId = 700L + i;
            ProducerRecord<String, String> record =
                    new ProducerRecord<String, String>(ADDRESS_TOPIC, addressId.toString(), "address" + i);
//            LOGGER.info("Produced address :" + addressId);
            addressProducer.send(record);
        }
        
        addressProducer.close();
        
        KafkaProducer<String, String> customerProducer =
                new KafkaProducer<String, String>(loadProperties());
        
        for (int i = 0; i < 10; i++) {
            RecordMetadata recordMetadata;
            Long customerId = 700L + i;
            ProducerRecord<String, String> record =
                    new ProducerRecord<String, String>(CUSTOMER_TOPIC, customerId.toString(), "customer" + i);
            try {
                recordMetadata = customerProducer.send(record).get();
            } catch (InterruptedException | ExecutionException e) {
                recordMetadata = null;
                LOGGER.info("error caught : " + e.getCause());
            }
            
            if (recordMetadata != null) {
                System.out.printf("Record offset [%d] and partition [%d] with customer Id [%d] \n",
                        recordMetadata.offset(), recordMetadata.partition(), customerId);
            }
        }
        customerProducer.close();
    }
    
}
