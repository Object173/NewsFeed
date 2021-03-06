package com.object173.newsfeed.libs.parser.xml.annotations;

import com.object173.newsfeed.libs.parser.xml.StringParser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value=ElementType.FIELD)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface XmlAttribute {
    String key();
}
