package com.ailiwean.diff_gen_compile;

import com.ailiwean.diff_gen_annotation.DiffAsynch;
import com.ailiwean.diff_gen_annotation.DiffId;
import com.ailiwean.diff_gen_annotation.DiffItem;
import com.ailiwean.diff_gen_annotation.DiffSynch;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

/***
 * Author : aiWean
 */
@AutoService(Processor.class)
public class DiffGenProcessor extends AbstractProcessor {
    private ProcessingEnvironment processingEnvironment;
    private Messager messager;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.processingEnvironment = processingEnvironment;
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        List<JavaFile> asynchJavaFile = new GenAsynchHelper(messager).gen(set, roundEnvironment);
        List<JavaFile> synchJavaFile = new GenSynchHelper(messager).gen(set, roundEnvironment);
        try {
            //创建DiffUtil.ItemCallback
            for (JavaFile javaFile : asynchJavaFile) {
                javaFile.writeTo(filer);
            }
            //创建DiffUtil.Callback
            for (JavaFile javaFile : synchJavaFile) {
                javaFile.writeTo(filer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        LinkedHashSet<String> strings = new LinkedHashSet<>();
        strings.add(DiffAsynch.class.getCanonicalName());
        strings.add(DiffSynch.class.getCanonicalName());
        return strings;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnvironment.getSourceVersion();
    }

}
