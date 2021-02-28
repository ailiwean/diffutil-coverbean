package com.ailiwean.diff_gen_compile;

import com.ailiwean.diff_gen_annotation.DiffId;
import com.ailiwean.diff_gen_annotation.DiffItem;
import com.ailiwean.diff_gen_annotation.DiffSynch;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/***
 * Author : aiWean
 */
public class GenSynchHelper implements Gen {

    Messager messager;

    GenSynchHelper(Messager messager) {
        this.messager = messager;
    }


    @Override
    public List<JavaFile> gen(Set<? extends TypeElement> set, RoundEnvironment rou) {
        Set<? extends Element> type = rou.getElementsAnnotatedWith(DiffSynch.class);
        List<PackInfo> packInfoList = new ArrayList<>();
        //获取注解标记
        for (Element i : type) {
            //class注解
            PackInfo.Build build = new PackInfo.Build();
            build.buildClass((TypeElement) i);
            for (Element enclosedElement : i.getEnclosedElements()) {
                if (enclosedElement.getAnnotation(DiffId.class) != null)
                    build.buildIdField(enclosedElement);
                if (enclosedElement.getAnnotation(DiffItem.class) != null)
                    build.buildField(enclosedElement);
            }
            packInfoList.add(build.build());
        }
        List<JavaFile> javaFileList = new ArrayList<>();
        if (packInfoList.isEmpty())
            return javaFileList;
        //一个PackInfo对应一个bean
        for (PackInfo packInfo : packInfoList) {
            javaFileList.add(createJavaFile(packInfo));
        }
        return javaFileList;
    }


    private JavaFile createJavaFile(PackInfo packInfo) {
        String genClassName = packInfo.getClassName() + "CallbackGenerate";
        TypeSpec typeSpec = TypeSpec.classBuilder(genClassName)
                .superclass(getSuperClass(packInfo))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addJavadoc(Constant.javaDoc)
                .addMethod(getAreItemsTheSame(packInfo))
                .addMethod(getAreContentsTheSame(packInfo))
                .addMethod(getOldListSize(packInfo))
                .addMethod(getNewListSize(packInfo))
                .build();

        return JavaFile.builder(Constant.packName, typeSpec).build();
    }

    /***
     * areItemsTheSame方法
     * @return
     */
    public MethodSpec getAreItemsTheSame(PackInfo packInfo) {
        ClassName textUtils = ClassName.get("android.text", "TextUtils");
        return MethodSpec.methodBuilder("areItemsTheSame")
                .addAnnotation(Override.class)
                .addParameters(getAreParameter(packInfo))
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return $T.equals(oldItem." + packInfo.getIdFunName() + ", newItem."
                        + packInfo.getIdFunName() + ")", textUtils)
                .returns(boolean.class)
                .build();
    }

    /***
     * areItemsTheSame与areContentsTheSame 方法参数
     * @return
     */
    public List<ParameterSpec> getAreParameter(PackInfo packInfo) {
        List<ParameterSpec> parameterSpecList = new ArrayList<>();
        parameterSpecList.add(ParameterSpec.builder(int.class, "oldItemPosition").build());
        parameterSpecList.add(ParameterSpec.builder(int.class, "newItemPosition").build());
        return parameterSpecList;
    }


    /**
     * areContentsTheSame 方法
     */
    public MethodSpec getAreContentsTheSame(PackInfo packInfo) {
        ClassName textUtils = ClassName.get("android.text", "TextUtils");
        Object[] classNames = new ClassName[packInfo.getItemSize()];
        Arrays.fill(classNames, textUtils);
        MethodSpec.Builder builder = MethodSpec.methodBuilder("areContentsTheSame")
                .addAnnotation(Override.class)
                .addParameters(getAreParameter(packInfo))
                .addModifiers(Modifier.PUBLIC)
                .returns(boolean.class);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("return ");
        for (int i = 0; i < packInfo.getItemSize(); i++) {
            String statement = "$T.equals(oldItem." + packInfo.getItemFunName(i) +
                    ", newItem." + packInfo.getItemFunName(i) + ")&&";
            stringBuilder.append(statement);
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        builder.addStatement(stringBuilder.toString(), classNames);
        return builder.build();
    }


    /***
     * getOldListSize 方法
     */
    public MethodSpec getOldListSize(PackInfo packInfo) {
        return MethodSpec.methodBuilder("getOldListSize")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .build();
    }

    /***
     * getNewListSize 方法
     */
    public MethodSpec getNewListSize(PackInfo packInfo) {
        return MethodSpec.methodBuilder("getNewListSize")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .build();
    }

    /***
     *  父类
     * @param packInfo
     * @return
     */
    public ClassName getSuperClass(PackInfo packInfo) {
        return ClassName.get("androidx.recyclerview.widget.DiffUtil", "Callback");
    }

    /***
     * 数据类型
     */
    public TypeName getSuperTypeClass(PackInfo packInfo) {
        return ClassName.get(packInfo.getPackName(), packInfo.getClassName());
    }

}
