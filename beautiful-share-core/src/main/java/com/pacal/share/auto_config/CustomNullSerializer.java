package com.pacal.share.auto_config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class CustomNullSerializer {
    public static final JsonSerializer<Object> NULL_STRING_SERIALIZER = new JsonSerializer<>() {
        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString( "" );
        }
    };
    public static final JsonSerializer<Object> NULL_OBJECT_SERIALIZER = new JsonSerializer<>() {
        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeObject( null );
        }
    };
    public static final JsonSerializer<Object> NULL_ARRAY_SERIALIZER = new JsonSerializer<>() {
        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartArray();
            jsonGenerator.writeEndArray();
        }
    };
    public static final JsonSerializer<Object> NULL_LIST_SERIALIZER = new JsonSerializer<>() {
        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartArray();
            jsonGenerator.writeEndArray();
        }
    };
}
