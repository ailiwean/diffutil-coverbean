package com.ailiwean.diff_gen_compile;

import com.squareup.javapoet.JavaFile;

import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

/***
 * Author : aiWean
 */
public interface Gen {
    List<JavaFile> gen(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment);
}
