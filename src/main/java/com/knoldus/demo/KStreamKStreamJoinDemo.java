package com.knoldus.demo;

import com.knoldus.demo.streams.KStreamKStreamJoin;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.kafka.streams.KafkaStreams;

public class KStreamKStreamJoinDemo {
    
    private static final Config CONF = ConfigFactory.load();
    
    private static final String bootstrapServers = CONF.getString("kafka.brokers");
    private static final String stateDirectory = CONF.getString("stream.state-directory");
    
    public static void main(String[] args) {
        KStreamKStreamJoin streamJoin = new KStreamKStreamJoin();
        final KafkaStreams streams =
                streamJoin.createStreams(bootstrapServers, stateDirectory);
        streams.cleanUp();
        streams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
