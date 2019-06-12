package io.bitmax.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Mapper  {
    private static ObjectMapper mapper = new ObjectMapper();

    public static <T> T asObject(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String asString(Object message) {
        try {
            return mapper.writeValueAsString(message);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
