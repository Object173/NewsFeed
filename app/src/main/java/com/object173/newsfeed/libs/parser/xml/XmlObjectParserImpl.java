package com.object173.newsfeed.libs.parser.xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class XmlObjectParserImpl implements XmlObjectParser {

    private final Class<?> parsedType;
    private final String tag;

    private final Map<String, Field> tagToField;
    private final Map<String, Field> attrToField;
    private final Map<String, Method> tagToMethod;

    private final String verifyingKey;
    private final String verifyingValue;

    private final List<Field> requiredFields;

    private final Map<Class<?>, XmlObjectParser> objectParsers;
    private final Map<Class<?>, StringParser> stringParsers;

    private XmlObjectParserImpl(Class<?> parsedType, String tag, Map<String, Field> tagToField,
                                Map<String, Field> attrToField,
                                Map<String, Method> tagToMethod,
                                String verifyingKey,
                                String verifyingValue,
                                List<Field> requiredFields,
                                Map<Class<?>, XmlObjectParser> objectParsers,
                                Map<Class<?>, StringParser> stringParsers) {
        if(parsedType == null || tag == null) {
            throw new IllegalArgumentException("configuration class don't have required params");
        }
        this.parsedType = parsedType;
        this.tag = tag;

        this.tagToField = tagToField;
        this.attrToField = attrToField;
        this.tagToMethod = tagToMethod;
        this.verifyingKey = verifyingKey;
        this.verifyingValue = verifyingValue;
        this.requiredFields = requiredFields;
        this.objectParsers = objectParsers;
        this.stringParsers = stringParsers;
    }

    @Override
    public boolean checkObjectInXml(XmlPullParser xmlParser) {
        if(!tag.equals(xmlParser.getName())) {
            return false;
        }
        if(verifyingKey != null) {
            String value = xmlParser.getAttributeValue(null, verifyingKey);
            return value != null && value.equals(verifyingValue);
        }
        return true;
    }

    @Override
    public Object parse(XmlPullParser xmlParser) throws IOException, XmlPullParserException {
        try {
            final Object result = parsedType.newInstance();

            for(Map.Entry<String, Field> fieldEntry : attrToField.entrySet()) {
                String attr = xmlParser.getAttributeValue(null, fieldEntry.getKey());
                if(attr != null) {
                    setObjectField(result, fieldEntry.getValue(), attr);
                }
            }

            for (xmlParser.nextTag();
                 !xmlParser.getName().equals(tag) || xmlParser.getEventType() != XmlPullParser.END_TAG;
                 xmlParser.nextTag()) {

                if(xmlParser.getName().equals(tag)) {
                    continue;
                }
                while(xmlParser.getEventType() != XmlPullParser.START_TAG) {
                    xmlParser.next();
                }
                String currentTag = xmlParser.getName();
                if(tagToField.containsKey(currentTag)) {
                    Field field = tagToField.get(currentTag);
                    Object value = parseObject(xmlParser, field.getType());
                    setObjectField(result, field, value);
                }
                else if (tagToMethod.containsKey(currentTag)) {
                    Method method = tagToMethod.get(currentTag);
                    Object value = parseObject(xmlParser, method.getParameterTypes()[0]);
                    setObjectMethod(result, method, value);
                }
                else {
                    skipTag(xmlParser);
                }
            }

            if(!checkRequired(result)) {
                return null;
            }
            return result;
        }
        catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new IllegalStateException(String.format(
                    "Invalid configuration class %s", parsedType.getName()), e);
        }
    }

    private boolean checkRequired(final Object result) throws IllegalAccessException {
        for(Field field : requiredFields) {
            field.setAccessible(true);
            if(field.get(result) == null) {
                return false;
            }
        }
        return true;
    }

    private Object parseObject(final XmlPullParser xmlParser, Class<?> parsedType)
            throws XmlPullParserException, IOException {
        final XmlObjectParser objectParser = objectParsers.get(parsedType);
        if(objectParser != null) {
            return objectParser.parse(xmlParser);
        }
        if(stringParsers.containsKey(parsedType)) {
            String content = readText(xmlParser);
            return stringParsers.get(parsedType).parse(content);
        }
        throw new IllegalArgumentException(String.format("unsupport field type %s",
                parsedType.getName()));
    }

    private void setObjectField(final Object object, final Field field, final Object value)
            throws IllegalAccessException {
        field.setAccessible(true);
        field.set(object, value);
    }

    private void setObjectMethod(final Object object, final Method method, final Object value)
            throws InvocationTargetException, IllegalAccessException {
        method.setAccessible(true);
        method.invoke(object, value);
    }

    private void skipTag(final XmlPullParser xmlParser) throws IOException, XmlPullParserException {
        if (xmlParser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (xmlParser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private String readText(final XmlPullParser xmlParser) throws IOException, XmlPullParserException {
        String result = "";
        if (xmlParser.next() == XmlPullParser.TEXT) {
            result = xmlParser.getText();
            xmlParser.nextTag();
        }
        return result;
    }

    public static class Builder {
        private String verifyingKey;
        private String verifyingValue;
        private Map<String, Field> tagToField;
        private Map<String, Field> attrToField;
        private Map<String, Method> tagToMethod;
        private Map<Class<?>, XmlObjectParser> objectParsers;
        private Map<Class<?>, StringParser> stringParser;
        private List<Field> requiredFields;

        public XmlObjectParserImpl build(Class<?> parsedType, String tag) {
            return new XmlObjectParserImpl(parsedType, tag,
                    getMap(tagToField),
                    getMap(attrToField),
                    getMap(tagToMethod),
                    verifyingKey, verifyingValue,
                    getList(requiredFields),
                    getMap(objectParsers),
                    getMap(stringParser));
        }

        Builder setVerifyingKey(String verifyingKey) {
            this.verifyingKey = verifyingKey;
            return this;
        }

        Builder setVerifyingValue(String verifyingValue) {
            this.verifyingValue = verifyingValue;
            return this;
        }

        Builder setTagToField(Map<String, Field> tagToField) {
            this.tagToField = tagToField;
            return this;
        }

        Builder setRequiredFields(List<Field> requiredFields) {
            this.requiredFields = requiredFields;
            return this;
        }

        Builder setAttrToField(Map<String, Field> attrToField) {
            this.attrToField = attrToField;
            return this;
        }

        Builder setObjectParsers(Map<Class<?>, XmlObjectParser> objectParsers) {
            this.objectParsers = objectParsers;
            return this;
        }

        Builder setTagToMethod(Map<String, Method> tagToMethod) {
            this.tagToMethod = tagToMethod;
            return this;
        }

        Builder setStringParser(Map<Class<?>, StringParser> stringParser) {
            this.stringParser = stringParser;
            return this;
        }

        private Map getMap(Map map) {
            return map != null ? map : Collections.emptyMap();
        }

        private List getList(List list) {
            return list != null ? list : Collections.emptyList();
        }
    }
}
