package com.luxoft.homework.module3.task2;

import org.springframework.kafka.support.serializer.JsonDeserializer;

public class CustomJsonDeserializer extends JsonDeserializer<Book> {

    @Override
    public JsonDeserializer<Book> trustedPackages(String... packages) {
        this.typeMapper.addTrustedPackages("*");
        return this;
    }
}
