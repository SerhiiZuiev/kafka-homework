package com.luxoft.homework.module3.task1;

import com.luxoft.homework.module3.util.Config;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Producer2 {
	private static final Logger LOG = LoggerFactory.getLogger(Producer2.class);
	private static final String CONFIG_FILE_PATH = "src/main/resources/config.task1.properties";
	private static final Properties config = new Config(CONFIG_FILE_PATH).getConfig();

	private static org.apache.kafka.clients.producer.Producer<String, Integer> producer;
	private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	public Producer2() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getProperty("bootstrap.servers"));
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, config.getProperty("key.serializer.class"));
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, config.getProperty("value.serializer.class"));

		props.put(ProducerConfig.ACKS_CONFIG, config.getProperty("acks.value")); // default wait for leader or wait for all leader and followers
		props.put(ProducerConfig.CLIENT_ID_CONFIG, config.getProperty("client.id"));  // optional

		producer = new KafkaProducer<>(props);
	}

	public void sendAsync(String topic, String key, Integer value) {
		executor.scheduleAtFixedRate(() -> send(topic, key, value), 0, 3, TimeUnit.SECONDS); // no blocking

	}

	private void send(String topic, String key, Integer value) {
		ProducerRecord<String, Integer> data = new ProducerRecord<>(topic, key, value); // zero partition

		try {
			RecordMetadata meta = producer.send(data).get();
			LOG.info("Write to {}. Hash routing strategy key = {}, value = {} => partition = {}, offset= {}",
					topic, data.key(), data.value(), meta.partition(), meta.offset());
		} catch (InterruptedException | ExecutionException e) {
			producer.flush();
		}

	}

}
