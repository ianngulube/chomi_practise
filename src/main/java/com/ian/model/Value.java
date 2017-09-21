package com.ian.model;

public class Value {

    private String parameter1;
    private String parameter2;

    Value(String parameter1, String parameter2) {
        this.parameter1 = parameter1;
        this.parameter2 = parameter2;
    }

    Value() {

    }

    public String getParameter1() {
        return parameter1;
    }

    public void setParameter1(String parameter) {
        this.parameter1 = parameter;
    }

    public String getParameter2() {
        return parameter2;
    }

    public void setParameter2(String parameter2) {
        this.parameter2 = parameter2;
    }

}
