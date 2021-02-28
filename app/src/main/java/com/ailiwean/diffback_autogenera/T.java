package com.ailiwean.diffback_autogenera;

import com.ailiwean.diff_gen_annotation.DiffAsynch;
import com.ailiwean.diff_gen_annotation.DiffId;
import com.ailiwean.diff_gen_annotation.DiffItem;
import com.ailiwean.diff_gen_annotation.DiffSynch;

@DiffSynch
@DiffAsynch
public class T {

    @DiffItem
    String ai;

    @DiffId
    String bi;

    @DiffItem
    String values;

    int age;

    String sex;

    public String getAi() {
        return ai;
    }

    public void setAi(String ai) {
        this.ai = ai;
    }

    public String getBi() {
        return bi;
    }

    public void setBi(String bi) {
        this.bi = bi;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
