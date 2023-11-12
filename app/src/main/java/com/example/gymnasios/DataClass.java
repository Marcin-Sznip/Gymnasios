package com.example.gymnasios;

import android.provider.ContactsContract;

public class DataClass {

    private String exercise;
    private int set1;
    private int set2;
    private int set3;
    private int set4;
    private int weight1;
    private int weight2;
    private int weight3;
    private int weight4;
    private String date;

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public DataClass(String exercise, int set1, int set2, int set3, int set4, int weight1, int weight2, int weight3, int weight4, String date) {
        this.exercise = exercise;
        this.set1 = set1;
        this.set2 = set2;
        this.set3 = set3;
        this.set4 = set4;
        this.weight1 = weight1;
        this.weight2 = weight2;
        this.weight3 = weight3;
        this.weight4 = weight4;
        this.date = date;
    }

    public DataClass() {

    }

    public String getExercise() {
        return exercise;
    }

    public int getSet1() {
        return set1;
    }

    public int getSet2() {
        return set2;
    }

    public int getSet3() {
        return set3;
    }

    public int getSet4() {
        return set4;
    }

    public int getWeight1() {
        return weight1;
    }

    public int getWeight2() {
        return weight2;
    }

    public int getWeight3() {
        return weight3;
    }

    public int getWeight4() {
        return weight4;
    }

    public String getDate() {
        return date;
    }


    public void setDate(String date) {
        this.date = date;
    }
}
