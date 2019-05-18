package com.object173.newsfeed.libs.parser.xml;

import com.object173.newsfeed.libs.parser.xml.annotations.XmlAttribute;
import com.object173.newsfeed.libs.parser.xml.annotations.XmlField;
import com.object173.newsfeed.libs.parser.xml.annotations.XmlMethod;
import com.object173.newsfeed.libs.parser.xml.annotations.XmlObject;
import com.object173.newsfeed.libs.parser.xml.annotations.XmlRequired;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class XmlObjectParserFactory {

    public static XmlObjectParser get(final Class<?> parsedType) {
        return createParser(parsedType, new HashMap<>(), new HashMap<>());
    }

    private static XmlObjectParser createParser(final Class<?> parsedType,
                                                final Map<Class<?>, XmlObjectParser> objectParserMap,
                                                final Map<Class<?>, StringParser> stringParserMap) {
        if(!parsedType.isAnnotationPresent(XmlObject.class)) {
            throw new IllegalArgumentException(String.format("class %s not annotated XmlObject", parsedType));
        }

        final XmlObject xmlObject = parsedType.getAnnotation(XmlObject.class);
        final XmlObjectParserImpl.Builder builder = new XmlObjectParserImpl.Builder();

        if(!xmlObject.verifyingKey().isEmpty()) {
            builder.setVerifyingKey(xmlObject.verifyingKey());
            builder.setVerifyingValue(xmlObject.verifyingValue());
        }

        final Map<String, Field> tagToField = new HashMap<>();
        final Map<String, Field> attrToField = new HashMap<>();
        final Map<String, Method> tagToMethod = new HashMap<>();
        final List<Field> requiredField = new LinkedList<>();

        final Field[] fields = parsedType.getDeclaredFields();
        for(Field field: fields){
            if(field.isAnnotationPresent(XmlField.class)) {
                XmlField xmlField = field.getAnnotation(XmlField.class);
                String tag = addObjectParser(field.getType(), xmlField.tag(), xmlField.parser(),
                        objectParserMap, stringParserMap);
                tagToField.put(tag, field);
            }
            if(field.isAnnotationPresent(XmlAttribute.class)) {
                XmlAttribute parsedAttr = field.getAnnotation(XmlAttribute.class);
                attrToField.put(parsedAttr.key(), field);
            }
            if(field.isAnnotationPresent(XmlRequired.class)) {
                requiredField.add(field);
            }
        }

        final Method[] methods = parsedType.getDeclaredMethods();
        for(Method method : methods) {
            if(!method.isAnnotationPresent(XmlMethod.class)) {
                continue;
            }
            if(method.getParameterTypes().length != 1) {
                throw new IllegalArgumentException(String
                        .format("Invalid number of input parameters method %s", method.getName()));
            }
            final XmlMethod xmlMethod = method.getAnnotation(XmlMethod.class);
            final Class<?> type = method.getParameterTypes()[0];

            if(stringParserMap.containsKey(method.getClass())) {
                tagToMethod.put(xmlMethod.tag(), method);
                continue;
            }

            final String tag = addObjectParser(type, xmlMethod.tag(), xmlMethod.parser(), objectParserMap, stringParserMap);
            tagToMethod.put(tag, method);
        }

        return builder.setTagToField(tagToField)
                .setAttrToField(attrToField)
                .setRequiredFields(requiredField)
                .setObjectParsers(objectParserMap)
                .setTagToMethod(tagToMethod)
                .setStringParser(stringParserMap)
                .build(parsedType, xmlObject.tag());
    }

    private static String addObjectParser(final Class<?> type, final String tag, Class<? extends StringParser> parserClass,
                                          final Map<Class<?>, XmlObjectParser> objectParsers,
                                          final Map<Class<?>, StringParser> stringParserMap) {
        if(type.isAnnotationPresent(XmlObject.class)) {
            if(!objectParsers.containsKey(type)) {
                objectParsers.put(type, XmlObjectParserFactory.createParser(type, objectParsers, stringParserMap));
            }
            XmlObject childObject = type.getAnnotation(XmlObject.class);
            return childObject.tag();
        }
        if(!stringParserMap.containsKey(parserClass)) {
            try {
                stringParserMap.put(type, parserClass.newInstance());
            }
            catch (IllegalAccessException | InstantiationException e) {
                throw new IllegalArgumentException(String
                        .format("Invalid string parser class: %s", parserClass.getName()), e);
            }
        }
        return tag;
    }
}
