package com.luxoft.homework.module3.task1;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.luxoft.homework.module3.util.Config;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Producer {
	private static final Logger LOG = LoggerFactory.getLogger(Producer.class);
	private static final String CONFIG_FILE_PATH = "src/main/resources/config.task1.properties";
	private static final Properties config = new Config(CONFIG_FILE_PATH).getConfig();

	private static org.apache.kafka.clients.producer.Producer<String, Integer> producer;

	public static void main(String[] args) {
		Properties props = new Properties();

		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getProperty("bootstrap.servers"));
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, config.getProperty("key.serializer.class"));
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, config.getProperty("value.serializer.class"));

		props.put(ProducerConfig.ACKS_CONFIG, config.getProperty("acks.value")); // default wait for leader or wait for all leader and followers
		props.put(ProducerConfig.CLIENT_ID_CONFIG, config.getProperty("client.id"));  // optional

		producer = new KafkaProducer<>(props);

		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

		executor.scheduleAtFixedRate(() -> send(config.getProperty("source.topic.name")), 0, 3, TimeUnit.SECONDS); // no blocking

	}

	@SuppressWarnings({ "boxing", "unused" })
	public static void send(String topic) {
		final int number = new Random().nextInt(10);
		ProducerRecord<String, Integer> data = new ProducerRecord<>(topic, "key" + number, number); // zero partition

		try {
			RecordMetadata meta = producer.send(data).get();
			LOG.info("Hash routing strategy key = {}, value = {} => partition = {}, offset= {}", data.key(), data.value(), meta.partition(), meta.offset());
		} catch (InterruptedException | ExecutionException e) {
			producer.flush();
		}

	}

}
