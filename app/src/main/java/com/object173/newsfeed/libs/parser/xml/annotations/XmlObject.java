package com.object173.newsfeed.libs.parser.xml.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value=ElementType.TYPE)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface XmlObject {
    String tag();
    String verifyingKey() default "";
    String verifyingValue() default "";
}
