package com.cit.abakar.application.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Directory_Equipment_Condition")
public class Directory_Equipment_Condition extends Model {

    @Column(name = "name")
    public String name;

    @Column(name = "description")
    public String description;

    public Directory_Equipment_Condition(){
        super();

    }

    public Directory_Equipment_Condition(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }
}
