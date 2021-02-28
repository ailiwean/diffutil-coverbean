package com.ailiwean.diff_gen_compile;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/***
 * Author : aiWean
 */
public class PackInfo {

    private List<Element> fieldList;
    private Element idElement;
    private TypeElement typeElement;

    private PackInfo(Build build) {
        this.fieldList = build.fieldList;
        this.idElement = build.idElement;
        this.typeElement = build.typeElement;
    }

    public String getClassName() {
        return typeElement.getSimpleName().toString();
    }

    public String getPackName() {
        return typeElement.getQualifiedName().toString().replaceAll("." + getClassName(), "");
    }

    public String getFunName(String name) {
        return "get" + name.substring(0, 1).toUpperCase() + name.substring(1) + "()+\"\"";
    }

    public String getIdFunName() {
        String name = idElement.getSimpleName().toString();
        return getFunName(name);
    }

    public String getItemFunName(int index) {
        return getFunName(fieldList.get(index).getSimpleName().toString());
    }

    public int getItemSize() {
        return fieldList.size();
    }

    @Override
    public String toString() {
        return "PackInfo{" +
                "fieldList=" + fieldList +
                ", idElement=" + idElement +
                ", typeElement=" + typeElement +
                '}';
    }

    public static class Build {

        private List<Element> fieldList = new ArrayList<>();
        private Element idElement;
        private TypeElement typeElement;

        public Build() {
        }


        void buildClass(TypeElement typeElement) {
            this.typeElement = typeElement;
        }

        void buildIdField(Element element) {
            this.idElement = element;
        }

        void buildField(Element element) {
            this.fieldList.add(element);
        }

        PackInfo build() {
            return new PackInfo(this);
        }


    }

}
