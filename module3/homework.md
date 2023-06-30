3. Module 3: Java Client API (via Confluent Rest Services)

Issue 1: Write an application that will do the following:
1. Producer: Generate messages and write to the source topic 4 part with round-robin strategy  1) with null key value 2) ProducerRecord(String topic, Integer partition, K key, V value) (optional: programmatically)
2. Write the Consumer (with subscribe method) which will read messages from beginning
3. Add one more Consumer to consumer group, find the assignment consumers to the partitions in the log
4. Consumer: Add writing to another topic add manually fix commit offset only after success writing data

Steps:
1. Open the terminal and run the command `docker-compose up -d` in the `Module_3_Java_client_api/` directory. 
2. Open the terminal and run the command `docker-compose ps` in the `Module_3_Java_client_api/` directory.
3. If all containers are running, then you can proceed to the next step.
4. Open the AKHQ web interface in your browser at http://localhost:8080/.
5. Create a topic `source` with 4 partitions and replication factor 1.
6. Open the project `solution` in IntelliJ IDEA.
7. Create a package `com.homework` in `src/main/java`.
8. Create a class `Producer` in `com.homework` package.
9. Add the code with necessary configuration in properties to the `Producer` class like in class `com.luxoft.eas026.module3.simple.GeneratorProducer`.
10. Initialize the `KafkaProducer` class in the `main` method of the `Producer` class with properties and Key and Value types.
11. Create a Record and send it to the `source` topic with Round Robin strategy. Use the constructor of `ProducerRecord` without key or with key and partition.
12. Print logs with the `RecordMetadata` object if the message was sent successfully.
13. Create a class `Consumer` in `com.homework` package.
14. Add the code with necessary configuration in properties to the `Consumer` class like in class `com.luxoft.eas026.module3.simple.ExampleConsumer`.
15. Initialize the `KafkaConsumer` class in the `main` method of the `Consumer` class with properties and Key and Value types.
16. Subscribe to the `source` topic with method `subscribe` of the `KafkaConsumer` instance class.
17. Read messages from the beginning of the topic with method `poll`.
18. Process the messages and print them to the console.
19. Fix the consumer group id in the properties file or in the code.
20. Allow in the configuration in IntelliJ IDEA to run multiple instances of the `Consumer` class. https://www.jetbrains.com/help/objc/run-and-debug-on-multiple-simulators.html#:~:text=Allow%20parallel%20run%EF%BB%BF,run%20checkbox%20and%20click%20OK. 
21. Run one more consumer with the same group id. 
22. Read the logs of both consumers and find the assignment consumers to the partitions in the log.
23. Create a new topic `target` with 4 partitions and replication factor 1.
24. Add the instance of Producer to the `Consumer` class like in class `com.luxoft.eas026.module3.streams`.
25. Send the message to the new topic `target` with the same key and transformed value (for example increased value).
26. Disable in the Kafka config auto commit and commit the offset manually after the message was sent to the new topic.
27. Click left to the `Consumer` class name and select `Create gist` in the context menu.
28. Click left to the `Producer` class name and select `Create gist` in the context menu.
29. Send the links to the gists to the mentor.


Issue 2:
1. Producer: Using Jackson Read data from a CSV file, which can be downloaded from the link - https://www.kaggle.com/sootersaalu/amazon-top-50-bestselling-books-2009-2019, serialize them into JSON, and write to the books topic of the locally deployed Apache Kafka service.
2. Write the consumer with Assign option which will Read last 3 records from any one partition from the books topic and print the (with the maximum records value) to the console. When reading a topic, only 3 records can be stored in memory at the same time.

