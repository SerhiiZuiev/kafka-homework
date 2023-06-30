package com.luxoft.homework.module3.task1;

import com.luxoft.homework.module3.util.Config;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class Consumer2 {

	private static final Logger LOG = LoggerFactory.getLogger(Consumer2.class);
	private static final String CONFIG_FILE_PATH = "src/main/resources/config.task1.properties";
	private static final Properties config = new Config(CONFIG_FILE_PATH).getConfig();

	private static final Producer2 producer = new Producer2();

	@SuppressWarnings("boxing")
	public static void main(String[] args) {

		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getProperty("bootstrap.servers"));
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, config.getProperty("key.deserializer.class"));
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, config.getProperty("value.deserializer.class"));

		props.put(ConsumerConfig.GROUP_ID_CONFIG, config.getProperty("consumer.group"));
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, config.getProperty("consumer.offset.reset"));

//		optional
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, config.getProperty("consumer.max.pull.records"));


		try (KafkaConsumer<String, Integer> consumer = new KafkaConsumer<>(props)) {
			consumer.subscribe(Collections.singleton(config.getProperty("source.topic.name")));

//			consumer.assign(Collections.singleton(new org.apache.kafka.common.TopicPartition(TOPIC_NAME, 0))); // will not use rebalance


			while (true) {
				ConsumerRecords<String, Integer> records = consumer.poll(Duration.ofSeconds(2));

				for (ConsumerRecord<String, Integer> data : records) {
					LOG.info("key = {}, value = {} => partition = {}, offset= {}", data.key(), data.value(), data.partition(), data.offset());
					producer.sendAsync(config.getProperty("target.topic.name"), data.key(), data.value());
				}

//				consumer.close();
			}
		} catch (Exception e) {
			LOG.error("Something goes wrong: {}", e.getMessage(), e);
		}
	}
}
