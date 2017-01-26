package com.cit.abakar.application.ExampleClasses;

import com.activeandroid.Model;



public class Condition extends Model {

    public int id;
    public String name;
    public String description;

    public Condition(){

    }

    public Condition(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
