package com.luxoft.homework.module3.task2;

import com.luxoft.homework.module3.util.Config;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

public class Consumer {

	private static final Logger LOG = LoggerFactory.getLogger(Consumer.class);
	private static final String CONFIG_FILE_PATH = "src/main/resources/config.task2.properties";
	private static final Properties config = new Config(CONFIG_FILE_PATH).getConfig();


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

		try (KafkaConsumer<String, Book> consumer = new KafkaConsumer<>(props)) {

			TopicPartition topicPartition = new TopicPartition(config.getProperty("book.topic.name"), 0);
			consumer.assign(Collections.singleton(topicPartition));

			// read only 3 last messages
			Map<TopicPartition, Long> endOffsets = consumer.endOffsets(Collections.singleton(topicPartition));
			for (Map.Entry<TopicPartition, Long> entry : endOffsets.entrySet()) {
				long newOffsetPosition = entry.getValue() - 3;
				consumer.seek(entry.getKey(), newOffsetPosition);
			}

			while (true) {
				ConsumerRecords<String, Book> records = consumer.poll(Duration.ofSeconds(2));

				for (ConsumerRecord<String, Book> data : records) {
					LOG.info("key = {}, value = {} => partition = {}, offset= {}", data.key(), data.value(), data.partition(), data.offset());
				}

			}
		} catch (Exception e) {
			LOG.error("Something goes wrong: {}", e.getMessage(), e);
		}
	}
}
