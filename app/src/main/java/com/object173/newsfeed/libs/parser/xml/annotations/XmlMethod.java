package com.object173.newsfeed.libs.parser.xml.annotations;

import com.object173.newsfeed.libs.parser.xml.StringParser;
import com.object173.newsfeed.libs.parser.xml.StringParserImpl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value=ElementType.METHOD)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface XmlMethod {
    String tag() default "";
    Class<? extends StringParser> parser() default StringParserImpl.class;
}
