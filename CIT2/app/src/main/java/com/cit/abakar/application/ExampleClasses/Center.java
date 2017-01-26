package com.cit.abakar.application.ExampleClasses;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

public class Center extends Model  {

    public String name;
    public int id;

    public Center(){

    }


    public Center(int id, String name) {
        this.name = name;
        this.id = id;
    }

}
