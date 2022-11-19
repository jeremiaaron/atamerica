package com.example.atamerica.javaclass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonMapper {

    public static <T> List<T> readList(String str, Class<T> type) {
        return readList(str, ArrayList.class, type);
    }

    /**
     * @param <T>
     * @param str
     * @param type
     * @param elementType
     * @return List<T>
     */
    public static <T> List<T> readList(String str, Class<? extends Collection> type, Class<T> elementType) {
        final ObjectMapper mapper = newMapper();
        try {
            return mapper.readValue(str, mapper.getTypeFactory().constructCollectionType(type, elementType));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ObjectMapper newMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new Jdk8Module());
        return mapper;
    }
}
