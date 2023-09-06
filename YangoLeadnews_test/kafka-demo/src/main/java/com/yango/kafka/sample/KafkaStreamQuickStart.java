package com.yango.kafka.sample;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.ValueMapper;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * ClassName: KafkaStreamQuickStart
 * Package: com.yango.kafka.sample
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/4-11:27
 */
public class KafkaStreamQuickStart {
    public static void main(String[] args) {
        //streams配置
        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.162.101:9092");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG,"streams-quickstart");
        //streams构建器
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        //流式计算
        streamProcessor(streamsBuilder);

        //kafkaStreams对象
        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(),properties);
        //开启流式计算
        kafkaStreams.start();
    }

    private static void streamProcessor(StreamsBuilder streamsBuilder) {
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
    }
}
