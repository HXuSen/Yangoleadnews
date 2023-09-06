package com.yango.kafka.stream;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Arrays;

/**
 * ClassName: KafkaStreamHelloListener
 * Package: com.yango.kafka.stream
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/4-12:09
 */
@Configuration
@Slf4j
public class KafkaStreamHelloListener {
    @Bean
    public KStream<String,String> kStream(StreamsBuilder streamsBuilder){
        KStream<String, String> stream = streamsBuilder.stream("yango-topic-input");

        stream.flatMapValues(new ValueMapper<String, Iterable<String>>() {
                    @Override
                    public Iterable<String> apply(String value) {
                        return Arrays.asList(value.split(" "));
                    }
                })
                .groupBy((key,value) -> value)
                .windowedBy(TimeWindows.of(Duration.ofSeconds(3)))
                .count()
                .toStream()
                .map((key,value) -> {
                    System.out.println("key:" + key + ",value:" + value);
                    return new KeyValue<>(key.key().toString(),value.toString());
                })
                .to("yango-topic-output");
        return stream;
    }
}
