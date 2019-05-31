package io.bitmax.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Mapper  {
    private static ObjectMapper mapper = new ObjectMapper();

    public static <T> T readObjectFromString(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
