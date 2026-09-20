package com.pacal.share.auto_config;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.util.Collection;
import java.util.List;

public class CustomBeanSerializerModifier extends BeanSerializerModifier {
    @Override
    public List<BeanPropertyWriter> changeProperties(
            SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
        for ( BeanPropertyWriter writer : beanProperties ) {
            Class<?> rawType = writer.getType().getRawClass();
            if ( CharSequence.class.isAssignableFrom( rawType ) ) {
                writer.assignNullSerializer( CustomNullSerializer.NULL_STRING_SERIALIZER );
            } else if ( Collection.class.isAssignableFrom( rawType ) ) {
                writer.assignNullSerializer( CustomNullSerializer.NULL_LIST_SERIALIZER );
            } else {
                writer.assignNullSerializer( CustomNullSerializer.NULL_OBJECT_SERIALIZER );
            }
        }
        return beanProperties;
    }
}
