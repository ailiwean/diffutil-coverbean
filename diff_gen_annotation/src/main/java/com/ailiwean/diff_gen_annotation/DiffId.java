package com.ailiwean.diff_gen_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * Author : aiWean
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface DiffId {
}
