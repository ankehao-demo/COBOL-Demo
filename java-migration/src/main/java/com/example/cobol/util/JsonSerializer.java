package com.example.cobol.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonSerializer {
    private static final ObjectMapper mapper;
    
    static {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }
    
    public static <T> String serialize(T object) throws Exception {
        return mapper.writeValueAsString(object);
    }
    
    public static ObjectMapper getMapper() {
        return mapper;
    }
}
