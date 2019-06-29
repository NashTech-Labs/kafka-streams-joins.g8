package com.knoldus.demo.streams;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.log4j.Logger;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class KStreamKStreamJoin {
    
    private static final Logger LOGGER = Logger.getLogger(KStreamKStreamJoin.class);
    private static final Config CONF = ConfigFactory.load();
    
    private static final String CUSTOMER_TOPIC = CONF.getString("stream.topic.customer.source");
    private static final String ADDRESS_TOPIC = CONF.getString("stream.topic.address.source");
    private static final String OUTPUT_TOPIC = CONF.getString("stream.topic.output");
    
    public KafkaStreams createStreams(final String bootstrapServers,
                                      final String stateDir) {
        final Properties streamsConfiguration = loadProperties(bootstrapServers, stateDir);
        
        final KStreamBuilder builder = new KStreamBuilder();
        
        // Get the stream of customers
        final KStream<String, String> customerStream = builder.stream(Serdes.String(),
                Serdes.String(), CUSTOMER_TOPIC);
        customerStream.print("Customer input stream");
        
        // Get the stream of addresses
        final KStream<String, String> addressStream = builder.stream(Serdes.String(),
                Serdes.String(), ADDRESS_TOPIC);
        addressStream.print("address input stream:");
        
        final KStream<String, String> addressCustomerJoin = customerStream.join(addressStream,
                (customer, address) -> customer + " \\ " + address,
                JoinWindows.of(TimeUnit.MINUTES.toMillis(1)),
                Serdes.String(), Serdes.String(), Serdes.String());
        
        addressCustomerJoin.print("joined output");
        addressCustomerJoin.to(Serdes.String(),Serdes.String(),OUTPUT_TOPIC);
        
        return new KafkaStreams(builder, new StreamsConfig(streamsConfiguration));
    }
    
    private static Properties loadProperties(String bootstrapServers, String stateDir) {
        final Properties streamsConfiguration = new Properties();
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "kstream-join-example");
        streamsConfiguration.put(StreamsConfig.CLIENT_ID_CONFIG, "kstream-join-example-client");
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        streamsConfiguration.put(StreamsConfig.STATE_DIR_CONFIG, stateDir);
        streamsConfiguration.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        streamsConfiguration.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        streamsConfiguration.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        streamsConfiguration.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        streamsConfiguration.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        streamsConfiguration.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return streamsConfiguration;
    }
    
}
